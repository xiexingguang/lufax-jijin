package com.lufax.jijin.product.constant;


public enum ProductType {

    LOAN_REQUEST("00", ""),
    TRANSFER_REQUEST("01", ""),
    TIME_SPLIT("03", "");

    private String value;
    private String desc;

    private ProductType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
