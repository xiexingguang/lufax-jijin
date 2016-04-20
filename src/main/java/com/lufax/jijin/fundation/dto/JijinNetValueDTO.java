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
 * @version create time:May 6, 2015 10:15:56 AM
 * 
 */
public class JijinNetValueDTO  extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5161005897616055813L;
	
	/* 基金代码 */
    private String fundCode;
    /* 基金状态 正常 发行 发行成功 发行失败 停止交易 停止申购 停止赎回 权益登记 红利发放 基金封闭 基金终止 */
    private String fundStatus;
    /* 净值日期yyyymmdd */
    private String netValueDate;
    /* 净值日期yyyymmdd */
    private BigDecimal netValue;
    /* 累计单位净值 */
    private BigDecimal totalNetValue;
    /* 万份收益 */
    private BigDecimal benefitPerTenthousand;
    /* 七日年化收益率 */
    private BigDecimal interestratePerSevenday;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    private String status;
    
    
    
    
    
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getFundStatus() {
		return fundStatus;
	}
	public void setFundStatus(String fundStatus) {
		this.fundStatus = fundStatus;
	}
	public String getNetValueDate() {
		return netValueDate;
	}
	public void setNetValueDate(String netValueDate) {
		this.netValueDate = netValueDate;
	}
	public BigDecimal getNetValue() {
		return netValue;
	}
	public void setNetValue(BigDecimal netValue) {
		this.netValue = netValue;
	}
	public BigDecimal getTotalNetValue() {
		return totalNetValue;
	}
	public void setTotalNetValue(BigDecimal totalNetValue) {
		this.totalNetValue = totalNetValue;
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
