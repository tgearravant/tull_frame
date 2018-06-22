package com.gearreald.tullframe.columns;

import java.util.ArrayList;
import java.util.List;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class DoubleColumn extends Column {
	
	private static final long serialVersionUID = 1291818685093182854L;
	private ArrayList<Double> values;

	public DoubleColumn(){
		values = new ArrayList<Double>(ORIGINAL_ARRAY_LIST_CAPACITY);
	}
	
	@Override
	public Double getDouble(int index) {
		return values.get(index);
	}

	@Override
	public Object getValue(int index) {
		return getDouble(index);
	}
	@Override
	public Object setValue(int index, String value){
		try{
			return this.setValue(index, (value == null ? null : Double.parseDouble(value)));
		}catch (NumberFormatException e){
			throw new ColumnTypeMismatchException(String.format("The value %s at index %d is not a double.", value, index), e);
		}catch (NullPointerException e){
			Double i = null;
			return this.setValue(index, i);
		}
	}
	@Override
	public Object setValue(int index, Double value){
		assureArrayListSize(index, this.values);
		this.values.set(index, value);
		return value;
	}
	@Override
	protected List<? extends Object> getBackingList() {
		return values;
	}

	@Override
	public ColumnType getColumnType() {
		return ColumnType.DOUBLE;
	}
}
