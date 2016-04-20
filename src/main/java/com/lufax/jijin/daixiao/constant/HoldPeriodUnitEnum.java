package com.lufax.jijin.daixiao.constant;

/**
 * Created by NiuZhanJun on 8/18/15.
 */
public enum HoldPeriodUnitEnum {
    天("1"),
    日("1"),
    周("2"),
    月("3"),
    年("4");

    private String unitValue;

    HoldPeriodUnitEnum(String unitValue) {
        this.setUnitValue(unitValue);
    }

    public static String getUnitValueByName(String unitName) {
        if (unitName == null || "".equals(unitName)) {
            return null;
        }
        for (HoldPeriodUnitEnum typeEnum : HoldPeriodUnitEnum.values()) {
            if (typeEnum.name().equals(unitName)) {
                return typeEnum.getUnitValue();
            }
        }
        return null;
    }

    public String getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(String unitValue) {
        this.unitValue = unitValue;
    }
}
