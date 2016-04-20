package com.lufax.jijin.facade.caller;

import com.lufax.jersey.client.JerseyService;
import com.lufax.jijin.sysFacade.caller.BaseInterfaceCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AccountInterfaceCaller extends BaseInterfaceCaller {

    @Qualifier("jerseyServiceForAccount")
    @Autowired
    private JerseyService jerseyServiceForAccount;

    @Override
    protected JerseyService getJerseyService() {
        return jerseyServiceForAccount;
    }
}
