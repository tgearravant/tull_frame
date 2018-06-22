package com.gearreald.tullframe.columns;

import java.util.ArrayList;
import java.util.List;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class IntegerColumn extends Column {
	
	private static final long serialVersionUID = -8952095090836479665L;
	private ArrayList<Integer> values;

	public IntegerColumn(){
		values = new ArrayList<Integer>(ORIGINAL_ARRAY_LIST_CAPACITY);
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
	public Object setValue(int index, String value){
		try{
			return this.setValue(index, (value == null ? null : Integer.parseInt(value)));
		}catch (NumberFormatException e){
			throw new ColumnTypeMismatchException(String.format("The value %s at index %d is not an int.", value, index), e);
		}catch (NullPointerException e){
			Integer i = null;
			return this.setValue(index, i);
		}
	}
	@Override
	public Object setValue(int index, Integer value){
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
		return ColumnType.INTEGER;
	}
}
