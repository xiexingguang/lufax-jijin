package com.lufax.jijin.fundation.constant;

/**
 * Created by NiuZhanJun on 9/24/15.
 */
public enum RedeemTypeEnum {
    FAST("0", "T+0赎回"),
    NORMAL("1", "T+1赎回");

    private String typeCode;
    private String typeName;

    RedeemTypeEnum(String typeCode, String typeName) {
        setTypeCode(typeCode);
        setTypeName(typeName);
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
