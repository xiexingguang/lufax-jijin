package com.lufax.jijin.sysFacade.caller;

import com.lufax.jersey.client.JerseyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DahuaGWInterfaceCaller extends BaseInterfaceCaller {

    @Autowired
    private JerseyService jerseyServiceForDahuaGW;

    @Override
    protected JerseyService getJerseyService() {
        return jerseyServiceForDahuaGW;
    }
    
}
