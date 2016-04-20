package com.lufax.jijin.sysFacade.gson;

import java.math.BigDecimal;

/**
 * Payment app, 请求参数
 * @author XUNENG311
 *
 */
public class PaymentRequest {

	private String channelId;
    private String instructionNo;
    private String paymentOrderNo;
    private String frozenCode; //冻结号（不可与paymentOrderNo同时传，但必传其中一个；使用冻结号时必须带清算信息clearInfo）
    private String transactionType; //解冻交易类型	
    private String remark; //解冻备注	
    private SubtractInfo subtractInfo; //调减信息
    private ClearInfo 	clearInfo; //清算信息 (此对象不传，便不进行清算)
	private String productCode; //产品编码	
	private String trxDate; //交易日期	 
	private String referenceId;//业务参考id（具体值业务系统负责） 
	private String referenceType; //支付相关引用信息，调用方标明	
	private String useCase;// biz业务场景
	private String bizId;//业务id（bizId&bizType要做业务uk，只能成功一笔）
	private String bizType;//业务类型
		
	public PaymentRequest(String channelId, String instructionNo,
			String paymentOrderNo, String frozenCode, String transactionType,
			String remark, SubtractInfo subtractInfo, ClearInfo clearInfo,
			String productCode, String trxDate, String referenceId,
			String referenceType, String useCase, String bizId, String bizType) {
		this.channelId = channelId;
		this.instructionNo = instructionNo;
		this.paymentOrderNo = paymentOrderNo;
		this.frozenCode = frozenCode;
		this.transactionType = transactionType;
		this.remark = remark;
		this.subtractInfo = subtractInfo;
		this.clearInfo = clearInfo;
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

	public SubtractInfo getSubtractInfo() {
		return subtractInfo;
	}

	public void setSubtractInfo(SubtractInfo subtractInfo) {
		this.subtractInfo = subtractInfo;
	}

	public ClearInfo getClearInfo() {
		return clearInfo;
	}

	public void setClearInfo(ClearInfo clearInfo) {
		this.clearInfo = clearInfo;
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
