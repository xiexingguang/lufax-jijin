package com.lufax.jijin.fundation.remote.gson.request;

public class GWRedeemRequestGson extends GWBaseRequest {

    private String applicationVol;
    private String chargeType;
    private String businessCode;
    private String fundCode;
    //private String type;
    private String hugeSum;
    //private String isForceDeal;
    private ExtensionGson extension;
    private String type;


    public String getHugeSum() {
        return hugeSum;
    }

    public void setHugeSum(String hugeSum) {
        this.hugeSum = hugeSum;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

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

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public ExtensionGson getExtension() {
        return extension;
    }

    public void setExtension(String requestTime, String payNo) {
        ExtensionGson extensionGson = new ExtensionGson();
        extensionGson.setRequestTime(requestTime);
        extensionGson.setCdCard(payNo);
        this.extension = extensionGson;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    class ExtensionGson {
        private String requestTime;
        private String cdCard;

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getCdCard() {
            return cdCard;
        }

        public void setCdCard(String cdCard) {
            this.cdCard = cdCard;
        }
    }
}
