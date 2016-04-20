package com.lufax.jijin.sysFacade.gson;

import java.math.BigDecimal;

/**
 * Payment app, 调增入账请求参数
 * @author XUNENG311
 *
 */
public class PaymentIncreaseRequest {

	private String channelId;
	private String instructionNo;
	private Long userId;
	private String transactionType;
	private String businessRefNo;
	private String vendorCode;
	private BigDecimal transactionAmount;
	private String tradingDate;
	private String remark;
	private String trxDate; //交易日期	 
	private String productCode;
	private String referenceId;//业务参考id（具体值业务系统负责） 
	private String referenceType; //支付相关引用信息，调用方标明	
	private String useCase;// biz业务场景
	private String bizId;//业务id（bizId&bizType要做业务uk，只能成功一笔）
	private String bizType;//业务类型
	
	public PaymentIncreaseRequest(String channelId, String instructionNo,
			Long userId, String transactionType, String businessRefNo,
			String vendorCode, BigDecimal transactionAmount,
			String tradingDate, String remark, String trxDate,
			String productCode, String referenceId, String referenceType,
			String useCase, String bizId, String bizType) {
		this.channelId = channelId;
		this.instructionNo = instructionNo;
		this.userId = userId;
		this.transactionType = transactionType;
		this.businessRefNo = businessRefNo;
		this.vendorCode = vendorCode;
		this.transactionAmount = transactionAmount;
		this.tradingDate = tradingDate;
		this.remark = remark;
		this.trxDate = trxDate;
		this.productCode = productCode;
		this.referenceId = referenceId;
		this.referenceType = referenceType;
		this.useCase = useCase;
		this.bizId = bizId;
		this.bizType = bizType;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getReferenceType() {
		return referenceType;
	}
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	public String getUseCase() {
		return useCase;
	}
	public void setUseCase(String useCase) {
		this.useCase = useCase;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
		

	
}
