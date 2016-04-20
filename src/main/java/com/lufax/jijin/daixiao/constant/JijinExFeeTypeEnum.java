package com.lufax.jijin.daixiao.constant;

/**
 * Created by NiuZhanJun on 7/28/15.
 */
public enum JijinExFeeTypeEnum {
    认购费率("认购费率", "20"),
    申购费率("申购费率", "22"),
    赎回费率("赎回费率", "24");

    private String feeTypeName;
    private String feeTypeCode;

    JijinExFeeTypeEnum(String feeTypeName, String feeTypeCode) {
        this.feeTypeName = feeTypeName;
        this.feeTypeCode = feeTypeCode;
    }

    public static String getFeeTypeCodeByName(String typeName) {
        for (JijinExFeeTypeEnum typeEnum : JijinExFeeTypeEnum.values()) {
            if (typeEnum.getFeeTypeName().equals(typeName)) {
                return typeEnum.getFeeTypeCode();
            }
        }
        return null;
    }

    public static String getFeeTypeNameByCode(String typeCode) {
        for (JijinExFeeTypeEnum typeEnum : JijinExFeeTypeEnum.values()) {
            if (typeEnum.getFeeTypeCode().equals(typeCode)) {
                return typeEnum.getFeeTypeName();
            }
        }
        return null;
    }


    public String getFeeTypeCode() {
        return feeTypeCode;
    }

    public void setFeeTypeCode(String feeTypeCode) {
        this.feeTypeCode = feeTypeCode;
    }

    public String getFeeTypeName() {
        return feeTypeName;
    }

    public void setFeeTypeName(String feeTypeName) {
        this.feeTypeName = feeTypeName;
    }
}
