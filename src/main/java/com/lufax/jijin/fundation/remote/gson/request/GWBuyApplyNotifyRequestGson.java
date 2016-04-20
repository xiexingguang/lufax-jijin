package com.lufax.jijin.fundation.remote.gson.request;


public class GWBuyApplyNotifyRequestGson extends GWBaseRequest {

    private String originalApplicationNo;
    private String appSheetSerialNo;
    private String payState;
    private String paidTime;
    private BuyNotifyExtension extension;


    public BuyNotifyExtension getExtension() {
        return extension;
    }

    public void setExtension(BuyNotifyExtension extension) {
        this.extension = extension;
    }

    public String getOriginalApplicationNo() {
        return originalApplicationNo;
    }

    public void setOriginalApplicationNo(String originalApplicationNo) {
        this.originalApplicationNo = originalApplicationNo;
    }

    public String getAppSheetSerialNo() {
        return appSheetSerialNo;
    }

    public void setAppSheetSerialNo(String appSheetSerialNo) {
        this.appSheetSerialNo = appSheetSerialNo;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }

    public String getPaidTime() {
        return paidTime;
    }

    public void setPaidTime(String paidTime) {
        this.paidTime = paidTime;
    }
}
