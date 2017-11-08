package com.gearreald.tullframe.utils;

import com.gearreald.tullframe.exceptions.ColumnTypeMismatchException;

public class TypeConverter {

	public boolean getObjectAsBoolean(Object o){
		if (o instanceof Boolean){
			return (Boolean) o;
		}
		String s = o.toString();
		if(s.toLowerCase().equals("true") || s.equals("1") || s.toLowerCase().equals("yes")){
			return true;
		}
		if(s.toLowerCase().equals("false") || s.equals("0") || s.toLowerCase().equals("no")){
			return false;
		}
		throw new ColumnTypeMismatchException("The object cannot be coerced into a boolean.");
	}
}
