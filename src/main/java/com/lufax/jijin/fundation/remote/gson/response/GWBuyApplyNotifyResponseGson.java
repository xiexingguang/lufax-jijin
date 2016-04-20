package com.lufax.jijin.fundation.remote.gson.response;


public class GWBuyApplyNotifyResponseGson extends GWBaseResponseGson {

    private String transactionDate;
    private String transactionTime;

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }
}
