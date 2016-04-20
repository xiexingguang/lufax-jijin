/**
 * 
 */
package com.lufax.jijin.fundation.remote.gson.paf.request;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 11, 2015 9:04:35 AM
 * 
 */
public class PAFRegisterRequest {
	
	private long userId;
	private String mobile;
	private String name;
	private String idType;
	private String idNo;
	private String instId;
	private String fundCode;
	private String sequence;
    private String merchantId;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}	
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
