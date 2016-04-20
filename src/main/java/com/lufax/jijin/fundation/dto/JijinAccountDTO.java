/**
 * 
 */
package com.lufax.jijin.fundation.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 5, 2015 1:40:49 PM
 * 
 */
public class JijinAccountDTO extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5916716576962625546L;
	
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
    /* 是否被删除 0 未删除 1 已删除 */
    private boolean deleted = false;
    /* default sysdate */
    private Date createdAt;
    /* default sysdate */
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    
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
	public void setPayNo(String pafNo) {
		this.payNo = pafNo;
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
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.deleted = isDeleted;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}

}
