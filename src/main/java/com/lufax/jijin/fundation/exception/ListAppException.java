package com.lufax.jijin.fundation.exception;

/**
 * list app调用异常
 * @author chenqunhui
 *
 */
public class ListAppException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6845236331061568468L;
	
	private String retCode;
	
	private String retMessage;

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMessage() {
		return retMessage;
	}

	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}
	
	public ListAppException(){
		
	}
	
	public ListAppException(String retCode,String retMessage){
		super(retMessage);
		this.retCode = retCode;
		this.retMessage = retMessage;
	}

}
