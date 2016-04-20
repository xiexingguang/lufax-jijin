package com.lufax.jijin.ylx.util;

public enum InsuranceFundRecordType {

    WITHDRAW(1, "代付"),
    RECHARGE(2, "代扣");

    private Integer code;
    private String name;

    private InsuranceFundRecordType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static InsuranceFundRecordType getByCode(Integer code){
    	for (InsuranceFundRecordType type : InsuranceFundRecordType.values()) {
            if (type.code.equals(code)) return type;
        }
        return null;
    }
}
