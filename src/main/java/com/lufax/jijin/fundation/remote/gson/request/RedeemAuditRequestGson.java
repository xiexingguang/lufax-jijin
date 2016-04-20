package com.lufax.jijin.fundation.remote.gson.request;

import java.math.BigDecimal;

public class RedeemAuditRequestGson {
	
	
	private String channelId;
	private String recordId;
	private Long userId;
	private String transactionType;
	private String businessRefNo;
	private String vendorCode;
	private BigDecimal transactionAmount;
	private String tradingDate;
	private String productCode;
	private String remark;
	
	public RedeemAuditRequestGson(String channelId, String recordId,Long userId,String transactionType, String businessRefNo, String vendorCode,
			BigDecimal transactionAmount, String tradingDate, String productCode, String remark){
		this.channelId = channelId;
		this.recordId =  recordId;
		this.userId = userId;
		this.transactionType = transactionType;
		this.businessRefNo = businessRefNo;
		this.vendorCode = vendorCode;
		this.transactionAmount = transactionAmount;
		this.tradingDate = tradingDate;
		this.productCode = productCode;
		this.remark = remark;
	}
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getBusinessRefNo() {
		return businessRefNo;
	}
	public void setBusinessRefNo(String businessRefNo) {
		this.businessRefNo = businessRefNo;
	}
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getTradingDate() {
		return tradingDate;
	}
	public void setTradingDate(String tradingDate) {
		this.tradingDate = tradingDate;
	}
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	

}
