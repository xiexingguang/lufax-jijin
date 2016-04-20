package com.lufax.jijin.sysFacade.caller;

import com.lufax.jersey.client.JerseyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExtInterfaceCaller extends BaseInterfaceCaller {

    @Autowired
    private JerseyService jerseyServiceForExt;

    @Override
    protected JerseyService getJerseyService() {
        return jerseyServiceForExt;
    }
}
