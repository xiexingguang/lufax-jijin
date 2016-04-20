package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * redis T0赎回的热点账户
 * @author XUNENG311
 *
 */
public class JijinRedeemBalDTO  extends BaseDTO {
	
    private String fundCode;
    private BigDecimal amount;
    private Long version;
    private String snapshotTime;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    
    
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getSnapshortTime() {
		return snapshotTime;
	}
	public void setSnapshotTime(String snapshotTime) {
		this.snapshotTime = snapshotTime;
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
