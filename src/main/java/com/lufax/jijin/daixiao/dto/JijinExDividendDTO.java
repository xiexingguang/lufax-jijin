package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 历史分红信息
 * @author 
 *
 */
public class JijinExDividendDTO extends BaseDTO {

    private String fundCode; //基金代码
    private String annDate;//公告日期
    private String recordDate;//权益登记日
    private String exDate;//除息日
    private String divEdexDate;//场外除息日
    private String divDate;//净值除权日
    private String dividendDate;//分红发放日
    private String currencyCode;//货币代码
    private BigDecimal perDividend;// 每份分红（元）

    private Long batchId; //批次号
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

    public String getAnnDate() {
        return annDate;
    }

    public void setAnnDate(String annDate) {
        this.annDate = annDate;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getExDate() {
        return exDate;
    }

    public void setExDate(String exDate) {
        this.exDate = exDate;
    }

    public String getDivEdexDate() {
        return divEdexDate;
    }

    public void setDivEdexDate(String divEdexDate) {
        this.divEdexDate = divEdexDate;
    }

    public String getDividendDate() {
        return dividendDate;
    }

    public void setDividendDate(String dividendDate) {
        this.dividendDate = dividendDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getPerDividend() {
        return perDividend;
    }

    public void setPerDividend(BigDecimal perDividend) {
        this.perDividend = perDividend;
    }

    public String getDivDate() {
        return divDate;
    }

    public void setDivDate(String divDate) {
        this.divDate = divDate;
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

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
