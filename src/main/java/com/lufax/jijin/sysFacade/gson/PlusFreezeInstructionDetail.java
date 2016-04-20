package com.lufax.jijin.sysFacade.gson;

import java.math.BigDecimal;

/**
 * 清算调增,转银行卡 
 * @author XUNENG311
 *
 */
public class PlusFreezeInstructionDetail {
	private BigDecimal amount;//（清算）调增并冻结金额，必须和取现金额一致	 
	private String businessRefNo;//业务编号（业务流水号） - 该字段用于后续清算（核销）和对账
	private String vendorCode;//供应商代码（基金公司代码）
	private String tradingDate;//所属交易日
	private String transactionType	;//（资金）交易类型	 
	private String remark;//备注	 
	private String plusBizId;//调增业务记录的标识（业务防重用）
	private String plusBizType;//调增业务记录的类型（业务防重用）
	private String 	clearBizId;//清算业务记录的标识（业务防重用）
	private String clearBizType;//清算业务记录的类型（业务防重用）
	private String freezeTransactionType;//冻结资金类型（冻结用，具体值请咨询业务）	 
	private String freezeRemark;//	冻结资金备注（冻结用）	 
	private String freezeBizId;//冻结业务id（业务防重用）	 
	private String freezeBizType;//冻结业务类型（业务防重用）	 
	private String productCode;//支付相关引用信息，产品编码	 
	private String trxDate;//支付相关引用信息，交易日期	 
	private String referenceId;//业务参考id（具体值业务系统负责）	 
	private String referenceType;//支付相关引用信息，调用方标明	 
	private String useCase;//业务场景	 
	
	
	
	public PlusFreezeInstructionDetail(BigDecimal amount, String businessRefNo,
			String vendorCode, String tradingDate, String transactionType,
			String remark, String plusBizId, String plusBizType,
			String clearBizId, String clearBizType,
			String freezeTransactionType, String freezeRemark,
			String freezeBizId, String freezeBizType, String productCode,
			String trxDate, String referenceId, String referenceType,
			String useCase) {
		this.amount = amount;
		this.businessRefNo = businessRefNo;
		this.vendorCode = vendorCode;
		this.tradingDate = tradingDate;
		this.transactionType = transactionType;
		this.remark = remark;
		this.plusBizId = plusBizId;
		this.plusBizType = plusBizType;
		this.clearBizId = clearBizId;
		this.clearBizType = clearBizType;
		this.freezeTransactionType = freezeTransactionType;
		this.freezeRemark = freezeRemark;
		this.freezeBizId = freezeBizId;
		this.freezeBizType = freezeBizType;
		this.productCode = productCode;
		this.trxDate = trxDate;
		this.referenceId = referenceId;
		this.referenceType = referenceType;
		this.useCase = useCase;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	public String getTradingDate() {
		return tradingDate;
	}
	public void setTradingDate(String tradingDate) {
		this.tradingDate = tradingDate;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPlusBizId() {
		return plusBizId;
	}
	public void setPlusBizId(String plusBizId) {
		this.plusBizId = plusBizId;
	}
	public String getPlusBizType() {
		return plusBizType;
	}
	public void setPlusBizType(String plusBizType) {
		this.plusBizType = plusBizType;
	}
	public String getClearBizId() {
		return clearBizId;
	}
	public void setClearBizId(String clearBizId) {
		this.clearBizId = clearBizId;
	}
	public String getClearBizType() {
		return clearBizType;
	}
	public void setClearBizType(String clearBizType) {
		this.clearBizType = clearBizType;
	}
	public String getFreezeTransactionType() {
		return freezeTransactionType;
	}
	public void setFreezeTransactionType(String freezeTransactionType) {
		this.freezeTransactionType = freezeTransactionType;
	}
	public String getFreezeRemark() {
		return freezeRemark;
	}
	public void setFreezeRemark(String freezeRemark) {
		this.freezeRemark = freezeRemark;
	}
	public String getFreezeBizId() {
		return freezeBizId;
	}
	public void setFreezeBizId(String freezeBizId) {
		this.freezeBizId = freezeBizId;
	}
	public String getFreezeBizType() {
		return freezeBizType;
	}
	public void setFreezeBizType(String freezeBizType) {
		this.freezeBizType = freezeBizType;
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
}
