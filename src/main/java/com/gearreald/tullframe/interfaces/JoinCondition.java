package com.gearreald.tullframe.interfaces;

import com.gearreald.tullframe.Row;

@FunctionalInterface
public interface JoinCondition {
	public boolean condition(Row leftRow, Row rightRow);
}
