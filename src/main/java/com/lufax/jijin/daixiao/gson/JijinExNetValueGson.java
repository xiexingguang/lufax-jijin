package com.lufax.jijin.daixiao.gson;


import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;

import java.math.BigDecimal;

public class JijinExNetValueGson {

    private String fundCode; //基金代码
    private String endDate; //截止日期
    private BigDecimal netValue; //单位净值
    private BigDecimal totalNetValue; //累计净值
    private BigDecimal dayIncrease; //日增长率

    public JijinExNetValueGson(JijinExNetValueDTO jijinExNetValueDTO) {
        this.fundCode = jijinExNetValueDTO.getFundCode();
        this.endDate = jijinExNetValueDTO.getEndDate();
        this.netValue = jijinExNetValueDTO.getNetValue();
        this.totalNetValue = jijinExNetValueDTO.getTotalNetValue();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    public BigDecimal getTotalNetValue() {
        return totalNetValue;
    }

    public void setTotalNetValue(BigDecimal totalNetValue) {
        this.totalNetValue = totalNetValue;
    }

    public BigDecimal getDayIncrease() {
        return dayIncrease;
    }

    public void setDayIncrease(BigDecimal dayIncrease) {
        this.dayIncrease = dayIncrease;
    }
}
