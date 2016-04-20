package com.lufax.jijin.sysFacade.gson;

public class BaseGson {
    private String retCode;
    private String retMessage;

    public BaseGson() {
    }

    public BaseGson(String retMessage, String retCode) {
        this.retMessage = retMessage;
        this.retCode = retCode;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return null == retMessage ? "" : retMessage;
    }

    public String getShortRetMessage() {
        return null == retMessage ? "" : (retMessage.length() < 100 ? retMessage : retMessage.substring(0, 100));
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }
}
