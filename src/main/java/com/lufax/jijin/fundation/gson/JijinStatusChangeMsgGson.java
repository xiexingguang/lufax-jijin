package com.lufax.jijin.fundation.gson;

public class JijinStatusChangeMsgGson {

	private String code;//BUS_JIJIN_INFO productCode

	private String beOldStatus;

	private String beNewStatus;

	private String beOperationType; //0:更改购买状态，1：更改赎回状态

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBeOldStatus() {
		return beOldStatus;
	}

	public void setBeOldStatus(String beOldStatus) {
		this.beOldStatus = beOldStatus;
	}

	public String getBeNewStatus() {
		return beNewStatus;
	}

	public void setBeNewStatus(String beNewStatus) {
		this.beNewStatus = beNewStatus;
	}

	public String getBeOperationType() {
		return beOperationType;
	}

	public void setBeOperationType(String beOperationType) {
		this.beOperationType = beOperationType;
	}


	
	
	
	
}
