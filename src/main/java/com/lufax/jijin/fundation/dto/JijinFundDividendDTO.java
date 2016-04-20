/**
 * 
 */
package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * Fund dividend
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 25, 2015 9:36:48 AM
 * 
 */
public class JijinFundDividendDTO  extends BaseDTO{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4941788524810536364L;
	/* 基金公司代码 */
    private String fundCode;
    /* 分红日期 */
    private String dividendDate;
    /* 分红金额 */
    private BigDecimal amount;
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
	public String getDividendDate() {
		return dividendDate;
	}
	public void setDividendDate(String dividendDate) {
		this.dividendDate = dividendDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
