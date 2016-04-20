package com.lufax.jijin.daixiao.dto;


import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class JijinExNetValueDTO extends BaseDTO {

    private String fundCode; //基金代码
    private String endDate; //截止日期
    private BigDecimal netValue; //单位净值
    private BigDecimal totalNetValue; //累计净值
    private BigDecimal adjNetValue;//复权单位净值
	private Long batchId; //批次号
    private String status;
    private String errorMsg;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private Integer isValid;


    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    public BigDecimal getTotalNetValue() {
        return totalNetValue;
    }

    public void setTotalNetValue(BigDecimal totalNetValue) {
        this.totalNetValue = totalNetValue;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
	public BigDecimal getAdjNetValue() {
		return adjNetValue;
	}

	public void setAdjNetValue(BigDecimal adjNetValue) {
		this.adjNetValue = adjNetValue;
	}
}
