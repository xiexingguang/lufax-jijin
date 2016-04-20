package com.lufax.jijin.ylx.dto;

import java.util.Date;

import com.lufax.jijin.base.dto.BaseDTO;


public abstract class BaseConfirmDetailDTO extends BaseDTO {
	protected Long batchId;
	protected Long internalTrxId;
	protected String resultCode;
	protected String resultInfo;
	protected String bankAccount;
	protected String thirdCustomerAccount;
	protected String thirdAccount;
	protected String thirdAccountType;

	protected String createdBy;
	protected String updatedBy;
	protected Date createdAt;
	protected Date updatedAt;

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Long getInternalTrxId() {
		return internalTrxId;
	}

	public void setInternalTrxId(Long internalTrxId) {
		this.internalTrxId = internalTrxId;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getThirdCustomerAccount() {
		return thirdCustomerAccount;
	}

	public void setThirdCustomerAccount(String thirdCustomerAccount) {
		this.thirdCustomerAccount = thirdCustomerAccount;
	}

	public String getThirdAccount() {
		return thirdAccount;
	}

	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}

	public String getThirdAccountType() {
		return thirdAccountType;
	}

	public void setThirdAccountType(String thirdAccountType) {
		this.thirdAccountType = thirdAccountType;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
