package com.lufax.jijin.fundation.remote;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.facade.caller.AccountrInterfaceCaller;
import com.lufax.jijin.fundation.remote.gson.response.AccountrResponseGson;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.sun.jersey.api.client.ClientResponse;
@Service
public class AccountrAppCallerService {

	@Autowired
    private AccountrInterfaceCaller accountrInterfaceCaller;
	
	public static final String  GET_BALANCE_USER_ID="/accounts/user/";
	/**
	 * 查询虚拟户中的余额,异常时返回null
	 * @param userId
	 * @return
	 */
	public BigDecimal getCanBeUsedBalanceByUserId(Long userId){
		ClientResponse clientResponse = accountrInterfaceCaller.getClientResponse(new InterfaceCallObject().withUrl(GET_BALANCE_USER_ID+userId));
		Logger.info(this, "Call accountr-app /accounts/user/ response status is : " + clientResponse);
        String result = clientResponse.getEntity(String.class);
        Logger.info(this, "Call accounrt-app /accounts/user/ response info is : " + result);
        AccountrResponseGson responseGson = new Gson().fromJson(result, AccountrResponseGson.class);
        if("000".equals(responseGson.getRetCode())){
        	return responseGson.getBalanceAmount().subtract(responseGson.getFrozenAmount());
        }else{
        	Logger.info(this, String.format("Call accounrt-app /accounts/user/ error userId is [%s],result is[%s]",userId,result));
        	
        }
		return null;
	}
}
