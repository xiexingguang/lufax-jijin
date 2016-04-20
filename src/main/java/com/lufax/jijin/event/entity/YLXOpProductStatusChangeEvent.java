package com.lufax.jijin.event.entity;

public class YLXOpProductStatusChangeEvent {
	private long productId;
	
	private String productCode;
	
	private String remoteSystemOldStatus;
	
	private String remoteSystemNewStatus;
	
	private String successFlag;
	
	private String firstTime;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getRemoteSystemOldStatus() {
		return remoteSystemOldStatus;
	}

	public void setRemoteSystemOldStatus(String remoteSystemOldStatus) {
		this.remoteSystemOldStatus = remoteSystemOldStatus;
	}

	public String getRemoteSystemNewStatus() {
		return remoteSystemNewStatus;
	}

	public void setRemoteSystemNewStatus(String remoteSystemNewStatus) {
		this.remoteSystemNewStatus = remoteSystemNewStatus;
	}

	public String getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}

	public String getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(String firstTime) {
		this.firstTime = firstTime;
	}
}
