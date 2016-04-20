package com.lufax.jijin.fundation.constant;

public class ResourceResponseCode {
	
	
	public static final String SUCCESS = "00";
	public static final String ACCOUNT_NOT_EXIST = "11";
	public static final String REDEEM_AMOUNT_NOT_ENOUGH = "12";
	public static final String REDEEM_PWD_WRONG = "13";
	public static final String REDEEM_FREEZE_FAIL = "14";
	public static final String REDEEM_AMOUNT_NOT_CORRECT = "15";
	public static final String REDEEM_PWD_LOCK = "16";
	public static final String REDEEM_SUBMIT_FAIL = "17";
	public static final String CANCEL_REDEEM_FAIL = "18";
	public static final String CANCEL_REDEEM_RECALLING = "19"; // 赎回撤单处理中 
	public static final String FAIL_WITH_EXCEPTION = "99";
    public static final String FUND_CODE_EMPTY = "20";
	public static final String FUND_CODE_WRONG = "21";

	
	
	//====================分红修改response code========================
	/**
	 * ［基金公司API］返回［修改分红方式］［失败］
	 */
	public static final String DIVIDEND_MODIFY_JIJIN_COMPANY_RETURN_FAIL = "01";
	/**
	 * 传入参数为空
	 */
	public static final String DIVIDEND_MODIFY_PARAM_IS_EMPTY="02";
	/**
	 * 调用GW时失败，调用未到达基金公司
	 */
	public static final String DIVIDEND_MODIFY_GW_ERROR ="03";
	/**
	 * 基金API处理参数失败（发生异常或返回错误）
	 */
	public static final String DIVIDEND_MODIFY_JIJIN_COMPANY_ERROR ="04";
	
	/**
	 * 重复申请
	 */
	public static final String DIVIDEND_MODIFY_APPLY_REPEAT ="05";
	/**
	 * 不允许修改分红方式
	 */
	public static final String DIVIDEND_MODIFY_NOT_ALLOWED ="06";
	/**
	 * 增加基金信息失败
	 */
	public static final String JIJIN_INFO_ADD_FAIL = "21";
	/**
	 * 修改基金信息失败
	 */
	public static final String JIJIN_INFO_UPDATE_FAIL ="22";
	
	/* ====================register response code======================== */
	public static final String REGISTER_PAY_FAIL = "31";
	public static final String REGISTER_JIJIN_FAIL = "32";
	public static final String REGISTER_DB_FAIL = "33";
	public static final String CHECK_DB_FAIL = "34";
	public static final String USER_ID_NOT_VALID = "35";
	public static final String INST_ID_NOT_VALID = "36";
    public static final String USER_CAN_NOT_REGISTER = "37";
    public static final String NO_SUCH_JIJIN = "38";
    public static final String USER_CAN_NOT_BUY = "39";
}
