package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
public class JijinExFeeDTO extends BaseDTO {
    private Date createdAt;  //创建时间
    private String createdBy;  //创建人
    private Date updatedAt;  //修改时间
    private String updatedBy;  //修改人
    private String fundCode;  //基金代码
    private String feeType;  //费率类型代码
    private String chargeType;  //收费类型
    private BigDecimal upperLimitAmount;  //金额上限
    private BigDecimal lowerLimitAmount;  //金额下限
    private BigDecimal lowerLimitHoldYear;  //持有年限下限
    private BigDecimal upperLimitHoldYear;  //持有年限上限
    private BigDecimal fee;  //费率
    private String changeDate;  //变动日期
    private Long batchId;  //批次号(文件ID)
    private String status;  //状态NEW/DISPATCH
    private String errMsg; //错误信息
    private Integer isValid;//是否有效
    private String feeMemo;//收费补充说明
    private String holdPeriodUnit;//持有期限单位
    private BigDecimal fixValue;//固定费率

    public BigDecimal getFixValue() {
		return fixValue;
	}

	public void setFixValue(BigDecimal fixValue) {
		this.fixValue = fixValue;
	}

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

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getUpperLimitAmount() {
        return upperLimitAmount;
    }

    public void setUpperLimitAmount(BigDecimal upperLimitAmount) {
        this.upperLimitAmount = upperLimitAmount;
    }

    public BigDecimal getLowerLimitAmount() {
        return lowerLimitAmount;
    }

    public void setLowerLimitAmount(BigDecimal lowerLimitAmount) {
        this.lowerLimitAmount = lowerLimitAmount;
    }

    public BigDecimal getLowerLimitHoldYear() {
        return lowerLimitHoldYear;
    }

    public void setLowerLimitHoldYear(BigDecimal lowerLimitHoldYear) {
        this.lowerLimitHoldYear = lowerLimitHoldYear;
    }

    public BigDecimal getUpperLimitHoldYear() {
        return upperLimitHoldYear;
    }

    public void setUpperLimitHoldYear(BigDecimal upperLimitHoldYear) {
        this.upperLimitHoldYear = upperLimitHoldYear;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
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

    public String getFeeMemo() {
        return feeMemo;
    }

    public void setFeeMemo(String feeMemo) {
        this.feeMemo = feeMemo;
    }

    public String getHoldPeriodUnit() {
        return holdPeriodUnit;
    }

    public void setHoldPeriodUnit(String holdPeriodUnit) {
        this.holdPeriodUnit = holdPeriodUnit;
    }
}
