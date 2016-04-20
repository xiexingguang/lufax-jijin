package com.lufax.jijin.sysFacade.caller;

import com.lufax.jersey.client.JerseyService;
import com.lufax.jersey.client.response.helper.GSONHelper;
import com.sun.jersey.api.client.ClientResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListInterfaceCaller extends BaseInterfaceCaller {

    @Autowired
    private JerseyService jerseyServiceForListApp;

    @Override
    protected JerseyService getJerseyService() {
        return jerseyServiceForListApp;
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
