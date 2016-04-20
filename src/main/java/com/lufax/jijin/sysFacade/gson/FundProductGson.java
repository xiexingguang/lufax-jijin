package com.lufax.jijin.sysFacade.gson;


import java.math.BigDecimal;

public class FundProductGson {

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
    private BigDecimal fullRedemptionRatioLimit;
    private BigDecimal partialRedemptionRatioLimit;
    private BigDecimal singleDayRedemptionAmountLimit;
    private Long foreignId;
    private String productCategory;
    private String sourceType;

    public String getProductCode() {
        return productCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BigDecimal getMinInvestAmount() {
        return minInvestAmount;
    }

    public BigDecimal getIncreaseInvestAmount() {
        return increaseInvestAmount;
    }

    public String getContractCode() {
        return contractCode;
    }

    public Long getSpvUserId() {
        return spvUserId;
    }

    public Long getLoaneeUserId() {
        return loaneeUserId;
    }

    public String getPublishInstitutions() {
        return publishInstitutions;
    }

    public String getDepositInstitutions() {
        return depositInstitutions;
    }

    public String getBidAssetName() {
        return bidAssetName;
    }

    public String getBidAssetType() {
        return bidAssetType;
    }

    public String getThirdCompanyCode() {
        return thirdCompanyCode;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public BigDecimal getFullRedemptionRatioLimit() {
        return fullRedemptionRatioLimit;
    }

    public BigDecimal getPartialRedemptionRatioLimit() {
        return partialRedemptionRatioLimit;
    }

    public BigDecimal getSingleDayRedemptionAmountLimit() {
        return singleDayRedemptionAmountLimit;
    }

    public Long getForeignId() {
        return foreignId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getSourceType() {
        return sourceType;
    }
}
