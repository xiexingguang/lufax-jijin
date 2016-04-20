package com.lufax.jijin.event.entity;

import com.google.gson.JsonObject;
import com.lufax.jijin.base.utils.JsonHelper;

public class YLXOpProductResultEventParser {

    public static String YLX_OP_PRODUCT_RESULT_PRODUCT_ID = "productId";

    public static YLXOpProductResultEvent parse(String message){
		 
		    YLXOpProductResultEvent event = new YLXOpProductResultEvent();
		 	JsonObject jsonResult = JsonHelper.parse(message);
			long productId = jsonResult.get(YLX_OP_PRODUCT_RESULT_PRODUCT_ID).getAsLong();
			event.setProductId(productId);
			return event;
	 }
}
