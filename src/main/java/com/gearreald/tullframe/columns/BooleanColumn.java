package com.gearreald.tullframe.columns;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import com.gearreald.tullframe.Column;
import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;

public class BooleanColumn extends Column {
	
	HashMap<Integer, Integer> values;

	@Override
	public int getInt(int index) {
		return values.get(index);
	}

	@Override
	public LocalDate getDate(int index) {
		throw new ColumnTypeMismatchException("This is not a date column.");
	}

	@Override
	public LocalDateTime getTime(int index) {
		throw new ColumnTypeMismatchException("This is not a time column.");
	}

	@Override
	public String getString(int index) {
		throw new ColumnTypeMismatchException("This is not a string column.");
	}

	@Override
	public boolean getBoolean(int index) {
		throw new ColumnTypeMismatchException("This is not a boolean column.");
	}

	@Override
	public long getLong(int index) {
		throw new ColumnTypeMismatchException("This is not a long column.");
	}

	@Override
	public Object getValue(int index) {
		return values.get(index);
	}
	
	@Override
	public Iterable<Integer> getIndices() {
		return values.keySet();
	}
}
