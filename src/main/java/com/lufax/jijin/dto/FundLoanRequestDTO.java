package com.lufax.jijin.dto;

import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.product.gson.ProductInputGson;
import com.lufax.jijin.sysFacade.gson.FundProductGson;

import java.math.BigDecimal;
import java.util.Date;

public class FundLoanRequestDTO extends BaseDTO {

    private String productCode;
    private String displayName;
    private BigDecimal minInvestAmount;
    private BigDecimal increaseInvestAmount;
    private String contractCode;
    private Long spvUserId;
    private Long loaneeUserId;
    private String publishInstitutions;
    private String depositInstitutions;
    private String bidAssetName;
    private String bidAssetType;
    private String thirdCompanyCode;
    private String repaymentMethod;
    private BigDecimal fullRedemRatioLimit;
    private BigDecimal partialRedemRatioLimit;
    private BigDecimal singleDayRedemAmountLimit;
    private Long foreignId;
    private String productCategory;
    private String sourceType;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    public FundLoanRequestDTO() {
    }

    public FundLoanRequestDTO(FundProductGson fundProductGson) {
        this.productCode = fundProductGson.getProductCode();
        this.displayName = fundProductGson.getDisplayName();
        this.minInvestAmount = fundProductGson.getMinInvestAmount();
        this.increaseInvestAmount = fundProductGson.getIncreaseInvestAmount();
        this.contractCode = fundProductGson.getContractCode();
        this.spvUserId = fundProductGson.getSpvUserId();
        this.loaneeUserId = fundProductGson.getLoaneeUserId();
        this.publishInstitutions = fundProductGson.getPublishInstitutions();
        this.depositInstitutions = fundProductGson.getDepositInstitutions();
        this.bidAssetName = fundProductGson.getBidAssetName();
        this.bidAssetType = fundProductGson.getBidAssetType();
        this.thirdCompanyCode = fundProductGson.getThirdCompanyCode();
        this.repaymentMethod = fundProductGson.getRepaymentMethod();
        this.fullRedemRatioLimit = fundProductGson.getFullRedemptionRatioLimit();
        this.partialRedemRatioLimit = fundProductGson.getPartialRedemptionRatioLimit();
        this.singleDayRedemAmountLimit = fundProductGson.getSingleDayRedemptionAmountLimit();
        this.foreignId = fundProductGson.getForeignId();
        this.productCategory = fundProductGson.getProductCategory();
        this.sourceType = fundProductGson.getSourceType();
    }
    
    public FundLoanRequestDTO(ProductInputGson fundProductGson) {
        this.productCode = fundProductGson.getProductCode();
        this.displayName = fundProductGson.getDisplayName();
        this.minInvestAmount = fundProductGson.getMinInvestAmount();
        this.increaseInvestAmount = fundProductGson.getIncreaseInvestAmount();
        this.spvUserId = fundProductGson.getSpvUserId();
        this.loaneeUserId = fundProductGson.getLoaneeUserId();
        this.thirdCompanyCode = fundProductGson.getThirdCompanyCode();
        this.repaymentMethod = fundProductGson.getRepaymentMethod();
        this.foreignId = fundProductGson.getForeignId();
        this.productCategory = fundProductGson.getProductCategory();
        ////分割线
        ;
        this.sourceType = this.bidAssetType = this.bidAssetName = this.depositInstitutions = this.publishInstitutions = this.contractCode = " ";
        this.fullRedemRatioLimit = BigDecimal.ZERO;
        this.partialRedemRatioLimit = BigDecimal.ZERO;
        this.singleDayRedemAmountLimit = BigDecimal.ZERO;
        this.sourceType = fundProductGson.getSourceType();
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public BigDecimal getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(BigDecimal minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public BigDecimal getIncreaseInvestAmount() {
        return increaseInvestAmount;
    }

    public void setIncreaseInvestAmount(BigDecimal increaseInvestAmount) {
        this.increaseInvestAmount = increaseInvestAmount;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Long getSpvUserId() {
        return spvUserId;
    }

    public void setSpvUserId(Long spvUserId) {
        this.spvUserId = spvUserId;
    }

    public Long getLoaneeUserId() {
        return loaneeUserId;
    }

    public void setLoaneeUserId(Long loaneeUserId) {
        this.loaneeUserId = loaneeUserId;
    }

    public String getPublishInstitutions() {
        return publishInstitutions;
    }

    public void setPublishInstitutions(String publishInstitutions) {
        this.publishInstitutions = publishInstitutions;
    }

    public String getDepositInstitutions() {
        return depositInstitutions;
    }

    public void setDepositInstitutions(String depositInstitutions) {
        this.depositInstitutions = depositInstitutions;
    }

    public String getBidAssetName() {
        return bidAssetName;
    }

    public void setBidAssetName(String bidAssetName) {
        this.bidAssetName = bidAssetName;
    }

    public String getBidAssetType() {
        return bidAssetType;
    }

    public void setBidAssetType(String bidAssetType) {
        this.bidAssetType = bidAssetType;
    }

    public String getThirdCompanyCode() {
        return thirdCompanyCode;
    }

    public void setThirdCompanyCode(String thirdCompanyCode) {
        this.thirdCompanyCode = thirdCompanyCode;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public BigDecimal getFullRedemRatioLimit() {
        return fullRedemRatioLimit;
    }

    public void setFullRedemRatioLimit(BigDecimal fullRedemRatioLimit) {
        this.fullRedemRatioLimit = fullRedemRatioLimit;
    }

    public BigDecimal getPartialRedemRatioLimit() {
        return partialRedemRatioLimit;
    }

    public void setPartialRedemRatioLimit(BigDecimal partialRedemRatioLimit) {
        this.partialRedemRatioLimit = partialRedemRatioLimit;
    }

    public BigDecimal getSingleDayRedemAmountLimit() {
        return singleDayRedemAmountLimit;
    }

    public void setSingleDayRedemAmountLimit(BigDecimal singleDayRedemAmountLimit) {
        this.singleDayRedemAmountLimit = singleDayRedemAmountLimit;
    }

    public Long getForeignId() {
        return foreignId;
    }

    public void setForeignId(Long foreignId) {
        this.foreignId = foreignId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
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

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
