package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExFxPerformDTO;

import java.math.BigDecimal;


public class JijinExFxPerformGson {

    private String findexCode;  //指数代码
    private String performanceDay;  //日期
    private BigDecimal riseRateOneWeek;  //最近1周涨跌幅
    private BigDecimal riseRateOneMonth;  //最近 1月涨跌幅
    private BigDecimal riseRateThreeMonth;  //最近 3月涨跌幅
    private BigDecimal riseRateSixMonth;  //最近 6月涨跌幅
    private BigDecimal riseRateOneYear;  //最近1年涨跌幅
    private BigDecimal riseRateTwoYear;  //最近2年涨跌幅
    private BigDecimal riseRateThreeYear;  //最近3年涨跌幅
    private BigDecimal riseRateThisYear;  //本年来涨跌幅

    public JijinExFxPerformGson(JijinExFxPerformDTO jijinExFxPerformDTO) {
        this.findexCode = jijinExFxPerformDTO.getFindexCode();
        this.performanceDay = jijinExFxPerformDTO.getPerformanceDay();
        this.riseRateOneWeek = jijinExFxPerformDTO.getRiseRateOneWeek();
        this.riseRateOneMonth = jijinExFxPerformDTO.getRiseRateOneMonth();
        this.riseRateThreeMonth = jijinExFxPerformDTO.getRiseRateThreeMonth();
        this.riseRateSixMonth = jijinExFxPerformDTO.getRiseRateSixMonth();
        this.riseRateOneYear = jijinExFxPerformDTO.getRiseRateOneYear();
        this.riseRateTwoYear = jijinExFxPerformDTO.getRiseRateTwoYear();
        this.riseRateThreeYear = jijinExFxPerformDTO.getRiseRateThreeYear();
        this.riseRateThisYear = jijinExFxPerformDTO.getRiseRateThisYear();
    }

    public String getFindexCode() {
        return findexCode;
    }

    public void setFindexCode(String findexCode) {
        this.findexCode = findexCode;
    }

    public String getPerformanceDay() {
        return performanceDay;
    }

    public void setPerformanceDay(String performanceDay) {
        this.performanceDay = performanceDay;
    }

    public BigDecimal getRiseRateOneWeek() {
        return riseRateOneWeek;
    }

    public void setRiseRateOneWeek(BigDecimal riseRateOneWeek) {
        this.riseRateOneWeek = riseRateOneWeek;
    }

    public BigDecimal getRiseRateOneMonth() {
        return riseRateOneMonth;
    }

    public void setRiseRateOneMonth(BigDecimal riseRateOneMonth) {
        this.riseRateOneMonth = riseRateOneMonth;
    }

    public BigDecimal getRiseRateThreeMonth() {
        return riseRateThreeMonth;
    }

    public void setRiseRateThreeMonth(BigDecimal riseRateThreeMonth) {
        this.riseRateThreeMonth = riseRateThreeMonth;
    }

    public BigDecimal getRiseRateSixMonth() {
        return riseRateSixMonth;
    }

    public void setRiseRateSixMonth(BigDecimal riseRateSixMonth) {
        this.riseRateSixMonth = riseRateSixMonth;
    }

    public BigDecimal getRiseRateOneYear() {
        return riseRateOneYear;
    }

    public void setRiseRateOneYear(BigDecimal riseRateOneYear) {
        this.riseRateOneYear = riseRateOneYear;
    }

    public BigDecimal getRiseRateTwoYear() {
        return riseRateTwoYear;
    }

    public void setRiseRateTwoYear(BigDecimal riseRateTwoYear) {
        this.riseRateTwoYear = riseRateTwoYear;
    }

    public BigDecimal getRiseRateThreeYear() {
        return riseRateThreeYear;
    }

    public void setRiseRateThreeYear(BigDecimal riseRateThreeYear) {
        this.riseRateThreeYear = riseRateThreeYear;
    }

    public BigDecimal getRiseRateThisYear() {
        return riseRateThisYear;
    }

    public void setRiseRateThisYear(BigDecimal riseRateThisYear) {
        this.riseRateThisYear = riseRateThisYear;
    }
}
