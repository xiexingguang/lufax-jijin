package com.lufax.jijin.fundation.remote.gson.request;


public class PaymentInfo {
	
	private String paymentOrderNo;
	private String frozenCode;
	private String action;
	private String unfreezeTransactionType;
	private String unfreezeRemark;
	private SubTractInfo subtractInfo;
	private RevokeInfo revokeInfo;

	public PaymentInfo(String paymentOrderNo, String frozenCode, String action, String unfreezeTransactionType, String unfreezeRemark,
			SubTractInfo subtractInfo,RevokeInfo revokeInfo){
		
		this.paymentOrderNo = paymentOrderNo;	
		this.frozenCode = frozenCode;
		this.action = action;
		this.unfreezeTransactionType = unfreezeTransactionType;
		this.unfreezeRemark = unfreezeRemark;
		this.subtractInfo = subtractInfo;
		this.revokeInfo = revokeInfo;
	
	}
	
	public String getPaymentOrderNo() {
		return paymentOrderNo;
	}
	public void setPaymentOrderNo(String paymentOrderNo) {
		this.paymentOrderNo = paymentOrderNo;
	}
	public String getFrozenCode() {
		return frozenCode;
	}
	public void setFrozenCode(String frozenCode) {
		this.frozenCode = frozenCode;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUnfreezeTransactionType() {
		return unfreezeTransactionType;
	}
	public void setUnfreezeTransactionType(String unfreezeTransactionType) {
		this.unfreezeTransactionType = unfreezeTransactionType;
	}
	public String getUnfreezeRemark() {
		return unfreezeRemark;
	}
	public void setUnfreezeRemark(String unfreezeRemark) {
		this.unfreezeRemark = unfreezeRemark;
	}
	public SubTractInfo getSubtractInfo() {
		return subtractInfo;
	}
	public void setSubtractInfo(SubTractInfo subtractInfo) {
		this.subtractInfo = subtractInfo;
	}
	
	public RevokeInfo getRevokeInfo() {
		return revokeInfo;
	}

	public void setRevokeInfo(RevokeInfo revokeInfo) {
		this.revokeInfo = revokeInfo;
	}


}
