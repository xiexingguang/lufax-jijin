package com.lufax.jijin.sysFacade.caller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.client.JerseyService;

@Service
public class JijinGWInterfaceCaller extends BaseInterfaceCaller {

    @Autowired
    private JerseyService jerseyServiceForJijinGW;

    @Override
    protected JerseyService getJerseyService() {
        return jerseyServiceForJijinGW;
    }
    
}
