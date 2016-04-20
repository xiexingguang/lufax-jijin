package com.lufax.jijin.base.constant;

public enum SLPAccountTransferRecordStatus {
    NEW(0),
    SUCCESS(1),
    FAIL(2),
    ONGOING(3);

    private Integer code;

    private SLPAccountTransferRecordStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
