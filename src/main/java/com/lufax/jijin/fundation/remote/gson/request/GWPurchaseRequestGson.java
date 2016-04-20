package com.lufax.jijin.fundation.remote.gson.request;


public class GWPurchaseRequestGson extends GWBaseRequest {

    private String amount;
    private String chargeType;
    private String businessCode;
	private String fundCode;
	private String isAgreeRisk;
    private String investorName;
    private String certType;
    private String certificateNo;
    private PurchaseExtension extension;

    public String getIsAgreeRisk() {
		return isAgreeRisk;
	}

	public void setIsAgreeRisk(String isAgreeRisk) {
		this.isAgreeRisk = isAgreeRisk;
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

	public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getInvestorName() {
        return investorName;
    }

    public void setInvestorName(String investorName) {
        this.investorName = investorName;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public PurchaseExtension getExtension() {
        return extension;
    }

    public void setExtension(PurchaseExtension extension) {
        this.extension = extension;
    }
}
