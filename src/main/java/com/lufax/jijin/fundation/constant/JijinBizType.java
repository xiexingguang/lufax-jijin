package com.lufax.jijin.fundation.constant;


public enum JijinBizType {
    REGISTER("001"),
    APPLY("020"),
    APPLY_CONFIRM_TMP("120"),
    APPLY_CONFIRM("130"),
    CREATE_FAIL("149"),
    PURCHASE("022"),
    PURCHASE_CONFIRM("122"),
    REDEEM("024"),
    REDEEM_CONFIRM("124"),
    CANCEL_ORDER("053"),
    PAF_PAY("777"),
    PAF_CANCEL("888"),
    BUY_NOTIFY("999"),
    DIVIDEND_MODIFY_APPLY("029"),
    DIVIDEND_MODIFY_CONFIRM("129"),
    DAHUA_WITHDRAW_RECHARGE("201"),
    FORCE_INCREASE("144"),
    FORCE_REDUCED("145"),
    FORCE_REDEEM("142");//强制赎回
    
    private String code;

    JijinBizType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
