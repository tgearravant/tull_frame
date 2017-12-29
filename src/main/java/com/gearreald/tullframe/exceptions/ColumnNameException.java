package com.gearreald.tullframe.exceptions;

public class ColumnNameException extends RuntimeException {

	private static final long serialVersionUID = 230843586553816249L;

	public ColumnNameException() {
		super();
	}
	public ColumnNameException(String message) {
		super(message);
	}

	public ColumnNameException(Throwable cause) {
		super(cause);
	}

	public ColumnNameException(String message, Throwable cause) {
		super(message, cause);
	}
}
