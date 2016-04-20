package com.lufax.jijin.fundation.constant;


public enum TradeRecordType {	
	APPLY,
	PURCHASE,
	REDEEM,
	PURCHASE_CANCEL,
	REDEEM_CANCEL,
	DIVIDEND_CASH,
	DIVIDEND_SHARE,
	DIVIDEND_MODIFY,
    CURRENCY_INCOME,
    FORCE_INCREASE,
    FORCE_REDUCED,
    FORCE_REDEEM,
	HZ_APPLY,//货基转认购
	HZ_PURCHASE,//货基转申购
	HZ_REDEEM//货基转认购赎回(用于tradeLog)
}
