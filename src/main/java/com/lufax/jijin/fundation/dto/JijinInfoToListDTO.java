package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.product.constant.ProductTradingMode;

/**
 * 基金信息
 * @author chenqunhui
 *
 */
public class JijinInfoToListDTO extends BaseDTO {

	private static final long serialVersionUID = -4342733499334392513L;

    /* 基金产品代码 */
    private String fundCode;
    /* 基金品牌 */
    private String fundBrand;
    
    private String instId;
    /* 基金名称 */
    private String displayName;
    /* 是否首发：1－是　0－否 */
    private Integer  isFirstPublish;
    /* 	基金类型：股票型(stock),债券型（bond）,混合型（mix）,指数型（exponent），qdii（qdii）,货币型（currency）,其他（other） */
    private String subType;
    /* 风险等级：低（１）、中低（２）、中（３）、中高（４）、高（５ */
    private String riskLevel;
    /* 日累计购买金额设限：有（１）、无（0） */
    private String isBuyDailyLimit;
    /* 日累计购买金额上限，daily_limit为１时此字段为必填，且应大于起购金额 */
    private BigDecimal buyDailyLimit;
    /* 购买费率 */
    private String buyFeeRateDesc;
    /* 购买费率折扣 */
    private String buyFeeDiscountDesc;
    /* 起购金额 */
    private BigDecimal minInvestAmount;
    /* 赎回费率 */
    private String redemptionFeeRateDesc;

    /* 成立日期：ｘ年ｘ月ｘ日 */
    private String establishedDate;
    
    private Integer redemptionArrivalDay;

    /* 托管人 */
    private String trustee;
    
	private String productCategory;
    
    private String sourceType;
    
    private String code;
    
    private String price;  //产品价格
    
    private String principal; //产品本金
    
    private String collectionMode;
    
    private Long sourceId;  //jijinInfo表的id
    
    private String dividendMethod;  //分红方式
    
    /* 运作期限：ALWAYS_OPEN, REGULAR_OPEN */
    private String fundOpeningType;
    
    /**
     * 申购状态
     */
    private String buyStatus;
    
    /**
     * 赎回状态
     */
    private String redemptionStatus;
    
    
	private String tradingMode;
	
	private String fundIntroduce;  //基金介绍
    private String fundManager;    //基金经理
    private String fundManagerIntroduce; //基金经理介绍
    private BigDecimal benefitPerTenThousand;//万份收益
    private BigDecimal interestRatePerSevenDay;//7日年化收益率 
    private String profitEndDate;//年化收益截止日期
    
    public JijinInfoToListDTO(){
    	
    }
    
    public JijinInfoToListDTO(JijinInfoDTO dto){
    	this.buyDailyLimit = dto.getBuyDailyLimit();
    	this.buyFeeDiscountDesc = dto.getBuyFeeDiscountDesc();
    	this.buyFeeRateDesc = dto.getBuyFeeRateDesc();
    	this.collectionMode = dto.getCollectionMode(); 
    	this.displayName = dto.getFundName();
    	if(null != dto.getEstablishedDate()){
    		this.establishedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dto.getEstablishedDate());
    	}
    	this.fundBrand = dto.getFundBrand();
    	this.fundCode = dto.getFundCode();
    	this.isBuyDailyLimit = dto.getIsBuyDailyLimit();
    	this.isFirstPublish = dto.getIsFirstPublish();
    	this.minInvestAmount = dto.getMinInvestAmount();
    	this.price = dto.getAppliedAmount();
    	this.principal = dto.getAppliedAmount();
    	this.productCategory = dto.getProductCategory();
    	this.code = dto.getProductCode();
    	this.buyStatus = dto.getBuyStatus();
    	this.redemptionStatus = dto.getRedemptionStatus();
    	this.redemptionFeeRateDesc = dto.getRedemptionFeeRateDesc();
    	this.riskLevel = dto.getRiskLevel();
    	this.sourceId = dto.getId();
    	this.sourceType = dto.getSourceType();
    	this.subType = dto.getFundType();
    	this.tradingMode = ProductTradingMode.COLLECTION.getValue();
    	this.fundIntroduce = dto.getFundIntroduce();
    	this.fundManager = dto.getFundManager();
    	this.fundManagerIntroduce = dto.getFundManagerIntroduce();
    	this.instId = dto.getInstId();
    	this.redemptionArrivalDay = dto.getRedemptionArrivalDay();
    	this.trustee = dto.getTrustee();
    	this.dividendMethod = dto.getDividendType();
		if("1".equals(dto.getFundOpeningType())){
    		this.fundOpeningType ="ALWAYS_OPEN";
    	}else{
    		this.fundOpeningType ="REGULAR_OPEN";
    	}
		this.benefitPerTenThousand = dto.getBenefitPerTenthousand();
		this.interestRatePerSevenDay = dto.getInterestratePerSevenday();
		if(!EmptyChecker.isEmpty(dto.getProfitEndDate())){
			this.profitEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.parseDate(dto.getProfitEndDate()));
		}
		
    }
    
    
    public String getFundIntroduce() {
		return fundIntroduce;
	}

	public void setFundIntroduce(String fundIntroduce) {
		this.fundIntroduce = fundIntroduce;
	}

	public String getFundManager() {
		return fundManager;
	}

	public void setFundManager(String fundManager) {
		this.fundManager = fundManager;
	}

	public String getFundManagerIntroduce() {
		return fundManagerIntroduce;
	}

	public void setFundManagerIntroduce(String fundManagerIntroduce) {
		this.fundManagerIntroduce = fundManagerIntroduce;
	}

	public String getTradingMode() {
		return tradingMode;
	}

	public void setTradingMode(String tradingMode) {
		this.tradingMode = tradingMode;
	}

    public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Integer getRedemptionArrivalDay() {
		return redemptionArrivalDay;
	}

	public void setRedemptionArrivalDay(Integer redemptionArrivalDay) {
		this.redemptionArrivalDay = redemptionArrivalDay;
	}

	public String getTrustee() {
		return trustee;
	}

	public void setTrustee(String trustee) {
		this.trustee = trustee;
	}


	public String getBuyStatus() {
		return buyStatus;
	}

	public void setBuyStatus(String buyStatus) {
		this.buyStatus = buyStatus;
	}

	public String getRedemptionStatus() {
		return redemptionStatus;
	}

	public void setRedemptionStatus(String redemptionStatus) {
		this.redemptionStatus = redemptionStatus;
	}

	public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }



    public String getFundBrand() {
        return fundBrand;
    }

    public void setFundBrand(String fundBrand) {
        this.fundBrand = fundBrand;
    }
    
  

	public String getEstablishedDate() {
		return establishedDate;
	}

	public void setEstablishedDate(String establishedDate) {
		this.establishedDate = establishedDate;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getCollectionMode() {
		return collectionMode;
	}

	public void setCollectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getIsFirstPublish() {
		return isFirstPublish;
	}

	public void setIsFirstPublish(Integer isFirstPublish) {
		this.isFirstPublish = isFirstPublish;
	}



	public BigDecimal getBuyDailyLimit() {
		return buyDailyLimit;
	}

	public void setBuyDailyLimit(BigDecimal buyDailyLimit) {
		this.buyDailyLimit = buyDailyLimit;
	}

	public String getBuyFeeRateDesc() {
		return buyFeeRateDesc;
	}

	public void setBuyFeeRateDesc(String buyFeeRateDesc) {
		this.buyFeeRateDesc = buyFeeRateDesc;
	}

	public String getBuyFeeDiscountDesc() {
		return buyFeeDiscountDesc;
	}

	public void setBuyFeeDiscountDesc(String buyFeeDiscountDesc) {
		this.buyFeeDiscountDesc = buyFeeDiscountDesc;
	}

	public BigDecimal getMinInvestAmount() {
		return minInvestAmount;
	}

	public void setMinInvestAmount(BigDecimal minInvestAmount) {
		this.minInvestAmount = minInvestAmount;
	}

	public String getRedemptionFeeRateDesc() {
		return redemptionFeeRateDesc;
	}

	public void setRedemptionFeeRateDesc(String redemptionFeeRateDesc) {
		this.redemptionFeeRateDesc = redemptionFeeRateDesc;
	}


	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getIsBuyDailyLimit() {
		return isBuyDailyLimit;
	}

	public void setIsBuyDailyLimit(String isBuyDailyLimit) {
		this.isBuyDailyLimit = isBuyDailyLimit;
	}

	public String getDividendMethod() {
		return dividendMethod;
	}

	public void setDividendMethod(String dividendMethod) {
		this.dividendMethod = dividendMethod;
	}

	public String getFundOpeningType() {
		return fundOpeningType;
	}

	public void setFundOpeningType(String fundOpeningType) {
		this.fundOpeningType = fundOpeningType;
	}

	public BigDecimal getBenefitPerTenThousand() {
		return benefitPerTenThousand;
	}

	public void setBenefitPerTenThousand(BigDecimal benefitPerTenThousand) {
		this.benefitPerTenThousand = benefitPerTenThousand;
	}

	public BigDecimal getInterestRatePerSevenDay() {
		return interestRatePerSevenDay;
	}

	public void setInterestRatePerSevenDay(BigDecimal interestRatePerSevenDay) {
		this.interestRatePerSevenDay = interestRatePerSevenDay;
	}

	

	

}
