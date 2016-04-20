package com.lufax.jijin.daixiao.constant;

/**
 * Wind业务代码
 * @author chenqunhui
 *
 */
public enum WindBizType {

    APPLY("20"),
    PURCHASE("22"),
    REDEEM("24"),
    AUTOMATIC_INVESTMENT("39");//基金定投
   
	private String code;

	WindBizType(String code) {
		this.code = code;
	}

    public String getCode() {
        return code;
    }
}
