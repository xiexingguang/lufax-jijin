package com.lufax.jijin.daixiao.constant;


public enum RatingGagencyEnum {
    海通证券("海通证券", "HAITONG"),
    银河("银河", "YINHE"),
    上海证券("上海证券", "SHANGZHENG");

    RatingGagencyEnum(String gagencyName, String gagencyCode) {
        this.gagencyName = gagencyName;
        this.gagencyCode = gagencyCode;
    }

    private String gagencyName;
    private String gagencyCode;

    public static String getGagenCyCodeByName(String gagenCyName) {
        for (RatingGagencyEnum typeEnum : RatingGagencyEnum.values()) {
            if (typeEnum.getGagencyName().equals(gagenCyName)) {
                return typeEnum.getGagencyCode();
            }
        }
        return null;
    }

    public static String getGagenCyNameByCode(String gagenCyCode) {
        for (RatingGagencyEnum typeEnum : RatingGagencyEnum.values()) {
            if (typeEnum.getGagencyCode().equals(gagenCyCode)) {
                return typeEnum.getGagencyName();
            }
        }
        return null;
    }

    public String getGagencyName() {
        return gagencyName;
    }

    public void setGagencyName(String gagencyName) {
        this.gagencyName = gagencyName;
    }

    public String getGagencyCode() {
        return gagencyCode;
    }

    public void setGagencyCode(String gagencyCode) {
        this.gagencyCode = gagencyCode;
    }
}
