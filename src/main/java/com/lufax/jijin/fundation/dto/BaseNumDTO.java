package com.lufax.jijin.fundation.dto;

import com.lufax.jijin.base.dto.BaseDTO;

public class BaseNumDTO extends BaseDTO {
	
	private long num;

	public BaseNumDTO() {}

	public BaseNumDTO(long num) {
		this.num = num;
	}
	
	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}
}
