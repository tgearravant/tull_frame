package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.Map;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class IntegerColumn extends Column {
	
	private static final long serialVersionUID = -8952095090836479665L;
	private Map<Integer,Integer> values;

	public IntegerColumn(){
		values = new HashMap<Integer,Integer>();
	}
	
	@Override
	public Integer getInt(int index) {
		return values.get(index);
	}

	@Override
	public Object getValue(int index) {
		return getInt(index);
	}
	@Override
	public void setValue(int index, String value){
		try{
			this.setValue(index, (value == null ? null : Integer.parseInt(value)));
		}catch (NumberFormatException e){
			throw new ColumnTypeMismatchException(String.format("The value %s at index %d is not an int.", value, index), e);
		}catch (NullPointerException e){
			Integer i = null;
			this.setValue(index, i);
		}
	}
	@Override
	public void setValue(int index, Integer value){
		this.values.put(index, value);
	}
	@Override
	protected Map<Integer, ? extends Object> getBackingMap() {
		return values;
	}

	@Override
	public ColumnType getColumnType() {
		return ColumnType.INTEGER;
	}
}
