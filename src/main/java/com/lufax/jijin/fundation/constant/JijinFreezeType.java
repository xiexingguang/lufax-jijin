package com.lufax.jijin.fundation.constant;

/**
 * Created by wuxiujun685 on 2016/3/11.
 */
public enum JijinFreezeType {
    COMMON("0"),
    MARKET("1");//冻结类型
    private String code;

    JijinFreezeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
