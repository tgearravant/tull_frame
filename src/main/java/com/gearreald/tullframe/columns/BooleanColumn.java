package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.Map;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class BooleanColumn extends Column {
	
	String[] POTENTIAL_TRUE_VALUES = {"true","t","1","y","yes"};
	String[] POTENTIAL_FALSE_VALUES = {"false","f","0","n","no"};
	
	private Map<Integer,Boolean> values;

	public BooleanColumn(){
		values = new HashMap<Integer,Boolean>();
	}
	@Override
	public boolean getBoolean(int index) {
		return values.get(index);
	}
	@Override
	public Object getValue(int index) {
		return getBoolean(index);
	}
	@Override
	public void set(int index, String value){
		boolean matched = false;
		for(String s: POTENTIAL_TRUE_VALUES){
			if (value.toLowerCase().equals(s) && !matched){
				this.set(index, true);
				matched = true;
				break;
			}
		}
		for(String s: POTENTIAL_FALSE_VALUES){
			if (value.toLowerCase().equals(s) && !matched){
				this.set(index, false);
				matched = true;
				break;
			}
		}
		if(!matched){
			throw new ColumnTypeMismatchException(String.format("The value %s is not a boolean.", value));
		}
	}
	@Override
	public void set(int index, boolean value){
		this.values.put(index, value);
	}
	@Override
	protected Map<Integer, ? extends Object> getBackingMap() {
		return values;
	}
	@Override
	public ColumnType getColumnType() {
		return ColumnType.BOOLEAN;
	}
}
