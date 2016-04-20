package com.lufax.jijin.sysFacade.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lufax.jijin.base.constant.SmsTemplate;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.sysFacade.caller.ExtInterfaceCaller;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.gson.SendSmsGson;
import com.lufax.jijin.sysFacade.gson.result.SendSmsResultGson;
import com.lufax.jijin.user.dto.UserDTO;

@Service
public class ExtInterfaceService {
    @Autowired
    private ExtInterfaceCaller extInterfaceCaller;
    private static final String SEND_SMS_URL = "sms/send";

	public SendSmsResultGson sendSms(List<Map<String, String>> contentList) {
		Logger.info(this, String.format("Send smsList..."));
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(SEND_SMS_URL).addFormValue(
				ConstantsHelper.DTO, new Gson().toJson(new SendSmsGson(contentList)));
		return extInterfaceCaller.post(interfaceCallObject, SendSmsResultGson.class);
	}

	public void sendSmsToCustomers(List<Map<String, String>> contentList) {
		try {
			SendSmsResultGson sendSmsResultGson = sendSms(contentList);
			if (null == sendSmsResultGson) {
				Logger.error(this, "Send/ sms failed.");
			} else if (!sendSmsResultGson.isSucceed()) {
				Logger.error(
						this,
						String.format("Send sms failed,result is %s and smsRequestId is %s",
								sendSmsResultGson.getResult(), sendSmsResultGson.getSmsRequestId()));
			} else {
				Logger.info(this,
						String.format("Send sms successfully,smsRequestId is %s", sendSmsResultGson.getSmsRequestId()));
			}
		} catch (Exception e) {
			Logger.error(this, String.format("Send sms failed."), e);
		}
	}

    public Map<String, String> generateSmsContentMap(UserDTO user, SmsTemplate smsTemplate, Map<String, String> params) {
        HashMap<String, String> valueMap = new HashMap<String, String>();
        String requestId = String.valueOf(System.currentTimeMillis());
        valueMap.put(ConstantsHelper.BUSINESS_REQUEST_ID, requestId);
        valueMap.put(ConstantsHelper.SMS_TEMPLATE_ID, smsTemplate.getTemplateId());
        valueMap.put(ConstantsHelper.MOBILE_NO, user.getMobileNo());
        valueMap.putAll(params);
        Logger.info(this, String.format("Generate sms to user[id:%s,mobile:%s] with template %s ,requestId is %s", user.id(), user.getMobileNo(), smsTemplate.getTemplateId(), requestId));
        return valueMap;
    }

    public Map<String, String> generateSmsContentMap(String mobileNo, SmsTemplate smsTemplate, Map<String, String> params) {
        HashMap<String, String> valueMap = new HashMap<String, String>();
        String requestId = String.valueOf(System.currentTimeMillis());
        valueMap.put(ConstantsHelper.BUSINESS_REQUEST_ID, requestId);
        valueMap.put(ConstantsHelper.SMS_TEMPLATE_ID, smsTemplate.getTemplateId());
        valueMap.put(ConstantsHelper.MOBILE_NO, mobileNo);
        valueMap.putAll(params);
        Logger.info(this, String.format("Generate sms to mobile:%s with template %s ,requestId is %s", mobileNo, smsTemplate.getTemplateId(), requestId));
        return valueMap;
    }
}
