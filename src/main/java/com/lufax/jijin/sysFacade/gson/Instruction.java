package com.lufax.jijin.sysFacade.gson;

public class Instruction {
	
	private String method;//TRANSFER_FREEZE, PLUS_FREEZE	代付前准备操作方法（2016-4-12上线, 只支持“TRANSFER_FREEZE：单笔转账冻结”和“PLUS_FREEZE：(清算)调增冻结”）	 
	private String  detail;//操作明细JSON String (对应GSON结构为TRANSFER_FREEZE: TransferFreezeInstructionDetail, PLUS_FREEZE: PlusFreezeInstructionDetail)
	
	public Instruction(String method, String detail) {
		this.method = method;
		this.detail = detail;
	}
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
