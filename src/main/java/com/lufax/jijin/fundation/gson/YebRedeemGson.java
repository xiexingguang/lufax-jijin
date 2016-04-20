package com.lufax.jijin.fundation.gson;


import java.math.BigDecimal;

public class YebRedeemGson extends BaseGson{

    private String result;
    private BigDecimal remainingAmountOfToday;
    private BigDecimal redeemMaxPerDay;
    private BigDecimal redeemedAmountOfToday;

    public YebRedeemGson(BigDecimal remainingAmountOfToday, BigDecimal redeemedAmountOfToday, BigDecimal redeemMaxPerDay, String result) {
        this.remainingAmountOfToday = remainingAmountOfToday;
        this.redeemedAmountOfToday = redeemedAmountOfToday;
        this.redeemMaxPerDay = redeemMaxPerDay;
        this.result = result;
    }
}

