package com.lufax.jijin.daixiao.dto;


import com.lufax.jijin.base.dto.BaseDTO;

/**
 * 基金代销名录
 * @author chenqunhui
 *
 */
public class JijinExDictDTO extends BaseDTO {

	private static final long serialVersionUID = 3832041773027399667L;
	
	
	private String fundCode;
	private String status;   //初始化时为"NEW" 处理后为"DISPACHED"
	private Integer reTryTime; //重试次数
	private String errMsg;     //错误信息

	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getReTryTime() {
		return reTryTime;
	}
	public void setReTryTime(Integer reTryTime) {
		this.reTryTime = reTryTime;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
	
}
