package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
public class JijinExSellLimitDTO extends BaseDTO {
    private Date createdAt;  //创建时间
    private String createdBy;  //创建人
    private Date updatedAt;  //修改时间
    private String updatedBy;  //修改人
    private String fundCode;  //基金代码
    private String bizCode;  //业务代码
    private BigDecimal singleSellMinAmount;  //个人单笔最低限额
    private BigDecimal singleSellMaxAmount;  //个人单笔最高限额
    private Long batchId;  //批次号(文件ID)
    private String status;  //状态NEW/DISPATCH
    private String errMsg; //错误信息
    private Integer isValid;//是否有效

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public BigDecimal getSingleSellMinAmount() {
        return singleSellMinAmount;
    }

    public void setSingleSellMinAmount(BigDecimal singleSellMinAmount) {
        this.singleSellMinAmount = singleSellMinAmount;
    }

    public BigDecimal getSingleSellMaxAmount() {
        return singleSellMaxAmount;
    }

    public void setSingleSellMaxAmount(BigDecimal singleSellMaxAmount) {
        this.singleSellMaxAmount = singleSellMaxAmount;
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
