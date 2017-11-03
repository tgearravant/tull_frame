package com.gearreald.tullframe;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Column {

	public int getInt(int index);
	public int optInt(int index);
	public LocalDate getDate(int index);
	public LocalDate optDate(int index);
	public LocalTime getTime(int index);
	public LocalTime optTime(int index);
	public String getString(int index);
	public String optString(int index);
	public boolean getBoolean(int index);
	public boolean optBoolean(int index);
	public long getLong(int index);
	public long optLong(int index);
	
}
