package com.lufax.jijin.ylx.enums;

public enum YLXOpenRequestStatus {
    OPENING("OPENING", "开户中"),
    UN_CONFIRM("UN_CONFIRM", "confirm文件中不存在该记录,无法对账"),
    SUCCESS("SUCCESS", "开户成功"),
    FAIL("FAIL", "开户失败");

    private String code;
    private String desc;

    YLXOpenRequestStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
}
