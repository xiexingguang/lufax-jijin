/**
 * 
 */
package com.lufax.jijin.fundation.remote;

import org.springframework.stereotype.Service;

import com.lufax.jijin.fundation.remote.gson.request.GWBaseRequest;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 12, 2015 11:21:09 AM
 * 
 */
@Service
public class RequestResponseHelper{
	
	public <T extends GWBaseRequest> T buildGWBaseRequest(String instId, String appNo, T request){
		request.setVersion("1.0");
		request.setIsIndividual("1");
		request.setApplicationNo(appNo);
		request.setInstId(instId);
		return request;
	}

}
