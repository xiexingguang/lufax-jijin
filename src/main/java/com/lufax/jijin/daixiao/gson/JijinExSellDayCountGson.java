package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.daixiao.dto.JijinExSellDayCountDTO;


public class JijinExSellDayCountGson{
    private String fundCode; //基金代码
    private Long sellConfirmDayCount;//赎回到帐时间

    public JijinExSellDayCountGson() {
    }

    public JijinExSellDayCountGson(JijinExSellDayCountDTO jijinExSellDayCountDTO) {
        this.fundCode = jijinExSellDayCountDTO.getFundCode();
        this.sellConfirmDayCount = jijinExSellDayCountDTO.getSellConfirmDayCount();
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public Long getSellConfirmDayCount() {
        return sellConfirmDayCount;
    }

    public void setSellConfirmDayCount(Long sellConfirmDayCount) {
        this.sellConfirmDayCount = sellConfirmDayCount;
    }
}
