package com.lufax.jijin.base.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {
    public static final int PERCENTAGE_SCALE = 4;
    public static final int CURRENCY_SCALE = 2;
    public static final String PERCENTAGE_FORMAT = "#,##0.00%";
    public static final String FA_ASSERT_RATIO_FORMAT = "0.#######%";

    public static BigDecimal withPercentageScale(BigDecimal value) {
        return value.setScale(PERCENTAGE_SCALE, RoundingMode.HALF_UP);
    }

    public static String formatFAAssertRatioWithoutPercent(BigDecimal value) {
        DecimalFormat decimalFormat = new DecimalFormat(FA_ASSERT_RATIO_FORMAT);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        return decimalFormat.format(value).substring(0, decimalFormat.format(value).length() - 1);
    }

    public static BigDecimal withCurrencyScale(BigDecimal value) {
        return value.setScale(CURRENCY_SCALE, RoundingMode.HALF_UP);
    }

    public static boolean isLessThanZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isEqualsToZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isLessOrEqualThanZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static boolean isGreaterOrEqualThan(BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) >= 0;
    }

    public static String percentageFormat(BigDecimal value) {
        return new DecimalFormat(PERCENTAGE_FORMAT).format(value);
    }

    public static String formatForPercentage(BigDecimal value) {
        String perValue = percentageFormat(value);
        return perValue.substring(0, perValue.length() - 1);
    }

    public static boolean isEqual(BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) == 0;
    }

    public static boolean isGreaterThan(BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) > 0;
    }

    public static BigDecimal truncByScale(BigDecimal value, int scale) {
        return value.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    public static String stringFormat(BigDecimal value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(value);
    }

    public static BigDecimal string2BigDecimal(String s){
        try {
            if (s == null || "".equals(s.trim())) {
                return null;
            } else {
                return new BigDecimal(s);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal string2BigDecimalDefaul0(String s){
        try {
            if (s == null || "".equals(s.trim())) {
                return BigDecimal.ZERO;
            } else {
                return new BigDecimal(s);
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
