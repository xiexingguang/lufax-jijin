package com.lufax.jijin.product.constant;

public enum ProductStatus {
    UNPLANNED("未计划"),
    PLANNED("已计划"),
    PREVIEW("预览中"),
    ONLINE("已上架"),
    OFFLINE("强制下架"),
    EXPIRED("过期下架"),
    SPLIT("期限拆分"),
    DONE("完成"),
    CANCELED("取消"),
    REJECTED("电话拒绝"),
    WAITING_CONFIRM("等待确认"),
    PRESOLD("预约");

    private String value;

    ProductStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
