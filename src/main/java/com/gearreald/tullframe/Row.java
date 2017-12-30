package com.gearreald.tullframe;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.gearreald.tullframe.columns.Column;
import com.gearreald.tullframe.exceptions.ColumnNameException;

public class Row {
	private int index;
	private Map<String,Column> columns;
	private List<String> columnNames;
	
	protected Row(int index, Map<String,Column> columns, List<String> columnNames){
		this.index = index;
		this.columns = columns;
		this.columnNames = columnNames;
	}
	public int getInt(String columnName){
		throwIfNoKey(columnName);
		return columns.get(columnName).getInt(index);
	}
	public LocalDate getDate(String columnName){
		throwIfNoKey(columnName);
		return columns.get(columnName).getDate(index);
	}
	public LocalDateTime getTime(String columnName){
		throwIfNoKey(columnName);
		return columns.get(columnName).getTime(index);
	}
	public String getString(String columnName){
		throwIfNoKey(columnName);
		return columns.get(columnName).getString(index);
	}
	public boolean getBoolean(String columnName){
		throwIfNoKey(columnName);
		return columns.get(columnName).getBoolean(index);
	}
	public long getLong(String columnName){
		throwIfNoKey(columnName);
		return columns.get(columnName).getLong(index);
	}
	public double getDouble(String columnName){
		throwIfNoKey(columnName);
		return columns.get(columnName).getDouble(index);
	}
	private void throwIfNoKey(String columnName){
		if (!columnNames.contains(columnName))
			throw new ColumnNameException("The key " + columnName + " is not in the dataframe.");
		
	}
}
