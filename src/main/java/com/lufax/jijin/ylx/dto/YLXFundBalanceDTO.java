package com.lufax.jijin.ylx.dto;

import com.lufax.jijin.base.dto.YLXBaseDTO;

import java.math.BigDecimal;
import java.util.Date;

public class YLXFundBalanceDTO extends YLXBaseDTO {
    private long id;
    private long userId;
    private String fundName;
    private long fundAccountId;
    private String productCategory;
    private String productCode;
    private long productId;
    private String thirdCustomerAccount;
    private String thirdAccount;
    private BigDecimal balance;
    private BigDecimal frozenAmount;
    private BigDecimal unitPrice;
    private BigDecimal fundShare;
    private BigDecimal frozenFundShare;
    private BigDecimal totalBuyAmount;
    private BigDecimal totalSellAmount;
    private BigDecimal yestodayUnitPrice;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getFundShare() {
        return fundShare;
    }

    public void setFundShare(BigDecimal fundShare) {
        this.fundShare = fundShare;
    }

    public BigDecimal getFrozenFundShare() {
        return frozenFundShare;
    }

    public void setFrozenFundShare(BigDecimal frozenFundShare) {
        this.frozenFundShare = frozenFundShare;
    }

    public BigDecimal getTotalBuyAmount() {
        return totalBuyAmount;
    }

    public void setTotalBuyAmount(BigDecimal totalBuyAmount) {
        this.totalBuyAmount = totalBuyAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getFundAccountId() {
        return fundAccountId;
    }

    public void setFundAccountId(long fundAccountId) {
        this.fundAccountId = fundAccountId;
    }

    public BigDecimal getTotalSellAmount() {
        return totalSellAmount;
    }

    public void setTotalSellAmount(BigDecimal totalSellAmount) {
        this.totalSellAmount = totalSellAmount;
    }

	public BigDecimal getYestodayUnitPrice() {
		return yestodayUnitPrice;
	}

	public void setYestodayUnitPrice(BigDecimal yestodayUnitPrice) {
		this.yestodayUnitPrice = yestodayUnitPrice;
	}
}
