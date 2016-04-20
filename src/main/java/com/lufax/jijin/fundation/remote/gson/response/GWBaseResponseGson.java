package com.lufax.jijin.fundation.remote.gson.response;

public class GWBaseResponseGson {

	private String version;
	private String instId;
	//private String certId;
	private String isIndividual;
	private String contractNo;
	private String applicationNo;
	private String errorCode;
	private String errorMessage;
	private String extension;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getIsIndividual() {
		return isIndividual;
	}
	public void setIsIndividual(String isIndividual) {
		this.isIndividual = isIndividual;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getApplicationNo() {
		return applicationNo;
	}
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public boolean isSuccess(){
		return this.errorCode.equalsIgnoreCase("0000");
	}
}
