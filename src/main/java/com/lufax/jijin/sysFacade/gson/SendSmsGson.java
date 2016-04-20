package com.lufax.jijin.sysFacade.gson;


import com.lufax.jijin.base.constant.SmsTemplate;
import com.lufax.jijin.base.utils.ConstantsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendSmsGson {
    private String scheduleDate;
    private List<Map<String, String>> messages;

    public SendSmsGson(String requestId, SmsTemplate smsTemplate, String mobileNo, Map<String, String> valueMap) {
        List<Map<String, String>> messages = new ArrayList<Map<String, String>>();
        valueMap.put(ConstantsHelper.BUSINESS_REQUEST_ID, requestId);
        valueMap.put(ConstantsHelper.SMS_TEMPLATE_ID, smsTemplate.getTemplateId());
        valueMap.put(ConstantsHelper.MOBILE_NO, mobileNo);
        messages.add(valueMap);
        this.messages = messages;
    }

    public SendSmsGson(List<Map<String, String>> contentList) {
        this.messages = contentList;
    }
}
