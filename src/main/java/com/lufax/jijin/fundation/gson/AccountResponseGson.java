package com.lufax.jijin.fundation.gson;

import java.math.BigDecimal;

public class AccountResponseGson extends BaseResponseGson {
	
	//bus_jijin_user_balance.id
	private String yebAccountId;    
	//bus_jijin_user_balance.share_balance
    private BigDecimal balanceAmount;
	//bus_jijin_user_balance.frozen_share
    private BigDecimal frozenAmount;
	//select sum(t.daily_income) from bus_jijin_user_bal_audit t where t.user_id = '' and t.fund_code = '' and t.status = 'DISPATCHED';
    private BigDecimal allIncomeAmount;
    // @TODO
    private BigDecimal incomeAmount;
    //bus_jijin_user_balance.share_balance
    private BigDecimal availableAmount;
	
	public AccountResponseGson(String retMessage, String retCode) {
		super(retMessage, retCode);
	}

	public AccountResponseGson(String yebAccountId, BigDecimal frozenAmount, 
	        BigDecimal allIncomeAmount, BigDecimal availableAmount) {
		this.yebAccountId = yebAccountId;
		this.frozenAmount = null==frozenAmount ? BigDecimal.ZERO : frozenAmount;
		this.allIncomeAmount = allIncomeAmount;
		this.incomeAmount = allIncomeAmount;
		this.availableAmount = null==availableAmount ? BigDecimal.ZERO : availableAmount;
		this.balanceAmount = availableAmount.add(frozenAmount);
	}
	
	public String getYebAccountId() {
		return yebAccountId;
	}
    
	public void setYebAccountId(String yebAccountId) {
		this.yebAccountId = yebAccountId;
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
	
	public BigDecimal getAllIncomeAmount() {
		return allIncomeAmount;
	}
	
	public void setAllIncomeAmount(BigDecimal allIncomeAmount) {
		this.allIncomeAmount = allIncomeAmount;
	}
	
	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}
	
	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}
	
	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}
	
	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}
}
