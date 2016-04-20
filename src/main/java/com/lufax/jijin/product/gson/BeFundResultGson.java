package com.lufax.jijin.product.gson;

import com.google.gson.Gson;

public class BeFundResultGson {
    private String retCode;
    private String retMsg;

    public BeFundResultGson(String retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public static String successfulBeFundResult() {
        return new Gson().toJson(new BeFundResultGson("00", ""));
    }
}
