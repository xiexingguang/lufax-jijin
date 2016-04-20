package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
public class JijinExDiscountDTO extends BaseDTO {
    private Date createdAt;  //创建时间
    private String createdBy;  //创建人
    private Date updatedAt;  //修改时间
    private String updatedBy;  //修改人
    private String fundCode;  //基金代码
    private String bizCode;  //业务代码
    private String validDate;  //生效日期
    private String discountMode;  //折扣模式
    private String feePloy;  //计费策略
    private BigDecimal fixedRate;  //固定比例
    private BigDecimal fixedMinAmount;  //金额下限
    private BigDecimal fixedMaxAmount;  //金额上限
    private String setType;  //设置类型
    private BigDecimal setValue;  //设定值
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

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getDiscountMode() {
        return discountMode;
    }

    public void setDiscountMode(String discountMode) {
        this.discountMode = discountMode;
    }

    public String getFeePloy() {
        return feePloy;
    }

    public void setFeePloy(String feePloy) {
        this.feePloy = feePloy;
    }

    public BigDecimal getFixedRate() {
        return fixedRate;
    }

    public void setFixedRate(BigDecimal fixedRate) {
        this.fixedRate = fixedRate;
    }

    public BigDecimal getFixedMaxAmount() {
        return fixedMaxAmount;
    }

    public void setFixedMaxAmount(BigDecimal fixedMaxAmount) {
        this.fixedMaxAmount = fixedMaxAmount;
    }

    public BigDecimal getFixedMinAmount() {
        return fixedMinAmount;
    }

    public void setFixedMinAmount(BigDecimal fixedMinAmount) {
        this.fixedMinAmount = fixedMinAmount;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    public BigDecimal getSetValue() {
        return setValue;
    }

    public void setSetValue(BigDecimal setValue) {
        this.setValue = setValue;
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
