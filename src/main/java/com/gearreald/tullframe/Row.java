package com.gearreald.tullframe;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gearreald.tullframe.columns.Column;
import com.gearreald.tullframe.exceptions.ColumnNameException;
import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class Row {
	private int index;
	private TullFrame parentFrame;
	private Map<String,Column> columns;
	private List<String> columnNames;
	
	private Map<String,Integer> intValues;
	private Map<String, Boolean> booleanValues;
	private Map<String,LocalDate> dateValues;
	private Map<String,Double> doubleValues;
	private Map<String,Long> longValues;
	private Map<String,String> stringValues;
	private Map<String,LocalDateTime> timeValues;
	private Map<String,Object> objectValues;
	
	protected Row(int index, Map<String,Column> columns, List<String> columnNames, TullFrame parentFrame){
		this.index = index;
		this.columns = columns;
		this.columnNames = columnNames;
		this.parentFrame = parentFrame;
	}
	/**
	 * This method will cause all values in the row to become fixed and detached from the parent frame.
	 * Subsequent calls will refresh the values from the parent frame.
	 * It's some what wasteful of memory, so I don't suggest using it if you
	 * don't need to detach the row or make a huge number of repeated calls to the same row.
	 */
	public void cacheRowValues(){
		this.intValues = new HashMap<String, Integer>();
		this.booleanValues = new HashMap<String, Boolean>();
		this.dateValues = new HashMap<String, LocalDate>();
		this.doubleValues = new HashMap<String, Double>();
		this.longValues = new HashMap<String, Long>();
		this.stringValues = new HashMap<String, String>();
		this.timeValues = new HashMap<String, LocalDateTime>();
		this.objectValues = new HashMap<String, Object>();
		for(String colName: columnNames){
			try{
				this.intValues.put(colName, this.getInt(colName));
			}catch(ColumnTypeMismatchException e){}
			try{
				this.booleanValues.put(colName, this.getBoolean(colName));
			}catch(ColumnTypeMismatchException e){}
			try{
				this.dateValues.put(colName, this.getDate(colName));
			}catch(ColumnTypeMismatchException e){}
			try{
				this.doubleValues.put(colName, this.getDouble(colName));
			}catch(ColumnTypeMismatchException e){}
			try{
				this.longValues.put(colName, this.getLong(colName));
			}catch(ColumnTypeMismatchException e){}
			try{
				this.stringValues.put(colName, this.getString(colName));
			}catch(ColumnTypeMismatchException e){}
			try{
				this.timeValues.put(colName, this.getTime(colName));
			}catch(ColumnTypeMismatchException e){}
			try{
				this.objectValues.put(colName, this.getValue(colName));
			}catch(ColumnTypeMismatchException e){}
		}
	}
	public int getRowNumber(){
		return parentFrame.indexToRowNum(index);
	}
	protected int getIndex(){
		return this.index;
	}
	public Integer getInt(String columnName){
		throwIfNoKey(columnName);
		if(intValues != null && intValues.containsKey(columnName)){
			return intValues.get(columnName);
		}
		return columns.get(columnName).getInt(index);
	}
	public LocalDate getDate(String columnName){
		throwIfNoKey(columnName);
		if(dateValues != null && dateValues.containsKey(columnName)){
			return dateValues.get(columnName);
		}
		return columns.get(columnName).getDate(index);
	}
	public LocalDateTime getTime(String columnName){
		throwIfNoKey(columnName);
		if(timeValues != null && timeValues.containsKey(columnName)){
			return timeValues.get(columnName);
		}
		return columns.get(columnName).getTime(index);
	}
	public String getString(String columnName){
		throwIfNoKey(columnName);
		if(stringValues != null && stringValues.containsKey(columnName)){
			return stringValues.get(columnName);
		}
		return columns.get(columnName).getString(index);
	}
	public Boolean getBoolean(String columnName){
		throwIfNoKey(columnName);
		if(booleanValues != null && booleanValues.containsKey(columnName)){
			return booleanValues.get(columnName);
		}
		return columns.get(columnName).getBoolean(index);
	}
	public Long getLong(String columnName){
		throwIfNoKey(columnName);
		if(longValues != null && longValues.containsKey(columnName)){
			return longValues.get(columnName);
		}
		return columns.get(columnName).getLong(index);
	}
	public Double getDouble(String columnName){
		throwIfNoKey(columnName);
		if(doubleValues != null && doubleValues.containsKey(columnName)){
			return doubleValues.get(columnName);
		}
		return columns.get(columnName).getDouble(index);
	}
	public Object getValue(String columnName){
		throwIfNoKey(columnName);
		if(intValues != null && intValues.containsKey(columnName)){
			return intValues.get(columnName);
		}
		return columns.get(columnName).getValue(index);
	}
	public ColumnType getColumnType(String columnName){
		throwIfNoKey(columnName);
		return columns.get(columnName).getColumnType();
	}
	public Boolean hasColumn(String columnName){
		return columns.containsKey(columnName);
	}
	protected Column getColumn(String columnName){
		throwIfNoKey(columnName);
		return columns.get(columnName);
	}
	protected String[] toStringArray(){
		String[] values = new String[columnNames.size()];
		for(int i = 0; i < values.length; i++){
			values[i] = columns.get(columnNames.get(i)).getString(index);
		}
		return values;
	}
	private void throwIfNoKey(String columnName){
		if (!columnNames.contains(columnName))
			throw new ColumnNameException("The key " + columnName + " is not in the dataframe.");
		
	}
}
