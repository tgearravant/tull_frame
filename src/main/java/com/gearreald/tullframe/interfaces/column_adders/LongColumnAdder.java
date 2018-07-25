package com.gearreald.tullframe.interfaces.column_adders;

import com.gearreald.tullframe.Row;

@FunctionalInterface
public interface LongColumnAdder {
	public Long calculation(Row r);
}
