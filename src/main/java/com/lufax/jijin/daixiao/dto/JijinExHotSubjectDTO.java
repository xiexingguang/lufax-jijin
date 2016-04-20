package com.lufax.jijin.daixiao.dto;

import com.lufax.jijin.base.dto.BaseDTO;

import java.util.Date;

/**
 * Created by NiuZhanJun on 7/23/15.
 */
public class JijinExHotSubjectDTO extends BaseDTO {
    private Date createdAt;  //创建时间
    private String createdBy;  //创建人
    private Date updatedAt;  //修改时间
    private String updatedBy;  //修改人
    private String fundCode;  //基金代码
    private String subjectName;  //主题名称
    private Long subjectIndex;  //主题序号
    private Long batchId;  //批次号(文件ID)
    private String status;  //状态NEW/DISPATCHED
    private String errMsg;
    private String memo;


    public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getSubjectIndex() {
        return subjectIndex;
    }

    public void setSubjectIndex(Long subjectIndex) {
        this.subjectIndex = subjectIndex;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
