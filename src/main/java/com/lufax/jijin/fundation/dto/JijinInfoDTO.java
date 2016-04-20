package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * 基金信息
 * @author chenqunhui
 *
 */
public class JijinInfoDTO extends BaseDTO {

	private static final long serialVersionUID = -4342733499334392513L;
    /* 基金产品代码 */
    private String fundCode;
    /* 陆金所自定义（机构标识）,基金公司 */
    private String instId;
    /* 基金品牌 */
    private String fundBrand;
    /* 基金名称 */
    private String fundName;
    /* 是否首发：1－是　0－否 */
    private Integer  isFirstPublish;
    /* 	基金类型：股票型(stock),债券型（bond）,混合型（mix）,指数型（exponent），qdii（qdii）,货币型（currency）,其他（other） */
    private String fundType;
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
	/*最大购买金额  仅生成产品时使用*/
	private BigDecimal maxInvestAmount;
	/*投资递增金额  仅生成产品时使用*/
	private BigDecimal increaseInvestAmount;
    /* 赎回费率 */
    private String redemptionFeeRateDesc;
    /* 收费方式：A－前端收费　B－后端收费 */
    private String chargeType="A";//默认为前端收费
    /* 赎回到账时间：ｘ个工作日 */
    private Integer redemptionArrivalDay;
    /* 运作期限：1:永久开放　2:定期开放 */
    private String fundOpeningType;
    /* 成立日期：ｘ年ｘ月ｘ日 */
    private Date establishedDate;
    
	/* 默认分红方式　0：红利转投　1：现金分红 */
    private String dividendType;
    /* 托管人 */
    private String trustee;
    
    private String foreignId;
    
    private String productCategory;
    
    private String sourceType;
    
    private String productCode;
    
    private Long productId;   //由List app生成后回写
    
    private String appliedAmount;
    
    private String collectionMode;

	/**
     * 申购状态
     */
    private String buyStatus;
    
    /**
     * 赎回状态
     */
    private String redemptionStatus;
    
    private String fundIntroduce;  //基金介绍
    private String fundManager;    //基金经理
    private String fundManagerIntroduce; //基金经理介绍
    /* 万份收益 */
    private BigDecimal benefitPerTenthousand;
    /* 七日年化收益率 */
    private BigDecimal interestratePerSevenday;
    //年化收益日期
    private String profitEndDate;
    
	/* default sysdate */
    private Date createdAt;
    /* default sysdate */
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;

    private Integer isShow; //是否显示在前台 0:不显示 1:显示


    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getFundBrand() {
        return fundBrand;
    }

    public void setFundBrand(String fundBrand) {
        this.fundBrand = fundBrand;
    }
    
    public Date getEstablishedDate() {
		return establishedDate;
	}

	public void setEstablishedDate(Date establishedDate) {
		this.establishedDate = establishedDate;
	}
   
    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }


    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }


	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
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

	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getAppliedAmount() {
		return appliedAmount;
	}

	public void setAppliedAmount(String appliedAmount) {
		this.appliedAmount = appliedAmount;
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

	public String getFundOpeningType() {
		return fundOpeningType;
	}

	public void setFundOpeningType(String fundOpeningType) {
		this.fundOpeningType = fundOpeningType;
	}

	public String getDividendType() {
		return dividendType;
	}

	public void setDividendType(String dividendType) {
		this.dividendType = dividendType;
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
    public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getCollectionMode() {
		return collectionMode;
	}

	public void setCollectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
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

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

	
	public BigDecimal getBenefitPerTenthousand() {
		return benefitPerTenthousand;
	}

	public void setBenefitPerTenthousand(BigDecimal benefitPerTenthousand) {
		this.benefitPerTenthousand = benefitPerTenthousand;
	}

	public BigDecimal getInterestratePerSevenday() {
		return interestratePerSevenday;
	}

	public void setInterestratePerSevenday(BigDecimal interestratePerSevenday) {
		this.interestratePerSevenday = interestratePerSevenday;
	}

	public String getProfitEndDate() {
		return profitEndDate;
	}

	public void setProfitEndDate(String profitEndDate) {
		this.profitEndDate = profitEndDate;
	}
	
}
