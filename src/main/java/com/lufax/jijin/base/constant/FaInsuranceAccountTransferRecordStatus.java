package com.lufax.jijin.base.constant;

public enum FaInsuranceAccountTransferRecordStatus {
    NEW(0),
    SUCCESS(1),
    FAIL(2);

    private Integer code;

    private FaInsuranceAccountTransferRecordStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
