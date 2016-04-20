package com.lufax.jijin.base.dto;


import java.math.BigDecimal;

public class BizParametersDTO extends BaseDTO {

    public static final String ZJRS_NOTICE_MOBILE_NO = "zjrs.notice.mobile.no";

    public static final String PRODUCT_RISK_LEVEL_RULE_1 = "product.risk.level.rule.1";
    public static final String PRODUCT_RISK_LEVEL_RULE_2 = "product.risk.level.rule.2";
    public static final String PRODUCT_RISK_LEVEL_RULE_3 = "product.risk.level.rule.3";
    public static final String PRODUCT_RISK_LEVEL_RULE_4 = "product.risk.level.rule.4";
    public static final String PRODUCT_RISK_LEVEL_RULE_5 = "product.risk.level.rule.5";

    private String parameterCode;
    private BigDecimal parameterValue;
    private String value;

    public String getParameterCode() {
        return parameterCode;
    }

    public BigDecimal getParameterValue() {
        return parameterValue;
    }

    public String getValue() {
        return value;
    }
}
