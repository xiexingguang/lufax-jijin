package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 基金交易对账结果
 * @author xuneng
 *
 */
public class JijinTradeReconDTO extends BaseDTO {
	
	private String recordId; // bus_jijin_trade_record.id
	private String buyAuditId; //bus_jijin_paf_buy_audit.id
	private String remark;
	private String status; // LACK_BUY_AUDIT,NO_MATCH,NOT_EQUAL

	private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    

	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getBuyAuditId() {
		return buyAuditId;
	}
	public void setBuyAuditId(String buyAuditId) {
		this.buyAuditId = buyAuditId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
	          
}
