/**
 * 
 */
package com.lufax.jijin.sysFacade.caller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.client.JerseyService;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 12, 2015 2:08:55 PM
 * 
 */
@Service
public class PAFGWInterfaceCaller extends BaseInterfaceCaller{
	
	@Autowired
    private JerseyService jerseyServiceForPAFGW; // actually it is point to ljb gateway

    @Override
    protected JerseyService getJerseyService() {
        return jerseyServiceForPAFGW;
    }

}
