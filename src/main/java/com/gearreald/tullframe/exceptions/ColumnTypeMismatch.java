package com.gearreald.tullframe.exceptions;

public class ColumnTypeMismatch extends Exception {

	private static final long serialVersionUID = -2150466734520998179L;
	
	public ColumnTypeMismatch() {
		super();
	}
	public ColumnTypeMismatch(String message) {
		super(message);
	}

	public ColumnTypeMismatch(Throwable cause) {
		super(cause);
	}

	public ColumnTypeMismatch(String message, Throwable cause) {
		super(message, cause);
	}
}
