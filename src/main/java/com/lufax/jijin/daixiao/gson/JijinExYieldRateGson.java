package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExYieldRateDTO;

import java.math.BigDecimal;

/**
 * Created by NiuZhanJun on 9/16/15.
 */
public class JijinExYieldRateGson {
    private String fundCode;
    private String startDate;   //起始日期
    private String endDate;     //截止日期
    private String noticeDate;  //公告日期
    private BigDecimal benefitPerTenthousand; /* 万份收益 */
    private BigDecimal interestratePerSevenday; /* 七日年化收益率 */

    public JijinExYieldRateGson(JijinExYieldRateDTO jijinExYieldRateDTO) {
        setFundCode(jijinExYieldRateDTO.getFundCode());
        setStartDate(jijinExYieldRateDTO.getStartDate());
        setEndDate(jijinExYieldRateDTO.getEndDate());
        setNoticeDate(jijinExYieldRateDTO.getNoticeDate());
        setBenefitPerTenthousand(jijinExYieldRateDTO.getBenefitPerTenthousand());
        setInterestratePerSevenday(jijinExYieldRateDTO.getInterestratePerSevenday());
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public BigDecimal getInterestratePerSevenday() {
        return interestratePerSevenday;
    }

    public void setInterestratePerSevenday(BigDecimal interestratePerSevenday) {
        this.interestratePerSevenday = interestratePerSevenday;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public BigDecimal getBenefitPerTenthousand() {
        return benefitPerTenthousand;
    }

    public void setBenefitPerTenthousand(BigDecimal benefitPerTenthousand) {
        this.benefitPerTenthousand = benefitPerTenthousand;
    }
}
