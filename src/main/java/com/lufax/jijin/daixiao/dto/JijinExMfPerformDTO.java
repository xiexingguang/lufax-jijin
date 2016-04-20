package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
public class JijinExMfPerformDTO extends BaseDTO {
    private Date createdAt;  //创建时间
    private String createdBy;  //创建人
    private Date updatedAt;  //修改时间
    private String updatedBy;  //修改人
    private String fundCode;  //基金代码
    private String performanceDay;  //日期
    private BigDecimal benefitDaily;  //收益率(当天)
    private BigDecimal benefitOneWeek;  //收益率(一周)
    private BigDecimal benefitOneMonth;  //收益率(一个月)
    private BigDecimal benefitThreeMonth;  //收益率(三个月)
    private BigDecimal benefitSixMonth;  //收益率(六个月)
    private BigDecimal benefitOneYear;  //收益率(一年)
    private BigDecimal benefitTwoYear;  //收益率(两年)
    private BigDecimal benefitThreeYear;  //收益率(三年)
    private BigDecimal benefitThisYear;  //收益率(本年以来)
    private BigDecimal benefitTotal;  //收益率(成立以来)
    private BigDecimal kinBenefitOneWeek;  //最近一周同类基金收益率
    private BigDecimal kinBenefitOneMonth;  //最近一月同类基金收益率
    private BigDecimal kinBenefitThreeMonth;  //最近三月同类基金收益率
    private BigDecimal kinBenefitSixMonth;  //最近六月同类基金收益率
    private BigDecimal kinBenefitOneYear;  //最近一年同类基金收益率
    private BigDecimal kinBenefitTwoYear;  //最近两年同类基金收益率
    private BigDecimal kinBenefitThreeYear;  //最近三年同类基金收益率
    private BigDecimal kinBenefitThisYear;  //今年以来同类基金收益率
    private BigDecimal kinBenefitTotal;  //成立以来同类基金收益率
    private String orderOneWeek;  //最近一周同类排名
    private String orderOneMonth;  //最近一月同类排名
    private String orderThreeMonth;  //最近三月同类排名
    private String orderSixMonth;  //最近六月同类排名
    private String orderOneYear;  //最近一年同类排名
    private String orderTwoYear;  //最近两年同类排名
    private String orderThreeYear;  //最近三年同类排名
    private String orderThisYear;  //今年以来同类排名
    private String orderTotal;  //成立以来同类排名
    private Long batchId;  //批次号(文件ID)
    private String status;  //状态NEW/DISPATCHED
    private Integer isValid;//是否生效
    
    
    


	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getPerformanceDay() {
        return performanceDay;
    }

    public void setPerformanceDay(String performanceDay) {
        this.performanceDay = performanceDay;
    }


    public BigDecimal getBenefitDaily() {
		return benefitDaily;
	}

	public void setBenefitDaily(BigDecimal benefitDaily) {
		this.benefitDaily = benefitDaily;
	}

	public BigDecimal getBenefitOneWeek() {
		return benefitOneWeek;
	}

	public void setBenefitOneWeek(BigDecimal benefitOneWeek) {
		this.benefitOneWeek = benefitOneWeek;
	}

	public BigDecimal getBenefitOneMonth() {
        return benefitOneMonth;
    }

    public void setBenefitOneMonth(BigDecimal benefitOneMonth) {
        this.benefitOneMonth = benefitOneMonth;
    }

    public BigDecimal getBenefitThreeMonth() {
        return benefitThreeMonth;
    }

    public void setBenefitThreeMonth(BigDecimal benefitThreeMonth) {
        this.benefitThreeMonth = benefitThreeMonth;
    }

    public BigDecimal getBenefitSixMonth() {
        return benefitSixMonth;
    }

    public void setBenefitSixMonth(BigDecimal benefitSixMonth) {
        this.benefitSixMonth = benefitSixMonth;
    }

    public BigDecimal getBenefitOneYear() {
        return benefitOneYear;
    }

    public void setBenefitOneYear(BigDecimal benefitOneYear) {
        this.benefitOneYear = benefitOneYear;
    }

    public BigDecimal getBenefitTwoYear() {
        return benefitTwoYear;
    }

    public void setBenefitTwoYear(BigDecimal benefitTwoYear) {
        this.benefitTwoYear = benefitTwoYear;
    }

    public BigDecimal getBenefitThreeYear() {
        return benefitThreeYear;
    }

    public void setBenefitThreeYear(BigDecimal benefitThreeYear) {
        this.benefitThreeYear = benefitThreeYear;
    }

    public BigDecimal getBenefitThisYear() {
        return benefitThisYear;
    }

    public void setBenefitThisYear(BigDecimal benefitThisYear) {
        this.benefitThisYear = benefitThisYear;
    }

    public BigDecimal getBenefitTotal() {
        return benefitTotal;
    }

    public void setBenefitTotal(BigDecimal benefitTotal) {
        this.benefitTotal = benefitTotal;
    }

    public BigDecimal getKinBenefitOneWeek() {
        return kinBenefitOneWeek;
    }

    public void setKinBenefitOneWeek(BigDecimal kinBenefitOneWeek) {
        this.kinBenefitOneWeek = kinBenefitOneWeek;
    }

    public BigDecimal getKinBenefitOneMonth() {
        return kinBenefitOneMonth;
    }

    public void setKinBenefitOneMonth(BigDecimal kinBenefitOneMonth) {
        this.kinBenefitOneMonth = kinBenefitOneMonth;
    }

    public BigDecimal getKinBenefitThreeMonth() {
        return kinBenefitThreeMonth;
    }

    public void setKinBenefitThreeMonth(BigDecimal kinBenefitThreeMonth) {
        this.kinBenefitThreeMonth = kinBenefitThreeMonth;
    }

    public BigDecimal getKinBenefitSixMonth() {
        return kinBenefitSixMonth;
    }

    public void setKinBenefitSixMonth(BigDecimal kinBenefitSixMonth) {
        this.kinBenefitSixMonth = kinBenefitSixMonth;
    }

    public BigDecimal getKinBenefitOneYear() {
        return kinBenefitOneYear;
    }

    public void setKinBenefitOneYear(BigDecimal kinBenefitOneYear) {
        this.kinBenefitOneYear = kinBenefitOneYear;
    }

    public BigDecimal getKinBenefitTwoYear() {
        return kinBenefitTwoYear;
    }

    public void setKinBenefitTwoYear(BigDecimal kinBenefitTwoYear) {
        this.kinBenefitTwoYear = kinBenefitTwoYear;
    }

    public BigDecimal getKinBenefitThreeYear() {
        return kinBenefitThreeYear;
    }

    public void setKinBenefitThreeYear(BigDecimal kinBenefitThreeYear) {
        this.kinBenefitThreeYear = kinBenefitThreeYear;
    }

    public BigDecimal getKinBenefitThisYear() {
        return kinBenefitThisYear;
    }

    public void setKinBenefitThisYear(BigDecimal kinBenefitThisYear) {
        this.kinBenefitThisYear = kinBenefitThisYear;
    }

    public BigDecimal getKinBenefitTotal() {
        return kinBenefitTotal;
    }

    public void setKinBenefitTotal(BigDecimal kinBenefitTotal) {
        this.kinBenefitTotal = kinBenefitTotal;
    }

    public String getOrderOneWeek() {
        return orderOneWeek;
    }

    public void setOrderOneWeek(String orderOneWeek) {
        this.orderOneWeek = orderOneWeek;
    }

    public String getOrderOneMonth() {
        return orderOneMonth;
    }

    public void setOrderOneMonth(String orderOneMonth) {
        this.orderOneMonth = orderOneMonth;
    }

    public String getOrderThreeMonth() {
        return orderThreeMonth;
    }

    public void setOrderThreeMonth(String orderThreeMonth) {
        this.orderThreeMonth = orderThreeMonth;
    }

    public String getOrderSixMonth() {
        return orderSixMonth;
    }

    public void setOrderSixMonth(String orderSixMonth) {
        this.orderSixMonth = orderSixMonth;
    }

    public String getOrderOneYear() {
        return orderOneYear;
    }

    public void setOrderOneYear(String orderOneYear) {
        this.orderOneYear = orderOneYear;
    }

    public String getOrderTwoYear() {
        return orderTwoYear;
    }

    public void setOrderTwoYear(String orderTwoYear) {
        this.orderTwoYear = orderTwoYear;
    }

    public String getOrderThreeYear() {
        return orderThreeYear;
    }

    public void setOrderThreeYear(String orderThreeYear) {
        this.orderThreeYear = orderThreeYear;
    }

    public String getOrderThisYear() {
        return orderThisYear;
    }

    public void setOrderThisYear(String orderThisYear) {
        this.orderThisYear = orderThisYear;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
