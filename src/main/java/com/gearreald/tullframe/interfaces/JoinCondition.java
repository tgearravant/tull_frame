package com.gearreald.tullframe.interfaces;

import com.gearreald.tullframe.Row;

public interface JoinCondition {
	public boolean condition(Row leftRow, Row rightRow);
}
