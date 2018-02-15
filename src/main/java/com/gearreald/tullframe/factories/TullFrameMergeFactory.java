package com.gearreald.tullframe.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gearreald.tullframe.Row;
import com.gearreald.tullframe.TullFrame;
import com.gearreald.tullframe.TullFrameFactory;
import com.gearreald.tullframe.exceptions.TullFrameException;
import com.gearreald.tullframe.utils.ColumnType;

public class TullFrameMergeFactory {
	
	private TullFrame baseFrame;
	private TullFrame mergeFrame;
	private boolean force;
	private String baseColumn;
	private String mergeColumn;
	private String prefix;
	
	public TullFrameMergeFactory(){
		baseFrame = null;
		mergeFrame = null;
		force = false;
		baseColumn = null;
		mergeColumn = null;
		prefix = "";
	}
	public TullFrameMergeFactory byMerging(TullFrame base, TullFrame merge){
		this.baseFrame = base;
		this.mergeFrame = merge;
		return this;
	}
	public TullFrameMergeFactory ignoreIndexes(boolean force){
		this.force = force;
		return this;
	}
	public TullFrameMergeFactory onColumn(String colName){
		this.baseColumn = colName;
		return this;
	}
	public TullFrameMergeFactory andColumn(String colName){
		this.mergeColumn = colName;
		return this;
	}
	public TullFrameMergeFactory prefixingNewColumnsWith(String prefix){
		if(prefix == null)
			prefix = "";
		this.prefix = prefix;
		return this;
	}
	public TullFrame build(){
		if(baseFrame == null || mergeFrame == null || baseColumn == null)
			throw new IllegalArgumentException("You must set the merge frames and base merge column.");
		if(mergeColumn == null)
			mergeColumn = baseColumn;
		if(!baseFrame.hasColumn(baseColumn) || !mergeFrame.hasColumn(mergeColumn))
			throw new TullFrameException("Both frames need to contain the merge key!");
		
		List<String> baseHeaders = new ArrayList<String>();
		List<ColumnType> baseColumnTypes = new ArrayList<ColumnType>();
		for(String header: baseFrame.getColumnNames()){
			baseHeaders.add(header);
			baseColumnTypes.add(baseFrame.getTypeOfColumn(header));
		}
		
		List<String> mergeHeaders = new ArrayList<String>();
		List<ColumnType> mergeColumnTypes = new ArrayList<ColumnType>();
		for(String header: mergeFrame.getColumnNames()){
			mergeHeaders.add(header);
			mergeColumnTypes.add(mergeFrame.getTypeOfColumn(header));
		}
		
		List<String> newHeaders = new ArrayList<String>();
		List<ColumnType> newColumnTypes = new ArrayList<ColumnType>();
		Map<String, String> mapOfNewHeadersToOldHeaders = new HashMap<String, String>();
		newHeaders.addAll(baseHeaders);
		newColumnTypes.addAll(baseColumnTypes);
		
		for(int i=0; i < mergeHeaders.size(); i++){
			String header = mergeHeaders.get(i);
			ColumnType colType = mergeColumnTypes.get(i);
			if(header.equals(mergeColumn))
				continue;
			else{
				String newHeader = getNewHeader(newHeaders, prefix + header);
				newHeaders.add(newHeader);
				newColumnTypes.add(colType);
				mapOfNewHeadersToOldHeaders.put(newHeader, header);
			}
		}
		
		TullFrame newFrame = new TullFrameFactory()
				.setColumnHeaders(newHeaders)
				.setColumnTypes(newColumnTypes)
				.build();
		
		for(Row r: baseFrame){
			List<String> valuesToAdd = new ArrayList<String>();
			Object mergeKey = r.getValue(baseColumn);
			Row mergeRow = mergeFrame.lookupRowByUniqueKey(mergeKey, mergeColumn, this.force);
			for(String newHeader: newHeaders){
				if(newHeader.equals(mergeColumn)){
					valuesToAdd.add(r.getString(mergeColumn));
				}else if(baseFrame.hasColumn(newHeader)){
					valuesToAdd.add(r.getString(newHeader));
				}else{
					if(mergeRow == null)
						valuesToAdd.add(null);
					else if(mergeRow.hasColumn(newHeader)){
						valuesToAdd.add(mergeRow.getString(newHeader));
					}else{
						valuesToAdd.add(mergeRow.getString(mapOfNewHeadersToOldHeaders.get(newHeader)));
					}
				}
			}
			newFrame.addRow(valuesToAdd);
		}
		
		return newFrame;
	}
	
	private static String getNewHeader(List<String> existingHeaders, String headerToAdd){
		if(!existingHeaders.contains(headerToAdd))
			return headerToAdd;
		else
			return getNewHeader(existingHeaders, headerToAdd, 1);
	}
	private static String getNewHeader(List<String> existingHeaders, String headerToAdd, int index){
		String headerToTest = headerToAdd+"_"+index;
		if(!existingHeaders.contains(headerToTest)){
			return headerToTest;
		}else{
			return getNewHeader(existingHeaders, headerToAdd, index + 1);
		}
	}
}
