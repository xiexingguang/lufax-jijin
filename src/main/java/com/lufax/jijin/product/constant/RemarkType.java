package com.lufax.jijin.product.constant;

/**
 * Created with IntelliJ IDEA.
 * User: chenzhiqiang288
 * Date: 9/6/13
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public enum RemarkType {
    ENTRUST_FAILED("03"),
    RAISE_FAILED("04");

    private String value;

    private RemarkType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
