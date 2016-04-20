package com.lufax.jijin.ylx.enums;

public enum YLXBuyRequestStatus {
    BUYING("BUYING", "申购中"),
    NO_ACCOUNT("NO_ACCOUNT", "没有账户"),
    SUCCESS("SUCCESS", "申购成功"),
    FAIL("FAIL", "申购失败"),
    UN_CONFIRM("UN_CONFIRM", "confirm文件中不存在该记录,无法对账");

    private String code;
    private String desc;

    YLXBuyRequestStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
}
