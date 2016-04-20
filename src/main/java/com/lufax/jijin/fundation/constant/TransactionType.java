package com.lufax.jijin.fundation.constant;


public enum TransactionType {
	FROZEN_FUND,//冻结资金
    UNFROZEN_FUND,//解冻
    EXPENSE_PURCHASE,//申购转出
    EXPENSE_APPLY,//认购转出
    INCOME_PURCHASE_REFUND,//申购失败退款
    INCOME_APPLY_REFUND,//认购失败退款
    INCOME_CANCEL_REFUND,//申购撤单退款
    INCOME_REDEMPTION,//赎回转入
    INCOME_DIVIDENT,//基金分红转入
    EXPENSE_REDEMPTION,INCOME_PURCHASE,
    EXPENSE_COIN_EXCHANGE, //陆金币返还from
    INCOME_COIN_EXCHANGE,//陆金币返还to
    INCOME_FORCE_REDEEM;//强制赎回转入
}
