package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class JijinExScopeDTO extends BaseDTO {

    private String fundCode; //基金代码
    private String startDate;//开始日期
    private String reportDate; //报告日期
    private BigDecimal fundShare;  //基金份额
    private BigDecimal purchaseShare;  //期间申购份额
    private BigDecimal redeemShare;    //期间赎回份额
    private Long batchId; //批次号
    private String status;//状态
    private String errorMsg;//错误信息
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private Integer isValid; //0:无效 1:有效

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public BigDecimal getFundShare() {
        return fundShare;
    }

    public void setFundShare(BigDecimal fundShare) {
        this.fundShare = fundShare;
    }

    public BigDecimal getPurchaseShare() {
        return purchaseShare;
    }

    public void setPurchaseShare(BigDecimal purchaseShare) {
        this.purchaseShare = purchaseShare;
    }

    public BigDecimal getRedeemShare() {
        return redeemShare;
    }

    public void setRedeemShare(BigDecimal redeemShare) {
        this.redeemShare = redeemShare;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
