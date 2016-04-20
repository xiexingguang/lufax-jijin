package com.lufax.jijin.sysFacade.gson;

import java.util.List;

public class BatchTransferAndFreezeGson {

	private String channelId = "JIJIN";  //默认值
	private String instructionNo;
	
	private List<TransferInfo> transferList;
	private List<FreezeInfo> freezeList;
	private String productCode;
	private String trxDate;
	private String referenceId; 
	private String referenceType;
	private String useCase;
	private String bizId;
	private String bizType;
	
	
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
	public List<TransferInfo> getTransferList() {
		return transferList;
	}
	public void setTransferList(List<TransferInfo> transferList) {
		this.transferList = transferList;
	}
	public List<FreezeInfo> getFreezeList() {
		return freezeList;
	}
	public void setFreezeList(List<FreezeInfo> freezeList) {
		this.freezeList = freezeList;
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
