package com.lufax.jijin.facade.caller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lufax.jersey.client.JerseyService;
import com.lufax.jijin.sysFacade.caller.BaseInterfaceCaller;

@Service
public class AccountrInterfaceCaller extends BaseInterfaceCaller {

	@Qualifier("jerseyServiceForAccountr")
    @Autowired
    private JerseyService jerseyServiceForAccountr;
	
	@Override
	protected JerseyService getJerseyService() {
		// TODO Auto-generated method stub
		return jerseyServiceForAccountr;
	}

}
