/**
 * 
 */
package com.lufax.jijin.fundation.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jijin.base.dto.BaseDTO;

/**
 * User dividend
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 6, 2015 9:25:00 AM
 * 
 */
public class JijinDividendDTO extends BaseDTO{
	
	public enum Status{
		NEW, DISPATCHED, FAIL, UNMATCH;
	}
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = -4151299265000633371L;
	/* fk-bus_jijin_sync_file.id */
    private long fileId;
    
    private long userId;
    /* 陆金所自定义机构标识:基金公司 */
    private String instId;
    /* 基金公司分红流水号 */
    private String appSheetNo;
    private String fundCode;
    /* 默认分红方式 0-红利转投 1-现金分红 */
    private String dividendType;
    /* 权益日期yyyymmdd */
    private String rightDate;
    /* 交易确认日期yyyymmdd */
    private String trxDate;
    /* 分红发放日yyyymmdd */
    private String dividendDate;
    /* 收费方式 a-前收费 b-后收费 */
    private String chargeType;
    /* 转投份额 */
    private BigDecimal dividendShare;
    /* 分红金额 */
    private BigDecimal dividendAmount;
    /* 手续费 */
    private BigDecimal fee;
    /* 分红类型 0 普通分红，1 质押基金分红，2 货币基金收益结转，3 保本基金赔付 */
    private String dividendMode;
    private String resCode;
    private String resMsg;
    /* new, dispatched */
    private Status status = Status.NEW;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;    
    
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getAppSheetNo() {
		return appSheetNo;
	}
	public void setAppSheetNo(String appSheetNo) {
		this.appSheetNo = appSheetNo;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	/**
	 * only get by default
	 * @return
	 */
	public String getDividendType() {
		return this.dividendType;
	}
	
	/**
	 * only for write by DB automatically, don't use this
	 * @param status
	 */
	public void setDividendType(String dividendType) {
		this.dividendType = dividendType;
	}
	
	public String getRightDate() {
		return rightDate;
	}
	public void setRightDate(String rightDate) {
		this.rightDate = rightDate;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	public String getDividendDate() {
		return dividendDate;
	}
	public void setDividendDate(String dividendDate) {
		this.dividendDate = dividendDate;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public BigDecimal getDividendShare() {
		return dividendShare;
	}
	public void setDividendShare(BigDecimal dividendShare) {
		this.dividendShare = dividendShare;
	}
	public BigDecimal getDividendAmount() {
		return dividendAmount;
	}
	public void setDividendAmount(BigDecimal dividendAmount) {
		this.dividendAmount = dividendAmount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public String getDividendMode() {
		return dividendMode;
	}
	public void setDividendMode(String dividendMode) {
		this.dividendMode = dividendMode;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * only for write by DB automatically, don't use this
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = Status.valueOf(status);
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
	public String getInstId() {
		return instId;
	}
	public void setInstId(String instId) {
		this.instId = instId;
	}
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}

}
