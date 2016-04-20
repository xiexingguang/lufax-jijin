package com.lufax.jijin.daixiao.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.lufax.jijin.base.dto.LoanRequestSequenceDTO;
import com.lufax.jijin.base.repository.LoanRequestSequenceRepository;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.JijinExFundTypeEnum;
import com.lufax.jijin.daixiao.constant.RatingGagencyEnum;
import com.lufax.jijin.daixiao.dto.*;
import com.lufax.jijin.daixiao.gson.*;
import com.lufax.jijin.daixiao.repository.*;
import com.lufax.jijin.fundation.constant.JijinStatus;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.exception.ListAppException;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinNetValueRepository;
import com.lufax.jijin.fundation.service.EventService;
import com.lufax.jijin.fundation.util.JijinUtils;
import com.lufax.jijin.sysFacade.service.ProdOprSvcInterfaceService;

import net.sf.cglib.beans.BeanCopier;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class JijinDaixiaoInfoService {

    private static final String DATE_FORMAT = "yyMMdd";
    public static final int LENGTH_OF_SEQUENCE = 5;
    @Autowired
    private JijinExInfoRepository jijinExInfoRepository;
    @Autowired
    private JijinExScopeRepository jijinExScopeRepository;
    @Autowired
    private JijinExGradeRepository jijinExGradeRepository;
    @Autowired
    private JijinExDividendRepository jijinExDividendRepository;
    @Autowired
    private JijinExNetValueRepository jijinExNetValueRepository;
    @Autowired
    private JijinExManagerRepository jijinExManagerRepository;
    @Autowired
    private JijinExBuyLimitRepository jijinExBuyLimitRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinExIncomeModeRepository jijinExIncomeModeRepository;
    @Autowired
    private JijinExFeeRepository jijinExFeeRepository;
    @Autowired
    private JijinExDiscountService jijinExDiscountService;
    @Autowired
    private JijinExSellDayCountRepository jijinExSellDayCountRepository;
    @Autowired
    private JijinExMfPerformRepository jijinExMfPerformRepository;
    @Autowired
    private JijinExFundTypeRepository jijinExFundTypeRepository;
    @Autowired
    private JijinExRiskGradeRepository jijinExRiskGradeRepository;
    @Autowired
    private JijinExFxPerformRepository jijinExFxPerformRepository;
    @Autowired
    private JijinExSellLimitRepository jijinExSellLimitRepository;
    @Autowired
    private JijinExGoodSubjectRepository jijinExGoodSubjectRepository;
    @Autowired
    private JijinExHotSubjectRepository jijinExHotSubjectRepository;
    @Autowired
    private LoanRequestSequenceRepository loanRequestSequenceRepository;
    @Autowired
    private ProdOprSvcInterfaceService prodOprSvcInterfaceService;
    @Autowired
    private JijinExYieldRateRepository jijinExYieldRateRepository;
    @Autowired
    private JijinExCharacterRepository characterRepository;
    @Autowired
    private JijinExAssetConfRepository assetConfRepository;
    @Autowired
    private JijinExIndustryConfRepository industryConfRepository;
    @Autowired
    private JijinExStockConfRepository stockConfRepository;
    @Autowired
    private JijinExBondConfRepository bondConfRepository;
    @Autowired
    private JijinExAnnounceRepository announceRepository;
    @Autowired
    private JijinExMaPerfRepository managerPerfRepository;
    @Autowired
    private JijinNetValueRepository jijinNetValueRepository;

    private BeanCopier characterCopier = BeanCopier.create(JijinExCharacterDTO.class, JijinExCharacterGson.class, false);
    /*    private BeanCopier assetConfCopier = BeanCopier.create(JijinExAssetConfDTO.class, JijinExAssetConfGson.class, false);
    private BeanCopier industryConfCopier = BeanCopier.create(JijinExIndustryConfDTO.class, JijinExIndustryConfGson.class, false);
    private BeanCopier stockConfCopier = BeanCopier.create(JijinExStockConfDTO.class, JijinExStockConfGson.class, false);
    private BeanCopier bondConfCopier = BeanCopier.create(JijinExBondConfDTO.class, JijinExBondConfGson.class, false);
    //private BeanCopier announceCopier = BeanCopier.create(JijinExAnnounceDTO.class, JijinExAnnounceGson.class, false);
    private BeanCopier managerPerfCopier = BeanCopier.create(JijinExMaPerfDTO.class, JijinExMaPerfGson.class, false);*/


//    @Autowired
//    private RedisCacheConfig redisCacheConfig;
//    private RedisCacheStore<String> cache;
//
//    // 在bean初始化之后创建客户端
//    @PostConstruct
//    public void initRedisClient() {
//        cache = new RedisCacheStore<String>(redisCacheConfig, String.class, null, null);
//    }
//
//    // 在bean销毁时关闭客户端
//    @PreDestroy
//    public void destroyRedisClient() {
//        try {
//            cache.shutdown();
//        } catch (CacheException e) {
//            com.lufax.jersey.utils.Logger.error(this.getClass(), e.toString());
//        }
//    }


//    public JijinDaixiaoInfoGson getJijinDaixiaoInfoGsonByProductIdWithRedis(String productId) {
//        JijinDaixiaoInfoGson jijinDaixiaoInfoGson = new JijinDaixiaoInfoGson();
//        try {
//            final String cachekey = String.format("getDaixiaoInfoWithProductId_%s", productId);
//            if (cache.get(cachekey) != null) {
//                Logger.info(this, String.format("getDaixiaoInfoWithFundCode cache (%s) not hit : %s", cachekey, cache.get(cachekey)));
//                return new Gson().fromJson(cache.get(cachekey), JijinDaixiaoInfoGson.class);
//            } else {
//                Logger.info(this, String.format("getDaixiaoInfoWithFundCode cache (%s) hit : %s", cachekey, cache.get(cachekey)));
//                jijinDaixiaoInfoGson = getJijinDaixiaoInfoGsonByProductId(productId);
//                cache.put(cachekey, new Gson().toJson(jijinDaixiaoInfoGson), 300);
//            }
//        } catch (CacheException e) {
//            Logger.info(this, String.format("getDaixiaoInfoWithFundCode cache error. %s", e.toString()));
//            jijinDaixiaoInfoGson = getJijinDaixiaoInfoGsonByProductId(productId);
//        }
//        return jijinDaixiaoInfoGson;
//    }

    /**
     * 组装基金信息提供给list-app单品页显示
     *
     * @param productId
     * @return
     */
    public JijinDaixiaoInfoGson getJijinDaixiaoInfoGsonByProductId(String productId) {

        JijinDaixiaoInfoGson jijinDaixiaoInfoGson = new JijinDaixiaoInfoGson();

        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByProductId(Long.valueOf(productId));
        if (jijinInfoDTO == null) {
            Logger.info(this, String.format("can not find jijin info with productId [%s]", productId));
            return jijinDaixiaoInfoGson;
        }
        String fundCode = jijinInfoDTO.getFundCode();
        //获取基金类型 20-认购 22-申购
        String bizCode = jijinInfoDTO.getBuyStatus().equals(JijinStatus.BUY_STATUS_SUB_OPEN) ? "20" : "22";
        //Date date = new Date();
        //String dateStr = DateUtils.formatDateAsString(date);
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusYears(1);
        String oneYearAgoDateStr = DateUtils.formatDateAsString(dateTime.toDate());

        //2.1 认申购起点限额
        List<JijinExBuyLimitGson> jijinExBuyLimitGsons = new ArrayList<JijinExBuyLimitGson>();
        Long batchId = jijinExBuyLimitRepository.getLatestBatchIdByDate(fundCode, bizCode);
        if (null != batchId) {
            List<JijinExBuyLimitDTO> jijinExBuyLimitDTOs = jijinExBuyLimitRepository.getJijinExBuyLimitByBatchIdAndFundCode(batchId, fundCode, bizCode);
            for (JijinExBuyLimitDTO jijinExBuyLimitDTO : jijinExBuyLimitDTOs) {
                jijinExBuyLimitGsons.add(new JijinExBuyLimitGson(jijinExBuyLimitDTO));
            }
        }
        Logger.info(this, String.format("get jijinExBuyLimit info is [%s]", new Gson().toJson(jijinExBuyLimitGsons)));
        jijinDaixiaoInfoGson.setJijinExBuyLimitGsons(jijinExBuyLimitGsons);

        //2.2 优惠费率
        List<JijinExDiscountGson> jijinExDiscountGsons = new ArrayList<JijinExDiscountGson>();
        List<JijinExDiscountDTO> jijinExDiscountDTOs = jijinExDiscountService.getRightExDiscounts(fundCode, bizCode);
        for (JijinExDiscountDTO jijinExDiscountDTO : jijinExDiscountDTOs) {
            jijinExDiscountGsons.add(new JijinExDiscountGson(jijinExDiscountDTO));
        }
        Logger.info(this, String.format("get jijinExDiscount info is [%s]", new Gson().toJson(jijinExDiscountGsons)));
        jijinDaixiaoInfoGson.setJijinExDiscountGsons(jijinExDiscountGsons);

        //2.3 默认分红方式
        JijinExIncomeModeGson jijinExIncomeModeGson = new JijinExIncomeModeGson();
        JijinExIncomeModeDTO jijinExIncomeModeDTO = jijinExIncomeModeRepository.getLastestJijinExIncomeModeByFundCode(fundCode);
        if (jijinExIncomeModeDTO != null) {
            jijinExIncomeModeGson = new JijinExIncomeModeGson(jijinExIncomeModeDTO);
        }
        Logger.info(this, String.format("get jijinExIncomeMode info is [%s]", new Gson().toJson(jijinExIncomeModeGson)));
        jijinDaixiaoInfoGson.setJijinExIncomeModeGson(jijinExIncomeModeGson);

        //2.4 赎回到帐日期
        JijinExSellDayCountGson jijinExSellDayCountGson = new JijinExSellDayCountGson();
        JijinExSellDayCountDTO jijinExSellDayCountDTO = jijinExSellDayCountRepository.getLastestJijinExSellDayCountByFundCode(fundCode);
        if (jijinExSellDayCountDTO != null) {
            jijinExSellDayCountGson = new JijinExSellDayCountGson(jijinExSellDayCountDTO);
        }
        Logger.info(this, String.format("get jijinExSellDayCount info is [%s]", new Gson().toJson(jijinExSellDayCountGson)));
        jijinDaixiaoInfoGson.setJijinExSellDayCountGson(jijinExSellDayCountGson);

        //2.19 货基年化收益--只有货基才有
        if (JijinUtils.isHuoji(jijinInfoDTO)) {
            Logger.info(this, String.format("fund [code=%s,id=%s] type is currency", fundCode, productId));
            List<JijinExYieldRateDTO> jijinExYieldRateDTOs = jijinExYieldRateRepository.getYieldRateDtoByFundCodeAndEndDate(fundCode, oneYearAgoDateStr);
            Map<String, JijinExYieldRateDTO> mapYieldRate = new HashMap<String, JijinExYieldRateDTO>();
            List<JijinExYieldRateGson> jijinExYieldRate5Gsons = new ArrayList<JijinExYieldRateGson>();
            List<JijinExYieldRateGson> jijinExYieldRateAllGsons = new ArrayList<JijinExYieldRateGson>();
            List<JijinExYieldRateGson> tmpJijinExYieldRateGsons = new ArrayList<JijinExYieldRateGson>();
            if (!jijinExYieldRateDTOs.isEmpty()) {
                Logger.info(this, String.format("from [%s] find jijinExYieldRateDTOs size is[%s]", oneYearAgoDateStr, jijinExYieldRateDTOs.size()));
                for (JijinExYieldRateDTO jijinExYieldRateDTO : jijinExYieldRateDTOs) {
                    if (!mapYieldRate.containsKey(jijinExYieldRateDTO.getEndDate())) {
                        mapYieldRate.put(jijinExYieldRateDTO.getEndDate(), jijinExYieldRateDTO);
                        JijinExYieldRateGson jijinExYieldRateGson = new JijinExYieldRateGson(jijinExYieldRateDTO);
                        tmpJijinExYieldRateGsons.add(jijinExYieldRateGson);
                    }
                }

                int totalCount = tmpJijinExYieldRateGsons.size();
                //重新排序
                for (int i = 0; i < totalCount; i++) {
                    jijinExYieldRateAllGsons.add(tmpJijinExYieldRateGsons.get(totalCount - i - 1));
                }
            }
            Logger.info(this, String.format("get JijinExYieldRateDTO info is [%s]", new Gson().toJson(jijinExYieldRateAllGsons)));
            jijinDaixiaoInfoGson.setJijinExYieldRate5Gsons(jijinExYieldRate5Gsons);
            jijinDaixiaoInfoGson.setJijinExYieldRateGsons(jijinExYieldRateAllGsons);
        } else {
            //2.6 历史净值列表
            List<JijinExNetValueDTO> jijinExNetValueDTOs = jijinExNetValueRepository.getJijinExNetValueByFundCodeAndEndDate(fundCode, oneYearAgoDateStr);
            Map<String, JijinExNetValueDTO> netValueMap = new HashMap<String, JijinExNetValueDTO>();
            List<JijinExNetValueGson> jijinExNetValueGsons = new ArrayList<JijinExNetValueGson>();
            List<JijinExNetValueGson> tmpGsons = new ArrayList<JijinExNetValueGson>();
            List<JijinExNetValueGson> jijinExNetValueAllGsons = new ArrayList<JijinExNetValueGson>();
            if (!jijinExNetValueDTOs.isEmpty()) {
                for (JijinExNetValueDTO jijinExNetValueDTO : jijinExNetValueDTOs) {
                    if (!netValueMap.containsKey(jijinExNetValueDTO.getEndDate())) {
                        netValueMap.put(jijinExNetValueDTO.getEndDate(), jijinExNetValueDTO);
                        JijinExNetValueGson jijinExNetValueGson = new JijinExNetValueGson(jijinExNetValueDTO);
                        jijinExNetValueAllGsons.add(jijinExNetValueGson);
                        tmpGsons.add(jijinExNetValueGson);
                    }
                }

                if (tmpGsons.size() > 5) {
                    tmpGsons = tmpGsons.subList(0, 5);
                }
                for (JijinExNetValueGson jijinExNetValueGson : tmpGsons) {

                    JijinExMfPerformDTO jijinExMfPerformDTO = jijinExMfPerformRepository.findLastestJijinExMfPerformByFundCodeAndDate(jijinExNetValueGson.getFundCode(), jijinExNetValueGson.getEndDate());
                    if (jijinExMfPerformDTO != null) {
                        jijinExNetValueGson.setDayIncrease(jijinExMfPerformDTO.getBenefitDaily());
                    }
                    jijinExNetValueGsons.add(jijinExNetValueGson);
                }
            }
            //Logger.info(this, String.format("get jijinExNetValue info is [%s]", new Gson().toJson(jijinExNetValueGsons)));
            jijinDaixiaoInfoGson.setJijinExNetValueGsons(jijinExNetValueGsons);
            jijinDaixiaoInfoGson.setJijinExNetValueAllGsons(jijinExNetValueAllGsons);
        }

        //2.7 基金规模
        JijinExScopeDTO jijinExScopeDTO = jijinExScopeRepository.getLastestJijinExScopeByFundCode(fundCode, 1);
        JijinExScopeGson jijinExScopeGson = new JijinExScopeGson();
        if (jijinExScopeDTO != null) {
            jijinExScopeGson = new JijinExScopeGson(jijinExScopeDTO);
            JijinExNetValueDTO netValue = jijinExNetValueRepository.getLastestNetValuesByFundCodeAndEndDate(jijinExScopeGson.getFundCode(), jijinExScopeGson.getReportDate());
            if (netValue != null) {
                jijinExScopeGson.setNetValue(netValue.getNetValue());
            }
        }
        Logger.info(this, String.format("get jijinExFee info is [%s]", new Gson().toJson(jijinExScopeGson)));
        jijinDaixiaoInfoGson.setJijinExScopeGson(jijinExScopeGson);

        //2.8 基金评级列表, 显示 海通证券:3年 银河:3年 上海证券:3年
        JijinExGradeDTO haitong = jijinExGradeRepository.getLatestJijinExGradeByFundCodeAndRatingGagency(fundCode, RatingGagencyEnum.海通证券.getGagencyCode(), "3", 1);
        JijinExGradeDTO yinhe = jijinExGradeRepository.getLatestJijinExGradeByFundCodeAndRatingGagency(fundCode, RatingGagencyEnum.银河.getGagencyCode(), "3", 1);
        JijinExGradeDTO shanghai = jijinExGradeRepository.getLatestJijinExGradeByFundCodeAndRatingGagency(fundCode, RatingGagencyEnum.上海证券.getGagencyCode(), "3", 1);

        List<JijinExGradeGson> jijinExGradeGsons = new ArrayList<JijinExGradeGson>();
        if (haitong != null) {
            jijinExGradeGsons.add(new JijinExGradeGson(haitong));
        }
        if (yinhe != null) {
            jijinExGradeGsons.add(new JijinExGradeGson(yinhe));
        }
        if (shanghai != null) {
            jijinExGradeGsons.add(new JijinExGradeGson(shanghai));
        }

        Logger.info(this, String.format("get jijinExGrade info is [%s]", new Gson().toJson(jijinExGradeGsons)));
        jijinDaixiaoInfoGson.setJijinExGradeGsons(jijinExGradeGsons);

        //2.9 基金基本信息
        JijinExInfoDTO jijinExInfoDTO = jijinExInfoRepository.getLastestJijinExInfoByFundCode(fundCode);
        JijinExInfoGson jijinExInfoGson = new JijinExInfoGson();
        if (jijinExInfoDTO != null) {
            jijinExInfoGson = new JijinExInfoGson(jijinExInfoDTO);
        }
        Logger.info(this, String.format("get jijinExInfo is [%s]", new Gson().toJson(jijinExInfoGson)));
        jijinDaixiaoInfoGson.setJijinExInfoGson(jijinExInfoGson);

        //2.10 历史分红列表  只取最新的5条
        List<JijinExDividendDTO> jijinExDividendDTOs = jijinExDividendRepository.getJijinExDividendByFundCode(fundCode);
        List<JijinExDividendGson> jijinExDividendGsons = new ArrayList<JijinExDividendGson>();
        Map<String, JijinExDividendDTO> dividendMap = new HashMap<String, JijinExDividendDTO>();
        if (!CollectionUtils.isEmpty(jijinExDividendDTOs)) {

            for (JijinExDividendDTO jijinExDividendDTO : jijinExDividendDTOs) {
                if (!dividendMap.containsKey(jijinExDividendDTO.getDividendDate())) {
                    dividendMap.put(jijinExDividendDTO.getDividendDate(), jijinExDividendDTO);
                    jijinExDividendGsons.add(new JijinExDividendGson(jijinExDividendDTO));
                    if (jijinExDividendGsons.size() >= 5) {
                        break;
                    }
                }
            }
        }
        Logger.info(this, String.format("get jijinExDividend info is [%s]", new Gson().toJson(jijinExDividendGsons)));
        jijinDaixiaoInfoGson.setJijinExDividendGsons(jijinExDividendGsons);

        //2.11 基金分类直接通过jijinInfo拿
        JijinExFundTypeGson jijinExFundTypeGson = new JijinExFundTypeGson();
        jijinExFundTypeGson.setFundCode(fundCode);
        JijinExFundTypeEnum type = JijinExFundTypeEnum.getByFundTypeName(jijinInfoDTO.getFundType());
        if(null != type){
        	jijinExFundTypeGson.setFundType(type.name());
        }
        
        Logger.info(this, String.format("get jijinExFundType info is [%s]", new Gson().toJson(jijinExFundTypeGson)));
        jijinDaixiaoInfoGson.setJijinExFundTypeGson(jijinExFundTypeGson);

        //2.12 风险等级通过jijinInfo拿
        JijinExRiskGradeGson jijinExRiskGradeGson = new JijinExRiskGradeGson();
        jijinExRiskGradeGson.setFundCode(fundCode);
        jijinExRiskGradeGson.setRiskGrade(jijinInfoDTO.getRiskLevel());
        jijinDaixiaoInfoGson.setJijinExRiskGradeGson(jijinExRiskGradeGson);

        //2.13 基金净值增长率
        List<JijinExMfPerformGson> jijinExMfPerformGsons = new ArrayList<JijinExMfPerformGson>();

        List<JijinExMfPerformDTO> jijinExMfPerformDTOs = jijinExMfPerformRepository.getJijinExMfPerformDTOByFundCodeAndPerformanceDay(fundCode, oneYearAgoDateStr);
        Map<String, JijinExMfPerformDTO> exMfPerformMap = new HashMap<String, JijinExMfPerformDTO>();
        for (JijinExMfPerformDTO jijinExMfPerformDTO : jijinExMfPerformDTOs) {
            if (!exMfPerformMap.containsKey(jijinExMfPerformDTO.getPerformanceDay())) {
                exMfPerformMap.put(jijinExMfPerformDTO.getPerformanceDay(), jijinExMfPerformDTO);
                jijinExMfPerformGsons.add(new JijinExMfPerformGson(jijinExMfPerformDTO));
            }
        }
        //Logger.info(this, String.format("get jijinExMfPerfom info is [%s]", new Gson().toJson(jijinExMfPerformGsons)));
        jijinDaixiaoInfoGson.setJijinExMfPerformGsons(jijinExMfPerformGsons);


        //2.14 指数涨跌幅 000300-沪深 H11001-中证
        JijinExFxPerformDTO hs = jijinExFxPerformRepository.getLastestJijinExFxPerformByFindexCode("000300");
        JijinExFxPerformDTO zz = jijinExFxPerformRepository.getLastestJijinExFxPerformByFindexCode("H11001");

        List<JijinExFxPerformGson> jijinExFxPerformGsons = new ArrayList<JijinExFxPerformGson>();
        if (hs != null) {
            jijinExFxPerformGsons.add(new JijinExFxPerformGson(hs));
        }
        if (zz != null) {
            jijinExFxPerformGsons.add(new JijinExFxPerformGson(zz));
        }
        Logger.info(this, String.format("get jijinExFxPerform info is [%s]", new Gson().toJson(jijinExFxPerformGsons)));
        jijinDaixiaoInfoGson.setJijinExFxPerformGsons(jijinExFxPerformGsons);


        //2.15 赎回转换起点
        JijinExSellLimitGson jijinExSellLimitGson = new JijinExSellLimitGson();
        JijinExSellLimitDTO jijinExSellLimitDTO = jijinExSellLimitRepository.getLastestJijinExSellLimitByFundCodeAndBizCode(fundCode, bizCode);
        if (jijinExSellLimitDTO != null) {
            jijinExSellLimitGson = new JijinExSellLimitGson(jijinExSellLimitDTO);
        }
        Logger.info(this, String.format("get jijinExSellLimit info is [%s]", new Gson().toJson(jijinExSellLimitGson)));
        jijinDaixiaoInfoGson.setJijinExSellLimitGson(jijinExSellLimitGson);
        //基金经理信息
        jijinDaixiaoInfoGson.setJijinExManagerGsons(this.getManagerInfo(fundCode));
        
        //2.20 资产配置
        //jijinDaixiaoInfoGson.setAssetConfs(getAssetConf(fundCode)); 
        jijinDaixiaoInfoGson.setAssetConf(getAssertConfig(fundCode));
        //2.21 行业配置
        jijinDaixiaoInfoGson.setIndustryConfs(getIndustryConf(fundCode));
        //2.22 股票配置
        jijinDaixiaoInfoGson.setStockConfs(getStockConf(fundCode));
        //2.23 债券配置
        jijinDaixiaoInfoGson.setBondConfs(getBondConf(fundCode));
        //2.24 基金经理业绩
        //jijinDaixiaoInfoGson.setManagerPerf(getManagerPerf(fundCode));
        //2.25 基金公告 单独分页接口返回
        /*jijinDaixiaoInfoGson.setAnnounces(getAnnounce(fundCode));*/
        //2.26 基金特性
        jijinDaixiaoInfoGson.setCharacter(findCharacterInfo(fundCode));

        return jijinDaixiaoInfoGson;
    }

    /**
     * 分页查询基金公告
     * @return
     */
    public List<JijinExAnnounceGson> getAnnounceByPage(String fundCode,int pageSize,int pageNum){
    	int start = (pageNum - 1) * pageSize;
    	int end = pageSize*pageNum;
    	return announceRepository.getPageListByFundCodeAndStartEnd(fundCode, start, end);
    }
    /**
     * 统计公告数量
     * @param fundCode
     * @return
     */
    public int countExAnnounceByFundCode(String fundCode){
    	return announceRepository.countTotalNumByFundCode(fundCode);
    }

    /**
     * 生成productCode
     *
     * @return
     */
    public String createProductCode() {
        Date today = new Date();
        LoanRequestSequenceDTO loanRequestSequenceDTO = new LoanRequestSequenceDTO(today);

        long newId = loanRequestSequenceRepository.insertLoanRequestSequenceDTO(loanRequestSequenceDTO).id();

        Long maxIdBeforeToday = loanRequestSequenceRepository.findMaxIdBefore(DateUtils.startOfDay(loanRequestSequenceDTO.getCreatedAt()));
        long sequence = newId - maxIdBeforeToday;
        return String.format("%s%0" + LENGTH_OF_SEQUENCE + "d", new SimpleDateFormat(DATE_FORMAT).format(today), sequence);
    }

    /**
     * 更新product
     * 更新的key为code,不是productCode
     *
     * @param gson
     * @return
     */
    public BaseGson updateProduct(JijinUpdateGson gson) throws ListAppException {
        return prodOprSvcInterfaceService.updateProduct(gson);
    }


    /**
     * 更新精选主题或人气方案
     *
     * @param dtoList
     * @return
     */
    public BaseGson sendSubjectToProduct(List<JijinSubjectGson> dtoList) {
        Logger.info(this, "call list app sendSubject interface");
        return prodOprSvcInterfaceService.sendSubjectToProduct(dtoList);
    }

    /**
     * 更新产品费率信息
     *
     * @param dto
     * @return
     */
    public BaseGson sendFeeToProduct(JijinProductFeeGson dto) {
        Logger.info(this, "call list app sendfeeAndDiscount interface");
        Logger.info(this, "JijinProductFeeGson is" + new Gson().toJson(dto));
        return prodOprSvcInterfaceService.sendFeeAndDiscountToProduct(dto);
    }

    /**
     * 更新产品优惠费率信息
     *
     * @param dto
     * @return
     */
    public BaseGson sendDiscountToProduct(JijinProductFeeGson dto) {
        Logger.info(this, "call list app sendfeeAndDiscount interface");
        return prodOprSvcInterfaceService.sendFeeAndDiscountToProduct(dto);
    }

    public BaseGson updateJijinDaixiaoStatus(JijinUpdateStatusGson gson) {
        BaseGson rt = prodOprSvcInterfaceService.updateJijinStatus(gson);
        if ("000".equals(rt.getRetCode())) {
            //list更新成功
            Map<String, Object> condition = new HashMap<String, Object>();
            condition.put("productCode", gson.getCode());
            if ("0".equals(gson.getBeOperationType())) {
                if (!JijinStatus.isBuyStatus(gson.getBeNewStatus())) {
                    Logger.info(EventService.class, "Be call Jijin Status Change service ,param OperationType is error: " + gson.getBeOperationType());
                    return new BaseGson("基金状态枚举值错误", ResourceResponseCode.JIJIN_INFO_UPDATE_FAIL);

                }
                condition.put("buyStatus", gson.getBeNewStatus());
            } else if ("1".equals(gson.getBeOperationType())) {
                if (!JijinStatus.isRedStatus(gson.getBeNewStatus())) {
                    Logger.info(EventService.class, "Be call Jijin Status Change service,param OperationType is error: " + gson.getBeOperationType());
                    return new BaseGson("基金状态枚举值错误", ResourceResponseCode.JIJIN_INFO_UPDATE_FAIL);
                }
                condition.put("redemptionStatus", gson.getBeNewStatus());
            } else {
                return new BaseGson("operationType枚举值错误", ResourceResponseCode.JIJIN_INFO_UPDATE_FAIL);
            }
            //由productCode条件更新
            jijinInfoRepository.updateJijinStatusByProductCode(condition);
            return new BaseGson(null, ResourceResponseCode.SUCCESS);
        } else {
            return rt;
        }

    }

    private JijinExCharacterGson findCharacterInfo(String fundCode){
        
        JijinExCharacterDTO characterDto = characterRepository.queryLatestRecordByFundCode(fundCode);
        if(characterDto!=null){
        	JijinExCharacterGson characterGson = new JijinExCharacterGson();
            characterCopier.copy(characterDto,characterGson,null);
            return characterGson;
        }else{
        	return null;
        }
        
    }

    
    private JijinExAssetConfGson getAssertConfig(String fundCode){
    	JijinExAssetConfDTO dto = assetConfRepository.getLatestAssetConfByFundCode(fundCode);
    	if(null == dto){
    		return null;
    	}
    	JijinExAssetConfGson assetConf = JijinExAssetConfGson.castFromDto(dto);
        return assetConf;
    }

    private List<JijinExIndustryConfGson> getIndustryConf(String fundCode){
        List<JijinExIndustryConfGson> confs = Lists.newArrayList();

        //找到endDate 最晚值下，batchId的最大值
        JijinExIndustryConfDTO latestDto = industryConfRepository.getIndustryLatestEndDateAndBatchIdByFundCode(fundCode);
        if(latestDto!=null){
            List<JijinExIndustryConfDTO> dtos = industryConfRepository.queryLatestRecordsByFundCodeAndEndDateAndBatchId(fundCode, latestDto.getEndDate(), latestDto.getBatchId());
            if(dtos!=null){
                for(JijinExIndustryConfDTO dto : dtos){
                    if(dto!=null){
                        JijinExIndustryConfGson conf = JijinExIndustryConfGson.castFromDto(dto);
                        //industryConfCopier.copy(dto, conf, null);
                        confs.add(conf);
                    }
                }
            }
        }
        return confs;
    }

    private List<JijinExStockConfGson> getStockConf(String fundCode){
        List<JijinExStockConfGson> confs = Lists.newArrayList();

        //Long batchId = stockConfRepository.getMaxBatchIdByFundCode(fundCode);
        JijinExStockConfDTO latestDto = stockConfRepository.getStockConfLatestEndDateAndBatchIdByFundCode(fundCode);
        if(latestDto!=null){
            List<JijinExStockConfDTO> dtos = stockConfRepository.queryLatestStockConfRecordsByFundCode(fundCode, latestDto.getEndDate(), latestDto.getBatchId());
            if(dtos!=null){
                for(JijinExStockConfDTO dto : dtos){
                    JijinExStockConfGson conf = JijinExStockConfGson.castFromDTO(dto);
                    //stockConfCopier.copy(dto, conf, null);
                    confs.add(conf);
                }
            }
        }
        return confs;
    }

    private List<JijinExBondConfGson> getBondConf(String fundCode){
        List<JijinExBondConfGson> confs = Lists.newArrayList();

        //Long batchId = bondConfRepository.getMaxBatchIdByFundCode(fundCode);
        JijinExBondConfDTO latestDto = bondConfRepository.getBondConfLastEndDateAndBatchIdByFundCode(fundCode);
        
        if(latestDto!=null){
            List<JijinExBondConfDTO> dtos = bondConfRepository.getBondConfListByFundCodeAndEndDateAndBatchId(fundCode, latestDto.getEndDate(), latestDto.getBatchId());
            if(dtos!=null){
                for(JijinExBondConfDTO dto : dtos){
                    JijinExBondConfGson conf = JijinExBondConfGson.castFromDto(dto);
                    ///bondConfCopier.copy(dto, conf, null);
                    confs.add(conf);
                }
            }
        }
        return confs;
    }



    private List<JijinExManagerGson> getManagerInfo(String fundCode){
    	List<JijinExManagerGson> list = Lists.newArrayList();
    	Long managerBatchId = jijinExManagerRepository.getJijinExManagerBatchIdByFundCode(fundCode);
        if (managerBatchId != null) {
            List<JijinExManagerDTO> jijinExManagerDTOs = jijinExManagerRepository.getJijinExManagersByBatchIdAndFundCode(managerBatchId, fundCode);
            for (JijinExManagerDTO manager : jijinExManagerDTOs) {
            	JijinExManagerGson gson = new JijinExManagerGson(manager);
            	 //Long batchId = managerPerfRepository.getMaxBatchIdByManagerId(manager.getManagerId());
            	//填充基金经理业绩
            	 JijinExMaPerfDTO latestDto = managerPerfRepository.getManagerLatestPubDateAndBatchIdByManagerId(manager.getManagerId());
            	 if(null != latestDto){
            		 List<JijinExMaPerfDTO> dtolist = managerPerfRepository.queryLatestMaPerRecordByManagerIdAndPubDateAndBatchId(manager.getManagerId(), latestDto.getPubDate(), latestDto.getBatchId());
                	 List<JijinExMaPerfGson> perfGsonList = Lists.newArrayList();
                     if(dtolist!=null){
                    	 for(JijinExMaPerfDTO dto :dtolist){
                    		 JijinExMaPerfGson perf = new JijinExMaPerfGson(dto);
                             //managerPerfCopier.copy(dto, perf, null);
                             perfGsonList.add(perf);
                    	 }
                     }
                     gson.setManagerPerfs(perfGsonList);
            	 }
                 list.add(gson);
            }
        }
        return  list;
    }
    
    
    
   /* private List<JijinExMaPerfGson> getManagerPerf(String fundCode){
        List<JijinExMaPerfGson> perfs = Lists.newArrayList();
        Long managerBatchId = jijinExManagerRepository.getJijinExManagerBatchIdByFundCode(fundCode);
        if (managerBatchId != null) {
            List<JijinExManagerDTO> managers = jijinExManagerRepository.getJijinExManagersByBatchIdAndFundCode(managerBatchId, fundCode);
            for (JijinExManagerDTO manager : managers) {
            	//拿基金经理的最近的业绩数据
                Long batchId = managerPerfRepository.getMaxBatchIdByManagerId(manager.getManagerId());
                if (batchId != null) {
                    
                }
            }
        }
        return perfs;
    }*/
    
    public List<JijinYieldRateGson> getYieldByPage(String fundCode,int pageNum,int pageSize){
    	return jijinNetValueRepository.getJijinYieldRateByPageFromNetValueTable(fundCode,(pageNum-1)*pageSize,pageNum*pageSize);
    	
    }

}
