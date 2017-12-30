package com.gearreald.tullframe.exceptions;

public class TullFrameException extends RuntimeException {

	private static final long serialVersionUID = 230843586553816249L;

	public TullFrameException() {
		super();
	}
	public TullFrameException(String message) {
		super(message);
	}

	public TullFrameException(Throwable cause) {
		super(cause);
	}

	public TullFrameException(String message, Throwable cause) {
		super(message, cause);
	}
}
