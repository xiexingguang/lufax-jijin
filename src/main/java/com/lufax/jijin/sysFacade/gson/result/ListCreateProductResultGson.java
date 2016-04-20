package com.lufax.jijin.sysFacade.gson.result;


public class ListCreateProductResultGson {

    private Long productId;
    private String retCode;
    private String retMessage;

    public String getRetCode() {
        return retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public Long getProductId() {
        return productId;
    }

    public boolean isSuccess() {
        return retCode.equalsIgnoreCase(ReturnCode.SUCCESS.name());
    }

    public boolean isParamErr() {
        return retCode.equalsIgnoreCase(ReturnCode.PARAM_ERROR.name());
    }

    public boolean isFailed() {
        return retCode.equalsIgnoreCase(ReturnCode.FAIL.name());
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    private enum ReturnCode {

        SUCCESS("000"),
        PARAM_ERROR("001"),
        FAIL("009");

        private String code;

        private ReturnCode(String code) {
            this.code = code;
        }
    }

}
