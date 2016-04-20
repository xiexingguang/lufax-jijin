package com.lufax.jijin.daixiao.gson;


import com.lufax.jijin.daixiao.dto.JijinExGradeDTO;

public class JijinExGradeGson {

    private String fundCode; //基金代码
    private String rateDate; //评级日期
    private String starLevel;//星级
    private String fundType; //基金类型
    private String ratingInterval; //评级区间
    private String ratingGagency;//评级机构


    public JijinExGradeGson(JijinExGradeDTO jijinExGradeDTO) {
        this.fundCode = jijinExGradeDTO.getFundCode();
        this.rateDate = jijinExGradeDTO.getRateDate();
        this.starLevel = jijinExGradeDTO.getStarLevel();
        this.fundType = jijinExGradeDTO.getFundType();
        this.ratingInterval = jijinExGradeDTO.getRatingInterval();
        this.ratingGagency = jijinExGradeDTO.getRatingGagency();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getRatingInterval() {
        return ratingInterval;
    }

    public void setRatingInterval(String ratingInterval) {
        this.ratingInterval = ratingInterval;
    }

    public String getRatingGagency() {
        return ratingGagency;
    }

    public void setRatingGagency(String ratingGagency) {
        this.ratingGagency = ratingGagency;
    }
}
