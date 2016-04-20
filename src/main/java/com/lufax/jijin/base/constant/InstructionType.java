package com.lufax.jijin.base.constant;


public enum InstructionType {

    WITHDRAWAL("W"),
    RECHARGE("R"),
    TRANSFER("T"),
    FLUSH("F");

    private String value;

    InstructionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
