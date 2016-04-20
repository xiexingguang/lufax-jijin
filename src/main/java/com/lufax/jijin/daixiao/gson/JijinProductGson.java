package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.product.constant.ProductTradingMode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class JijinProductGson {


    private String fundCode;/* 基金产品代码 */
    private String fundBrand;/* 基金品牌 */
    private String instId;
    private String displayName;/* 基金名称 */
    private Integer isFirstPublish;/* 是否首发：1－是　0－否 */
    private String subType;/* 	基金类型：股票型(stock),债券型（bond）,混合型（mix）,指数型（exponent），qdii（qdii）,货币型（currency）,其他（other） */
    private String riskLevel;/* 风险等级：低（１）、中低（２）、中（３）、中高（４）、高（５ */
    private String isBuyDailyLimit;/* 日累计购买金额设限：有（１）、无（0） */
    private BigDecimal buyDailyLimit;/* (用户)日累计购买金额上限，daily_limit为１时此字段为必填，且应大于起购金额 */
    private String buyFeeRateDesc;/* 购买费率 */
    private String buyFeeDiscountDesc;/* 购买费率折扣 */
    private BigDecimal minInvestAmount;/* 起购金额 ,申购*/
    private BigDecimal minSubAmount;//最低认购金额
    private BigDecimal minFixAmount;//最低定投金额
    private BigDecimal maxInvestAmount;//单次最大投资金额
    private String redemptionFeeRateDesc;/* 赎回费率 */
    private String establishedDate;/* 成立日期：ｘ年ｘ月ｘ日 */
    private Integer redemptionArrivalDay;//赎回到账天数
    private String trustee;/* 托管人 */
    private String productCategory;
    private String sourceType;
    private String code;
    private String price;  //产品价格
    private String principal; //产品本金
    private String collectionMode;
    private Long sourceId;  //jijinInfo表的id
    private String dividendMethod;  //分红方式
    private String fundOpeningType;/* 运作期限：ALWAYS_OPEN, REGULAR_OPEN */
    private String buyStatus;//申购状态
    private String redemptionStatus;//赎回状态
    private String tradingMode;
    private String fundIntroduce;  //基金介绍
    private String fundManager;    //基金经理
    private String productCode; //产品编码、
    private BigDecimal fundScale;//基金规模
    private Integer haitongGrade; //海通平级
    private Integer shangzhengGrade;//上证平级
    private Integer yinheGrade; // 银河平级,
    private BigDecimal increaseInvestAmount; //递增金额
    private String fundManagerName;//管理人
    private String fundManagerCode;//管理人编号
    private String salesChannel; //销售渠道 0: PC_OR_MOBILE, 1:PC_ONLY, 2 :MOBILE_ONLY
    private String[] productUserGroup;//特定人组
    private String dayIncrease; //日增长率
    /**
     * 一周涨幅
     */
    private BigDecimal oneWeekIncrease;
    /*一月涨幅*/
    private BigDecimal oneMonIncrease;
    /*三月涨幅*/
    private BigDecimal threeMonIncrease;
    /*半年涨幅*/
    private BigDecimal sixMonIncrease;
    /*一年涨幅*/
    private BigDecimal twelveMonIncrease;
    /*今年以来涨幅*/
    private BigDecimal moreTwelveMonIncrease;
    /*成立以来涨幅*/
    private BigDecimal totalIncrease;
    /*涨幅数据更新时间*/
    private String increaseUpdateDate;
    /*单位净值*/
    private BigDecimal unitPrice2;
    /*净值日期*/
    private String unitPriceDate;

    private BigDecimal benefitPerTenThousand;//BENEFIT_PER_TEN_THOUSAND    NUMBER(19,6)  Y                万份收益                                                                                                                                                                         
    
    private BigDecimal interestRatePerSevenDay;//INTEREST_RATE_PER_SEVEN_DAY NUMBER(19,7)  Y                7日年化收益率 
    
    private String profitEndDate;//年化收益截止日期
    
    private JijinProductFeeGson productFee;  //费率与折扣信息


    public JijinProductGson(JijinInfoDTO dto) {
        this.isBuyDailyLimit = dto.getIsBuyDailyLimit();
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
        this.isFirstPublish = dto.getIsFirstPublish();
        this.minInvestAmount = dto.getMinInvestAmount();
        this.maxInvestAmount = dto.getMaxInvestAmount();
        this.increaseInvestAmount = dto.getIncreaseInvestAmount();
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

        if (dto.getFundType().indexOf("MIXED") != -1) {
            this.subType = "MIXED";
        } else if (dto.getFundType().indexOf("QDII") != -1) {
            this.subType = "QDII";
        } else {
            this.subType = dto.getFundType();
        }

        this.tradingMode = ProductTradingMode.COLLECTION.getValue();
        this.fundIntroduce = dto.getFundIntroduce();
        this.fundManager = dto.getFundManager();
        this.instId = dto.getInstId();
        this.redemptionArrivalDay = dto.getRedemptionArrivalDay();
        this.trustee = dto.getTrustee();
        this.dividendMethod = dto.getDividendType();
        if ("1".equals(dto.getFundOpeningType())) {
            this.fundOpeningType = "ALWAYS_OPEN";
        } else {
            this.fundOpeningType = "REGULAR_OPEN";
        }
    }

    public String getFundManagerName() {
        return fundManagerName;
    }

    public String getFundManagerCode() {
        return fundManagerCode;
    }

    public void setFundManagerCode(String fundManagerCode) {
        this.fundManagerCode = fundManagerCode;
    }

    public void setFundManagerName(String fundManagerName) {
        this.fundManagerName = fundManagerName;
    }

    public JijinProductFeeGson getProductFee() {
        return productFee;
    }

    public void setProductFee(JijinProductFeeGson productFee) {
        this.productFee = productFee;
    }

    public BigDecimal getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(BigDecimal maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public BigDecimal getFundScale() {
        return fundScale;
    }

    public void setFundScale(BigDecimal fundScale) {
        this.fundScale = fundScale;
    }

    public Integer getHaitongGrade() {
        return haitongGrade;
    }

    public void setHaitongGrade(Integer haitongGrade) {
        this.haitongGrade = haitongGrade;
    }

    public Integer getShangzhengGrade() {
        return shangzhengGrade;
    }

    public void setShangzhengGrade(Integer shangzhengGrade) {
        this.shangzhengGrade = shangzhengGrade;
    }

    public Integer getYinheGrade() {
        return yinheGrade;
    }

    public void setYinheGrade(Integer yinheGrade) {
        this.yinheGrade = yinheGrade;
    }

    public BigDecimal getIncreaseInvestAmount() {
        return increaseInvestAmount;
    }

    public void setIncreaseInvestAmount(BigDecimal increaseInvestAmount) {
        this.increaseInvestAmount = increaseInvestAmount;
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

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getIsFirstPublish() {
        return isFirstPublish;
    }

    public void setIsFirstPublish(Integer isFirstPublish) {
        this.isFirstPublish = isFirstPublish;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
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

    public String getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(String establishedDate) {
        this.establishedDate = establishedDate;
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

    public String getTradingMode() {
        return tradingMode;
    }

    public void setTradingMode(String tradingMode) {
        this.tradingMode = tradingMode;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String[] getProductUserGroup() {
        return productUserGroup;
    }

    public void setProductUserGroup(String[] productUserGroup) {
        this.productUserGroup = productUserGroup;
    }

    public String getDayIncrease() {
        return dayIncrease;
    }

    public void setDayIncrease(String dayIncrease) {
        this.dayIncrease = dayIncrease;
    }

    public BigDecimal getOneMonIncrease() {
        return oneMonIncrease;
    }

    public void setOneMonIncrease(BigDecimal oneMonIncrease) {
        this.oneMonIncrease = oneMonIncrease;
    }

    public BigDecimal getThreeMonIncrease() {
        return threeMonIncrease;
    }

    public void setThreeMonIncrease(BigDecimal threeMonIncrease) {
        this.threeMonIncrease = threeMonIncrease;
    }

    public BigDecimal getSixMonIncrease() {
        return sixMonIncrease;
    }

    public void setSixMonIncrease(BigDecimal sixMonIncrease) {
        this.sixMonIncrease = sixMonIncrease;
    }

    public BigDecimal getTwelveMonIncrease() {
        return twelveMonIncrease;
    }

    public void setTwelveMonIncrease(BigDecimal twelveMonIncrease) {
        this.twelveMonIncrease = twelveMonIncrease;
    }

    public BigDecimal getMoreTwelveMonIncrease() {
        return moreTwelveMonIncrease;
    }

    public void setMoreTwelveMonIncrease(BigDecimal moreTwelveMonIncrease) {
        this.moreTwelveMonIncrease = moreTwelveMonIncrease;
    }

    public BigDecimal getTotalIncrease() {
        return totalIncrease;
    }

    public void setTotalIncrease(BigDecimal totalIncrease) {
        this.totalIncrease = totalIncrease;
    }

    public String getIncreaseUpdateDate() {
        return increaseUpdateDate;
    }

    public void setIncreaseUpdateDate(String increaseUpdateDate) {
        this.increaseUpdateDate = increaseUpdateDate;
    }

    public BigDecimal getUnitPrice2() {
        return unitPrice2;
    }

    public void setUnitPrice2(BigDecimal unitPrice2) {
        this.unitPrice2 = unitPrice2;
    }

    public String getUnitPriceDate() {
        return unitPriceDate;
    }

    public void setUnitPriceDate(String unitPriceDate) {
        this.unitPriceDate = unitPriceDate;
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

	public String getProfitEndDate() {
		return profitEndDate;
	}

	public void setProfitEndDate(String profitEndDate) {
		this.profitEndDate = profitEndDate;
	}

	public BigDecimal getMinSubAmount() {
		return minSubAmount;
	}

	public void setMinSubAmount(BigDecimal minSubAmount) {
		this.minSubAmount = minSubAmount;
	}

	public BigDecimal getMinFixAmount() {
		return minFixAmount;
	}

	public void setMinFixAmount(BigDecimal minFixAmount) {
		this.minFixAmount = minFixAmount;
	}

	public BigDecimal getOneWeekIncrease() {
		return oneWeekIncrease;
	}

	public void setOneWeekIncrease(BigDecimal oneWeekIncrease) {
		this.oneWeekIncrease = oneWeekIncrease;
	}

    

}
