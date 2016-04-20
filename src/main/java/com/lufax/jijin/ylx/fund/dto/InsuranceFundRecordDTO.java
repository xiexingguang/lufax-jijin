package com.lufax.jijin.ylx.fund.dto;


import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class InsuranceFundRecordDTO extends BaseDTO {

    private Long productId;
    private Long fromUserId;
    private String toCardId;
    private int type;
    private BigDecimal amount;
    private String remark;
    private String recordId;
    private Integer status;
    private Long version;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer insuranceType;
    private String fundDate;
    
    

	public InsuranceFundRecordDTO() {
    }

    public InsuranceFundRecordDTO(Long productId, Long fromUserId, String toCardId, int type, BigDecimal amount, String remark, String recordId, Integer status, Integer insuranceType, String fundDate) {
        this.productId = productId;
        this.fromUserId = fromUserId;
        this.toCardId = toCardId;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
        this.recordId = recordId;
        this.status = status;
        this.version = 0L;
        this.createdAt = new Date();
        this.updatedAt = createdAt;
        this.createdBy = "SYS";
        this.updatedBy = "SYS";
        this.insuranceType = insuranceType;
        this.fundDate = fundDate;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getFromUserId() {
        return fromUserId;
    }

    public String getToCardId() {
        return toCardId;
    }

    public int getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getRemark() {
        return remark;
    }

    public String getRecordId() {
        return recordId;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getVersion() {
        return version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public void setToCardId(String toCardId) {
        this.toCardId = toCardId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(Integer insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getFundDate() {
        return fundDate;
    }

    public void setFundDate(String fundDate) {
        this.fundDate = fundDate;
    }
    
private String productDisPlayName;//不是数据库字段
    
    public String getProductDisPlayName() {
		return productDisPlayName;
	}
	public void setProductDisPlayName(String productDisPlayName) {
		this.productDisPlayName = productDisPlayName;
	}
}
