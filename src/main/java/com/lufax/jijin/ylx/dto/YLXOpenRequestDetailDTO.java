package com.lufax.jijin.ylx.dto;


import com.lufax.jijin.base.dto.YLXBaseDTO;

import java.util.Date;

public class YLXOpenRequestDetailDTO extends YLXBaseDTO{
	
	private long batchId;
	private long internalTrxId;
	private Date trxTime; //yyyyMMddhhmmss
	private Date trxDate; //yyyyMMdd
	private String virtualBankAccount;
	private String bankAccount;
	private String bankAccountType;
	private String name;
	private String idType;
	private String idNo;
	private String mobileNo;
	private String riskLevel;
	private String sex;
	private String lufaxBankCode;
	private String lufaxBank;
	private String status;

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getLufaxBankCode() {
		return lufaxBankCode;
	}
	public void setLufaxBankCode(String lufaxBankCode) {
		this.lufaxBankCode = lufaxBankCode;
	}
	public String getLufaxBank() {
		return lufaxBank;
	}
	public void setLufaxBank(String lufaxBank) {
		this.lufaxBank = lufaxBank;
	}
	public long getBatchId() {
		return batchId;
	}
	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}
	public long getInternalTrxId() {
		return internalTrxId;
	}
	public void setInternalTrxId(long internalTrxId) {
		this.internalTrxId = internalTrxId;
	}
	public Date getTrxTime() {
		return trxTime;
	}
	public void setTrxTime(Date trxTime) {
		this.trxTime = trxTime;
	}
	public Date getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
	}
	public String getVirtualBankAccount() {
		return virtualBankAccount;
	}
	public void setVirtualBankAccount(String virtualBankAccount) {
		this.virtualBankAccount = virtualBankAccount;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getBankAccountType() {
		return bankAccountType;
	}
	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
