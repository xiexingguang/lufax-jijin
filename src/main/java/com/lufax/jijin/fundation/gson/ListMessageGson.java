package com.lufax.jijin.fundation.gson;

public class ListMessageGson {

	/*private JijinStatusChangeMsgGson originalMsg;

	public JijinStatusChangeMsgGson getOriginalMsg() {
		return originalMsg;
	}

	public void setOriginalMsg(JijinStatusChangeMsgGson originalMsg) {
		this.originalMsg = originalMsg;
	}*/
	private boolean successFlag;
	
	private String originalMsg;

	public String getOriginalMsg() {
		return originalMsg;
	}

	public void setOriginalMsg(String originalMsg) {
		this.originalMsg = originalMsg;
	}

	public boolean isSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(boolean successFlag) {
		this.successFlag = successFlag;
	}
	
	
	
	
	
}
