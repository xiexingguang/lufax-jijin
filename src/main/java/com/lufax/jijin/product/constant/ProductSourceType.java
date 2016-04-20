package com.lufax.jijin.product.constant;

public enum ProductSourceType {
    FA("2"),
    HF("3"),
    YLX("4"),
    PJ("5");
    private String code;

    private ProductSourceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ProductSourceType convertToEnum(String code) {
        for (ProductSourceType sourceType : ProductSourceType.values()) {
            if (sourceType.getCode().equalsIgnoreCase(code)) {
                return sourceType;
            }
        }
        return null;
    }
}
