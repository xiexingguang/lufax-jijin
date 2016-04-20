package com.lufax.jijin.fundation.remote.gson.request;

public class GWTransformBuyRequestGson extends GWBaseRequest {

    private String fundCode; //赎回基金
    private String applicationVol; //赎回基金份额
    private String hugeSum = "1";
    private String targetFundCode;
    private String targetBusinessCode;
    private String redeemToBuyType; // 赎回转认申购类型
    private String chargeType; //原基金收费方式
    private String targetChargeType;//目标基金收费方式
    private String isAgreeRisk;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getApplicationVol() {
        return applicationVol;
    }

    public void setApplicationVol(String applicationVol) {
        this.applicationVol = applicationVol;
    }

    public String getHugeSum() {
        return hugeSum;
    }

    public void setHugeSum(String hugeSum) {
        this.hugeSum = hugeSum;
    }

    public String getTargetFundCode() {
        return targetFundCode;
    }

    public void setTargetFundCode(String targetFundCode) {
        this.targetFundCode = targetFundCode;
    }

    public String getTargetBusinessCode() {
        return targetBusinessCode;
    }

    public void setTargetBusinessCode(String targetBusinessCode) {
        this.targetBusinessCode = targetBusinessCode;
    }

    public String getRedeemToBuyType() {
        return redeemToBuyType;
    }

    public void setRedeemToBuyType(String redeemToBuyType) {
        this.redeemToBuyType = redeemToBuyType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getTargetChargeType() {
        return targetChargeType;
    }

    public void setTargetChargeType(String targetChargeType) {
        this.targetChargeType = targetChargeType;
    }

    public String getIsAgreeRisk() {
        return isAgreeRisk;
    }

    public void setIsAgreeRisk(String isAgreeRisk) {
        this.isAgreeRisk = isAgreeRisk;
    }


}