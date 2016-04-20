package com.lufax.jijin.fundation.remote.gson.response;

import java.math.BigDecimal;

public class AccountrResponseGson {

	private String retCode;
	
	private BigDecimal balanceAmount;
	
	private BigDecimal frozenAmount;

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public BigDecimal getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(BigDecimal frozenAmount) {
		this.frozenAmount = frozenAmount;
	}



	
	
}
