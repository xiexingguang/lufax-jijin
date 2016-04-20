package com.lufax.jijin.sysFacade.caller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.client.JerseyService;

@Service
public class PaymentAppCaller extends BaseInterfaceCaller {

	@Autowired
	private JerseyService jerseyServiceForPaymentApp;
	
	@Override
	protected JerseyService getJerseyService() {
		return jerseyServiceForPaymentApp;
	}

}
