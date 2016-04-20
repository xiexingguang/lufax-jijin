/**
 * 
 */
package com.lufax.jijin.fundation.remote.gson.request;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 12, 2015 10:49:11 AM
 * 
 */
public class GWRegisterRequestGson extends GWBaseRequest{
	
	/* 投资人在平台的用户号, 即lufax user id*/
	private String bankId;
	/* 签约协议号 即pay_no */
	private String cdCard;
	/* 投资人姓名 */
	private String investorName;
	/* 证件类型 */
	private String certType;
	
	private String certificateNo;
	
	private String mobilePhone;
	
	private String extension;
	

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getInvestorName() {
		return investorName;
	}

	public void setInvestorName(String investorName) {
		this.investorName = investorName;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getCdCard() {
		return cdCard;
	}

	public void setCdCard(String cdCard) {
		this.cdCard = cdCard;
	}

	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}

}
