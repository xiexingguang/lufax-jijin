package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.dto.BaseDTO;

public class JijinThirdPaySyncDTO extends BaseDTO {
	
	public enum Status{
		NEW, DISPATCHED, FAIL, UNMATCH,REDO;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479098977880218351L;
	private Long fileId;
	/* 平安付帐号 */
	private String payNo;
	/* 平安付代发流水号 */
	private String paySerialNo;
	/* 商户端代发流水号-即基金返回流水号 */
	private String appSheetNo;
	/* 金额 */
	private BigDecimal amount;
	/* 币种 */
	private String currency;
	/* 代发类型 */
	private String payType;
	/* 代发时间 */
	private String trxTime;
	/* 代发结果 */
	private String trxResult;
	/* 状态 */
	private Status status;
	/* 渠道名称 */
	private String channel;
	private String instId;

	private Date createdAt;
	private Date updatedAt;
	private String createdBy;
	private String updatedBy;

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getPaySerialNo() {
		return paySerialNo;
	}

	public void setPaySerialNo(String paySerialNo) {
		this.paySerialNo = paySerialNo;
	}

	public String getAppSheetNo() {
		return appSheetNo;
	}

	public void setAppSheetNo(String appSheetNo) {
		this.appSheetNo = appSheetNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(String trxTime) {
		this.trxTime = trxTime;
	}

	public String getTrxResult() {
		return trxResult;
	}

	public void setTrxResult(String trxResult) {
		this.trxResult = trxResult;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

}
