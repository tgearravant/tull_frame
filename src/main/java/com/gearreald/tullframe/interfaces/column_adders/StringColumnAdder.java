package com.gearreald.tullframe.interfaces.column_adders;

import com.gearreald.tullframe.Row;

@FunctionalInterface
public interface StringColumnAdder {
	public String calculation(Row r);
}
