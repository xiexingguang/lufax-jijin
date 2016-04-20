package com.lufax.jijin.product.constant;

public enum ProductStatusChangeRemark {
    NONE("无"),
    MANUAL_CANCEL("手动撤销"),
    PREPAY_CANCEL("提前还款撤销"),
    TIME_EXPIRED("超时失效");

    private String desc;

    ProductStatusChangeRemark(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isManualCancel() {
        return this.equals(MANUAL_CANCEL);
    }

    public boolean isPrepayCancel() {
        return this.equals(PREPAY_CANCEL);
    }
}
