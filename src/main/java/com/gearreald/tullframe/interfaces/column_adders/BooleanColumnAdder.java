package com.gearreald.tullframe.interfaces.column_adders;

import com.gearreald.tullframe.Row;

@FunctionalInterface
public interface BooleanColumnAdder {
	public Boolean calculation(Row r);
}
