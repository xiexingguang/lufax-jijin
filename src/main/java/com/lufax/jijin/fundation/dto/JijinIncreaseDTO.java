/**
 * 
 */
package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 6, 2015 3:26:49 PM
 * 
 */
public class JijinIncreaseDTO extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3542031688019929359L;
	
	/* 基金代码 */
    private String fundCode;
    /* 基金涨幅日期 */
    private String increaseDate;
    /* 日涨幅 */
    private BigDecimal dayIncrease;
    /* 近一个月涨幅 */
    private BigDecimal monthIncrease;
    /* 近三个月涨幅 */
    private BigDecimal threeMonthIncrease;
    /* 近六个月涨幅 */
    private BigDecimal sixMonthIncrease;
    /* 近一年涨幅 */
    private BigDecimal yearIncrease;
    /* 今年涨幅 */
    private BigDecimal thisYearIncrease;
    private BigDecimal totalIncrease;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getIncreaseDate() {
		return increaseDate;
	}
	public void setIncreaseDate(String increaseDate) {
		this.increaseDate = increaseDate;
	}
	public BigDecimal getDayIncrease() {
		return dayIncrease;
	}
	public void setDayIncrease(BigDecimal dayIncrease) {
		this.dayIncrease = dayIncrease;
	}
	public BigDecimal getMonthIncrease() {
		return monthIncrease;
	}
	public void setMonthIncrease(BigDecimal monthIncrease) {
		this.monthIncrease = monthIncrease;
	}
	public BigDecimal getThreeMonthIncrease() {
		return threeMonthIncrease;
	}
	public void setThreeMonthIncrease(BigDecimal threeMonthIncrease) {
		this.threeMonthIncrease = threeMonthIncrease;
	}
	public BigDecimal getSixMonthIncrease() {
		return sixMonthIncrease;
	}
	public void setSixMonthIncrease(BigDecimal sixMonthIncrease) {
		this.sixMonthIncrease = sixMonthIncrease;
	}
	public BigDecimal getYearIncrease() {
		return yearIncrease;
	}
	public void setYearIncrease(BigDecimal yearIncrease) {
		this.yearIncrease = yearIncrease;
	}
	public BigDecimal getThisYearIncrease() {
		return thisYearIncrease;
	}
	public void setThisYearIncrease(BigDecimal thisYearIncrease) {
		this.thisYearIncrease = thisYearIncrease;
	}
	public BigDecimal getTotalIncrease() {
		return totalIncrease;
	}
	public void setTotalIncrease(BigDecimal totalIncrease) {
		this.totalIncrease = totalIncrease;
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
    
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}

}
