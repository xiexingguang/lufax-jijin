package com.lufax.jijin.ylx.batch.domain;

public enum YLXBatchType {
	YLX_SLP_OPEN_REQUEST("account_open","开户申请"),
    YLX_SLP_OPEN_CONFIRM("account_open_confirm","开户确认"),
    YLX_SLP_BUY_REQUEST("prod_buy","认购申请"),
    YLX_SLP_BUY_CONFIRM("prod_buy_confirm","认购确认"),
    YLX_SLP_PURCHASE_REQUEST("prod_purchase","申购申请"),
    YLX_SLP_PURCHASE_CONFIRM("prod_purchase_confirm","申购确认"),
    YLX_SLP_REDEEM_REQUEST("prod_redeem","赎回申请"),
    YLX_SLP_REDEEM_CONFIRM("prod_redeem_confirm","赎回确认"),
    YLX_SLP_PROD_PROFIT("prod_profit_confirm","产品收益"),
    YLX_SLP_INVESTOR_PROFIT("invst_prf_confirm","投资人收益");
	
    private String fileType;
    private String desc;
    
    private YLXBatchType(String fileType,String desc){
		this.fileType=fileType;
        this.desc = desc;
	}

	public String getFileType() {
		return fileType;
	}

    public String getDesc(){
        return this.desc;
    }
    

}
