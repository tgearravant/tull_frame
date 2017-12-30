package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.Map;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class DoubleColumn extends Column {
	
	private Map<Integer,Double> values;

	public DoubleColumn(){
		values = new HashMap<Integer,Double>();
	}
	
	@Override
	public double getDouble(int index) {
		return values.get(index);
	}

	@Override
	public Object getValue(int index) {
		return getDouble(index);
	}
	@Override
	public void set(int index, String value){
		try{
			this.set(index, Double.parseDouble(value));
		}catch (NumberFormatException e){
			throw new ColumnTypeMismatchException(String.format("The value %s at index %d is not a double.", value, index), e);
		}
	}
	@Override
	public void set(int index, double value){
		this.values.put(index, value);
	}
	@Override
	protected Map<Integer, ? extends Object> getBackingMap() {
		return values;
	}

	@Override
	public ColumnType getColumnType() {
		return ColumnType.DOUBLE;
	}
}
