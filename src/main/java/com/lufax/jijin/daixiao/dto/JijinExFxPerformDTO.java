package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
public class JijinExFxPerformDTO extends BaseDTO {
    private Date createdAt;  //创建时间
    private String createdBy;  //创建人
    private Date updatedAt;  //修改时间
    private String updatedBy;  //修改人
    private String findexCode;  //指数代码
    private String performanceDay;  //日期
    private BigDecimal riseRateOneWeek;  //最近1周涨跌幅
    private BigDecimal riseRateOneMonth;  //最近 1月涨跌幅
    private BigDecimal riseRateThreeMonth;  //最近 3月涨跌幅
    private BigDecimal riseRateSixMonth;  //最近 6月涨跌幅
    private BigDecimal riseRateOneYear;  //最近1年涨跌幅
    private BigDecimal riseRateTwoYear;  //最近2年涨跌幅
    private BigDecimal riseRateThreeYear;  //最近3年涨跌幅
    private BigDecimal riseRateThisYear;  //本年来涨跌幅
    private Long batchId;  //批次号(文件ID)
    private String status;  //状态NEW/DISPATCH
    private Integer isValid;//是否有效

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

    public String getFindexCode() {
        return findexCode;
    }

    public void setFindexCode(String findexCode) {
        this.findexCode = findexCode;
    }

    public String getPerformanceDay() {
        return performanceDay;
    }

    public void setPerformanceDay(String performanceDay) {
        this.performanceDay = performanceDay;
    }

    public BigDecimal getRiseRateOneWeek() {
        return riseRateOneWeek;
    }

    public void setRiseRateOneWeek(BigDecimal riseRateOneWeek) {
        this.riseRateOneWeek = riseRateOneWeek;
    }

    public BigDecimal getRiseRateOneMonth() {
        return riseRateOneMonth;
    }

    public void setRiseRateOneMonth(BigDecimal riseRateOneMonth) {
        this.riseRateOneMonth = riseRateOneMonth;
    }

    public BigDecimal getRiseRateThreeMonth() {
        return riseRateThreeMonth;
    }

    public void setRiseRateThreeMonth(BigDecimal riseRateThreeMonth) {
        this.riseRateThreeMonth = riseRateThreeMonth;
    }

    public BigDecimal getRiseRateSixMonth() {
        return riseRateSixMonth;
    }

    public void setRiseRateSixMonth(BigDecimal riseRateSixMonth) {
        this.riseRateSixMonth = riseRateSixMonth;
    }

    public BigDecimal getRiseRateOneYear() {
        return riseRateOneYear;
    }

    public void setRiseRateOneYear(BigDecimal riseRateOneYear) {
        this.riseRateOneYear = riseRateOneYear;
    }

    public BigDecimal getRiseRateTwoYear() {
        return riseRateTwoYear;
    }

    public void setRiseRateTwoYear(BigDecimal riseRateTwoYear) {
        this.riseRateTwoYear = riseRateTwoYear;
    }

    public BigDecimal getRiseRateThreeYear() {
        return riseRateThreeYear;
    }

    public void setRiseRateThreeYear(BigDecimal riseRateThreeYear) {
        this.riseRateThreeYear = riseRateThreeYear;
    }

    public BigDecimal getRiseRateThisYear() {
        return riseRateThisYear;
    }

    public void setRiseRateThisYear(BigDecimal riseRateThisYear) {
        this.riseRateThisYear = riseRateThisYear;
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
