package com.lufax.jijin.event.entity;


public class YLXOpProductResultEvent {
	
	private long productId;
	
	private String remark;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
