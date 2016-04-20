package com.lufax.jijin.ylx.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.ylx.util.YlxConstants;

public class YlxFundInvestorProfitDTO {
	
	private Long id;
    private String prodCode;
    private String profitDay;
    private BigDecimal totalAmount;
    private String investorName;
    private String investorFundAccount;
    private String investorTradeAccount;
    private BigDecimal totalShare;
    private BigDecimal availableShare;
    private BigDecimal frozenShare;
    private BigDecimal yestodayIncome;
    private BigDecimal totalIncome;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    
    public YlxFundInvestorProfitDTO() {}
    
	public YlxFundInvestorProfitDTO(String line) {
		String[] arr = line.split(YlxConstants.SPRT);
		prodCode = arr[0];
		profitDay = arr[1];
		investorName = arr[2];
		investorFundAccount = arr[3];
		investorTradeAccount = arr[4];
		totalAmount = new BigDecimal(arr[5]);
		totalShare = new BigDecimal(arr[6]);
		availableShare = new BigDecimal(arr[7]);
		frozenShare = new BigDecimal(arr[8]);
		try{
			yestodayIncome = new BigDecimal(arr[9]);
		}catch(Exception e){
			yestodayIncome = BigDecimal.ZERO;
		}
		try{
			totalIncome = new BigDecimal(arr[10]);
		}catch(Exception e){
			totalIncome = BigDecimal.ZERO;
		}
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getProfitDay() {
		return profitDay;
	}
	public void setProfitDay(String profitDay) {
		this.profitDay = profitDay;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getInvestorName() {
		return investorName;
	}
	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}
	public String getInvestorFundAccount() {
		return investorFundAccount;
	}
	public void setInvestorFundAccount(String investorFundAccount) {
		this.investorFundAccount = investorFundAccount;
	}
	public String getInvestorTradeAccount() {
		return investorTradeAccount;
	}
	public void setInvestorTradeAccount(String investorTradeAccount) {
		this.investorTradeAccount = investorTradeAccount;
	}
	public BigDecimal getTotalShare() {
		return totalShare;
	}
	public void setTotalShare(BigDecimal totalShare) {
		this.totalShare = totalShare;
	}
	public BigDecimal getAvailableShare() {
		return availableShare;
	}
	public void setAvailableShare(BigDecimal availableShare) {
		this.availableShare = availableShare;
	}
	public BigDecimal getFrozenShare() {
		return frozenShare;
	}
	public void setFrozenShare(BigDecimal frozenShare) {
		this.frozenShare = frozenShare;
	}
	public BigDecimal getYestodayIncome() {
		return yestodayIncome;
	}
	public void setYestodayIncome(BigDecimal yestodayIncome) {
		this.yestodayIncome = yestodayIncome;
	}
	public BigDecimal getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
    
}
