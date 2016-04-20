package com.lufax.jijin.sysFacade.gson;

import java.math.BigDecimal;

public class FreezeInfo {

	private BigDecimal frozenAmount;//冻结金额
	private Long businessId; //冻结业务id
	private String transactionType;
	private String remark; //冻结资金备注 N

	private Long userId; //冻结帐户
	private String productCode; //产品编码 N
	private String trxDate;//支付相关引用信息，交易日期	 N
	private String referenceId;//业务参考id（具体值业务系统负责）	 
	private String referenceType;//	支付相关引用信息，调用方标明	 
	private String useCase;//业务场景	  	 	N
	private String bizId;//业务id	 		 
	private String bizType;//业务类型

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public BigDecimal getFrozenAmount() {
		return frozenAmount;
	}
	public Long getBusinessId() {
		return businessId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public String getRemark() {
		return remark;
	}
	public void setFrozenAmount(BigDecimal frozenAmount) {
		this.frozenAmount = frozenAmount;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
