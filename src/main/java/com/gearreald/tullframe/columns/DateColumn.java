package com.gearreald.tullframe.columns;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;
import com.gearreald.tullframe.utils.ColumnType;

public class DateColumn extends Column {
	
	private static final long serialVersionUID = -9145953491716571136L;
	private ArrayList<LocalDate> values;

	public DateColumn(){
		values = new ArrayList<LocalDate>(ORIGINAL_ARRAY_LIST_CAPACITY);
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
		}catch (NullPointerException e){
			LocalDate i = null;
			return this.setValue(index, i);
		}
	}
	@Override
	public Object setValue(int index, LocalDate value){
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
		return ColumnType.DATE;
	}
}
