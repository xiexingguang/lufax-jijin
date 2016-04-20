package com.lufax.jijin.fundation.constant;

public enum YebTransactionType {

    SELL_FREEZE("04", "赎回冻结"),
    SELL_SUCCESS_UNFREEZE("05", "赎回成功解冻"),
    SELL_FAIL_UNFREEZE("07", "赎回失败解冻");

    private String code;
    private String desc;

    YebTransactionType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }
}
