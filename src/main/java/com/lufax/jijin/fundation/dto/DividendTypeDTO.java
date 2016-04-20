package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

public class DividendTypeDTO  extends BaseDTO {

	private static final long serialVersionUID = 9195072061496212971L;
	
	private Long  userId;        			//用户ID 
	
	private String  instId;    				//　机构标识　基金公司编号
	
	private String  contractNo;    			//　基金公司用户账号
	
	private String  applicationNo;    		//　业务流水号
	
	private String  fundCode;    			//　基金代码
	
	private String  dividendType;    		//	分红方式　0－红利转投　1－现金分红
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}


	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}


	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}


	public String getDividendType() {
		return dividendType;
	}

	public void setDividendType(String dividendType) {
		this.dividendType = dividendType;
	}


	
	
	
	

}
