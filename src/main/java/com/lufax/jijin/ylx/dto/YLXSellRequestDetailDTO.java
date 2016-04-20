package com.lufax.jijin.ylx.dto;

import com.lufax.jijin.base.dto.YLXBaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class YLXSellRequestDetailDTO extends YLXBaseDTO{

	private static final long serialVersionUID = 8670805041276199237L;
	
	private long batchId;
	private long internalTrxId; //investments.id
	private Date trxTime;
	private Date trxDate;
	private String bankAccount;
	private String thirdCustomerAccount;
	private String thirdAccount;
	private String thirdAccountType;
	private String prodCode;
	private String sellType;
	private BigDecimal fundShare;
	private BigDecimal principal;
	private BigDecimal interest;
	private String currency;
	private String internalTrxIds;//since we will do merge , so use String to present id, it may be '1234,1334,23243' for merge case
	
	private String internalSellType;
    private String productCategory;
    private String productCode;
    private long productId;
    private long userId;
    private String status;
    private String memo;
    private String recordId;
    private BigDecimal commissionFee;
    private BigDecimal confirmUnitPrice;
    private BigDecimal confirmFundShare;
	public BigDecimal getConfirmFundShare() {
		return confirmFundShare;
	}
	public void setConfirmFundShare(BigDecimal confirmFundShare) {
		this.confirmFundShare = confirmFundShare;
	}
	public BigDecimal getConfirmUnitPrice() {
		return confirmUnitPrice;
	}
	public void setConfirmUnitPrice(BigDecimal confirmUnitPrice) {
		this.confirmUnitPrice = confirmUnitPrice;
	}
	public String getInternalTrxIds() {
		return internalTrxIds;
	}
	public void setInternalTrxIds(String internalTrxIds) {
		this.internalTrxIds = internalTrxIds;
	}
	public BigDecimal getPrincipal() {
		return principal;
	}
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
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
	public String getThirdCustomerAccount() {
		return thirdCustomerAccount;
	}
	public void setThirdCustomerAccount(String thirdCustomerAccount) {
		this.thirdCustomerAccount = thirdCustomerAccount;
	}
	public String getThirdAccount() {
		return thirdAccount;
	}
	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}
	public String getThirdAccountType() {
		return thirdAccountType;
	}
	public void setThirdAccountType(String thirdAccountType) {
		this.thirdAccountType = thirdAccountType;
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
	public BigDecimal getFundShare() {
		return fundShare;
	}
	public void setFundShare(BigDecimal fundShare) {
		this.fundShare = fundShare;
	}

	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getInternalSellType() {
		return internalSellType;
	}
	public void setInternalSellType(String internalSellType) {
		this.internalSellType = internalSellType;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public BigDecimal getCommissionFee() {
        return commissionFee;
    }

    public void setCommissionFee(BigDecimal commissionFee) {
        this.commissionFee = commissionFee;
    }
}
