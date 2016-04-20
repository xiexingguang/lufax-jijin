package com.lufax.jijin.base.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalUtils {
    public static final MathContext _SCALE = new MathContext(100, RoundingMode.HALF_UP);

    public static BigDecimal divide(BigDecimal one, BigDecimal another) {
        return one.divide(another, _SCALE);
    }

    public static BigDecimal multiply(BigDecimal one, BigDecimal another) {
        return one.multiply(another, _SCALE);
    }

    public static BigDecimal pow(BigDecimal one, int another) {
        return one.pow(another, _SCALE);
    }

    public static BigDecimal subtract(BigDecimal one, BigDecimal another) {
        return one.subtract(another);
    }

    public static BigDecimal add(BigDecimal one, BigDecimal another) {
        return one.add(another);
    }
}
