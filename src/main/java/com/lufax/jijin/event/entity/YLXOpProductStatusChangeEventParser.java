package com.lufax.jijin.event.entity;

import com.google.gson.JsonObject;
import com.lufax.jijin.base.utils.JsonHelper;

public class YLXOpProductStatusChangeEventParser {
	public static String ID = "id";
    public static String CODE = "code";
    public static String REMOTE_SYSTEM_OLD_STATUS = "remoteSystemOldStatus";
    public static String REMOTE_SYSTEM_NEW_STATUS = "remoteSystemNewStatus";
    public static String SUCCESS_FLAG = "successFlag";
    public static String FIRST_TIME= "firstTime";
    public static String EXTRA_OBJECT = "extInfo";

    public static YLXOpProductStatusChangeEvent parse(String message){

        YLXOpProductStatusChangeEvent event = new YLXOpProductStatusChangeEvent();
        JsonObject jsonResult = JsonHelper.parse(message);
        long productId = jsonResult.get(ID).getAsLong();
        String productCode = jsonResult.get(CODE).getAsString();
        String remoteSystemOldStatus = jsonResult.get(REMOTE_SYSTEM_OLD_STATUS).getAsString();
        String remoteSystemNewStatus = jsonResult.get(REMOTE_SYSTEM_NEW_STATUS).getAsString();
        String successFlag =  jsonResult.get(SUCCESS_FLAG).getAsString();
        String firstTime = "false";
        if(null!=jsonResult.get(EXTRA_OBJECT)&&null!=jsonResult.get(EXTRA_OBJECT).getAsJsonObject()&&null!=jsonResult.get(EXTRA_OBJECT).getAsJsonObject().get(FIRST_TIME)){
        	firstTime = jsonResult.get(EXTRA_OBJECT).getAsJsonObject().get(FIRST_TIME).getAsString();
        }
        
        event.setProductId(productId);
        event.setProductCode(productCode);
        event.setRemoteSystemOldStatus(remoteSystemOldStatus);
        event.setRemoteSystemNewStatus(remoteSystemNewStatus);
        event.setSuccessFlag(successFlag);
        event.setFirstTime(firstTime);
        return event;
    }
}
