package com.lufax.jijin.base.constant;

import java.util.Arrays;
import java.util.List;

import com.lufax.kernel.utils.MaskUtils;

public enum DesensitizeType {
	AUTHENTICATION("信息：认证信息、密钥，规范：禁止打印",Arrays.asList("password","transferSn","signature","token","Sign")),
	USERNAMWE("信息：姓名，规范：隐藏首字*",Arrays.asList("name","fullName","lufax_borrower_username","alias","lufax_investor_username","realName")),
	IDENTITYCARD("信息： 身份证号码，规范：显示前6和后4位，中间补*",Arrays.asList("idNo","identitycard_no","identityNumber")),
	PASSPORT("信息： 护照号及港澳台身份证号，规范：显示前1/3和后1/3，中间补*",Arrays.asList("passport")),
	MOBILE("信息： 手机号码，规范：显示前3位和后4位，中间补*",Arrays.asList("mobileNo","moile_no","mobile_no","regAccount")),
	EMAIL("信息： 电子邮件，规范：邮箱登录名前3位+*+@+域名",Arrays.asList("email")),
	BANKCARD("信息： 银行卡号，规范：显示前6和后4位，中间补*",Arrays.asList("bank_no","bankAccount"));
	private String value;
	private String desc;
	private List<String> keyWords;

	private DesensitizeType(String desc, List<String> keyWords) {
		this.desc = desc;
		this.keyWords = keyWords;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<String> getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public static DesensitizeType getBykeyWord(String keyWord,String value){
		for(DesensitizeType t:values()){
			if(t.getKeyWords().contains(keyWord)){
				t.setValue(value);
				return t;
			}
		}
		return null;
	}


	public String mask(){
		switch(this){
		case AUTHENTICATION:  
			return   MaskUtils.maskAll(this.getValue());
		case USERNAMWE:  
			return   MaskUtils.maskName(this.getValue());
		case IDENTITYCARD:  
			return   MaskUtils.maskIDForLog(this.getValue());
		case PASSPORT:  
			return   MaskUtils.maskExternalID(this.getValue());
		case MOBILE:  
			return   MaskUtils.maskMobile(this.getValue());
		case EMAIL:  
			return   MaskUtils.maskEmail(this.getValue());
		case BANKCARD:  
			return   MaskUtils.maskBankCardForLog(this.getValue());
	    default:return this.getValue();
		}
	}
	
	
	
	
 
	
}
