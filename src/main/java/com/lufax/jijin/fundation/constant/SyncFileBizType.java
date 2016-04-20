package com.lufax.jijin.fundation.constant;

public enum SyncFileBizType {
	
	DIVIDEND("05"),//分红确认文件
	TRADE("04"),// 交易确认文件
	PAF_REDEEM_DIVIDEND("1"),//平安付赎回对账文件,平安付现金分红对账文件 这两个在一个文件里
	NETVALUE("07"),//净值文件
	PAF_BUY_AUDIT("10"),//平安付认申购对账文件
	JIJIN_BUY_AUDIT("10"),//基金认申购对账文件
	JIJIN_REDEEM_AUDIT("12"),//基金赎回，申购失败，分红对账文件
	JIJIN_BALANCE_AUDIT("06"),//基金用户持仓文件
	JIJIN_TRADE_RESULT("25"),//基金交易结果文件 , 大华货基使用
    JIJIN_LFX_REGISTER_RESULT("27"),
    JIJIN_LFX_PURCHASE_RESULT("28"),//代销认申购请求记录
    JIJIN_LFX_REDEEM_RESULT("29"),//代销赎回请求记录
    JIJIN_LFX_DIVIDEND_RESULT("30");
	
	
	private String filePrefix;
	
	private SyncFileBizType(String prefix){
		this.filePrefix = prefix;
	}
	
	public String getFilePrefix(){
		return this.filePrefix;
	}
}
		