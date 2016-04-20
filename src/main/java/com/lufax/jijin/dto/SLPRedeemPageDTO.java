package com.lufax.jijin.dto;

import java.math.BigDecimal;
import java.util.Date;

public class SLPRedeemPageDTO {
    private BigDecimal fundShare;
    private BigDecimal unitPrice;
    private Date estimateTime;
	public BigDecimal getFundShare() {
		return fundShare;
	}
	public void setFundShare(BigDecimal fundShare) {
		this.fundShare = fundShare;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Date getEstimateTime() {
		return estimateTime;
	}
	public void setEstimateTime(Date estimateTime) {
		this.estimateTime = estimateTime;
	}
}
