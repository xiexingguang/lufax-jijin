package com.lufax.jijin.yeb.repository;

public enum IncomeSyncStatus {
    NEW("1", "未处理"),
    PROCESSING("2", "处理中"),
    SUCCESS("3", "处理成功"),
    FAIL("4", "处理失败");

    private String code;
    private String desc;

    IncomeSyncStatus(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
}
