package com.lufax.jijin.fundation.remote.gson.request;

import java.math.BigDecimal;

public class BuyNotifyExtension {
	
	private String fundCode;
	private BigDecimal amount;
    private String cdCard;

    public BuyNotifyExtension(String fundCode, BigDecimal amount, String cdCard) {
        this.fundCode = fundCode;
        this.amount = amount;
        this.cdCard = cdCard;
    }

    public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

    public String getCdCard() {
        return cdCard;
    }

    public void setdCard(String cdCard) {
        this.cdCard = cdCard;
    }
}
