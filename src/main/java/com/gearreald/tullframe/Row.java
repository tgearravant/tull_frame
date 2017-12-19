package com.gearreald.tullframe;

import java.util.List;
import java.util.Map;

import com.gearreald.tullframe.columns.Column;

public class Row {
	private int index;
	private Map<String,Column> columns;
	private List<String> columnNames;
	
	protected Row(int index, Map<String,Column> columns, List<String> columnNames){
		this.index = index;
		this.columns = columns;
		this.columnNames = columnNames;
	}
}
