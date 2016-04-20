package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.fundation.constant.TradeRecordType;

import java.math.BigDecimal;
import java.util.Date;

public class JijinTradeRecordCountDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 5954538895875999018L;

    private int total;
	private BigDecimal amount;
    
    public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
    
}
