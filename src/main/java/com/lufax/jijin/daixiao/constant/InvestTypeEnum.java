package com.lufax.jijin.daixiao.constant;


public enum InvestTypeEnum {

    债卷型("债券型", "BOND"),
    保本型("保本型", "PRINCIPAL"),
    股票型("股票型", "STOCK"),
    混合型("混合型", "MIXED"),
    货币市场型("货币市场型", "CURRENCY"),
    商品型("商品型", "COMMODITY"),
    其他("其他", "OTHER");

    private String investTypeName;
    private String investTypeCode;

    InvestTypeEnum(String investTypeName, String investTypeCode) {
        this.investTypeName = investTypeName;
        this.investTypeCode = investTypeCode;
    }

    public static String getInvestTypeCodeByName(String typeName) {
        for (InvestTypeEnum typeEnum : InvestTypeEnum.values()) {
            if (typeEnum.getInvestTypeName().equals(typeName)) {
                return typeEnum.getInvestTypeCode();
            }
        }
        return "OTHER";
    }

    public static String getInvestTypeNameByCode(String typeCode) {
        for (InvestTypeEnum typeEnum : InvestTypeEnum.values()) {
            if (typeEnum.getInvestTypeCode().equals(typeCode)) {
                return typeEnum.getInvestTypeName();
            }
        }
        return "其他";
    }

    public String getInvestTypeName() {
        return investTypeName;
    }

    public void setInvestTypeName(String investTypeName) {
        this.investTypeName = investTypeName;
    }

    public String getInvestTypeCode() {
        return investTypeCode;
    }

    public void setInvestTypeCode(String investTypeCode) {
        this.investTypeCode = investTypeCode;
    }
}
