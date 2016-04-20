package com.lufax.jijin.daixiao.gson;

public class JijinUpdateStatusGson {


	private String code;// :"3333333",//Product Code
	private boolean isEffectiveImmediately = true;
	private String beOperationType; // //操作类型：0 修改产品状态， 1修改赎回的状态
	private String beNewStatus;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public boolean isEffectiveImmediately() {
		return isEffectiveImmediately;
	}
	public void setEffectiveImmediately(boolean isEffectiveImmediately) {
		this.isEffectiveImmediately = isEffectiveImmediately;
	}

	public String getBeOperationType() {
		return beOperationType;
	}
	public void setBeOperationType(String beOperationType) {
		this.beOperationType = beOperationType;
	}
	public String getBeNewStatus() {
		return beNewStatus;
	}
	public void setBeNewStatus(String beNewStatus) {
		this.beNewStatus = beNewStatus;
	}
	
	
}
