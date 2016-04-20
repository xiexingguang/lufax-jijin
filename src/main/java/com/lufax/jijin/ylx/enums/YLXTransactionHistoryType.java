package com.lufax.jijin.ylx.enums;

public enum YLXTransactionHistoryType {
    BUY("BUY","认购"),
    PURCHASE("PURCHASE","申购"),
    REDEEM("REDEEM","赎回");

    private String code;
    private String desc;

    YLXTransactionHistoryType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
