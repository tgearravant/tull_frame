package com.gearreald.tullframe.columns;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class DateColumn extends Column {
	
	private static final long serialVersionUID = -9145953491716571136L;
	private Map<Integer, LocalDate> values;

	public DateColumn(){
		values = new HashMap<Integer, LocalDate>();
	}
	
	@Override
	public LocalDate getDate(int index) {
		return values.get(index);
	}

	@Override
	public Object getValue(int index) {
		return getDate(index);
	}
	
	@Override
	public Object setValue(int index, String value){
		try{
			return this.setValue(index, LocalDate.parse(value));
		}catch (DateTimeParseException e){
			throw new ColumnTypeMismatchException(String.format("The value %s at index %d is not a properly formatted date.", value, index), e);
		}
	}
	@Override
	public Object setValue(int index, LocalDate value){
		this.values.put(index, value);
		return value;
	}
	@Override
	protected Map<Integer, ? extends Object> getBackingMap() {
		return values;
	}
	@Override
	public ColumnType getColumnType() {
		return ColumnType.DATE;
	}
}
