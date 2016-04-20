package com.lufax.jijin.ylx.dto;


import com.lufax.jijin.base.dto.YLXBaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class YLXBuyRequestDetailDTO extends YLXBaseDTO{
	
	private long batchId;
	private long internalTrxId;
	private Date trxTime; //yyyyMMddhhmmss
	private Date trxDate; //yyyyMMdd
	private String bankAccount;
	private String prodCode;
	private String buyType;
	private BigDecimal fundShare;
	private BigDecimal amount;
	private String currency;
    private String productCode;
    private String productCategory;
    private long productId;
    private long userId;
    private String internalBuyType;
    private String status;
    private BigDecimal purchaseFee;
	private BigDecimal confirmUnitPrice;
	public BigDecimal getPurchaseFee() {
		return purchaseFee;
	}
	public void setPurchaseFee(BigDecimal purchaseFee) {
		this.purchaseFee = purchaseFee;
	}
	public BigDecimal getConfirmUnitPrice() {
		return confirmUnitPrice;
	}
	public void setConfirmUnitPrice(BigDecimal confirmUnitPrice) {
		this.confirmUnitPrice = confirmUnitPrice;
	}
	public long getBatchId() {
		return batchId;
	}
	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}
	public long getInternalTrxId() {
		return internalTrxId;
	}
	public void setInternalTrxId(long internalTrxId) {
		this.internalTrxId = internalTrxId;
	}
	public Date getTrxTime() {
		return trxTime;
	}
	public void setTrxTime(Date trxTime) {
		this.trxTime = trxTime;
	}
	public Date getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(Date trxDate) {
		this.trxDate = trxDate;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getBuyType() {
		return buyType;
	}
	public void setBuyType(String buyType) {
		this.buyType = buyType;
	}
	public BigDecimal getFundShare() {
		return fundShare;
	}
	public void setFundShare(BigDecimal fundShare) {
		this.fundShare = fundShare;
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
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getInternalBuyType() {
        return internalBuyType;
    }

    public void setInternalBuyType(String internalBuyType) {
        this.internalBuyType = internalBuyType;
    }
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
