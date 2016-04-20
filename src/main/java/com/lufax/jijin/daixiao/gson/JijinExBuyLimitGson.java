package com.lufax.jijin.daixiao.gson;


import com.lufax.jijin.daixiao.dto.JijinExBuyLimitDTO;

import java.math.BigDecimal;

public class JijinExBuyLimitGson {

    private String fundCode;  //基金代码
    private String bizCode;  //业务代码
    private BigDecimal firstInvestMinAmount;  //首次最低限额
    private BigDecimal firstInvestMaxAmount;  //首次最高限额
    private BigDecimal addInvestMinAmount;  //追加最低限额
    private BigDecimal addInvestMaxAmount;  //追加最高限额
    private BigDecimal investDailyLimit;  //单日累计最高限额
    private BigDecimal singleInvestMinAmount;  //单笔最低限额
    private BigDecimal singleInvestMaxAmount;  //单笔最高限额
    private BigDecimal raisedAmount;  //级差


    public JijinExBuyLimitGson(JijinExBuyLimitDTO jijinExBuyLimitDTO) {
        this.fundCode = jijinExBuyLimitDTO.getFundCode();
        this.bizCode = jijinExBuyLimitDTO.getBizCode();
        this.firstInvestMinAmount = jijinExBuyLimitDTO.getFirstInvestMinAmount();
        this.firstInvestMaxAmount = jijinExBuyLimitDTO.getFirstInvestMaxAmount();
        this.addInvestMinAmount = jijinExBuyLimitDTO.getAddInvestMinAmount();
        this.addInvestMaxAmount = jijinExBuyLimitDTO.getAddInvestMaxAmount();
        this.investDailyLimit = jijinExBuyLimitDTO.getInvestDailyLimit();
        this.singleInvestMinAmount = jijinExBuyLimitDTO.getSingleInvestMinAmount();
        this.singleInvestMaxAmount = jijinExBuyLimitDTO.getSingleInvestMaxAmount();
        this.raisedAmount = jijinExBuyLimitDTO.getRaisedAmount();
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

    public BigDecimal getFirstInvestMinAmount() {
        return firstInvestMinAmount;
    }

    public void setFirstInvestMinAmount(BigDecimal firstInvestMinAmount) {
        this.firstInvestMinAmount = firstInvestMinAmount;
    }

    public BigDecimal getFirstInvestMaxAmount() {
        return firstInvestMaxAmount;
    }

    public void setFirstInvestMaxAmount(BigDecimal firstInvestMaxAmount) {
        this.firstInvestMaxAmount = firstInvestMaxAmount;
    }

    public BigDecimal getAddInvestMinAmount() {
        return addInvestMinAmount;
    }

    public void setAddInvestMinAmount(BigDecimal addInvestMinAmount) {
        this.addInvestMinAmount = addInvestMinAmount;
    }

    public BigDecimal getAddInvestMaxAmount() {
        return addInvestMaxAmount;
    }

    public void setAddInvestMaxAmount(BigDecimal addInvestMaxAmount) {
        this.addInvestMaxAmount = addInvestMaxAmount;
    }

    public BigDecimal getInvestDailyLimit() {
        return investDailyLimit;
    }

    public void setInvestDailyLimit(BigDecimal investDailyLimit) {
        this.investDailyLimit = investDailyLimit;
    }

    public BigDecimal getSingleInvestMinAmount() {
        return singleInvestMinAmount;
    }

    public void setSingleInvestMinAmount(BigDecimal singleInvestMinAmount) {
        this.singleInvestMinAmount = singleInvestMinAmount;
    }

    public BigDecimal getSingleInvestMaxAmount() {
        return singleInvestMaxAmount;
    }

    public void setSingleInvestMaxAmount(BigDecimal singleInvestMaxAmount) {
        this.singleInvestMaxAmount = singleInvestMaxAmount;
    }

    public BigDecimal getRaisedAmount() {
        return raisedAmount;
    }

    public void setRaisedAmount(BigDecimal raisedAmount) {
        this.raisedAmount = raisedAmount;
    }
}
