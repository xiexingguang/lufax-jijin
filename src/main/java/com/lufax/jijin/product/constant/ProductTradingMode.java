package com.lufax.jijin.product.constant;

public enum ProductTradingMode {
    NOW("00"),
    PRE_SALE("01"),
    BID("06"),
    COLLECTION("07"),
    OFFER("10");

    private String value;

    ProductTradingMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
