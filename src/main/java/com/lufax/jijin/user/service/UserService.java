package com.lufax.jijin.user.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.caller.UserInterfaceCaller;
import com.lufax.jijin.user.domain.BankAccountDetailGson;
import com.lufax.jijin.user.domain.CheckCaptchaGson;
import com.lufax.jijin.user.domain.CheckTradePasswordGson;
import com.lufax.jijin.user.domain.MiscUserInfoGson;
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.kernel.utils.MaskUtils;
import com.sun.jersey.api.client.ClientResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserInterfaceCaller userInterfaceCaller;

    public BankAccountDetailGson bankAccountDetail(Long userId) {
        try {
            InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl("bank/find-verified-bank-account").addQueryParam("userId", String.valueOf(userId));
            String result = userInterfaceCaller.getWithQueryParamsResponse(interfaceCallObject).getEntity().toString();
            List<BankAccountDetailGson> bankAccountDetailGsons = new Gson().fromJson(result, new TypeToken<List<BankAccountDetailGson>>() {
            }.getType());
            if (bankAccountDetailGsons != null && !bankAccountDetailGsons.isEmpty()) {
                return bankAccountDetailGsons.get(0);
            }
            return null;
        } catch (Exception e) {
            Logger.error(this, String.format("Call user failed. userId : [%s]", userId), e);
            return null;
        }
    }

    public MiscUserInfoGson getMiscUserInfo(Long userId) {
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject()
                .withUrl("user/get-misc-user-info").addQueryParam("userId", String.valueOf(userId));
        return userInterfaceCaller.getWithQueryParams(interfaceCallObject, MiscUserInfoGson.class);
    }
    
    
    public UserInfoGson getUserInfo(Long userId){
    	InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl("user/get-user-info").addQueryParam("userId", String.valueOf(userId));
    	return userInterfaceCaller.getWithQueryParams(interfaceCallObject, UserInfoGson.class); 		
    }    
    
    public CheckTradePasswordGson checkTradePwd(Long userId, String password, String source) {
        Logger.info(this, String.format("checkTradePwd userId[%s], password[%s], source[%s]", userId, password, source));
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl("/trading-password/check").addFormValue("password", password).addFormValue("source", source).addFormValue("userId", String.valueOf(userId));
        ClientResponse clientResponse = userInterfaceCaller.postClientResponse(interfaceCallObject);
        Logger.info(this, String.format("successful checkTradePwd clientResponse [%s]", clientResponse));
        CheckTradePasswordGson checkTradePasswordGson = new Gson().fromJson(clientResponse.getEntity(String.class), CheckTradePasswordGson.class);
        Logger.info(this, "checkTradePwd the response from user is : " + new Gson().toJson(checkTradePasswordGson));
        return checkTradePasswordGson;
    }

    public CheckCaptchaGson checkCaptcha(String inputValue, String ipAddress, String source, String imageId) {
        Logger.info(this, String.format("checkCaptcha inputValue[%s], ipAddress[%s], source[%s], imageId[%s]", inputValue, ipAddress, source, imageId));
        InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl("/captcha/check").addFormValue("inputValue", inputValue).addFormValue("ipAddress", ipAddress).addFormValue("source", source).addFormValue("imageId", imageId);
        ClientResponse clientResponse = userInterfaceCaller.postClientResponse(interfaceCallObject);
        Logger.info(this, String.format("get checkCaptcha response: clientResponse[%s]", clientResponse));
        CheckCaptchaGson checkCaptchaGson = new Gson().fromJson(clientResponse.getEntity(String.class), CheckCaptchaGson.class);
        Logger.info(this, "checkCaptcha the response from user is : " + new Gson().toJson(checkCaptchaGson));
        return checkCaptchaGson;
    }


}
