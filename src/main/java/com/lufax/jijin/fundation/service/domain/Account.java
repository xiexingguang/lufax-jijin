/**
 * 
 */
package com.lufax.jijin.fundation.service.domain;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * In jijin, each user has an account of one fund company
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 20, 2015 9:22:51 AM
 * 
 */
public class Account {
	
	private long userId;
    /* 陆金所自定义机构标识:基金公司 */
    private String instId;
    /* 支付帐号 */
    private String payNo;
    /* 支付渠道名称 */
    private String channel;
    /* 基金公司用户编号唯一标识 */
    private String custNo;
    /* 基金公司用户交易帐号 */
    private String contractNo;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
    
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}

}
