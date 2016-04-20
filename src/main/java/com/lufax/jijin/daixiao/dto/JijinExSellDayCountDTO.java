package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.util.Date;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
public class JijinExSellDayCountDTO extends BaseDTO {
    private String fundCode; //基金代码
    private Long sellConfirmDayCount;//赎回到帐时间
    private Long batchId; //批次号
    private String status;//状态
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String errMsg; //错误信息
    private Integer isValid;//是否有效

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public Long getSellConfirmDayCount() {
        return sellConfirmDayCount;
    }

    public void setSellConfirmDayCount(Long sellConfirmDayCount) {
        this.sellConfirmDayCount = sellConfirmDayCount;
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

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}
