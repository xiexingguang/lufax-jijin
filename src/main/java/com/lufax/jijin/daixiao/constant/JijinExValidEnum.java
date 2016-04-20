package com.lufax.jijin.daixiao.constant;


public enum JijinExValidEnum {
    IS_VALID(1),
    IS_NOT_VALID(0);

    private Integer code;

    JijinExValidEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
