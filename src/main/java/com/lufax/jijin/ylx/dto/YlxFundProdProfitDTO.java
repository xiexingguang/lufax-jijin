package com.lufax.jijin.ylx.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.lufax.jijin.ylx.util.YlxConstants;

public class YlxFundProdProfitDTO {
	private Long id;
	private String prodCode;
	private String prodName;
	private String profitDay;
	private BigDecimal unitPrice;
	private BigDecimal interestratePerSevenday;
	private BigDecimal benefitPerTenthousand;
	private BigDecimal totalAmount;
	private Date createdAt;
	private Date updatedAt;
	private String createdBy;
	private String updatedBy;
	private String productCode;
	private String productCategory;
	private Long productId;

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public YlxFundProdProfitDTO() {
	}

	public YlxFundProdProfitDTO(String line) {
		String[] arr = line.split(YlxConstants.SPRT);
		prodCode = arr[0];
		prodName = arr[1];
		profitDay = arr[2];
		try {
			unitPrice = new BigDecimal(arr[3]);
		} catch (Exception e) {
			unitPrice=BigDecimal.ONE;
		}
		// arr[4]累计净值
		try {
			interestratePerSevenday = new BigDecimal(arr[5]);
		} catch (Exception e) {
			interestratePerSevenday=BigDecimal.ZERO;
		}
		try {
			benefitPerTenthousand = new BigDecimal(arr[6]);
		} catch (Exception e) {
			benefitPerTenthousand= BigDecimal.ZERO;
		}
		try {
			totalAmount = new BigDecimal(arr[7]);
		} catch (Exception e) {
			totalAmount=BigDecimal.ZERO;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProfitDay() {
		return profitDay;
	}

	public void setProfitDay(String profitDay) {
		this.profitDay = profitDay;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getInterestratePerSevenday() {
		return interestratePerSevenday;
	}

	public void setInterestratePerSevenday(BigDecimal interestratePerSevenday) {
		this.interestratePerSevenday = interestratePerSevenday;
	}

	public BigDecimal getBenefitPerTenthousand() {
		return benefitPerTenthousand;
	}

	public void setBenefitPerTenthousand(BigDecimal benefitPerTenthousand) {
		this.benefitPerTenthousand = benefitPerTenthousand;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
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
}
