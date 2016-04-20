package com.lufax.jijin.fundation.exception;

public class DahuaReidsException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public DahuaReidsException(String msg) {
		super(msg);
	}

	public DahuaReidsException(String message, Throwable cause) {
		super(message, cause);
	}

}
