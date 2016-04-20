package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExFeeDTO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;


public class JijinExFeeGson {

    private String fundCode;  //基金代码
    private String feeType;  //费率类型代码
    private String chargeType;  //收费类型
    private BigDecimal upperLimitAmount;  //金额上限
    private BigDecimal lowerLimitAmount;  //金额下限
    private BigDecimal lowerLimitHoldYear;  //持有年限下限
    private BigDecimal upperLimitHoldYear;  //持有年限上限
    private BigDecimal fee;  //费率
    private String changeDate;  //变动日期
    private Long batchId;
    private String feeMemo;//收费补充说明
    private String holdPeriodUnit;//持有期限单位
    private BigDecimal fixValue; //固定费率


    public JijinExFeeGson(JijinExFeeDTO jijinExFeeDTO) {
        this.fundCode = jijinExFeeDTO.getFundCode();
        this.feeType = jijinExFeeDTO.getFeeType();
        this.chargeType = jijinExFeeDTO.getChargeType();
        this.upperLimitAmount = jijinExFeeDTO.getUpperLimitAmount();
        this.lowerLimitAmount = jijinExFeeDTO.getLowerLimitAmount();
        this.lowerLimitHoldYear = jijinExFeeDTO.getLowerLimitHoldYear();
        this.upperLimitHoldYear = jijinExFeeDTO.getUpperLimitHoldYear();
        this.fee = jijinExFeeDTO.getFee();
        this.changeDate = jijinExFeeDTO.getChangeDate();
        this.batchId = jijinExFeeDTO.getBatchId();
        this.feeMemo = jijinExFeeDTO.getFeeMemo();
        this.holdPeriodUnit = jijinExFeeDTO.getHoldPeriodUnit();
        this.fixValue = jijinExFeeDTO.getFixValue();
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

	public BigDecimal getFixValue() {
		return fixValue;
	}

	public void setFixValue(BigDecimal fixValue) {
		this.fixValue = fixValue;
	}
    
    
}
