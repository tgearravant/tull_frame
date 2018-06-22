package com.gearreald.tullframe.columns;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;


public class TimeColumn extends Column {
	
	private static final long serialVersionUID = -764806219465810839L;
	private ArrayList<LocalDateTime> values;

	public TimeColumn(){
		values = new ArrayList<LocalDateTime>(ORIGINAL_ARRAY_LIST_CAPACITY);
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
		return ColumnType.TIME;
	}
}
