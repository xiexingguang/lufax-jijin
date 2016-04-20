package com.lufax.jijin.base.constant;


public enum SmsTemplate {
	YLX_PROFIT_ERROR("LJS_YBS_20150812001", "陆金所-YLX-养老险账户对账差异"),
	
    YLX_WITHDRAW_COMPLETE("LJS_YBS_20141230001", "陆金所-YLX-资源划拨完成"),
    YLX_WITHDRAW_MONEY_NO_ENOUGH("LJS_YBS_20141230002", "陆金所-YLX-虚拟库余额不足"),
    YLX_BUY_AUDIT_FAIL("LJS_YBS_20141230003", "陆金所-YLX-认购文具核异常"),
    YLX_SELL_AUDIT_SUCCESS("LJS_YBS_20141230004", "陆金所-YLX-兑付文件核兑完成"),

    JIJIN_PURCHASE_SUCCESS("LJS_YBS_20150630001", "陆金所-基金-认申购确认短信"),
    JIJIN_REDEEM_SUCCESS("LJS_YBS_20150630002", "陆金所-基金-赎回资金到帐"),

    YLX_SELL_AUDIT_FAIL("LJS_YBS_20141230005", "陆金所-YLX-兑付文件核对异常"),
    YLX_SEND_BUY_REQ_FILE_FAIL("LJS_YBS_20141230006", "陆金所-YLX-认购文件发送异常"),
    YLX_BUY_SUCCESS_MESSAGE_TEMPLATE("LJS_YBS_20150518001","路金所-YLX-认购成功发送短消息"),
    YLX_PULL_CONFIRM_FILE_FAIL("LJS_YBS_20150518011","陆金所-YLX-下载文件异常"),
    YLX_HANDLE_CONFIRM_FILE_FAIL("LJS_YBS_20150518012","陆金所-YLX-处理文件异常"),
    
    YLX_PURCHASE_SUCCESS_MESSAGE("LJS_YBS_20150625012","路金所-YLX-申购确认成功发送短消息"),
    YLX_PURCHASE_FAIL_MESSAGE("LJS_YBS_20150625013","路金所-YLX-申购确认失败发送短消息"),
    YLX_REDEEM_SUCCESS_MESSAGE("LJS_YBS_20150625014","路金所-YLX-赎回确认成功发送短消息"),
    YLX_REDEEM_FAIL_MESSAGE("LJS_YBS_20150625015","路金所-YLX-赎回确认失败发送短消息"),
    YLX_PRODUCT_STATUS_OPEN_MESSAGE("LJS_YBS_20150625011","路金所-YLX-富赢增长产品开放发送短消息");
    
    

    private String templateId;
    private String des;

    private SmsTemplate(String templateId, String des) {
        this.templateId = templateId;
        this.des = des;
    }

    public String getTemplateId() {
        return templateId;
    }
}
