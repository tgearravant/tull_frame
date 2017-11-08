package com.gearreald.tullframe.exceptions;

public class DataFrameException extends RuntimeException {

	private static final long serialVersionUID = 230843586553816249L;

	public DataFrameException() {
		super();
	}
	public DataFrameException(String message) {
		super(message);
	}

	public DataFrameException(Throwable cause) {
		super(cause);
	}

	public DataFrameException(String message, Throwable cause) {
		super(message, cause);
	}
}
