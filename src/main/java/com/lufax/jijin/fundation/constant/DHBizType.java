package com.lufax.jijin.fundation.constant;

public enum DHBizType {

    PURCHASE("04"),//余额宝转入
    APPLY("02"),
    FIXED_INVESTMENT("03"), //定投
    REDEEM("10"),//余额宝实时转出
    REDEEM_T1("11");//余额宝T+1

    private String code;

    DHBizType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
