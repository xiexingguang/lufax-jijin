package com.lufax.jijin.ylx.dto;

import java.math.BigDecimal;

public class YLXSellConfirmDetailDTO extends BaseConfirmDetailDTO {
	private String prodCode;
	private String sellType;
	private BigDecimal amount;
	private BigDecimal confirmFundShare;
    private BigDecimal commissionFee;
	private String currency;
	private BigDecimal confirmUnitPrice;
	public BigDecimal getConfirmUnitPrice() {
		return confirmUnitPrice;
	}

	public void setConfirmUnitPrice(BigDecimal confirmUnitPrice) {
		this.confirmUnitPrice = confirmUnitPrice;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getSellType() {
		return sellType;
	}

	public void setSellType(String sellType) {
		this.sellType = sellType;
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

    public BigDecimal getCommissionFee() {
        return commissionFee;
    }

    public void setCommissionFee(BigDecimal commissionFee) {
        this.commissionFee = commissionFee;
    }
}
