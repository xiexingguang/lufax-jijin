package com.lufax.jijin.sysFacade.gson;

import com.lufax.jijin.product.constant.*;
import com.lufax.jijin.product.gson.ProductInputGson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.dto.FundLoanRequestDTO;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class ListProductGson {

    private String code;
    private String productType;
    private String productStatus;
    private String tradingMode;
    private String collectionMode;
    private Long loanRequestId;
    private Long sourceId;
    private BigDecimal price;
    private BigDecimal principal;
    private int numOfInstalments;
    private BigDecimal mgmtFeeRate;
    private Boolean feeDisplayFlag;
    private String productCategory;
    private String displayName;
    private BigDecimal investPeriod;
    private String investPeriodUnit;
    private BigDecimal minCollectAmount;
	private Long sellerUserId;
    @Deprecated
    private Date createdAt;
    @Deprecated
    private Date updatedAt;
    private String trxEndAt;      //募集结束时间
    private BigDecimal minInvestAmount; //最低投资金额
    private BigDecimal increaseInvestAmount; //递增金额
    /*private BigDecimal appliedAmount;//":50000, //认购规模*/
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
    private String sourceType;

    public ListProductGson(){}


    public String getCode() {
        return code;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }


    public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getTradingMode() {
		return tradingMode;
	}

	public void setTradingMode(String tradingMode) {
		this.tradingMode = tradingMode;
	}

	public String getCollectionMode() {
		return collectionMode;
	}

	public void setCollectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
	}

	public Long getLoanRequestId() {
		return loanRequestId;
	}

	public void setLoanRequestId(Long loanRequestId) {
		this.loanRequestId = loanRequestId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public int getNumOfInstalments() {
		return numOfInstalments;
	}

	public void setNumOfInstalments(int numOfInstalments) {
		this.numOfInstalments = numOfInstalments;
	}

	public BigDecimal getMgmtFeeRate() {
		return mgmtFeeRate;
	}

	public void setMgmtFeeRate(BigDecimal mgmtFeeRate) {
		this.mgmtFeeRate = mgmtFeeRate;
	}

	public Boolean getFeeDisplayFlag() {
		return feeDisplayFlag;
	}

	public void setFeeDisplayFlag(Boolean feeDisplayFlag) {
		this.feeDisplayFlag = feeDisplayFlag;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public BigDecimal getInvestPeriod() {
		return investPeriod;
	}

	public void setInvestPeriod(BigDecimal investPeriod) {
		this.investPeriod = investPeriod;
	}

	public String getInvestPeriodUnit() {
		return investPeriodUnit;
	}

	public void setInvestPeriodUnit(String investPeriodUnit) {
		this.investPeriodUnit = investPeriodUnit;
	}

	public BigDecimal getMinCollectAmount() {
		return minCollectAmount;
	}

	public void setMinCollectAmount(BigDecimal minCollectAmount) {
		this.minCollectAmount = minCollectAmount;
	}

	public Long getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(Long sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getTrxEndAt() {
		return trxEndAt;
	}

	public void setTrxEndAt(String trxEndAt) {
		this.trxEndAt = trxEndAt;
	}

	public BigDecimal getMinInvestAmount() {
		return minInvestAmount;
	}

	public void setMinInvestAmount(BigDecimal minInvestAmount) {
		this.minInvestAmount = minInvestAmount;
	}

	public BigDecimal getMaxInvestAmount() {
		return maxInvestAmount;
	}

	public void setMaxInvestAmount(BigDecimal maxInvestAmount) {
		this.maxInvestAmount = maxInvestAmount;
	}

	public BigDecimal getIncreaseInvestAmount() {
		return increaseInvestAmount;
	}

	public void setIncreaseInvestAmount(BigDecimal increaseInvestAmount) {
		this.increaseInvestAmount = increaseInvestAmount;
	}



	public void setCode(String code) {
		this.code = code;
	}

	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}



	
	public ListProductGson(ProductInputGson fundProductGson, FundLoanRequestDTO fundLoanRequestDTO) {
        this.code = fundProductGson.getProductCode();
        this.displayName = fundProductGson.getDisplayName();
        this.minInvestAmount = fundProductGson.getMinInvestAmount();
        this.increaseInvestAmount = fundProductGson.getIncreaseInvestAmount();
        this.sellerUserId = fundProductGson.getLoaneeUserId();
        this.productCategory = fundProductGson.getProductCategory();
        this.productStatus = "UNPLANNED";
        this.collectionMode =fundProductGson.getRepaymentMethod();
        this.productType = "LOAN_REQUEST";
        this.tradingMode = "07";
        this.sourceId = fundLoanRequestDTO.getId();
        this.price = fundProductGson.getAppliedAmount();//":50000, //认购规模
        this.principal = fundProductGson.getAppliedAmount();
        this.interestRate = fundProductGson.getInterestRate();//":0.1, //预期收益率
        this.riskLevel = fundProductGson.getRiskLevel();//":"1", //风险等级
        this.maxInvestAmount = fundProductGson.getMaxInvestAmount();//":1500, //单笔认购规模
        this.assetSource = fundProductGson.getAssetSource();//":"4", //资产来源
        this.isFirstPublish = fundProductGson.getIsFirstPublish();//":1, //是否首发 1表示首发
        this.collectStartDate = fundProductGson.getCollectStartDate();//":"2015-05-21 00:00:00", //认购开始日
        this.collectEndDate = fundProductGson.getCollectEndDate();//":"2015-05-22 16:40:00", //认购结束日
        this.establishedDate = fundProductGson.getEstablishedDate();//":"2015-05-24 00:00:00", //成立日
        this.closedPeriodStartDate = fundProductGson.getClosedPeriodStartDate();//":"2015-04-29 00:00:00", //封闭期起始日
        this.closedPeriodEndDate = fundProductGson.getClosedPeriodEndDate();//":"2015-04-29 00:00:00", //封闭期结束日
        this.openingDate = fundProductGson.getOpeningDate();//":"2015-05-31 00:00:00", //开放日
        this.subType = fundProductGson.getSubType();//":"STOCK", //基金类型 “STOCK” 表示股票型
        this.sourceType = fundProductGson.getSourceType();
    }
}
