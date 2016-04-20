package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * 大华货基赎回开关
 * @author chenqunhui
 *
 */
public class JijinRedeemThresholdDTO extends BaseDTO{

	private String fundCode;
	
	private String currentStatus;
	
	private BigDecimal openSwitch;
	
	private BigDecimal closeSwitch;
	
	private String accountStatus;

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public BigDecimal getOpenSwitch() {
		return openSwitch;
	}

	public void setOpenSwitch(BigDecimal openSwitch) {
		this.openSwitch = openSwitch;
	}

	public BigDecimal getCloseSwitch() {
		return closeSwitch;
	}

	public void setCloseSwitch(BigDecimal closeSwitch) {
		this.closeSwitch = closeSwitch;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	
	
	
	
}
