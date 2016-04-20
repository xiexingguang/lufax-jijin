package com.lufax.jijin.ylx.dto;


public class OpenConfirmDetailDTO extends BaseConfirmDetailDTO {

	private String name;
	private String identityType;
	private String identityNum;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getIdentityNum() {
		return identityNum;
	}

	public void setIdentityNum(String identityNum) {
		this.identityNum = identityNum;
	}

}
