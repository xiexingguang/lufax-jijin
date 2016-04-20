/**
 * 
 */
package com.lufax.jijin.fundation.remote.gson.paf.response;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 12, 2015 2:32:58 PM
 * 
 */
public class BaseResponseGson {
	
	private String version;
	private String charset;
	private String signMethod;
	private String signature;
	private String transType;
	private String respCode;
	private String respMsg;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getSignMethod() {
		return signMethod;
	}
	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespMsg() {
		return respMsg;
	}
	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}
	
	public boolean isSuccess(){
		return this.respCode.equalsIgnoreCase("0000");
	}
}
