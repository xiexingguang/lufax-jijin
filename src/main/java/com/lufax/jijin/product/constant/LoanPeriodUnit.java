package com.lufax.jijin.product.constant;

public enum LoanPeriodUnit {
    DAY("1", "天"),
    WEEK("2", "周"),
    MONTH("3", "个月"),
    YEAR("4", "年");

    private String value;
    private String desc;

    LoanPeriodUnit(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isWeek() {
        return this.equals(WEEK);
    }

    public boolean isDay() {
        return this.equals(DAY);
    }

    public boolean isMonth() {
        return this.equals(MONTH);
    }

    public boolean isYear() {
        return this.equals(YEAR);
    }

    public static LoanPeriodUnit convert(String unit) {
        if (unit.equalsIgnoreCase(MONTH.getValue())) {
            return MONTH;
        } else if (unit.equalsIgnoreCase(WEEK.getValue())) {
            return WEEK;
        } else if (unit.equalsIgnoreCase(DAY.getValue())) {
            return DAY;
        } else if (unit.equalsIgnoreCase(YEAR.getValue())) {
            return YEAR;
        } else
            return null;
    }
}
