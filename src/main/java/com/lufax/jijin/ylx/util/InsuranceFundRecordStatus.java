package com.lufax.jijin.ylx.util;

public enum InsuranceFundRecordStatus {

    WITHDRAW_NEW(10, "未代付"),
    WITHDRAW_PROCESSING(11, "代付中"),
    WITHDRAW_SUCCESS(12, "代付成功"),
    WITHDRAW_FAIL(13, "代付失败"),
    INVOKE_FUND_FAIL(14,"调用FUND接口失败"),
    RECHARGE_NEW(0, "未代扣"),
    RECHARGE_PROCESSING(1, "代扣中"),
    RECHARGE_SUCCESS(2, "代扣成功"),
    DISTRIBUTING(5, "分发money给用户的job正在进行中"),
    DISTRIBUTED(6, "完成分发money给用户"),
    RECHARGE_FAIL(3, "代扣失败");
    
    private Integer code;
    private String name;

    private InsuranceFundRecordStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
