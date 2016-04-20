package com.lufax.jijin.product.constant;


public enum ProductCollectionMode {
    MONTHLY_AVERAGE_CAPITAL_PLUS_INTEREST("1", "每月等额本息", "1"),
    MONTHLY_CAPITAL_PLUS_INTEREST("2", "每月本息", "2"),
    ONE_TIME_CAPITAL_PLUS_INTEREST("3", "一次性还本付息", "3"),
    MONTHLY_PAY_INTEREST("4", "按月付息到期还本", "4");

    private String collectionMode;
    private String desc;
    private String paymentMethod;

    ProductCollectionMode(String collectionMode, String desc, String paymentMethod) {
        this.collectionMode = collectionMode;
        this.desc = desc;
        this.paymentMethod = paymentMethod;
    }

    public static String convertToCollectionMode(String paymentMethod) {
        for (ProductCollectionMode mode : ProductCollectionMode.values()) {
            if (mode.getPaymentMethod().equalsIgnoreCase(paymentMethod))
                return mode.getCollectionMode();
        }
        return "";
    }
    public static ProductCollectionMode convert(String paymentMethod){
    	for (ProductCollectionMode mode : ProductCollectionMode.values()) {
            if (mode.collectionMode.equalsIgnoreCase(paymentMethod)) return mode;
        }
        return null;
    }
    public String getCollectionMode() {
        return collectionMode;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
