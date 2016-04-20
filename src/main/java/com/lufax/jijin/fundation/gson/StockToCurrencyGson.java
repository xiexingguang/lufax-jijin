package com.lufax.jijin.fundation.gson;

import java.math.BigDecimal;

/*
 * TODO 股转货 Gson
 */
public class StockToCurrencyGson {
    //来自JijinInfoDTO -                      BUS_JIJIN_INFO
    /* 基金产品代码 */
    private String fundName;
    /* 基金名称 */
    private String fundCode;
    /* 起购金额 */
//    private BigDecimal minInvestAmount;
    
    //来自JijinNetValueDTO -                  bus_jijin_net_value表
    /* 七日年化收益率 */
    private BigDecimal interestratePerSevenday;
    /* 万份收益 */
    private BigDecimal benefitPerTenthousand;
    
    //来自JijinExMfPerformDTO -               bus_jijin_ex_mf_perform表
    /* 收益率(一个月) */
    private BigDecimal benefitOneMonth;  
    /* 收益率(三个月) */
    private BigDecimal benefitThreeMonth;  
    /* 收益率(本年以来) */
    private BigDecimal benefitThisYear;  
    /* 收益率(成立以来) */
    private BigDecimal benefitTotal;
    
    public String getFundName() {
        return fundName;
    }
    public String getFundCode() {
        return fundCode;
    }
//    public BigDecimal getMinInvestAmount() {
//        return minInvestAmount;
//    }
    public BigDecimal getInterestratePerSevenday() {
        return interestratePerSevenday;
    }
    public BigDecimal getBenefitPerTenthousand() {
        return benefitPerTenthousand;
    }
    public BigDecimal getBenefitOneMonth() {
        return benefitOneMonth;
    }
    public BigDecimal getBenefitThreeMonth() {
        return benefitThreeMonth;
    }
    public BigDecimal getBenefitThisYear() {
        return benefitThisYear;
    }
    public BigDecimal getBenefitTotal() {
        return benefitTotal;
    }
    public void setFundName(String fundName) {
        this.fundName = fundName;
    }
    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }
//    public void setMinInvestAmount(BigDecimal minInvestAmount) {
//        this.minInvestAmount = minInvestAmount;
//    }
    public void setInterestratePerSevenday(BigDecimal interestratePerSevenday) {
        this.interestratePerSevenday = interestratePerSevenday;
    }
    public void setBenefitPerTenthousand(BigDecimal benefitPerTenthousand) {
        this.benefitPerTenthousand = benefitPerTenthousand;
    }
    public void setBenefitOneMonth(BigDecimal benefitOneMonth) {
        this.benefitOneMonth = benefitOneMonth;
    }
    public void setBenefitThreeMonth(BigDecimal benefitThreeMonth) {
        this.benefitThreeMonth = benefitThreeMonth;
    }
    public void setBenefitThisYear(BigDecimal benefitThisYear) {
        this.benefitThisYear = benefitThisYear;
    }
    public void setBenefitTotal(BigDecimal benefitTotal) {
        this.benefitTotal = benefitTotal;
    }
}
