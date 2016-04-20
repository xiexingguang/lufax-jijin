package com.lufax.jijin.daixiao.remote.caller;

import com.lufax.jersey.client.JerseyService;
import com.lufax.jijin.sysFacade.caller.BaseInterfaceCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by NiuZhanJun on 7/28/15.
 */
@Service
public class JijinExGWInterfaceCaller extends BaseInterfaceCaller {
    @Qualifier("jerseyServiceForJijinGW")
    @Autowired
    private JerseyService jerseyServiceForJijinGW;

    @Override
    protected JerseyService getJerseyService() {
        return jerseyServiceForJijinGW;
    }
}
