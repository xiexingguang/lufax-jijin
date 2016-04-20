package com.lufax.jijin.product.gson;


import java.math.BigDecimal;
import java.util.Date;

public class ProductInputGson {
	
    private String productCode;
    private String displayName; 
    private BigDecimal minInvestAmount;
    private BigDecimal increaseInvestAmount;
    private Long spvUserId;//认购申购帐号
    private Long loaneeUserId; //赎回帐号
    private String thirdCompanyCode;//第三方产品编码"PAYLXsssqq", 
    private String repaymentMethod;  //收益方式 "CALC_BY_MARKET_VALUE" 表示按市值计算  这里可能要给Fund一个
    private Long foreignId;
    private String productCategory;
    /********多出来的 */
    private BigDecimal appliedAmount;//":50000, //认购规模
    private BigDecimal interestRate;//":0.1, //预期收益率
    private String riskLevel;//":"1", //风险等级
	private BigDecimal maxInvestAmount;//":1500, //单笔认购规模
    private String assetSource;//":"4", //资产来源 
	private String isFirstPublish;//":1, //是否首发 1表示首发
	private String collectStartDate;//":"2015-05-21 00:00:00", //认购开始日
    private String collectEndDate;//":"2015-05-22 16:40:00", //认购结束日
    private String establishedDate;//":"2015-05-24 00:00:00", //成立日
    private String closedPeriodStartDate;//":"2015-04-29 00:00:00", //封闭期起始日
    private String closedPeriodEndDate;//":"2015-04-29 00:00:00", //封闭期结束日
    private String openingDate;//":"2015-05-31 00:00:00", //开放日
    private String subType;//":"STOCK", //基金类型 “STOCK” 表示股票型
    private Long sourceId;
    private String sourceType;
    //
    public Long getSourceId() {
		return sourceId;
	}
	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
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
	public BigDecimal getAppliedAmount() {
		return appliedAmount;
	}
	public void setAppliedAmount(BigDecimal appliedAmount) {
		this.appliedAmount = appliedAmount;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public BigDecimal getMaxInvestAmount() {
		return maxInvestAmount;
	}
	public void setMaxInvestAmount(BigDecimal maxInvestAmount) {
		this.maxInvestAmount = maxInvestAmount;
	}
	public String getAssetSource() {
		return assetSource;
	}
	public void setAssetSource(String assetSource) {
		this.assetSource = assetSource;
	}
	public String getIsFirstPublish() {
		return isFirstPublish;
	}
	
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public String getCollectStartDate() {
		return collectStartDate;
	}
	public void setCollectStartDate(String collectStartDate) {
		this.collectStartDate = collectStartDate;
	}
	public String getCollectEndDate() {
		return collectEndDate;
	}
	public void setCollectEndDate(String collectEndDate) {
		this.collectEndDate = collectEndDate;
	}
	public String getEstablishedDate() {
		return establishedDate;
	}
	public void setEstablishedDate(String establishedDate) {
		this.establishedDate = establishedDate;
	}
	public String getClosedPeriodStartDate() {
		return closedPeriodStartDate;
	}
	public void setClosedPeriodStartDate(String closedPeriodStartDate) {
		this.closedPeriodStartDate = closedPeriodStartDate;
	}
	public String getClosedPeriodEndDate() {
		return closedPeriodEndDate;
	}
	public void setClosedPeriodEndDate(String closedPeriodEndDate) {
		this.closedPeriodEndDate = closedPeriodEndDate;
	}
	public String getOpeningDate() {
		return openingDate;
	}
	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}
	public void setIsFirstPublish(String isFirstPublish) {
		this.isFirstPublish = isFirstPublish;
	}
    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
