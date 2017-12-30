package com.gearreald.tullframe.columns;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.exceptions.UnimplementedException;
import com.gearreald.tullframe.utils.ColumnType;

public abstract class Column {
	
	public static final int QUICK_TYPE_VALUES = 5;

	public int getInt(int index){
		throw new ColumnTypeMismatchException("This is not an integer column.");
	}
	public LocalDate getDate(int index){
		throw new ColumnTypeMismatchException("This is not a date column.");
	}
	public LocalDateTime getTime(int index){
		throw new ColumnTypeMismatchException("This is not a time column.");
	}
	public String getString(int index){
		return this.getValue(index).toString();
	}
	public boolean getBoolean(int index){
		throw new ColumnTypeMismatchException("This is not a boolean column.");
	}
	public long getLong(int index) {
		throw new ColumnTypeMismatchException("This is not a long column.");
	}
	public double getDouble(int index){
		throw new ColumnTypeMismatchException("This is not a double column.");
	}
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
	
	public void set(int index, int value){
		throw new ColumnTypeMismatchException("This is not an integer column.");
	}
	public void set(int index, double value){
		throw new ColumnTypeMismatchException("This is not a double column.");
	}
	public void set(int index, boolean value){
		throw new ColumnTypeMismatchException("This is not a boolean column.");
	}
	public void set(int index, LocalDate value){
		throw new ColumnTypeMismatchException("This is not a date column.");
	}
	public void set(int index, LocalDateTime value){
		throw new ColumnTypeMismatchException("This is not a time column.");
	}
	public void set(int index, long value){
		throw new ColumnTypeMismatchException("This is not a long column.");
	}
	public void set(int index, String value){
		throw new ColumnTypeMismatchException("This is not a string column.");
	}
	public void set(int index, Object value){
		set(index, value.toString());
	}
	
	protected abstract Map<Integer,? extends Object> getBackingMap(); 
	
	public Object removeIndex(int index){
		return getBackingMap().remove(index);
	}
	
	public ColumnType inferType(){
		boolean isBool = true;
		boolean isDate = true;
		boolean isTime = true;
		boolean isInt = true;
		boolean isLong = true;
		boolean isDouble = true;
		boolean hasNonNullValues = false;
		
		if (this.getColumnType().equals(ColumnType.STRING))
			return this.getColumnType();
		
		Map<Integer, ? extends Object> valueMap = getBackingMap();
		for(int i: valueMap.keySet()){
			Object o = valueMap.get(i);
			hasNonNullValues = true;
			if (o == null){				
				continue;
			}
			hasNonNullValues = true;
			String value = o.toString();
			if(!isLong){
				try{
					long l = Long.parseLong(value);
					if (l > Integer.MAX_VALUE) {
						isInt = false;
					}
				}catch(NumberFormatException e){
					isLong = false;
					isInt = false;
				}
			}
			if(!isDouble){
				try{
					Double.parseDouble(value);
				}catch(NumberFormatException e){
					isDouble = false;
				}
			}
			if(!isDate){
				try{
					LocalDate.parse(value);
				}catch(DateTimeParseException e){
					isDate = false;
				}
			}if(!isTime){
				try{
					LocalDateTime.parse(value);
				}catch(DateTimeParseException e){
					isTime = false;
				}
			}
			if(booleanAdder(isBool, isDate, isTime, isInt, isLong, isDouble) == 0)
				break;
		}
		if(!hasNonNullValues){
			return ColumnType.STRING;
		}else if(isTime){
			return ColumnType.TIME;
		}else if(isDate){
			return ColumnType.DATE;
		}else if(isInt){
			return ColumnType.INTEGER;
		}else if(isLong){
			return ColumnType.LONG;
		}else if(isDouble){
			return ColumnType.DOUBLE;
		}else if(isBool){
			return ColumnType.BOOLEAN;
		}else{
			return ColumnType.STRING;
		}
	}
	private static int booleanAdder(boolean... bs){
		int currentCount = 0;
		for(boolean b: bs){
			if(b)
				currentCount++;
		}
		return currentCount;
	}
	
	public abstract ColumnType getColumnType();
	
	public static Column getColumnFromColumnType(ColumnType type){
		switch (type){
		case BOOLEAN:
			return new BooleanColumn();
		case DATE:
			return new DateColumn();
		case TIME:
			return new TimeColumn();
		case LONG:
			return new LongColumn();
		case INTEGER:
			return new IntegerColumn();
		case DOUBLE:
			return new DoubleColumn();
		case STRING:
			return new StringColumn();
		default:
			throw new UnimplementedException("ColumnType is unsupported. I honestly have no idea how you got this error... I'd love to know, though. :)");
		}
	}
}
