package com.lufax.jijin.fundation.remote.gson.response;

/**
 * Created by NiuZhanJun on 10/12/15.
 */
public class GWQueryDahuaBalanceResponseGson extends GWBaseResponseGson {
    private String balance;//账户可用余额

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
