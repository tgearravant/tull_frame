package com.gearreald.tullframe;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gearreald.tullframe.columns.Column;
import com.gearreald.tullframe.columns.StringColumn;
import com.gearreald.tullframe.exceptions.DataFrameException;
import com.gearreald.tullframe.utils.ColumnType;

public class TullFrame {

	private Map<String, Column> columns;
	private List<String> columnNames;
	private int currentIndex;
	private List<Integer> indexes;
	
	protected TullFrame(){
		columns = new LinkedHashMap<String, Column>();
		columnNames = new ArrayList<String>();
		currentIndex = 0;
	}
	protected TullFrame(String[] headers, ColumnType[] columnTypes){
		super();
		for(int i = 0; i < headers.length; i++){
			addEmptyColumn(headers[i], columnTypes[i]);
		}
	}
	protected void addEmptyColumn(String name, ColumnType type){
		addColumn(name, Column.getColumnFromColumnType(type));
	}
	protected void addColumn(String name, Column c){
		columns.put(name, c);
		columnNames.add(name);
	}
	protected void initializeStringColumns(String[] names){
		columns.clear();
		columnNames.clear();
		for (String name: names){
			columns.put(name, new StringColumn());
			columnNames.add(name);
		}
	}
	public void addRow(String[] valueArray){
		if(valueArray.length != columns.size()){
			throw new DataFrameException("The new row has the wrong number of elements.");
		}
		for(String colName: columns.keySet()){
			Column col = columns.get(colName);
			col.set(currentIndex,colName);
		}
		indexes.add(currentIndex);
		currentIndex++;
		
	}
	public int countRows(){
		return 0;
	}
	public int countColumns(){
		return columns.size();
	}
	public void toCsv(File f){
	}
}
