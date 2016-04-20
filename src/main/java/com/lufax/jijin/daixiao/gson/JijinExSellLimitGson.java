package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExSellLimitDTO;

import java.math.BigDecimal;


public class JijinExSellLimitGson {

    private String fundCode;  //基金代码
    private String bizCode;  //业务代码
    private BigDecimal singleSellMinAmount;  //个人单笔最低限额
    private BigDecimal singleSellMaxAmount;  //个人单笔最高限额

    public JijinExSellLimitGson() {
    }

    public JijinExSellLimitGson(JijinExSellLimitDTO jijinExSellLimitDTO) {
        this.fundCode = jijinExSellLimitDTO.getFundCode();
        this.bizCode = jijinExSellLimitDTO.getBizCode();
        this.singleSellMinAmount = jijinExSellLimitDTO.getSingleSellMinAmount();
        this.singleSellMaxAmount = jijinExSellLimitDTO.getSingleSellMaxAmount();
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
}
