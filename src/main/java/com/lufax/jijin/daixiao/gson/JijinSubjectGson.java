package com.lufax.jijin.daixiao.gson;

import com.lufax.jijin.daixiao.dto.JijinExGoodSubjectDTO;
import com.lufax.jijin.daixiao.dto.JijinExHotSubjectDTO;

/**
 * 基金精选主题、人气方案
 * @author chenqunhui
 *
 */
public class JijinSubjectGson {

	private String fundCode;
    private Integer orderNum; //方案序号	
    private Long batchId;
    private String groupType;//类型2:人气方案,3:基金主题
    private String groupName;//主题／方案名称
    private String memo;
    
    
    public JijinSubjectGson(JijinExGoodSubjectDTO sub){
    	this.fundCode = sub.getFundCode();
    	this.orderNum = sub.getSubjectIndex().intValue();
    	this.batchId =  sub.getBatchId();
    	this.groupType = "3";
    	this.groupName = sub.getSubjectName();
    	this.memo = sub.getMemo();
    }
    
    public JijinSubjectGson(JijinExHotSubjectDTO sub){
    	this.fundCode = sub.getFundCode();
    	this.orderNum = sub.getSubjectIndex().intValue();
    	this.batchId =  sub.getBatchId();
    	this.groupType = "2";
    	this.groupName = sub.getSubjectName();
    	this.memo = sub.getMemo();
    }
    
    
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}


    
    
}
