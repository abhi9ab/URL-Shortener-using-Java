package com.shortenurl.exceptions;

public class DataNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DataNotFoundException(String msg) {
		super(msg);
	}

}
