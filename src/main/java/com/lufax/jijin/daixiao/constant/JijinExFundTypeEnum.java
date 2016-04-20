package com.lufax.jijin.daixiao.constant;

/**
 * Created by NiuZhanJun on 7/28/15.
 */
/**
 * wind数据基金类型与lufax平台基金类型对应的枚举，
 * typeName 为lufax基金类型
 * typeCode 为wind基金类型
 * @author chenqunhui
 *
 */


public enum JijinExFundTypeEnum {
    股票型("STOCK", "1","0"),
    债券型("BOND", "2" ,"1"),
    混合型("MIXED", "3", "3"),
    货币与理财型("CURRENCY", "4","2"),
    FOF("FOF", "5","7"),
    指数型("EXPONENT", "6","5"),
    QDII("QDII", "7","6"),
    其他("OTHER", "9","8");

    private String typeName;
    private String typeCode;//lufax bus_jijin_ex_fund_type表的02基金类型的枚举值
    private String lfexCode;//lfex基金类型的枚举值，也是数字

    JijinExFundTypeEnum(String fundTypeName, String fundTypeCode ,String lfexCode) {
        this.typeName = fundTypeName;
        this.typeCode = fundTypeCode;
        this.lfexCode = lfexCode;
    }

    /*根据中文取枚举*/
    public static JijinExFundTypeEnum getByFundTypeCnName(String fundTypeCnName) {
        for (JijinExFundTypeEnum typeEnum : JijinExFundTypeEnum.values()) {
            if (typeEnum.toString().equals(fundTypeCnName)) {
                return typeEnum;
            }
        }
        return null;
    }
    /**
     * 使用lfexCode获取
     * @param lfexCode
     * @return
     */
    public static JijinExFundTypeEnum getByLfexCode(String lfexCode){
    	for (JijinExFundTypeEnum typeEnum : JijinExFundTypeEnum.values()) {
            if (typeEnum.getLfexCode().equals(lfexCode)) {
                return typeEnum;
            }
        }
        return null;
    }

    /**
     * 根据Code取枚举
     * */
    public static JijinExFundTypeEnum getByFundCode(String typeCode) {
        for (JijinExFundTypeEnum typeEnum : JijinExFundTypeEnum.values()) {
            if (typeEnum.getTypeCode().equals(typeCode)) {
                return typeEnum;
            }
        }
        return null;
    }

    /*根据levelName取枚举*/
    public static JijinExFundTypeEnum getByFundTypeName(String typeName) {
        for (JijinExFundTypeEnum typeEnum : JijinExFundTypeEnum.values()) {
            if (typeEnum.getTypeName().equals(typeName)) {
                return typeEnum;
            }
        }
        return null;
    }


    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

	public String getLfexCode() {
		return lfexCode;
	}

	public void setLfexCode(String lfexCode) {
		this.lfexCode = lfexCode;
	}
    
    
}
