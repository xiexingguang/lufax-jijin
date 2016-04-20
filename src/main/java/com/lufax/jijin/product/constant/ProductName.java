package com.lufax.jijin.product.constant;

public enum ProductName {
    JIN_YING_TONG("5", "金盈通"),
    HUI_FU("6", "汇富");

    private String value;
    private String desc;

    ProductName(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
