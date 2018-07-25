package com.gearreald.tullframe.interfaces.column_adders;

import com.gearreald.tullframe.Row;

@FunctionalInterface
public interface DoubleColumnAdder {
	public Double calculation(Row r);
}
