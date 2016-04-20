package com.lufax.jijin.sysFacade.gson;

import java.math.BigDecimal;

public class SubtractInfo{
	
	public SubtractInfo(BigDecimal amount, String transactionType,
			String bankRefId, String remark) {
		this.amount = amount;
		this.transactionType = transactionType;
		this.bankRefId = bankRefId;
		this.remark = remark;
	}
	private BigDecimal amount;//调减金额	 
	private String transactionType;//调减交易类型	 
	private String bankRefId;	//调增凭证（与银行交互的流水号或订单号）	 
	private String remark;	//调减备注

}