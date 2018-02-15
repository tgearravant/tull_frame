package com.gearreald.tullframe.columns;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;


public class TimeColumn extends Column {
	
	private static final long serialVersionUID = -764806219465810839L;
	private Map<Integer, LocalDateTime> values;

	public TimeColumn(){
		values = new HashMap<Integer, LocalDateTime>();
	}
	@Override
	public LocalDateTime getTime(int index) {
		return values.get(index);
	}

	@Override
	public Object getValue(int index) {
		return getTime(index);
	}
	@Override
	public Object setValue(int index, String value){
		try{
			return this.setValue(index, LocalDateTime.parse(value));
		}catch (DateTimeParseException e){
			throw new ColumnTypeMismatchException(String.format("The value %s at index %d is not a properly formatted date time.", value, index), e);
		}catch (NullPointerException e){
			LocalDateTime i = null;
			return this.setValue(index, i);
		}
	}
	@Override
	public Object setValue(int index, LocalDateTime value){
		this.values.put(index, value);
		return value;
	}
	@Override
	protected Map<Integer, ? extends Object> getBackingMap() {
		return values;
	}
	@Override
	public ColumnType getColumnType() {
		return ColumnType.TIME;
	}
}
