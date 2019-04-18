package com.lvsen.domain;

public class SearchFilterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
	
	public SearchFilterException() {
		super();
	}

	public SearchFilterException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SearchFilterException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchFilterException(String message) {
		super(message);
	}

	public SearchFilterException(Throwable cause) {
		super(cause);
	}
}
