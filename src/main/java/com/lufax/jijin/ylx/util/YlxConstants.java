package com.lufax.jijin.ylx.util;

import com.lufax.jijin.base.utils.MapUtils;

import java.util.Map;

public class YlxConstants {
	public static final String SUCCESS_RESULT_CODE = "ETS-B0000";
	public static final String SELL_ORG_CODE = "090003";// 机构代码
	public static final String SELL_ORG_NAME = "ljs"; //机构名称
	public static final String STORE_CODE = "lufaxStore";// 商户号
	public static final String YLX_SYS_NAME = "op";// 养老险系统名称
	public static final String FIELD_SPRT = "_";
	public static final String VERSION = "1.0";
	public static final String LUFAX_BANK = "平安银行上海分行营业部";
	public static final String LUFAX_BANK_CODE = "920";
	public static final String BUY_TYPE = "101";
	public static final String SELL_TYPE="203";
	public static final String REDEEM_TYPE="202";
	public static final String OPEN_FILE_TYPE = "100";
	public static final String BUY_FILE_TYPE = "300";
    public static final String PURCHASE_FILE_TYPE="022";
	public static final String REDEEM_FILE_TYPE = "024";
	public static final String TRADE_CONFIRM_YLX_BUY_TRX_SUCCESS = "00";
	public static final String CURRENCY_RMB = "01";
	public static final Object FUND_RECORD_PREFIX = "slpylx";
	public static final String GENERAL_SUCCESS_CODE = "0000";
	public static final long ROW_LIMIT = 500l;
	public static final String SPRT = "\\|";
	@SuppressWarnings("unchecked")
	public static Map<String, String> ID_TYPE_DESC_MAP = MapUtils.buildKeyValueMap(
            "1","0",// "身份证", 
            "0","7",//"其它","O", "港澳居民来往内地通行证", 
            "P", "7",//"台湾居民来往大陆通行证", 
            "2", "1",//"护照",
            "3", "3",//"军官证或士兵证", 
            "4","7",// "少儿证", 
            "9","7",// "不详", 
            "5","7",// "异常身份证", 
            "6", "4",//"港澳通行证/回乡证或台胞证",
            "8", "7",//"转换不详", 
            "7", "7",//"驾驶证", 
            "A", "7",//"出生证", 
            "B", "7",//"税务登记证", 
            "C", "7",//"工商登记证", 
            "D", "7",//"组织机构代码证",
            "E", "7",//"律师证", 
            "F", "7",//"房产证", 
            "G", "7",//"行驶证", 
            "L", "5",//"户口本", 
            "M", "2",//"军官证", 
            "N", "3",//"士兵证", 
            "Q", "7",//"临时身份证",
            "R", "7",//"外国人居留证", 
            "S", "9");//"警官证"

	public static final String YLX_WITHDRAW_LIMIT = "fa.ylx.withdraw.limit";
	public static final long DAY_TIME_MILLS = 1000 * 60 * 60 * 24;

}
