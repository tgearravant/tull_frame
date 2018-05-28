package com.gearreald.tullframe.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.gearreald.tullframe.Row;
import com.gearreald.tullframe.TullFrame;
import com.gearreald.tullframe.TullFrameFactory;
import com.gearreald.tullframe.exceptions.TullFrameException;
import com.gearreald.tullframe.interfaces.JoinCondition;
import com.gearreald.tullframe.threading.MergeWorker;
import com.gearreald.tullframe.utils.ColumnType;

import net.tullco.tullutils.Pair;

public class TullFrameMergeFactory {
	
	private TullFrame baseFrame;
	private TullFrame mergeFrame;
	private boolean force;
	private boolean concatenate;
	private String baseColumn;
	private String mergeColumn;
	private String prefix;
	private JoinCondition joinCondition;
	
	private static final int COUNT_WORKERS = 4;
	
	public TullFrameMergeFactory(){
		baseFrame = null;
		mergeFrame = null;
		concatenate = false;
		force = false;
		baseColumn = null;
		mergeColumn = null;
		prefix = "";
		joinCondition = null;
	}
	public TullFrameMergeFactory byConcatenating(TullFrame base, TullFrame merge){
		concatenate = true;
		return this;
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
	public TullFrameMergeFactory onCondition(JoinCondition jc){
		joinCondition = jc;
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
		if(concatenate)
			return concatenate();
		else
			return merge();
	}
	private TullFrame concatenate(){
		for(String colName: baseFrame.getColumnNames()){
			if(!mergeFrame.hasColumn(colName))
				throw new TullFrameException("The merge frame doesn't have matching columns to the original.");
		}
		for(Row r: mergeFrame){
			r.getClass();
		}
		return null;
	}
	private TullFrame merge(){
		if( baseFrame == null || mergeFrame == null || (baseColumn == null && joinCondition == null) )
			throw new IllegalArgumentException("You must set the merge frames and base merge column.");
		if(mergeColumn == null)
			mergeColumn = baseColumn;
		if(( !baseFrame.hasColumn(baseColumn) || !mergeFrame.hasColumn(mergeColumn) ) && joinCondition == null)
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
		if(joinCondition != null){
			Queue<Pair<Row,Row>> joinQueue = new ConcurrentLinkedQueue<Pair<Row,Row>>();
			List<MergeWorker> workers = new ArrayList<MergeWorker>(); 
			
			for(int i = 0; i < COUNT_WORKERS; i++){
				MergeWorker worker = new MergeWorker(joinQueue, newFrame, joinCondition);
				worker.start();
				workers.add(worker);
			}
			
			for(Row br: baseFrame){
				for(Row mr: mergeFrame){
					joinQueue.add(Pair.<Row, Row>of(br, mr));
				}
			}
			while(joinQueue.size() > 0){
				try{
					Thread.sleep(10);
				}catch(InterruptedException e){}
			}
			for(MergeWorker worker : workers){
				worker.noMore();
			}
			while(true){
				int livingWorkers = 0;
				for(MergeWorker worker : workers){
					if(worker.isAlive())
						livingWorkers++;
				}
				if(livingWorkers == 0){
					break;
				}else{
					try{
						Thread.sleep(10);
					}catch(InterruptedException e){}
				}
			}
		}else{
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
