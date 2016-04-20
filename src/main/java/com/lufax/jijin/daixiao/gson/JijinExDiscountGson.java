package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExDiscountDTO;

import java.math.BigDecimal;


public class JijinExDiscountGson{

	private Long batchId; // 
    private String fundCode;  //基金代码
    private String bizCode;  //业务代码
    private String validDate;  //生效日期
    private String discountMode;  //折扣模式
    private String feePloy;  //计费策略
    private BigDecimal fixedRate;  //固定比例
    private BigDecimal fixedMaxAmount;  //金额下限
    private BigDecimal fixedMinAmount;  //金额上限
    private String setType;  //设置类型
    private BigDecimal setValue;  //设定值

    public JijinExDiscountGson(JijinExDiscountDTO jijinExDiscountDTO) {
        this.fundCode = jijinExDiscountDTO.getFundCode();
        this.bizCode = jijinExDiscountDTO.getBizCode();
        this.validDate =  jijinExDiscountDTO.getValidDate();
        this.discountMode = jijinExDiscountDTO.getDiscountMode();
        this.feePloy = jijinExDiscountDTO.getFeePloy();
        this.fixedRate = jijinExDiscountDTO.getFixedRate();
        this.fixedMaxAmount = jijinExDiscountDTO.getFixedMaxAmount();
        this.fixedMinAmount = jijinExDiscountDTO.getFixedMinAmount();
        this.setType = jijinExDiscountDTO.getSetType();
        this.setValue = jijinExDiscountDTO.getSetValue();
        this.batchId = jijinExDiscountDTO.getBatchId();
    }

    

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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
}
