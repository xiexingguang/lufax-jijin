package com.lufax.jijin.daixiao.gson;


import com.lufax.jijin.fundation.gson.BaseGson;

import java.util.List;

public class JijinDaixiaoInfoGson extends BaseGson{

    private List<JijinExBuyLimitGson> jijinExBuyLimitGsons; //2.1 认申购起点限额
    private List<JijinExDiscountGson> jijinExDiscountGsons;//2.2 优惠费率
    private JijinExIncomeModeGson jijinExIncomeModeGson; //2.3 默认分红方式
    private JijinExSellDayCountGson jijinExSellDayCountGson; //2.4 赎回到帐日期
    private List<JijinExFeeGson> jijinExFeeGsons;  //2.5 费率
    private List<JijinExNetValueGson> jijinExNetValueGsons; //2.6 历史净值列表(单品页显示最多5条)
    private List<JijinExNetValueGson> jijinExNetValueAllGsons; //2.6 历史净值全量数据
    private JijinExScopeGson jijinExScopeGson; //2.7 基金规模信息
    private List<JijinExGradeGson> jijinExGradeGsons;//2.8 基金评级列表
    private JijinExInfoGson jijinExInfoGson;//2.9 基金基本信息
    private List<JijinExDividendGson> jijinExDividendGsons; //2.10历史分红列表
    private JijinExFundTypeGson jijinExFundTypeGson; //2.11 基金分类
    private JijinExRiskGradeGson jijinExRiskGradeGson; //2.12 风险等级
    private List<JijinExMfPerformGson> jijinExMfPerformGsons;//2.13 基金净值增长率
    private List<JijinExFxPerformGson> jijinExFxPerformGsons;//2.14 指数涨跌幅
    private JijinExSellLimitGson jijinExSellLimitGson; //2.15 赎回转换起点
    /*private List<JijinExGoodSubjectGson> jijinExGoodSubjectGsons;//2.16 精选主题
    private List<JijinExHotSubjectGson> jijinExHotSubjectGsons;//2.17 人气方案
*/    private List<JijinExManagerGson> jijinExManagerGsons;   //2.18基金经理列表

    private List<JijinExYieldRateGson> jijinExYieldRate5Gsons; //2.19 货基年化收益列表(单品页显示最多5条)
    private List<JijinExYieldRateGson> jijinExYieldRateGsons;//2.19 货基七日/万分收益

    private List<JijinExAssetConfGson> assetConfs; //2.20 资产配置
    private JijinExAssetConfGson assetConf;//资产配置应该只有一条
    private List<JijinExIndustryConfGson> industryConfs; //2.21 行业配置
    private List<JijinExStockConfGson> stockConfs; //2.22 股票配置
    private List<JijinExBondConfGson> bondConfs;   //2.23 债券配置
    //private List<JijinExAnnounceGson> announces;   //2.25 基金公告
    private JijinExCharacterGson character; //2.26 基金特性

    public List<JijinExAssetConfGson> getAssetConfs() {
        return assetConfs;
    }

    public void setAssetConfs(List<JijinExAssetConfGson> assetConfs) {
        this.assetConfs = assetConfs;
    }

    public List<JijinExBuyLimitGson> getJijinExBuyLimitGsons() {
        return jijinExBuyLimitGsons;
    }

    public void setJijinExBuyLimitGsons(List<JijinExBuyLimitGson> jijinExBuyLimitGsons) {
        this.jijinExBuyLimitGsons = jijinExBuyLimitGsons;
    }

    public List<JijinExDiscountGson> getJijinExDiscountGsons() {
        return jijinExDiscountGsons;
    }

    public void setJijinExDiscountGsons(List<JijinExDiscountGson> jijinExDiscountGsons) {
        this.jijinExDiscountGsons = jijinExDiscountGsons;
    }

    public JijinExIncomeModeGson getJijinExIncomeModeGson() {
        return jijinExIncomeModeGson;
    }

    public void setJijinExIncomeModeGson(JijinExIncomeModeGson jijinExIncomeModeGson) {
        this.jijinExIncomeModeGson = jijinExIncomeModeGson;
    }

    public JijinExSellDayCountGson getJijinExSellDayCountGson() {
        return jijinExSellDayCountGson;
    }

    public void setJijinExSellDayCountGson(JijinExSellDayCountGson jijinExSellDayCountGson) {
        this.jijinExSellDayCountGson = jijinExSellDayCountGson;
    }

    public List<JijinExFeeGson> getJijinExFeeGsons() {
        return jijinExFeeGsons;
    }

    public void setJijinExFeeGsons(List<JijinExFeeGson> jijinExFeeGsons) {
        this.jijinExFeeGsons = jijinExFeeGsons;
    }


    public JijinExScopeGson getJijinExScopeGson() {
        return jijinExScopeGson;
    }

    public void setJijinExScopeGson(JijinExScopeGson jijinExScopeGson) {
        this.jijinExScopeGson = jijinExScopeGson;
    }

    public List<JijinExGradeGson> getJijinExGradeGsons() {
        return jijinExGradeGsons;
    }

    public void setJijinExGradeGsons(List<JijinExGradeGson> jijinExGradeGsons) {
        this.jijinExGradeGsons = jijinExGradeGsons;
    }

    public JijinExInfoGson getJijinExInfoGson() {
        return jijinExInfoGson;
    }

    public void setJijinExInfoGson(JijinExInfoGson jijinExInfoGson) {
        this.jijinExInfoGson = jijinExInfoGson;
    }

    public List<JijinExDividendGson> getJijinExDividendGsons() {
        return jijinExDividendGsons;
    }

    public void setJijinExDividendGsons(List<JijinExDividendGson> jijinExDividendGsons) {
        this.jijinExDividendGsons = jijinExDividendGsons;
    }

    public JijinExFundTypeGson getJijinExFundTypeGson() {
        return jijinExFundTypeGson;
    }

    public void setJijinExFundTypeGson(JijinExFundTypeGson jijinExFundTypeGson) {
        this.jijinExFundTypeGson = jijinExFundTypeGson;
    }

    public JijinExRiskGradeGson getJijinExRiskGradeGson() {
        return jijinExRiskGradeGson;
    }

    public void setJijinExRiskGradeGson(JijinExRiskGradeGson jijinExRiskGradeGson) {
        this.jijinExRiskGradeGson = jijinExRiskGradeGson;
    }

    public List<JijinExMfPerformGson> getJijinExMfPerformGsons() {
        return jijinExMfPerformGsons;
    }

    public void setJijinExMfPerformGsons(List<JijinExMfPerformGson> jijinExMfPerformGsons) {
        this.jijinExMfPerformGsons = jijinExMfPerformGsons;
    }

    public List<JijinExFxPerformGson> getJijinExFxPerformGsons() {
        return jijinExFxPerformGsons;
    }

    public void setJijinExFxPerformGsons(List<JijinExFxPerformGson> jijinExFxPerformGsons) {
        this.jijinExFxPerformGsons = jijinExFxPerformGsons;
    }

    public JijinExSellLimitGson getJijinExSellLimitGson() {
        return jijinExSellLimitGson;
    }

    public void setJijinExSellLimitGson(JijinExSellLimitGson jijinExSellLimitGson) {
        this.jijinExSellLimitGson = jijinExSellLimitGson;
    }

  /*  public List<JijinExGoodSubjectGson> getJijinExGoodSubjectGsons() {
        return jijinExGoodSubjectGsons;
    }

    public void setJijinExGoodSubjectGsons(List<JijinExGoodSubjectGson> jijinExGoodSubjectGsons) {
        this.jijinExGoodSubjectGsons = jijinExGoodSubjectGsons;
    }

    public List<JijinExHotSubjectGson> getJijinExHotSubjectGsons() {
        return jijinExHotSubjectGsons;
    }

    public void setJijinExHotSubjectGsons(List<JijinExHotSubjectGson> jijinExHotSubjectGsons) {
        this.jijinExHotSubjectGsons = jijinExHotSubjectGsons;
    }*/

    public List<JijinExManagerGson> getJijinExManagerGsons() {
        return jijinExManagerGsons;
    }

    public void setJijinExManagerGsons(List<JijinExManagerGson> jijinExManagerGsons) {
        this.jijinExManagerGsons = jijinExManagerGsons;
    }

    public List<JijinExNetValueGson> getJijinExNetValueGsons() {
        return jijinExNetValueGsons;
    }

    public void setJijinExNetValueGsons(List<JijinExNetValueGson> jijinExNetValueGsons) {
        this.jijinExNetValueGsons = jijinExNetValueGsons;
    }

    public List<JijinExNetValueGson> getJijinExNetValueAllGsons() {
        return jijinExNetValueAllGsons;
    }

    public void setJijinExNetValueAllGsons(List<JijinExNetValueGson> jijinExNetValueAllGsons) {
        this.jijinExNetValueAllGsons = jijinExNetValueAllGsons;
    }

    public List<JijinExYieldRateGson> getJijinExYieldRateGsons() {
        return jijinExYieldRateGsons;
    }

    public void setJijinExYieldRateGsons(List<JijinExYieldRateGson> jijinExYieldRateGsons) {
        this.jijinExYieldRateGsons = jijinExYieldRateGsons;
    }

    public List<JijinExYieldRateGson> getJijinExYieldRate5Gsons() {
        return jijinExYieldRate5Gsons;
    }

    public void setJijinExYieldRate5Gsons(List<JijinExYieldRateGson> jijinExYieldRate5Gsons) {
        this.jijinExYieldRate5Gsons = jijinExYieldRate5Gsons;
    }

    public JijinExCharacterGson getCharacter() {
        return character;
    }

    public void setCharacter(JijinExCharacterGson character) {
        this.character = character;
    }

    public List<JijinExIndustryConfGson> getIndustryConfs() {
        return industryConfs;
    }

    public void setIndustryConfs(List<JijinExIndustryConfGson> industryConfs) {
        this.industryConfs = industryConfs;
    }

    public List<JijinExStockConfGson> getStockConfs() {
        return stockConfs;
    }

    public void setStockConfs(List<JijinExStockConfGson> stockConfs) {
        this.stockConfs = stockConfs;
    }

    public List<JijinExBondConfGson> getBondConfs() {
        return bondConfs;
    }

    public void setBondConfs(List<JijinExBondConfGson> bondConfs) {
        this.bondConfs = bondConfs;
    }


	public JijinExAssetConfGson getAssetConf() {
		return assetConf;
	}

	public void setAssetConf(JijinExAssetConfGson assetConf) {
		this.assetConf = assetConf;
	}
/*
	public List<JijinExAnnounceGson> getAnnounces() {
		return announces;
	}

	public void setAnnounces(List<JijinExAnnounceGson> announces) {
		this.announces = announces;
	}*/

}
