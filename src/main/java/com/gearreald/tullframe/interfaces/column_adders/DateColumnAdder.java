package com.gearreald.tullframe.interfaces.column_adders;

import java.time.LocalDate;

import com.gearreald.tullframe.Row;

public interface DateColumnAdder {
	public LocalDate calculation(Row r);
}
