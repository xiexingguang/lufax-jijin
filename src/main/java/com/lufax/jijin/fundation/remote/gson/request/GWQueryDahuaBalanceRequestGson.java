package com.lufax.jijin.fundation.remote.gson.request;

/**
 * Created by NiuZhanJun on 10/12/15.
 */
public class GWQueryDahuaBalanceRequestGson extends GWBaseRequest {
    private int accountType;//垫资户类型
    private String requestTime;//请求时间

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
