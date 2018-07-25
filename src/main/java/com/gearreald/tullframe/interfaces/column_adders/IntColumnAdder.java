package com.gearreald.tullframe.interfaces.column_adders;

import com.gearreald.tullframe.Row;

@FunctionalInterface
public interface IntColumnAdder {
	public Integer calculation(Row r);
}
