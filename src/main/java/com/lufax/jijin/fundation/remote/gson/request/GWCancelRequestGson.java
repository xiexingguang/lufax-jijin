package com.lufax.jijin.fundation.remote.gson.request;


public class GWCancelRequestGson extends GWBaseRequest {
    private String originalAppSheetNo;

    public String getOriginalAppSheetNo() {
        return originalAppSheetNo;
    }

    public void setOriginalAppSheetNo(String originalAppSheetNo) {
        this.originalAppSheetNo = originalAppSheetNo;
    }
}
