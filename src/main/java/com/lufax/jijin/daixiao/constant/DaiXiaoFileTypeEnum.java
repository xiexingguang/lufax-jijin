package com.lufax.jijin.daixiao.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiuZhanJun on 7/24/15.
 */
public enum DaiXiaoFileTypeEnum {
    认申购起点限额("JIJIN_EX_BUY_LIMIT", "01"),
    优惠费率("JIJIN_EX_DISCOUNT", "02"),
    默认分红方式("JIJIN_EX_INCOME_MODE", "03"),
    赎回到账日期("JIJIN_EX_SELL_DAYCOUNT", "04"),
    费率("JIJIN_EX_FEE", "05"),
    净值("JIJIN_EX_NET_VALUE", "06"),
    规模及规模变动("JIJIN_EX_SCOPE", "07"),
    机构评级("JIJIN_EX_GRADE", "08"),
    基金基本要素("JIJIN_EX_INFO", "09"),
    历史分红信息("JIJIN_EX_DIVIDEND", "10"),
    基金分类("JIJIN_EX_FUND_TYPE", "11"),
    风险等级("JIJIN_EX_RISK_GRADE", "12"),
    基金净值增长率("JIJIN_EX_MF_PERFORM", "13"),
    沪深300_中证全债涨跌幅("JIJIN_EX_FX_PERFORM", "14"),
    赎回转换起点("JIJIN_EX_SELL_LIMIT", "15"),
    精选主题("JIJIN_EX_GOOD_SUBJECT", "16"),
    人气方案("JIJIN_EX_HOT_SUBJECT", "17"),
    基金经理("JIJIN_EX_MANAGER", "18"),
    年化收益("JIJIN_EX_YIELD_RATE","19"),
    资产配置("JIJIN_EX_ASSET_CONF","20"),
    行业配置("JIJIN_EX_INDUSTRY_CONF","21"),
    持股配置("JIJIN_EX_STOCK_CONF","22"),
    持劵配置("JIJIN_EX_BOND_CONF","23"),
    基金经理业绩("JIJIN_EX_MA_PERF","24"),
    基金公告("JIJIN_EX_ANNOUNCE","25"),
    基金特性("JIJIN_EX_CHARACTER","26")
    ;

    private String typeName;
    private String typeCode;

    DaiXiaoFileTypeEnum(String typeName, String typeCode) {
        setTypeName(typeName);
        setTypeCode(typeCode);
    }

    public static String getTypeNameByCode(String typeCode) {
        for (DaiXiaoFileTypeEnum typeEnum : DaiXiaoFileTypeEnum.values()) {
            if (typeCode.equals(typeEnum.getTypeCode())) {
                return typeEnum.getTypeName();
            }
        }
        return null;
    }

    public static List<String> getTypeList() {
        List<String> lstType = new ArrayList<String>();
        for (DaiXiaoFileTypeEnum typeEnum : DaiXiaoFileTypeEnum.values()) {
            lstType.add(typeEnum.getTypeName());
        }
        return lstType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
