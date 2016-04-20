package com.lufax.jijin.sysFacade.gson.result;

public class ListResultGson {

    private String result;

    public String getResult() {
        return result;
    }

    public boolean isSuccess() {
        return Boolean.valueOf(getResult());
    }

    public void setResult(String result) {
        this.result = result;
    }
}
