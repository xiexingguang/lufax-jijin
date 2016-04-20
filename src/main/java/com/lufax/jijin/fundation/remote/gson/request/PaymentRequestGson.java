package com.lufax.jijin.fundation.remote.gson.request;

public class PaymentRequestGson {
	
	private String channelId;
	private String instructionNo;
	private PaymentInfo paymentInfo;
	
	public PaymentRequestGson(String channelId, String instructionNo, PaymentInfo paymentInfo){
		this.channelId = channelId;
		this.instructionNo = instructionNo;
		this.paymentInfo = paymentInfo;
	}
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getInstructionNo() {
		return instructionNo;
	}
	public void setInstructionNo(String instructionNo) {
		this.instructionNo = instructionNo;
	}
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	

}
