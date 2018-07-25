package com.gearreald.tullframe.interfaces.column_adders;

import java.time.LocalDateTime;

import com.gearreald.tullframe.Row;

@FunctionalInterface
public interface TimeColumnAdder {
	public LocalDateTime calculation(Row r);
}
