package com.lufax.jijin.daixiao.constant;

/**
 * Created by NiuZhanJun on 7/28/15.
 */
public enum JijinExRiskGradeEnum {
    低("low", "1"),//TODO 待定
    中低("mid-low", "2"),
    中("middle", "3"),
    中高("mid-high", "4"),
    高("hign", "5");

    private String levelName;
    private String levelCode;

    JijinExRiskGradeEnum(String riskLevelName, String riskLevelCode) {
        this.levelName = riskLevelName;
        this.levelCode = riskLevelCode;
    }

    /*根据中文取枚举
    public static JijinExRiskGradeEnum getByRiskCnName(String riskCnName) {
        for (JijinExRiskGradeEnum typeEnum : JijinExRiskGradeEnum.values()) {
            if (typeEnum.toString().equals(riskCnName)) {
                return typeEnum;
            }
        }
        return null;
    }*/

    /*根据levelName取枚举*/
    public static JijinExRiskGradeEnum getByRiskLevelName(String riskName) {
        for (JijinExRiskGradeEnum typeEnum : JijinExRiskGradeEnum.values()) {
            if (typeEnum.getLevelName().equals(riskName)) {
                return typeEnum;
            }
        }
        return null;
    }


    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
}
