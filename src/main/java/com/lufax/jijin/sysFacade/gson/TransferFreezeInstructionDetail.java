package com.lufax.jijin.sysFacade.gson;

import java.math.BigDecimal;

/**
 * 虚拟户转账,再转银行卡
 * 
 * @author XUNENG311
 *
 */
public class TransferFreezeInstructionDetail {

	private BigDecimal amount;//转账并冻结金额，必须和取现金额一致	 
	private Long fromUserId;//转出帐户	 
	private String fromTransactionType;//转出类型（转账用，具体值请咨询业务）	 
	private String fromRemark;//转出备注（转账用）	 
	private String toTransactionType;//转入类型（转账用，具体值请咨询业务）	 
	private String toRemark;//转入备注（转账用）	 
	private String transferBizId;//转账业务id（业务防重用）	 
	private String transferBizType;//转账业务类型（业务防重用）	 
	private String freezeTransactionType	;//冻结资金类型（冻结用，具体值请咨询业务）	 
	private String freezeRemark;//冻结资金备注（冻结用）	 
	private String freezeBizId;//冻结业务id（业务防重用）	 
	private String freezeBizType;//冻结业务类型（业务防重用）	 
	private String productCode;//支付相关引用信息，产品编码	 
	private String trxDate;//支付相关引用信息，交易日期	 
	private String referenceId;//业务参考id（具体值业务系统负责）	 
	private String referenceType;//支付相关引用信息，调用方标明	 
	private String useCase;//业务场景	 
	
	
	public TransferFreezeInstructionDetail(BigDecimal amount, Long fromUserId,
			String fromTransactionType, String fromRemark,
			String toTransactionType, String toRemark, String transferBizId,
			String transferBizType, String freezeTransactionType,
			String freezeRemark, String freezeBizId, String freezeBizType,
			String productCode, String trxDate, String referenceId,
			String referenceType, String useCase) {

		this.amount = amount;
		this.fromUserId = fromUserId;
		this.fromTransactionType = fromTransactionType;
		this.fromRemark = fromRemark;
		this.toTransactionType = toTransactionType;
		this.toRemark = toRemark;
		this.transferBizId = transferBizId;
		this.transferBizType = transferBizType;
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
	public String getTransferBizId() {
		return transferBizId;
	}
	public void setTransferBizId(String transferBizId) {
		this.transferBizId = transferBizId;
	}
	public String getTransferBizType() {
		return transferBizType;
	}
	public void setTransferBizType(String transferBizType) {
		this.transferBizType = transferBizType;
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
