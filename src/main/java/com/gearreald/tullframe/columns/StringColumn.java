package com.gearreald.tullframe.columns;

import java.util.HashMap;
import java.util.Map;

import com.gearreald.tullframe.utils.ColumnType;

public class StringColumn extends Column {
	
	private Map<Integer, String> values;

	public StringColumn(){
		values = new HashMap<Integer, String>();
	}
	
	@Override
	public Object getValue(int index) {
		return values.get(index);
	}
	@Override
	public void set(int index, String value){
		this.values.put(index, value);
	}

	@Override
	protected Map<Integer, ? extends Object> getBackingMap() {
		return values;
	}

	@Override
	public ColumnType getColumnType() {
		return ColumnType.STRING;
	}
}
