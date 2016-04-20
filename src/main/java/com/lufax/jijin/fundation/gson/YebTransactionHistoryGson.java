package com.lufax.jijin.fundation.gson;

import java.math.BigDecimal;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;

public class YebTransactionHistoryGson {
    private long id;
    private String createdAt;
    private String transactionType;
    private BigDecimal transactionAmount = BigDecimal.ZERO;
    private String remark;
    private BigDecimal incomeAmount = BigDecimal.ZERO;            
    private BigDecimal expenseAmount = BigDecimal.ZERO;           
    private BigDecimal yebBalanceAmount = BigDecimal.ZERO;        

    public YebTransactionHistoryGson(JijinTradeLogDTO jijinTradeLogDTO) {
        this.id = jijinTradeLogDTO.getId();
        this.createdAt = DateUtils.formatDateTime(jijinTradeLogDTO.getCreatedAt());
        TradeRecordType type = jijinTradeLogDTO.getType();
        
        if(TradeRecordType.PURCHASE.equals(type)){
        	this.transactionAmount = jijinTradeLogDTO.getAmount();
        	this.expenseAmount = jijinTradeLogDTO.getAmount();
        	this.transactionType = "01";
        } else if(TradeRecordType.REDEEM.equals(type)){
        	this.transactionAmount = jijinTradeLogDTO.getReqShare();
        	this.transactionType = "06";
        } else if(TradeRecordType.CURRENCY_INCOME.equals(type)){
        	this.transactionAmount = jijinTradeLogDTO.getAmount();
        	this.incomeAmount = jijinTradeLogDTO.getAmount();
        	this.transactionType = "00";
        	
        }
        this.remark = jijinTradeLogDTO.getRemark();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	public BigDecimal getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(BigDecimal expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public BigDecimal getYebBalanceAmount() {
		return yebBalanceAmount;
	}

	public void setYebBalanceAmount(BigDecimal yebBalanceAmount) {
		this.yebBalanceAmount = yebBalanceAmount;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}