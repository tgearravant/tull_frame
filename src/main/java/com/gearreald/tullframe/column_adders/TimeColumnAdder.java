package com.gearreald.tullframe.column_adders;

import java.time.LocalDateTime;

import com.gearreald.tullframe.Row;

public interface TimeColumnAdder {
	public LocalDateTime calculation(Row r);
}
