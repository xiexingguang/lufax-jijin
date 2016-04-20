package com.lufax.jijin.daixiao.constant;

/**
 * 评级机构枚举
 * @author chenqunhui
 *
 */
public enum GradeEnum {
	海通证券("HAITONG"),
	银河("YINHE"),
	上海证券("SHANGZHENG");
	
	private  String code;
	
	GradeEnum(String code){
		this.code = code;
	}
	
	public String getCode(){
		return code;
	}
}
