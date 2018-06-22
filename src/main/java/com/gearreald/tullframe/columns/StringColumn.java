package com.gearreald.tullframe.columns;

import java.util.ArrayList;
import java.util.List;

import com.gearreald.tullframe.utils.ColumnType;

public class StringColumn extends Column {
	
	private static final long serialVersionUID = -4258592413320365821L;
	private ArrayList<String> values;

	public StringColumn(){
		values = new ArrayList<String>(ORIGINAL_ARRAY_LIST_CAPACITY);
	}
	
	@Override
	public Object getValue(int index) {
		return values.get(index);
	}
	@Override
	public Object setValue(int index, String value){
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
		return ColumnType.STRING;
	}
}
