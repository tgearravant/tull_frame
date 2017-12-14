package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.Map;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class LongColumn extends Column {
	
	private Map<Integer,Long> values;
	
	public LongColumn(){
		values = new HashMap<Integer,Long>();
	}

	@Override
	public long getLong(int index) {
		return values.get(index);
	}

	@Override
	public Object getValue(int index) {
		return getLong(index);
	}
	@Override
	public void set(int index, String value){
		try{
			this.set(index, Long.parseLong(value));
		}catch (NumberFormatException e){
			throw new ColumnTypeMismatchException(String.format("The value %s at index %d is not a long.", value, index), e);
		}
	}
	@Override
	public void set(int index, long value){
		this.values.put(index, value);
	}
	@Override
	protected Map<Integer, ? extends Object> getBackingMap() {
		return values;
	}
	@Override
	public ColumnType getColumnType() {
		return ColumnType.LONG;
	}
}
