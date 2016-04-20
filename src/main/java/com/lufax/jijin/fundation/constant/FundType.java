package com.lufax.jijin.fundation.constant;

/**
 * lufax基金类型与平安付基金类型的对应关系
 * 
 * @author chenqunhui
 *
 */
public enum FundType {

    //混合型（MIXED）偏债型（BOND_PARTIAL）保本型基金（PRINCIPAL） 股票型(STOCK),债券型（BOND）,指数型（EXPONENT），QDII（QDII）,货币型（CURRENCY）,其他（OTHER）

	
    STOCK("001"), //股票型
    EXPONENT("002"), //指数型
    MIXED("003"),//混合型
    //BOND_PARTIAL("005"),//偏债型 暂时没有
    BOND("005"),//债券型
    //PRINCIPAL("007"),//保本型  暂时没有
    CURRENCY("008"),//货币型
    QDII("001"),
    FOF("999"),
    OTHER("999");

    private String code;

    FundType(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }

    /**
     * 通过lufax基金类型或取平安付基金类型
     * @param instId
     * @param value
     * @return
     */
    public static String getCodeByName(String instId,String value) {
    	for (FundType fundType : FundType.values()) {
            if (fundType.name().equals(value)) {
                return fundType.code();
            }
        }
        return null;
    }
}
