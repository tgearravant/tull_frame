package com.gearreald.tullframe.columns;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;

public class BooleanColumn extends Column {
	
	private ArrayList<Boolean> values;

	@Override
	public int getInt(int index) {
		throw new ColumnTypeMismatchException("This is not an integer column.");
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
		return values.get(index);
	}

	@Override
	public long getLong(int index) {
		throw new ColumnTypeMismatchException("This is not a long column.");
	}

	@Override
	public Object getValue(int index) {
		return values.get(index);
	}
}
