package com.gearreald.tullframe.exceptions;

public class BooleanParseException extends Exception {

	private static final long serialVersionUID = 230843586553816249L;

	public BooleanParseException() {
		super();
	}
	public BooleanParseException(String message) {
		super(message);
	}

	public BooleanParseException(Throwable cause) {
		super(cause);
	}

	public BooleanParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
