package com.gearreald.tullframe;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public abstract class Column {
	
	public static final int QUICK_TYPE_VALUES = 5;

	public abstract int getInt(int index);
	public int optInt(int index){
		try{
			return getInt(index);
		}catch(ColumnTypeMismatchException e){
			return 0;
		}
	}
	public abstract LocalDate getDate(int index);
	public LocalDate optDate(int index) {
		try{
			return getDate(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public abstract LocalDateTime getTime(int index);
	public LocalDateTime optTime(int index){
		try{
			return getTime(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public abstract String getString(int index);
	public String optString(int index){
		try{
			return getString(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public abstract boolean getBoolean(int index);
	public boolean optBoolean(int index){
		try{
			return getBoolean(index);
		}catch(ColumnTypeMismatchException e){
			return false;
		}
	}
	public abstract long getLong(int index);
	public long optLong(int index){
		try{
			return getLong(index);
		}catch(ColumnTypeMismatchException e){
			return 0L;
		}		
	}
	public abstract Object getValue(int index);
	public Object optValue(int index){
		try{
			return getValue(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	protected abstract Iterable<Integer> getIndices();
	public ColumnType inferTypeQuick(){
		// Get the first few existing values
		Iterator<Integer> i = getIndices().iterator();
		List<Object> values = new ArrayList<Object>();
		while(i.hasNext() && values.size() <= QUICK_TYPE_VALUES){
			
		}
		return ColumnType.STRING;
	}
	public ColumnType inferType(){
		return ColumnType.STRING;
	}
}
