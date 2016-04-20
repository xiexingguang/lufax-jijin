package com.lufax.jijin.fundation.constant;

/**
 * jijin balance history业务类型
 * @author chenqunhui
 * 数据库限制最长32位
 */
public enum BalanceHistoryBizType {

	申购转入在途成功("PUR_APPLY_AMOUNT"),
	申购下单成功("PUR_ORDER_SUCCESS"),
	申购确认成功("PUR_CONFIRM_SUCCESS"),
	申购部分成功("PUR_PARTIAL_SUCCESS"),
	申购失败回款("PUR_FAIL_REFUND"),
	申购撤单("PUR_CANCEL_ORDER"),
	赎回下单成功("REDEEM_ORDER_SUCCESS"),
	赎回实时失败("REDEEM_ORDER_FAIL"),
	赎回确认失败("REDEEM_COMFIRM_FAIL"),
	赎回成功回款("REDEEM_COMFIRM_SUCCESS"),
	赎回撤单("REDEEM_CANCEL_ORDER"),
	份额分红("DIVIDEND_SHARE"),
	货基收益("INCOME"),
	强制调增("FORCE_INCREASE"),
	强制调减("FORCE_REDUCED"),
	强制赎回("FORCE_REDEEM"),
	到期解冻("UNFREEZE_SCHEDULE"),
	转购下单成功("TRANSFORM_ORDER_SUCCESS"),
	转购转出成功("PURCHASE_OUT"),
	转购转入成功("PURCHASE_IN"),
	转购失败("PURCHASE_IN_FAIL"),
	转购部分成功("PURCHASE_IN_PARTIAL_SUCCESS"),
	转购失败回款("PURCHASE_IN_REFUND"),
	转购撤单成功("TRANSFORM_CANCEL_ORDER");

	
	String code;
	
	BalanceHistoryBizType(String code){
		this.code = code;
	}
	
	public String getCode(){
		return code;
	}
}
