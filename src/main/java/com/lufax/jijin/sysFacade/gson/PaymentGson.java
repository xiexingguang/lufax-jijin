package com.lufax.jijin.sysFacade.gson;

/**
 * 
 * @author chenqunhui168
 *
 */
public class PaymentGson {
	
	private final String channelId = "JIJIN";  //默认值，不可修改  
	private String instructionNo;
	private Long userId;
	private PlusInfo plusInfo;
	private FreezeInfo freezeInfo;
	private RevokeClearInfo revokeClearInfo;
	private String productCode;
	private String trxDate;
	/*private String referenceId;
	private String referenceType;*/
	private String useCase;
	private String bizId;
	private String bizType;
	
	
	public String getChannelId() {
		return channelId;
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
	public PlusInfo getPlusInfo() {
		return plusInfo;
	}
	public void setPlusInfo(PlusInfo plusInfo) {
		this.plusInfo = plusInfo;
	}
	public FreezeInfo getFreezeInfo() {
		return freezeInfo;
	}
	public void setFreezeInfo(FreezeInfo freezeInfo) {
		this.freezeInfo = freezeInfo;
	}
	public RevokeClearInfo getRevokeClearInfo() {
		return revokeClearInfo;
	}
	public void setRevokeClearInfo(RevokeClearInfo revokeClearInfo) {
		this.revokeClearInfo = revokeClearInfo;
	}
	/*public String getReferenceId() {
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
	}*/
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

	public String getProductCode() {
		return productCode;
	}

	public String getTrxDate() {
		return trxDate;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	
	
	
}
