package com.gearreald.tullframe.interfaces.column_adders;

import java.time.LocalDate;

import com.gearreald.tullframe.Row;

@FunctionalInterface
public interface DateColumnAdder {
	public LocalDate calculation(Row r);
}
