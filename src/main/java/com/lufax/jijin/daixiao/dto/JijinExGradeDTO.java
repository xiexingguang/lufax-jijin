package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.util.Date;


public class JijinExGradeDTO extends BaseDTO {

    private String fundCode; //基金代码
    private String rateDate; //评级日期
    private String starLevel;//星级
    private String fundType; //基金类型
    private String ratingInterval; //评级区间
    private String ratingGagency;//评级机构
    private Long batchId; //批次号
    private String status;//状态
    private String errorMsg; //错误信息
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private Integer isValid;//0无效 1有效


    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getRatingInterval() {
        return ratingInterval;
    }

    public void setRatingInterval(String ratingInterval) {
        this.ratingInterval = ratingInterval;
    }

    public String getRatingGagency() {
        return ratingGagency;
    }

    public void setRatingGagency(String ratingGagency) {
        this.ratingGagency = ratingGagency;
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
