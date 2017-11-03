package com.gearreald.tullframe.exceptions;

public class ColumnTypeMismatchException extends RuntimeException {

	private static final long serialVersionUID = -2150466734520998179L;
	
	public ColumnTypeMismatchException() {
		super();
	}
	public ColumnTypeMismatchException(String message) {
		super(message);
	}

	public ColumnTypeMismatchException(Throwable cause) {
		super(cause);
	}

	public ColumnTypeMismatchException(String message, Throwable cause) {
		super(message, cause);
	}
}
