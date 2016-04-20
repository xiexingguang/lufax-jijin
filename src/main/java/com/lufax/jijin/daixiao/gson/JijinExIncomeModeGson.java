package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExIncomeModeDTO;

import java.math.BigDecimal;

public class JijinExIncomeModeGson {
    private String fundCode;  //基金代码
    private String incomeMode;  //分红方式
    private BigDecimal minHoldShareCount;  //最低持有份额

    public JijinExIncomeModeGson() {
    }

    public JijinExIncomeModeGson(JijinExIncomeModeDTO jijinExIncomeModeDTO) {
        this.fundCode = jijinExIncomeModeDTO.getFundCode();
        this.incomeMode = jijinExIncomeModeDTO.getIncomeMode();
        this.minHoldShareCount = jijinExIncomeModeDTO.getMinHoldShareCount();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getIncomeMode() {
        return incomeMode;
    }

    public void setIncomeMode(String incomeMode) {
        this.incomeMode = incomeMode;
    }

    public BigDecimal getMinHoldShareCount() {
        return minHoldShareCount;
    }

    public void setMinHoldShareCount(BigDecimal minHoldShareCount) {
        this.minHoldShareCount = minHoldShareCount;
    }
}
