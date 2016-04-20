package com.lufax.jijin.base.utils;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ConstantsHelper {

    public static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE;
    public static final String CURRENCY_FORMAT = new DecimalFormatSymbols(DEFAULT_LOCALE).getCurrencySymbol() + "#,##0.00";
    public static final String PERCENTAGE_FORMAT = "#,##0.00%";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String DTO = "dto";
    public static final String BUSINESS_REQUEST_ID = "RequestID";
    public static final String SMS_TEMPLATE_ID = "TemplateID";
    public static final String MOBILE_NO = "MobileNo";
    public static final String JIJIN_DAIXIAO_INIT_SWITCH = "jijin.daixiao.init.switch";
    public static final String JIJIN_DAIXIAO_SALE_CHANNEL= "jijin.daixiao.salechannel";
    public static final String JIJIN_DAIXIAO_USER_GROUP = "jijin.daixiao.product.user.groups";
    public static final String JIJIN_DAIXIAO_SEND_NET_VALUE_MSG ="jijin.daixiao.send.net.value.msg";
    public static final String JIJIN_DAHUA_ACCOUNT_STATUS_CHANGE_SWITCH ="jijin.dahua.account.status.change.switch";
    
    public static final int PAGE_LIMIT_15 = 15;
    public static final String RET_CODE_SUCCESS = "000";
    public static final String RET_CODE_FAILURE = "999";
}
