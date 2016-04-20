package com.lufax.jijin.daixiao.constant;

/**
 * Created by NiuZhanJun on 7/28/15.
 */
public enum JijinExFundBizEnum {
    认购("subscrip", "20"),
    申购("buy", "22"),
    定投("autoBuy", "39"),
    赎回("sell", "24"),
    转换("transfer", "36");

    private String bizName;
    private String bizCode;

    JijinExFundBizEnum(String bizName, String bizCode) {
        this.bizName = bizName;
        this.bizCode = bizCode;
    }

    public static JijinExFundBizEnum getByCnName(String cnName) {
        for (JijinExFundBizEnum typeEnum : JijinExFundBizEnum.values()) {
            if (typeEnum.toString().equals(cnName)) {
                return typeEnum;
            }
        }
        return null;
    }

    public static JijinExFundBizEnum getByCode(String typeCode) {
        for (JijinExFundBizEnum typeEnum : JijinExFundBizEnum.values()) {
            if (typeEnum.getBizCode().equals(typeCode)) {
                return typeEnum;
            }
        }
        return null;
    }


    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }
}
