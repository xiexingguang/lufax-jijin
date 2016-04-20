package com.lufax.jijin.daixiao.dto;

import java.math.BigDecimal;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * 七日年化 万份收益
 * @author chenqunhui
 *
 */
public class JijinExYieldRateDTO extends BaseDTO {

	private static final long serialVersionUID = 1529852986168202794L;

	private String fundCode;
	
	private String startDate;   //起始日期
	
	private String endDate;     //截上日期
	
	private String noticeDate;  //公告日期
    
    private BigDecimal benefitPerTenthousand; /* 万份收益 */
    
    private BigDecimal interestratePerSevenday; /* 七日年化收益率 */
    
    private Long batchId;
    
    private String errMsg;
    
    private String status;
    
    private Integer isValid;//是否有效

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(String noticeDate) {
		this.noticeDate = noticeDate;
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

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
    
}
