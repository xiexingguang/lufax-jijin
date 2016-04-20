package com.lufax.jijin.ylx.enums;

public enum YLXSellRequestStatus {
    SELLING("SELLING", "赎回中"),
    CONFIRMED("CONFIRMED","赎回确认"),
    SUCCESS("SUCCESS", "赎回成功"),
    FAIL("FAIL", "赎回失败"),
    UN_CONFIRM("UN_CONFIRM", "confirm文件中不存在该记录,无法对账");

    private String code;
    private String desc;

    YLXSellRequestStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
}
