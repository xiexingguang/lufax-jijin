package com.lufax.jijin.fundation.remote.gson.response;

import com.lufax.jijin.fundation.gson.BaseGson;

public class GWResponseGson extends BaseGson{
	
	private String content;// response content from 基金公司

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean isSuccess(){
		return this.getRetCode().equalsIgnoreCase(GWResponseCode.SUCCESS);
	}

}
