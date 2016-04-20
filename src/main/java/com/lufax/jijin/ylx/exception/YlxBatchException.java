package com.lufax.jijin.ylx.exception;

public class YlxBatchException extends RuntimeException {
	
	private int errorCode;
	
	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public YlxBatchException() {
	}
	
	public YlxBatchException(int errorCode, String msg) {
		super(msg);
		this.errorCode= errorCode;
	}

	public YlxBatchException(Throwable t) {
		super(t);
	}

	public YlxBatchException(String msg) {
		super(msg);
	}

	public YlxBatchException(String msg, Throwable t) {
		super(msg, t);
	}
}
