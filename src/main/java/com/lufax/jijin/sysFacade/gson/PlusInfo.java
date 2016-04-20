package com.lufax.jijin.sysFacade.gson;

import java.math.BigDecimal;

public class PlusInfo {
	
	private BigDecimal plusAmount;//调增金额
	private String transactionType;
	private String remark;
	private String plusBankRefId;//调增凭证（与银行交互的流水号或订单号）

	public BigDecimal getPlusAmount() {
		return plusAmount;
	}
	public void setPlusAmount(BigDecimal plusAmount) {
		this.plusAmount = plusAmount;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPlusBankRefId() {
		return plusBankRefId;
	}
	public void setPlusBankRefId(String plusBankRefId) {
		this.plusBankRefId = plusBankRefId;
	}
	
	
}
