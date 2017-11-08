package com.gearreald.tullframe.columns;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public abstract class Column {
	
	public static final int QUICK_TYPE_VALUES = 5;

	public abstract int getInt(int index);
	public abstract LocalDate getDate(int index);
	public abstract LocalDateTime getTime(int index);
	public abstract String getString(int index);
	public abstract boolean getBoolean(int index);
	public abstract long getLong(int index);
	public abstract double getDouble(int index);
	public abstract Object getValue(int index);
	
	public int optInt(int index){
		try{
			return getInt(index);
		}catch(ColumnTypeMismatchException e){
			return 0;
		}
	}
	public LocalDate optDate(int index) {
		try{
			return getDate(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public LocalDateTime optTime(int index){
		try{
			return getTime(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public String optString(int index){
		try{
			return getString(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	public boolean optBoolean(int index){
		try{
			return getBoolean(index);
		}catch(ColumnTypeMismatchException e){
			return false;
		}
	}
	public long optLong(int index){
		try{
			return getLong(index);
		}catch(ColumnTypeMismatchException e){
			return 0L;
		}		
	}
	public double optDouble(int index){
		try{
			return getDouble(index);
		}catch(ColumnTypeMismatchException e){
			return 0d;
		}		
	}
	public Object optValue(int index){
		try{
			return getValue(index);
		}catch(ColumnTypeMismatchException e){
			return null;
		}
	}
	
	public abstract void set(int index, int value);
	public abstract void set(int index, double value);
	public abstract void set(int index, boolean value);
	public abstract void set(int index, LocalDate value);
	public abstract void set(int index, LocalDateTime value);
	public abstract void set(int index, long value);
	public abstract void set(int index, String value);
	public abstract void set(int index, Object value);
	
	public abstract void add(int value);
	public abstract void add(double value);
	public abstract void add(boolean value);
	public abstract void add(LocalDate value);
	public abstract void add(LocalDateTime value);
	public abstract void add(long value);
	public abstract void add(String value);
	public abstract void add(Object value);
	
	
	public ColumnType inferType(){
		return ColumnType.STRING;
	}
	
	public static Column getColumnFromColumnType(ColumnType type){
		if (type.equals(ColumnType.BOOLEAN)){
			return new BooleanColumn();
		}else{
			return new StringColumn();
		}
	}
}
