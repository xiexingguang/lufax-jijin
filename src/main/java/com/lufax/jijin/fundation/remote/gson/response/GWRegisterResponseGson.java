/**
 * 
 */
package com.lufax.jijin.fundation.remote.gson.response;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 12, 2015 11:07:53 AM
 * 
 */
public class GWRegisterResponseGson extends GWBaseResponseGson{
	
	/* 基金公司唯一标识用户的编号 */
	private String custNo;

	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}
}
