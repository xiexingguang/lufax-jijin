package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 1.基金公司用户持仓文件
 * @author xuneng
 *
 */
public class JijinUserBalanceAuditDTO extends BaseDTO {
	
	public enum Status{
		NEW, DISPATCHED, WARN, UNMATCH;
	}
	
	private Long fileId;
	private String fundCode;
	private String feeType;
	private String instId;
	private String contractNo;
	private Long userId;
	private BigDecimal totalShare;
	private BigDecimal frozenShare;
	private BigDecimal availableShare;
	private BigDecimal unpaiedIncome;
	private BigDecimal dailyIncome;
	private BigDecimal totalIncome;
	private String dividendType;
	private String bizDate;
	private String fundType; // null- 非货基, 1 - 货基
	private String status;
	private Long version;//乐观锁

	private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
    
	public Long getFileId() {
		return fileId;
	}
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public BigDecimal getTotalShare() {
		return totalShare;
	}
	public void setTotalShare(BigDecimal totalShare) {
		this.totalShare = totalShare;
	}
	public BigDecimal getFrozenShare() {
		return frozenShare;
	}
	public void setFrozenShare(BigDecimal frozenShare) {
		this.frozenShare = frozenShare;
	}
	public BigDecimal getAvailableShare() {
		return availableShare;
	}
	public void setAvailableShare(BigDecimal availableShare) {
		this.availableShare = availableShare;
	}
	public BigDecimal getUnpaiedIncome() {
		return unpaiedIncome;
	}
	public void setUnpaiedIncome(BigDecimal unpaiedIncome) {
		this.unpaiedIncome = unpaiedIncome;
	}
	public BigDecimal getDailyIncome() {
		return dailyIncome;
	}
	public void setDailyIncome(BigDecimal dailyIncome) {
		this.dailyIncome = dailyIncome;
	}
	public BigDecimal getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}
	public String getDividendType() {
		return dividendType;
	}
	public void setDividendType(String dividendType) {
		this.dividendType = dividendType;
	}
	public String getBizDate() {
		return bizDate;
	}
	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
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
    
    
		

   
}
