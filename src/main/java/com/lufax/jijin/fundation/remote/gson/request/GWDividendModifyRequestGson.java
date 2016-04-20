package com.lufax.jijin.fundation.remote.gson.request;

/**
 * 修改分红方式传给GW的参数
 * @author chenqunhui
 *
 */
public class GWDividendModifyRequestGson {

    private String  version  = "1.0";    			//　版本号　默认为1.0
	
	private String  isIndividual="1";   		//　个人／机构标志　０－机构　１－个人，默认为个人
	
	private String  contractNo;    			//　基金公司用户账号
	
	private String  applicationNo;    		//　业务流水号
	
	private String  businessCode ="029";    		//　业务代码
	
	private String instId;
	
	private String  fundCode;    			//　基金代码
	
	private String  chargeType="A";    			//　收费方式：Ａ－前收费　Ｂ－后收费
	
	private String  dividendType;    		//	分红方式　0－红利转投　1－现金分红
	
	
	/**
	 * 
	 * @param applicationNo 流水号
	 * @param contractNo	客户账号
	 * @param fundCode		基金编码
	 * @param dividendType  分红方式
	 */
	public GWDividendModifyRequestGson(String applicationNo,String contractNo,String fundCode, String dividendType,String instId){
		this.applicationNo = applicationNo;
		this.contractNo = contractNo;
		this.fundCode = fundCode;
		this.dividendType = dividendType;
		this.instId = instId;
	}

	public String getVersion() {
		return version;
	}

	
	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public String getIsIndividual() {
		return isIndividual;
	}

	public void setIsIndividual(String isIndividual) {
		this.isIndividual = isIndividual;
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

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public String getDividendType() {
		return dividendType;
	}

	public void setDividendType(String dividendType) {
		this.dividendType = dividendType;
	}
	

	
}
