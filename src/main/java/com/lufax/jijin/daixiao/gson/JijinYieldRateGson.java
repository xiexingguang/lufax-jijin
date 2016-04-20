package com.lufax.jijin.daixiao.gson;

import java.math.BigDecimal;

public class JijinYieldRateGson {

	private String endDate;
	
    private BigDecimal benefitPerTenthousand; /* 万份收益 */
    
    private BigDecimal interestratePerSevenday; /* 七日年化收益率 */

	public String getEndDate() {
		return endDate;
	}

	public BigDecimal getBenefitPerTenthousand() {
		return benefitPerTenthousand;
	}

	public BigDecimal getInterestratePerSevenday() {
		return interestratePerSevenday;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setBenefitPerTenthousand(BigDecimal benefitPerTenthousand) {
		this.benefitPerTenthousand = benefitPerTenthousand;
	}

	public void setInterestratePerSevenday(BigDecimal interestratePerSevenday) {
		this.interestratePerSevenday = interestratePerSevenday;
	}
    
    
}
