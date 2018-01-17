package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.Map;

import com.gearreald.tullframe.exceptions.BooleanParseException;
import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class BooleanColumn extends Column {
	
	private static final long serialVersionUID = -3031544693642350198L;
	private final static String[] POTENTIAL_TRUE_VALUES = {"true","t","1","y","yes"};
	private final static String[] POTENTIAL_FALSE_VALUES = {"false","f","0","n","no"};
	
	private Map<Integer,Boolean> values;

	public BooleanColumn(){
		values = new HashMap<Integer,Boolean>();
	}
	@Override
	public Boolean getBoolean(int index) {
		return values.get(index);
	}
	@Override
	public Object getValue(int index) {
		return getBoolean(index);
	}
	@Override
	public Object setValue(int index, String value){
		try{
			return this.setValue(index, parseBoolean(value));
		}catch(BooleanParseException e){
			throw new ColumnTypeMismatchException(String.format("The value %s is not a boolean.", value), e);
		}
	}
	@Override
	public Object setValue(int index, Boolean value){
		this.values.put(index, value);
		return value;
	}
	@Override
	protected Map<Integer, ? extends Object> getBackingMap() {
		return values;
	}
	@Override
	public ColumnType getColumnType() {
		return ColumnType.BOOLEAN;
	}
	protected static Boolean parseBoolean(String value) throws BooleanParseException{
		if(value == null)
			return null;
		for(String s: POTENTIAL_TRUE_VALUES){
			if (value.toLowerCase().equals(s)){
				return true;
			}
		}
		for(String s: POTENTIAL_FALSE_VALUES){
			if (value.toLowerCase().equals(s)){
				return false;
			}
		}
		throw new BooleanParseException();
	}
}
