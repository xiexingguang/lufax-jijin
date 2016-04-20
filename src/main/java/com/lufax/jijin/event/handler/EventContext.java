package com.lufax.jijin.event.handler;



public class EventContext {


	private String message;

	public EventContext(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
