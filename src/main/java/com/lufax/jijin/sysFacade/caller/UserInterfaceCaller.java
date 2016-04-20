package com.lufax.jijin.sysFacade.caller;

import com.lufax.jersey.client.JerseyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInterfaceCaller extends BaseInterfaceCaller {

    @Autowired
    private JerseyService jerseyServiceForUser;

    @Override
    protected JerseyService getJerseyService() {
        return jerseyServiceForUser;
    }
}
