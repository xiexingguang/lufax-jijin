package com.lufax.jijin.ylx.dto;

import com.lufax.jijin.base.dto.YLXBaseDTO;

public class YLXAccountDTO extends YLXBaseDTO {
	private String thirdCustomerAccount;
	private String thirdAccount;
	private String thirdAccountType;
	private Long userId;

	public String getThirdCustomerAccount() {
		return thirdCustomerAccount;
	}

	public void setThirdCustomerAccount(String thirdCustomerAccount) {
		this.thirdCustomerAccount = thirdCustomerAccount;
	}

	public String getThirdAccount() {
		return thirdAccount;
	}

	public void setThirdAccount(String thirdAccount) {
		this.thirdAccount = thirdAccount;
	}

	public String getThirdAccountType() {
		return thirdAccountType;
	}

	public void setThirdAccountType(String thirdAccountType) {
		this.thirdAccountType = thirdAccountType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
