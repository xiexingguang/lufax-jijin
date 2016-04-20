package com.lufax.jijin.base.dto;

import java.util.Date;

public class TradeDaysDTO extends BaseDTO{

    private Date tradeDate;
    private String isTradeDay;
    private Date createdAt;

    public TradeDaysDTO() {
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getIsTradeDay() {
        return isTradeDay;
    }

    public void setIsTradeDay(String isTradeDay) {
        this.isTradeDay = isTradeDay;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
