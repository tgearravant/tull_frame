package com.gearreald.tullframe.interfaces;

import com.gearreald.tullframe.Row;

@FunctionalInterface
public interface Filter {
	public boolean condition(Row r);
}
