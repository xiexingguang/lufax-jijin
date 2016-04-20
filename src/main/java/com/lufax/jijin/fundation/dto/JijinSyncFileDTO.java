package com.lufax.jijin.fundation.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.lufax.jijin.base.dto.BaseDTO;

public class JijinSyncFileDTO extends BaseDTO{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2715159751532561101L;
	/* 交易日期 */
    private String bizDate;
    /* 文件名称 */
    private String fileName;
    private String bizType;
    /* 状态-init，ready，not_exist,read_success,read_fail */
    private String status;
    /* 备注 */
    private String memo;
    /* 重试次数 */
    private Long retryTimes;
    /* 当前行数 */
    private Long currentLine;
    /* 创建时间 */
    private Date createdAt;
    /* 更新时间 */
    private Date updatedAt;
    /* 创建人 */
    private String createdBy;
    /* 更新人 */
    private String updatedBy;

    /* 文件元目录 @wind */
    private String sourcePath;

    public JijinSyncFileDTO() {
    }

    public String getBizDate() {
        return bizDate;
    }

    public void setBizDate(String bizDate) {
        this.bizDate = bizDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Long retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Long getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(Long currentLine) {
        this.currentLine = currentLine;
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
    
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
