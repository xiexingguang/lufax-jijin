package com.lufax.jijin.sysFacade.caller;

import com.lufax.jersey.client.JerseyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundInterfaceCaller extends BaseInterfaceCaller {
    @Autowired
    private JerseyService jerseyServiceForFund;

    @Override
    protected JerseyService getJerseyService() {
        return jerseyServiceForFund;
    }
}
