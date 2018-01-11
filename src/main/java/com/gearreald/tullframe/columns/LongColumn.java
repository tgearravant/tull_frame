package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.Map;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class LongColumn extends Column {
	
	private static final long serialVersionUID = 468609121746258096L;
	private Map<Integer,Long> values;
	
	public LongColumn(){
		values = new HashMap<Integer,Long>();
	}

	@Override
	public Long getLong(int index) {
		return values.get(index);
	}

	@Override
	public Object getValue(int index) {
		return getLong(index);
	}
	@Override
	public void setValue(int index, String value){
		try{
			this.setValue(index, Long.parseLong(value));
		}catch (NumberFormatException e){
			throw new ColumnTypeMismatchException(String.format("The value %s at index %d is not a long.", value, index), e);
		}
	}
	@Override
	public void setValue(int index, long value){
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
