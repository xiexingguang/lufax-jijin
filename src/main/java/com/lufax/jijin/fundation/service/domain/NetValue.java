/**
 * 
 */
package com.lufax.jijin.fundation.service.domain;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 21, 2015 3:28:52 PM
 * 
 */
public class NetValue implements Comparable<NetValue>{
	
	private String date;
	private BigDecimal value;

	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public BigDecimal getValue() {
		return value;
	}


	public void setValue(BigDecimal value) {
		this.value = value;
	}


	@Override
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}


	@Override
	public int compareTo(NetValue o) {
		return date.compareTo(o.date);
	}

}
