package com.lufax.jijin.fundation.remote.gson.paf.request;


public class PAFPayMerReservedRequestGson {
    private String fundSaleCode;
    private String fundApplyType;
    private String fundCode;
    private String fundType;
    private String fundDate;
    private String fundAttribution;


    public String getFundAttribution() {
		return fundAttribution;
	}

	public void setFundAttribution(String fundAttribution) {
		this.fundAttribution = fundAttribution;
	}

	public String getFundSaleCode() {
        return fundSaleCode;
    }

    public void setFundSaleCode(String fundSaleCode) {
        this.fundSaleCode = fundSaleCode;
    }

    public String getFundApplyType() {
        return fundApplyType;
    }

    public void setFundApplyType(String fundApplyType) {
        this.fundApplyType = fundApplyType;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundDate() {
        return fundDate;
    }

    public void setFundDate(String fundDate) {
        this.fundDate = fundDate;
    }
}
