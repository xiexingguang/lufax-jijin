package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExScopeDTO;

import java.math.BigDecimal;

public class JijinExScopeGson {

    private String fundCode; //基金代码
    private String startDate;//开始日期
    private String reportDate; //报告日期
    private BigDecimal fundShare;  //基金份额
    private BigDecimal purchaseShare;  //期间申购份额
    private BigDecimal redeemShare;    //期间赎回份额
    private BigDecimal netValue;     //单位净值


    public JijinExScopeGson() {
    }

    public JijinExScopeGson(JijinExScopeDTO jijinExScopeDTO) {
        this.fundCode = jijinExScopeDTO.getFundCode();
        this.startDate = jijinExScopeDTO.getStartDate();
        this.reportDate = jijinExScopeDTO.getReportDate();
        this.fundShare = jijinExScopeDTO.getFundShare();
        this.purchaseShare = jijinExScopeDTO.getPurchaseShare();
        this.redeemShare = jijinExScopeDTO.getRedeemShare();
    }

    public BigDecimal getRedeemShare() {
        return redeemShare;
    }

    public void setRedeemShare(BigDecimal redeemShare) {
        this.redeemShare = redeemShare;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public BigDecimal getFundShare() {
        return fundShare;
    }

    public void setFundShare(BigDecimal fundShare) {
        this.fundShare = fundShare;
    }

    public BigDecimal getPurchaseShare() {
        return purchaseShare;
    }

    public void setPurchaseShare(BigDecimal purchaseShare) {
        this.purchaseShare = purchaseShare;
    }

    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }
}
