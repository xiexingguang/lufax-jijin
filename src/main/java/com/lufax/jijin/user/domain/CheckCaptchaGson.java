package com.lufax.jijin.user.domain;


public class CheckCaptchaGson {

    private String result;


    public CheckCaptchaGson() {
    }

    public boolean isSuccess() {
        return result.equals("SUCCESS");
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
