package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 1.基金公司赎回，申购失败，现金分红对账文件
 * @author xuneng
 *
 */
public class JijinRedeemAuditDTO extends BaseDTO {
	
	
	private Long fileId;
	private String customerId;
	private String distributorCode;
	private String payOrgId;
	private String receiveAcct;
	private String receiveAcctName;
	private String recBankName;
	private String recBankCode;
	private String payAcct;
	private String payAcctName;
	private String payBankName;
	private String payBankCode;
	private String txnDate;
	private BigDecimal txnAmount;
	private String currency;
	private String txnType;
	private String fundDate;
	private String fundTime;
	private String fundSeqId;
	private String fundType;
	private String txnMode;
	
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
	
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
	public String getTxnMode() {
		return txnMode;
	}
	public void setTxnMode(String txnMode) {
		this.txnMode = txnMode;
	}
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getDistributorCode() {
		return distributorCode;
	}
	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	}
	public String getPayOrgId() {
		return payOrgId;
	}
	public void setPayOrgId(String payOrgId) {
		this.payOrgId = payOrgId;
	}
	public String getReceiveAcct() {
		return receiveAcct;
	}
	public void setReceiveAcct(String receiveAcct) {
		this.receiveAcct = receiveAcct;
	}
	public String getReceiveAcctName() {
		return receiveAcctName;
	}
	public void setReceiveAcctName(String receiveAcctName) {
		this.receiveAcctName = receiveAcctName;
	}
	public String getRecBankName() {
		return recBankName;
	}
	public void setRecBankName(String recBankName) {
		this.recBankName = recBankName;
	}
	public String getRecBankCode() {
		return recBankCode;
	}
	public void setRecBankCode(String recBankCode) {
		this.recBankCode = recBankCode;
	}
	public String getPayAcct() {
		return payAcct;
	}
	public void setPayAcct(String payAcct) {
		this.payAcct = payAcct;
	}
	public String getPayAcctName() {
		return payAcctName;
	}
	public void setPayAcctName(String payAcctName) {
		this.payAcctName = payAcctName;
	}
	public String getPayBankName() {
		return payBankName;
	}
	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}
	public String getPayBankCode() {
		return payBankCode;
	}
	public void setPayBankCode(String payBankCode) {
		this.payBankCode = payBankCode;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public BigDecimal getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getFundDate() {
		return fundDate;
	}
	public void setFundDate(String fundDate) {
		this.fundDate = fundDate;
	}
	public String getFundTime() {
		return fundTime;
	}
	public void setFundTime(String fundTime) {
		this.fundTime = fundTime;
	}
	public String getFundSeqId() {
		return fundSeqId;
	}
	public void setFundSeqId(String fundSeqId) {
		this.fundSeqId = fundSeqId;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

   
}
