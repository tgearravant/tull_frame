package com.gearreald.tullframe.exceptions;

public class UnimplementedException extends UnsupportedOperationException {

	private static final long serialVersionUID = -2150466734520998179L;
	
	public UnimplementedException() {
		super();
	}
	public UnimplementedException(String message) {
		super(message);
	}

	public UnimplementedException(Throwable cause) {
		super(cause);
	}

	public UnimplementedException(String message, Throwable cause) {
		super(message, cause);
	}
}
