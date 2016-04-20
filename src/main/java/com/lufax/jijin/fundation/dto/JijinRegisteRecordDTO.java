/**
 * 
 */
package com.lufax.jijin.fundation.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jijin.base.dto.BaseDTO;
import com.lufax.jijin.base.utils.StringUtils;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 5, 2015 4:23:53 PM
 * 
 */
public class JijinRegisteRecordDTO  extends BaseDTO{
	
	public enum RegisteStatus{
		SUCCESS("SUCCESS"),
		FAIL("FAIL"),
		;
		
		private final String name;
		
		private RegisteStatus(RegisteStatus type) {
			this.name = type.name;
		}
		
		private RegisteStatus(String name) {
			this.name = name;
		}

		public String toString() {
			return this.name;
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6940662392324076587L;
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
    /* success,fail */
    private RegisteStatus status;
    /* 注册请求流水号 */
    private String appNo;
    private String errorCode;
    private String errorMsg;
    /* default sysdate */
    private Date createdAt;
    /* default sysdate */
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
    
    public boolean registerSuccess(){
    	return RegisteStatus.SUCCESS.equals(this.getStatus());
    }
    
    public boolean registerPaySuccess(){
    	return StringUtils.isNotBlank(this.payNo);
    }
    
    public boolean registerJijinSuccess(){
    	return StringUtils.isNotBlank(this.contractNo)
    			&& StringUtils.isNotBlank(this.custNo);
    }
    
    
    /**
     * getter and setters
     */
    
    
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
	public RegisteStatus getStatus() {
		return status;
	}
	public void setStatus(RegisteStatus status) {
		this.status = status;
	}
	/**
	 * only for write by DB automatically, don't use this
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = RegisteStatus.valueOf(status);
	}
	
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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
