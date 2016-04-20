package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * redis T0赎回的热点账户流水
 * @author XUNENG311
 *
 */
public class JijinRedeemBalHisDTO  extends BaseDTO {
	
    private BigDecimal amount;
    private String remark;
	private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    
    public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
