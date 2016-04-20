package com.lufax.jijin.sysFacade.gson;

import java.math.BigDecimal;
import java.util.List;

public class PaymentWithdrawRequest {
	
	private String channelId;
    private String instructionNo;
	private String isCompany;//是否对公取现，1 对公，0 对私
    private Long userId;//用户ID
    private BigDecimal amount;//取现金额
    private String withdrawType;//取现详细类型（例如：CUSTOMER_WITHDRAWAL-传空时默认值，如不符合请询问资金运营团队)
    private String tradeTime;//交易时间（yyyy-MM-dd HH:mm:ss）
    private String remark;//备注 	 	 	
    private String cardId; //user绑卡表的主键id
    private String paymentChannel;//指定支付渠道
    private List<Instruction> prepareOperationList;//主账户提现（代付）前准备操作，如转账冻结，清算调增冻结。（2016-4-12上线, 只支持单条指令操作；直接从虚拟户提现不需要此参数）。isRequiredPrepare值为1时必传。	
	private String productCode; //产品编码	
	private String trxDate; //交易日期	 
	private String referenceId;//业务参考id（具体值业务系统负责） 
	private String referenceType; //支付相关引用信息，调用方标明	
	private String useCase;// biz业务场景
	private String bizId;//业务id（bizId&bizType要做业务uk，只能成功一笔）
	private String bizType;//业务类型
	
	
	public PaymentWithdrawRequest(String channelId, String instructionNo,
			String isCompany, Long userId, BigDecimal amount,
			String withdrawType, String tradeTime, String remark,
			String cardId, String paymentChannel, 
			List<Instruction> prepareOperationList, String productCode,
			String trxDate, String referenceId, String referenceType,
			String useCase, String bizId, String bizType) {
		this.channelId = channelId;
		this.instructionNo = instructionNo;
		this.isCompany = isCompany;
		this.userId = userId;
		this.amount = amount;
		this.withdrawType = withdrawType;
		this.tradeTime = tradeTime;
		this.remark = remark;
		this.cardId = cardId;
		this.paymentChannel = paymentChannel;
		this.prepareOperationList = prepareOperationList;
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
	public String getIsCompany() {
		return isCompany;
	}
	public void setIsCompany(String isCompany) {
		this.isCompany = isCompany;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getWithdrawType() {
		return withdrawType;
	}
	public void setWithdrawType(String withdrawType) {
		this.withdrawType = withdrawType;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public List<Instruction> getPrepareOperationList() {
		return prepareOperationList;
	}
	public void setPrepareOperationList(List<Instruction> prepareOperationList) {
		this.prepareOperationList = prepareOperationList;
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
