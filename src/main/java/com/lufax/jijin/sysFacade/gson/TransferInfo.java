package com.lufax.jijin.sysFacade.gson;

import java.math.BigDecimal;

public class TransferInfo {
	private BigDecimal transferAmount;//冻结金额
	private Long fromUserId;
	private String fromTransactionType;
	private String fromRemark;
	
	private Long toUserId;
	private String toTransactionType;
	private String toRemark;
	
	private Long businessId; //冻结业务id
	private String productCode; //产品编码 N
	private String trxDate;//支付相关引用信息，交易日期	 N
	private String referenceId;//业务参考id（具体值业务系统负责）	 
	private String referenceType;//	支付相关引用信息，调用方标明	 
	private String useCase;//业务场景	  	 	N
	private String bizId;//业务id	 		 
	private String bizType;//业务类型
	
	public BigDecimal getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}
	public Long getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getFromTransactionType() {
		return fromTransactionType;
	}
	public void setFromTransactionType(String fromTransactionType) {
		this.fromTransactionType = fromTransactionType;
	}
	public String getFromRemark() {
		return fromRemark;
	}
	public void setFromRemark(String fromRemark) {
		this.fromRemark = fromRemark;
	}
	public Long getToUserId() {
		return toUserId;
	}
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}
	public String getToTransactionType() {
		return toTransactionType;
	}
	public void setToTransactionType(String toTransactionType) {
		this.toTransactionType = toTransactionType;
	}
	public String getToRemark() {
		return toRemark;
	}
	public void setToRemark(String toRemark) {
		this.toRemark = toRemark;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
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
