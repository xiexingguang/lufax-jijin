package com.lufax.jijin.sysFacade.caller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.client.JerseyService;
import com.lufax.jersey.client.response.helper.GSONHelper;
import com.sun.jersey.api.client.ClientResponse;
@Service
public class TradeSvcCaller extends BaseInterfaceCaller {

	@Autowired
	private JerseyService jerseyServiceForTradeSvc;
	
	@Override
	protected JerseyService getJerseyService() {
		return jerseyServiceForTradeSvc;
	}

	public <T> T getWithQueryParams(InterfaceCallObject interfaceCallObject, Class<T> clazz) {
        ClientResponse response = getJerseyService()
                .getInstance(interfaceCallObject.getUrl())
                .withUser(interfaceCallObject.getUserId())
                .getResource()
                .queryParams(interfaceCallObject.getQueryParams())
                .get(ClientResponse.class);
        return GSONHelper.convert(response, clazz);
    }
}
