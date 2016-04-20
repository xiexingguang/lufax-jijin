package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExDividendDTO;

import java.math.BigDecimal;


public class JijinExDividendGson {

    private String fundCode; //基金代码
    private String annDate;//公告日期
    private String recordDate;//权益登记日
    private String exDate;//除息日
    private String divEdexDate;//场外除息日
    private String dividendDate;//分红发放日
    private String currencyCode;//货币代码
    private BigDecimal perDividend;// 每份分红（元）
    private String divDate;//净值除权日

    public JijinExDividendGson(JijinExDividendDTO jijinExDividendDTO) {
        this.fundCode = jijinExDividendDTO.getFundCode();
        this.annDate = jijinExDividendDTO.getAnnDate();
        this.recordDate = jijinExDividendDTO.getRecordDate();
        this.exDate = jijinExDividendDTO.getExDate();
        this.divEdexDate = jijinExDividendDTO.getDivEdexDate();
        this.dividendDate = jijinExDividendDTO.getDividendDate();
        this.currencyCode = jijinExDividendDTO.getCurrencyCode();
        this.perDividend = jijinExDividendDTO.getPerDividend();
        this.divDate = jijinExDividendDTO.getDivDate();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getAnnDate() {
        return annDate;
    }

    public void setAnnDate(String annDate) {
        this.annDate = annDate;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getExDate() {
        return exDate;
    }

    public void setExDate(String exDate) {
        this.exDate = exDate;
    }

    public String getDivEdexDate() {
        return divEdexDate;
    }

    public void setDivEdexDate(String divEdexDate) {
        this.divEdexDate = divEdexDate;
    }

    public String getDividendDate() {
        return dividendDate;
    }

    public void setDividendDate(String dividendDate) {
        this.dividendDate = dividendDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getPerDividend() {
        return perDividend;
    }

    public void setPerDividend(BigDecimal perDividend) {
        this.perDividend = perDividend;
    }

	public String getDivDate() {
		return divDate;
	}

	public void setDivDate(String divDate) {
		this.divDate = divDate;
	}
    
    
}
