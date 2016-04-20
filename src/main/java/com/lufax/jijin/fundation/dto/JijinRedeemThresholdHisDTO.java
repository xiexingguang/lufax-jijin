package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * 大华货基赎回状态变动历史
 * @author chenqunhui
 *
 */
public class JijinRedeemThresholdHisDTO extends BaseDTO{

	private String fundCode;
	
	private String oldStatus;
	
	private String newStatus;
	
	private BigDecimal currentAmount;
	
	public JijinRedeemThresholdHisDTO(){
		
	}
	
	public JijinRedeemThresholdHisDTO(String fundCode,String oldStatus,String newStatus,BigDecimal currentAmount){
		this.fundCode = fundCode;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.currentAmount = currentAmount;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	public BigDecimal getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(BigDecimal currentAmount) {
		this.currentAmount = currentAmount;
	}
	
	
	
	
}
