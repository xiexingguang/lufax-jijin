package com.lufax.jijin.sysFacade.gson;

import java.util.List;

public class BatchTxUnfreezeRequest {
	private String channelId;//channelId&instructionId要做数据库uk）
	private String instructionNo;
	private List<FrozenNo>	frozenNoList;//冻结号列表
	private String transactionType;//解冻类型(取值范围：UNFROZEN_FUND_CMSWITHDRAW、UNFROZEN_FUND_INVESTMENT、UNFROZEN_FUND、UNFROZEN_FUND_INVESTMENT_SUCCESS)
	private String remark;//备注
	private String productCode;//支付相关引用信息，产品编码
	private String trxDate;//支付相关引用信息，交易日期
	private String referenceId;//业务参考id（具体值业务系统负责）
	private String referenceType;//支付相关引用信息，调用方标明
	private String useCase;//业务场景
	private String bizId;//业务id
	private String bizType;//业务类型
	
	public BatchTxUnfreezeRequest(String channelId, String instructionNo,
			List<FrozenNo> frozenNoList, String transactionType, String remark,
			String productCode, String trxDate, String referenceId,
			String referenceType, String useCase, String bizId, String bizType) {
		this.channelId = channelId;
		this.instructionNo = instructionNo;
		this.frozenNoList = frozenNoList;
		this.transactionType = transactionType;
		this.remark = remark;
		this.productCode = productCode;
		this.trxDate = trxDate;
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
	public List<FrozenNo> getFrozenNoList() {
		return frozenNoList;
	}
	public void setFrozenNoList(List<FrozenNo> frozenNoList) {
		this.frozenNoList = frozenNoList;
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
