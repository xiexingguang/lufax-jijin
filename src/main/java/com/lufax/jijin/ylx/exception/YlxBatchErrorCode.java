package com.lufax.jijin.ylx.exception;

public enum YlxBatchErrorCode {
	FILE_HEADER_ERROR(10000,"输入文件的头内容有问题，无法进一步处理"),
	FILE_BODY_ERROR(10001, "输入文件的正文内容有问题，无法进一步处理"),
	FILE_ROW_NUM_ERROR(10002, "输入文件的行数有问题,无法进一步处理"),
	ACCOUNT_ERROR(10003, "账户未被确认");

    private int code;
    private String desc;

    YlxBatchErrorCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public int getCode(){
    	return this.code;
    }
}
