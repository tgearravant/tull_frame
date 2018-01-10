package com.gearreald.tullframe.exceptions;

public class IndexException extends RuntimeException {

	private static final long serialVersionUID = 230843586553816249L;

	public IndexException() {
		super();
	}
	public IndexException(String message) {
		super(message);
	}

	public IndexException(Throwable cause) {
		super(cause);
	}

	public IndexException(String message, Throwable cause) {
		super(message, cause);
	}
}
