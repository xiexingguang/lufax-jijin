package com.lufax.jijin.product.dto;



import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.product.constant.LoanPeriodUnit;
import com.lufax.jijin.product.constant.ProductSourceType;
import com.lufax.jijin.product.constant.ProductType;

import java.math.BigDecimal;
import java.util.Date;

public class ProductDTO extends BaseDTO {
    //    private Long id;
//    private Long loanRequestId;
    private String productType;
    //    private String productStatus;
//    private Date createdAt;
//    private Date updatedAt;
//    private Date expiredAt;
//    private Long buyerUserId;
//    private Long version;
    private BigDecimal price;
    //    private BigDecimal principal;
    private BigDecimal interestRate;
    //    private Long numOfInstalments;
    private Long sourceId;
    private Date publishedAt;
    //    private Date previewAt;
//    private Long previousProductId;
    private Long rootProductId;
    private String code;
    //    private String collectionMode;
//    private String productName;
//    private String tradingMode;
//    private BigDecimal mgmtFeeRate;
//    private Boolean feeDisplayFlag;
    private String sourceType;
    //    private BigDecimal buyerTransactionFee;
    private String productCategory;
//    private BigDecimal minCollectAmount;
    //    private BigDecimal maxInvestAmount;
    private BigDecimal minInvestAmount;
    private BigDecimal increaseInvestAmount;
    //    private BigDecimal remainingAmount;
    private BigDecimal raisedAmount;
    private BigDecimal investPeriod;
    private String investPeriodUnit;
    private String interestRateDisplay;
    private String displayName;
    private Date trxEndAt;
    private Date valueDate;
    private Boolean onlineStatusFlag;
    private Date openingDate;

    public ProductDTO() {
    }

    public BigDecimal getRaisedAmount() {
        return raisedAmount;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public BigDecimal getInvestPeriod() {
        return investPeriod;
    }

    public LoanPeriodUnit getInvestPeriodUnit() {
        return LoanPeriodUnit.convert(investPeriodUnit);
    }

    public String getInterestRateDisplay() {
        return interestRateDisplay;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Long getRootProductId() {
        return rootProductId;
    }

    public String getProductType() {
        return productType;
    }

    public boolean isPrimaryProduct() {
        return ProductType.LOAN_REQUEST.name().equalsIgnoreCase(this.productType);
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public Date getTrxEndAt() {
        return trxEndAt;
    }

    public BigDecimal getInterestRate() {
        return interestRate;

    }
    public BigDecimal getMinInvestAmount() {
        return minInvestAmount;
    }

    public BigDecimal getIncreaseInvestAmount() {
        return increaseInvestAmount;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRaisedAmount(BigDecimal raisedAmount) {
        this.raisedAmount = raisedAmount;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public void setMinInvestAmount(BigDecimal minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public void setIncreaseInvestAmount(BigDecimal increaseInvestAmount) {
        this.increaseInvestAmount = increaseInvestAmount;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setTrxEndAt(Date trxEndAt) {
        this.trxEndAt = trxEndAt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public Boolean getOnlineStatusFlag() {
        return onlineStatusFlag;
    }

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

}
