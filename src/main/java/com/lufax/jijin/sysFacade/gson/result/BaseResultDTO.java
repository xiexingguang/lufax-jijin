package com.lufax.jijin.sysFacade.gson.result;


public class BaseResultDTO {
    private Long responseNo;
    private String retCode;
    private String retMessage;

    public BaseResultDTO() {
    }

    public BaseResultDTO(String retMessage, String retCode, Long responseNo) {
        this.retMessage = retMessage;
        this.retCode = retCode;
        this.responseNo = responseNo;
    }

    public String getResponseNo() {
        if (responseNo != null) {
            return responseNo.toString();
        }
        return "";
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    public boolean isSuccess() {
        return "000".equals(this.retCode);
    }

    public boolean isLowBalance() {
        return "002".equals(this.retCode);
    }

    @Override
    public String toString() {
        return "{responseNo: " + responseNo + " retCode: " + retCode + " retMessage: " + retMessage + "}";
    }
}
