package com.lufax.jijin.ylx.dto;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class BuyConfirmDetailDTO extends BaseConfirmDetailDTO {

	private String prodCode;
	private String buyType;
	private BigDecimal confirmFundShare;
	private BigDecimal amount;
	private String currency;
	private BigDecimal purchaseFee = BigDecimal.ZERO;
	private BigDecimal confirmUnitPrice = BigDecimal.ONE;

	public BigDecimal getConfirmUnitPrice() {
		return confirmUnitPrice;
	}

	public void setConfirmUnitPrice(BigDecimal confirmUnitPrice) {
		this.confirmUnitPrice = confirmUnitPrice;
	}

	public BigDecimal getPurchaseFee() {
		return purchaseFee;
	}

	public void setPurchaseFee(BigDecimal purchaseFee) {
		this.purchaseFee = purchaseFee;
	}

	public String getBuyType() {
		return buyType;
	}

	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}

	public BigDecimal getConfirmFundShare() {
		return confirmFundShare;
	}

	public void setConfirmFundShare(BigDecimal confirmFundShare) {
		this.confirmFundShare = confirmFundShare;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

}
