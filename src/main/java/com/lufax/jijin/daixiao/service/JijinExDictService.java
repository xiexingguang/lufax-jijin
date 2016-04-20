package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.daixiao.constant.*;
import com.lufax.jijin.daixiao.dto.*;
import com.lufax.jijin.daixiao.gson.JijinExDiscountGson;
import com.lufax.jijin.daixiao.gson.JijinExFeeGson;
import com.lufax.jijin.daixiao.gson.JijinProductFeeGson;
import com.lufax.jijin.daixiao.gson.JijinProductGson;
import com.lufax.jijin.daixiao.repository.*;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.FundType;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.mq.client.util.MapUtils;
import com.site.lookup.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JijinExDictService {

    @Autowired
    private JijinExBuyLimitRepository jijinExBuyLimitRepository;
    @Autowired
    private JijinExDiscountRepository jijinExDiscountRepository;
    @Autowired
    private JijinExFeeRepository jijinExFeeRepository;
    @Autowired
    private JijinExFundTypeRepository jijinExFundTypeRepository;
    @Autowired
    private JijinExGradeRepository jijinExGradeRepository;
    @Autowired
    private JijinExIncomeModeRepository jijinExIncomeModeRepository;
    @Autowired
    private JijinExInfoRepository jijinExInfoRepository;
    @Autowired
    private JijinExManagerRepository jijinExManagerRepository;
    @Autowired
    private JijinExRiskGradeRepository jijinExRiskGradeRepository;
    @Autowired
    private JijinExSellDayCountRepository jijinExSellDayCountRepository;
    @Autowired
    private JijinExSellLimitRepository jijinExSellLimitRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinExScopeRepository jijinExScopeRepository;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;
    @Autowired
    private JijinExDictRepository jijinExDictRepository;
    @Autowired
    private BizParametersRepository bizParametersRepository;
    @Autowired
    private JijinExMfPerformRepository jijinExMfPerformRepository;
    @Autowired
    private JijinExNetValueRepository jijinExNetValueRepository;
    @Autowired
    private JijinExDictTransactionalService jijinExDictTransactionalService;
    @Autowired
    private JijinExBuyLimitService jijinExBuyLimitService;
    @Autowired
    private JijinExYieldRateRepository jijinExYieldRateRepository;


    public void handExDict(JijinExDictDTO item) {
        JijinInfoDTO jijinInfo = jijinInfoRepository.findJijinInfoByFundCode(item.getFundCode());
        if (null != jijinInfo) {
            //已产生的基金不再生成了
            jijinExDictRepository.updateJijinExDict(MapUtils.buildKeyValueMap("id", item.getId(), "status", RecordStatus.DISPACHED.name()));
            return;
        }
        //代销名录产品上架是否需要购买限额数据
        String fundCode = item.getFundCode();
        StringBuilder errMsg = new StringBuilder("");
        //基金类型
        JijinExFundTypeDTO fundType = jijinExFundTypeRepository.getLastestJijinExFundTypeByFundCode(fundCode);
        if (null == fundType) {
        	errMsg.append("无基金类型数据，BUS_JIJIN_EX_FUND_TYPE;");
        }
        List<JijinExFeeDTO> feeList = new ArrayList<JijinExFeeDTO>();
        //认购费率
        Long subFeeBatchId = jijinExFeeRepository.getLatestBatchIdByFundCodeAndType(fundCode, JijinExFeeTypeEnum.认购费率.getFeeTypeCode());
        if (null != subFeeBatchId) {
            //拿最新批次的认购数据,
            List<JijinExFeeDTO> subfeeList = jijinExFeeRepository.getJijinExFeeByBatchIdAndFundCodeAndBizCode(subFeeBatchId, fundCode, JijinExFeeTypeEnum.认购费率.getFeeTypeCode());
            if (!EmptyChecker.isEmpty(subfeeList)) {
                feeList.addAll(subfeeList);
            }
        }else{
        	//货基需要补充费率数据
        	if(null != fundType && JijinExFundTypeEnum.货币与理财型.getTypeCode().equals(fundType.getFundType())){
        		JijinExFeeDTO currenceFee = this.createFee(fundCode, JijinExFeeTypeEnum.认购费率.getFeeTypeCode());
        		currenceFee = jijinExFeeRepository.insertJijinExFee(currenceFee);
        		feeList.add(currenceFee);
        	}
        }
        //申购
        Long purFeeBatchId = jijinExFeeRepository.getLatestBatchIdByFundCodeAndType(fundCode, JijinExFeeTypeEnum.申购费率.getFeeTypeCode());
        if (null != purFeeBatchId) {
            //拿最新批次的申购数据,不关注isValid字段
            List<JijinExFeeDTO> purfeeList = jijinExFeeRepository.getJijinExFeeByBatchIdAndFundCodeAndBizCode(purFeeBatchId, fundCode, JijinExFeeTypeEnum.申购费率.getFeeTypeCode());
            if (!EmptyChecker.isEmpty(purfeeList)) {
                feeList.addAll(purfeeList);
            }
        } else {
        	//货基需要补充费率数据
        	if(null != fundType && JijinExFundTypeEnum.货币与理财型.getTypeCode().equals(fundType.getFundType())){
        		JijinExFeeDTO currenceFee = this.createFee(fundCode, JijinExFeeTypeEnum.申购费率.getFeeTypeCode());
        		currenceFee = jijinExFeeRepository.insertJijinExFee(currenceFee);
        		feeList.add(currenceFee);
        	}else{
        		//必须要有申购费率
            	errMsg.append("无申购费率数据,BUS_JIJIN_EX_FEE;");
        	}
        }
        //赎回费率(非必须)
        Long redBatchId = jijinExFeeRepository.getLatestBatchIdByFundCodeAndType(fundCode, JijinExFeeTypeEnum.赎回费率.getFeeTypeCode());
        if (!EmptyChecker.isEmpty(redBatchId)) {
            List<JijinExFeeDTO> redfeeList = jijinExFeeRepository.getJijinExFeeByBatchIdAndFundCodeAndBizCode(redBatchId, fundCode, JijinExFeeTypeEnum.赎回费率.getFeeTypeCode());
            if (!EmptyChecker.isEmpty(redfeeList)) {
                feeList.addAll(redfeeList);
            }
        } else {
        	//货基需要补充费率数据
        	if(null != fundType && JijinExFundTypeEnum.货币与理财型.getTypeCode().equals(fundType.getFundType())){
        		JijinExFeeDTO currenceFee = this.createFee(fundCode, JijinExFeeTypeEnum.赎回费率.getFeeTypeCode());
        		currenceFee = jijinExFeeRepository.insertJijinExFee(currenceFee);
        		feeList.add(currenceFee);
        	}else{
        		//必须要有赎回费率
            	errMsg.append("无赎回费率数据,BUS_JIJIN_EX_FEE;");
        	}
        }
        //费率折扣 一批多条
        List<JijinExDiscountDTO> discountList = new ArrayList<JijinExDiscountDTO>();
        //认购折扣
        Long subDiscountBatchId = jijinExDiscountRepository.getLatestBatchIdByFundCodeAndType(fundCode, JijinExFeeTypeEnum.认购费率.getFeeTypeCode());
        if (null != subDiscountBatchId) {
            List<JijinExDiscountDTO> subDiscountList = jijinExDiscountRepository.getJijinExDiscountsByBatchIdFundCodeAndType(subDiscountBatchId, fundCode, JijinExFeeTypeEnum.认购费率.getFeeTypeCode());
            discountList.addAll(subDiscountList);
        }
        //申购折扣
        Long purDiscountBatchId = jijinExDiscountRepository.getLatestBatchIdByFundCodeAndType(fundCode, JijinExFeeTypeEnum.申购费率.getFeeTypeCode());
        if (null != purDiscountBatchId) {
            List<JijinExDiscountDTO> purDiscountList = jijinExDiscountRepository.getJijinExDiscountsByBatchIdFundCodeAndType(purDiscountBatchId, fundCode, JijinExFeeTypeEnum.申购费率.getFeeTypeCode());
            discountList.addAll(purDiscountList);
        }
        //赎回费率折扣
        Long redDiscountBatchId = jijinExDiscountRepository.getLatestBatchIdByFundCodeAndType(fundCode, JijinExFeeTypeEnum.赎回费率.getFeeTypeCode());
        if (null != redDiscountBatchId) {
            List<JijinExDiscountDTO> redDiscountList = jijinExDiscountRepository.getJijinExDiscountsByBatchIdFundCodeAndType(redDiscountBatchId, fundCode, JijinExFeeTypeEnum.赎回费率.getFeeTypeCode());
            discountList.addAll(redDiscountList);
        }
        //评级（可选数据，非必须） 一批多条
        List<JijinExGradeDTO> gradeList = new ArrayList<JijinExGradeDTO>();
        JijinExGradeDTO shGrade = jijinExGradeRepository.getLatestJijinExGradeByFundCodeAndRatingGagency(fundCode, RatingGagencyEnum.上海证券.getGagencyCode(), "3",null);
        if (null != shGrade) {
            gradeList.add(shGrade);
        }
        JijinExGradeDTO htGrade = jijinExGradeRepository.getLatestJijinExGradeByFundCodeAndRatingGagency(fundCode, RatingGagencyEnum.海通证券.getGagencyCode(), "3",null);
        if (null != htGrade) {
            gradeList.add(htGrade);
        }
        JijinExGradeDTO yhGrade = jijinExGradeRepository.getLatestJijinExGradeByFundCodeAndRatingGagency(fundCode, RatingGagencyEnum.银河.getGagencyCode(), "3",null);
        if (null != yhGrade) {
            gradeList.add(yhGrade);
        }
        //分红方式
        JijinExIncomeModeDTO dividendType = null;
        if(null != fundType && JijinExFundTypeEnum.货币与理财型.getTypeCode().equals(fundType.getFundType())){
        	//货基默认使用 0：红利再投的分红方式
        	dividendType = new JijinExIncomeModeDTO();
        	dividendType.setIncomeMode("0");
        }else{
        	dividendType  = jijinExIncomeModeRepository.getLastestJijinExIncomeModeByFundCode(fundCode);
            if (null == dividendType) {
            	errMsg.append("无分红方式数据, bus_jijin_ex_income_mode;");
            }
        }
        
        //基金其他信息
        JijinExInfoDTO exInfo = jijinExInfoRepository.getLastestJijinExInfoByFundCode(fundCode);
        if (null == exInfo) {
        	errMsg.append("无基金其他基本信息数据，BUS_JIJIN_EX_INFO;");
        }
        //基金经理，一批多条
        Long batchId = jijinExManagerRepository.getJijinExManagerBatchIdByFundCode(fundCode);
        List<JijinExManagerDTO> managerList = new ArrayList<JijinExManagerDTO>();
        if (null == batchId) {
            errMsg.append("无基金经理信息数据，BUS_JIJIN_EX_MANAGER;");
        }else{
        	managerList = jijinExManagerRepository.getJijinExManagersByBatchIdAndFundCode(batchId, fundCode);
            if (EmptyChecker.isEmpty(managerList)) {
            	errMsg.append("无基金经理信息数据，BUS_JIJIN_EX_MANAGER;");
            }
        }
        //风险等级
        JijinExRiskGradeDTO riskGrade = jijinExRiskGradeRepository.getLastestJijinExRiskGradeByFundCode(fundCode);
        if (null == riskGrade) {
        	errMsg.append("无风险等级数据，BUS_JIJIN_EX_RISK_GRADE;");
        }
        //赎回到账日期
        JijinExSellDayCountDTO sellDay = jijinExSellDayCountRepository.getLastestJijinExSellDayCountByFundCode(fundCode);
        if (null == sellDay) {
        	errMsg.append("无赎回到账日期数据，BUS_JIJIN_EX_SELL_DAYCOUNT;");
        }
        //七日年化、万份收益 为非必填数据，因为认购期的货基没有这部数据
        JijinExYieldRateDTO yieldRate = jijinExYieldRateRepository.getLastestJijinExYieldRateByFundCode(fundCode);
        /*if(null != fundType && JijinExFundTypeEnum.货币与理财型.getTypeCode().equals(fundType.getFundType())){
        	if(null == yieldRate){
        		errMsg.append("无七日年化万份收益数据，BUS_JIJIN_EX_YIELD_RATE;");
        	}
        }*/
        //数据不全不处理
        if(null != errMsg && !EmptyChecker.isEmpty(errMsg.toString())){
        	setFailedStatus(item,errMsg.toString());
        	return;
        }
        //基金规模
        JijinExScopeDTO scope = jijinExScopeRepository.getLastestJijinExScopeByFundCode(fundCode, null);
        //申购起点限制 
        JijinExBuyLimitDTO buyLimit = jijinExBuyLimitRepository.getLatestJijinExBuyLimitByFundCodeAndBizCode(fundCode, WindBizType.PURCHASE.getCode());
        //认购起点限制
        JijinExBuyLimitDTO subLimit = jijinExBuyLimitRepository.getLatestJijinExBuyLimitByFundCodeAndBizCode(fundCode, WindBizType.APPLY.getCode());
        //基金定投起点限制
        JijinExBuyLimitDTO autoInvestmentLimit = jijinExBuyLimitRepository.getLatestJijinExBuyLimitByFundCodeAndBizCode(fundCode, WindBizType.AUTOMATIC_INVESTMENT.getCode());
        //净值增长率
        JijinExMfPerformDTO exMf = jijinExMfPerformRepository.getLastJijinExMfPerformDTOByFundCode(fundCode);
        //净值
        JijinExNetValueDTO netValue = null;
        if(null != fundType && JijinExFundTypeEnum.货币与理财型.getTypeCode().equals(fundType.getFundType())){
        	//货基固定交净值为1
        	netValue = new JijinExNetValueDTO();
        	netValue.setEndDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        	netValue.setNetValue(new BigDecimal("1"));
        }else{
        	netValue = jijinExNetValueRepository.getLastJijinExNetValueByFundCode(fundCode);
        }
        
        
        JijinInfoDTO info = new JijinInfoDTO();
        //生成productCode
        try {
            String productCode = jijinDaixiaoInfoService.createProductCode();
            info.setProductCode(productCode);
        } catch (Exception e) {
            Logger.error(JijinExDictService.class, String.format("jijin daixiao get productCode error, fundCode=[%s] ", fundCode),e);
            setFailedStatus(item, "生成productCode失败");
        }
        JijinProductGson product = setValue(info, feeList, discountList, fundType, gradeList, dividendType, exInfo, managerList, riskGrade,
        		sellDay, buyLimit, scope, exMf, netValue,yieldRate,subLimit,autoInvestmentLimit);
        Logger.info(this, String.format("add jijin info [%s]", new Gson().toJson(info)));
        try {
            jijinExDictTransactionalService.addJijinInfoAndProduct(item, fundCode, info, product);
        } catch (Exception e) {
            //新增基金失败,修改名录状态
            Logger.error(this, "处理代销名录错误", e);
            setFailedStatus(item, e.getMessage());
        }
    }

    /**
     * 填充JijinInfoDTO和JijinProductGson
     *
     * @param dto
     * @param feeList
     * @param discountList
     * @param fundType
     * @param gradeList
     * @param dividendType
     * @param exInfo
     * @param managerList
     * @param risk
     * @param sellDay
     * @param buyLimit
     * @param scope
     * @param exMf
     * @param netValue	
     * @param subLimit  认购起点
     * @param autoInvestmentLimit 基金定投起点
     * @return
     */
    private JijinProductGson setValue(JijinInfoDTO dto, List<JijinExFeeDTO> feeList, List<JijinExDiscountDTO> discountList, JijinExFundTypeDTO fundType,
                                      List<JijinExGradeDTO> gradeList, JijinExIncomeModeDTO dividendType, JijinExInfoDTO exInfo, List<JijinExManagerDTO> managerList,
                                      JijinExRiskGradeDTO risk, JijinExSellDayCountDTO sellDay, JijinExBuyLimitDTO buyLimit, JijinExScopeDTO scope, JijinExMfPerformDTO exMf, JijinExNetValueDTO netValue,
                                      JijinExYieldRateDTO yieldRate,JijinExBuyLimitDTO subLimit,JijinExBuyLimitDTO autoInvestmentLimit) {
        dto.setAppliedAmount("999999999999");
        //dto.setBuyFeeRateDesc();
        dto.setBuyStatus("PUR_OPEN");
        dto.setChargeType("A");
        dto.setCollectionMode("6");
        dto.setCreatedAt(new Date());
        dto.setCreatedBy("SYS");
        dto.setDividendType(dividendType.getIncomeMode());//0：红利转投　1：现金分红　
        if(!StringUtils.isEmpty(exInfo.getSetupDate())){
        	dto.setEstablishedDate(DateUtils.parseDate(exInfo.getSetupDate(), DateUtils.DATE_STRING_FORMAT));
        }
        dto.setFundBrand(exInfo.getManagementComp());//基金品牌实际为管理人
        dto.setFundCode(exInfo.getFundCode());
        //拼接基金经理名称
        StringBuffer sb = new StringBuffer("");
        if (!EmptyChecker.isEmpty(managerList)) {
            for (JijinExManagerDTO manger : managerList) {
                sb.append(manger.getManager()).append(",");
            }
            String manager = sb.substring(0, sb.length() - 1);
            dto.setFundManager(manager);
        }

        dto.setFundName(exInfo.getName());//传简称 @槐远0820
        dto.setFundOpeningType("1");//运作期限
        dto.setFundType(JijinExFundTypeEnum.getByFundCode(fundType.getFundType()).getTypeName());
        dto.setInstId(FundSaleCode.LFX.getInstId());
        dto.setIsFirstPublish(0);//默认为非首发
        dto.setProductCategory("802");
        dto.setRedemptionArrivalDay(sellDay.getSellConfirmDayCount().intValue());
        dto.setRedemptionStatus("RED_OPEN");
        dto.setRiskLevel(risk.getRiskGrade());
        dto.setSourceType("8");
        dto.setTrustee(exInfo.getCustodianBank());
        dto.setUpdatedAt(new Date());
        dto.setUpdatedBy("SYS");

        //申购限额补充到JijinInfoDto
        dto=jijinExBuyLimitService.addToJijinInfoDto(dto,buyLimit);

        //转换为productGson
        JijinProductGson product = new JijinProductGson(dto);
        //认购起点
        if(null != subLimit){
        	product.setMinSubAmount(subLimit.getFirstInvestMinAmount());
        }
        //定投起点
        if(null != autoInvestmentLimit){
        	product.setMinFixAmount(autoInvestmentLimit.getFirstInvestMinAmount());
        }
        if(null != scope){
        	product.setFundScale(scope.getFundShare());
        }
        product.setFundManagerName(exInfo.getManagementComp());

        //填充评级信息
        if (!EmptyChecker.isEmpty(gradeList)) {
            for (JijinExGradeDTO gd : gradeList) {
                if (null == gd) {
                    continue;
                }
                if (GradeEnum.上海证券.getCode().equals(gd.getRatingGagency())) {
                    product.setShangzhengGrade(Double.valueOf(gd.getStarLevel()).intValue());
                } else if (GradeEnum.海通证券.getCode().equals(gd.getRatingGagency())) {
                    product.setHaitongGrade(Double.valueOf(gd.getStarLevel()).intValue());
                } else if (GradeEnum.银河.getCode().equals(gd.getRatingGagency())) {
                    product.setYinheGrade(Double.valueOf(gd.getStarLevel()).intValue());
                }
            }
        }
        //填充费率折扣
        JijinProductFeeGson feeGson = new JijinProductFeeGson();
        if (!EmptyChecker.isEmpty(feeList)) {
            List<JijinExFeeGson> feeGsonList = new ArrayList<JijinExFeeGson>();
            for (JijinExFeeDTO fee : feeList) {
                if (null == fee) {
                    continue;
                }
                feeGsonList.add(new JijinExFeeGson(fee));
            }
            feeGson.setFeeList(feeGsonList);
        }
        if (!EmptyChecker.isEmpty(discountList)) {
            List<JijinExDiscountGson> discountGsonList = new ArrayList<JijinExDiscountGson>();
            for (JijinExDiscountDTO discount : discountList) {
                if (null == discount) {
                    continue;
                }
                discountGsonList.add(new JijinExDiscountGson(discount));
            }
            feeGson.setDiscountList(discountGsonList);
        }
        product.setProductFee(feeGson);
        //填充净值增长率
        if (null != exMf) {
            if (null != exMf.getBenefitDaily()) {
                product.setDayIncrease(exMf.getBenefitDaily().divide(new BigDecimal("100")).toString());
            }
            if(null != exMf.getBenefitOneWeek()){
            	product.setOneWeekIncrease(exMf.getBenefitOneWeek().divide(new BigDecimal("100")));
            }
            if (null != exMf.getBenefitOneMonth()) {
                product.setOneMonIncrease(exMf.getBenefitOneMonth().divide(new BigDecimal("100")));
            }
            if (null != exMf.getBenefitThreeMonth()) {
                product.setThreeMonIncrease(exMf.getBenefitThreeMonth().divide(new BigDecimal("100")));
            }
            if (null != exMf.getBenefitSixMonth()) {
                product.setSixMonIncrease(exMf.getBenefitSixMonth().divide(new BigDecimal("100")));
            }
            if (null != exMf.getBenefitOneYear()) {
                product.setTwelveMonIncrease(exMf.getBenefitOneYear().divide(new BigDecimal("100")));
            }
            if (null != exMf.getBenefitThisYear()) {
                product.setMoreTwelveMonIncrease(exMf.getBenefitThisYear().divide(new BigDecimal("100")));
            }
            if (null != exMf.getBenefitTotal()) {
                product.setTotalIncrease(exMf.getBenefitTotal().divide(new BigDecimal("100")));
            }
            product.setIncreaseUpdateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.parseDate(exMf.getPerformanceDay(), DateUtils.DATE_STRING_FORMAT)));
        }
        if (null != netValue) {
            product.setUnitPriceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.parseDate(netValue.getEndDate(), DateUtils.DATE_STRING_FORMAT)));
            product.setUnitPrice2(netValue.getNetValue());
        }

        if(null != yieldRate){
        	product.setBenefitPerTenThousand(yieldRate.getBenefitPerTenthousand());
        	if(null != yieldRate.getInterestratePerSevenday()){
        		//跟净值增长率一样，货基的年化收益率展示时＊100加％；所以传过去要除掉100
        		product.setInterestRatePerSevenDay(yieldRate.getInterestratePerSevenday().divide(new BigDecimal("100")));
        	}
        	product.setProfitEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.parseDate(yieldRate.getEndDate(), DateUtils.DATE_STRING_FORMAT)));
        }
        //设置渠道
        String salesChannel = bizParametersRepository.findValueByCode(ConstantsHelper.JIJIN_DAIXIAO_SALE_CHANNEL);
        salesChannel = (salesChannel == null) ? "0" : salesChannel;
        product.setSalesChannel(salesChannel);
        //设置特定人组
        String groups = bizParametersRepository.findValueByCode(ConstantsHelper.JIJIN_DAIXIAO_USER_GROUP);
        if (StringUtils.isEmpty(groups)) {
            product.setProductUserGroup(new String[]{"DEFAULT"});
        } else {
            product.setProductUserGroup(groups.split(","));
        }
        return product;
    }


    /**
     * 在未达到可得试次数之前，不修改状态
     *
     * @param item
     */
    public void setFailedStatus(JijinExDictDTO item, String errMsg) {
        if (null == item.getReTryTime()) {
            item.setReTryTime(0);
        }
        if (StringUtils.isNotEmpty(errMsg) && errMsg.length() > 500) {
            errMsg = errMsg.substring(0, 500);
        }
        if (item.getReTryTime() >= 10) {
            jijinExDictRepository.updateJijinExDict(MapUtils.buildKeyValueMap("id", item.getId(), "status", RecordStatus.FAILED.name(), "errMsg", errMsg));
        } else {
            jijinExDictRepository.updateJijinExDict(MapUtils.buildKeyValueMap("id", item.getId(), "reTryTime", item.getReTryTime() + 1, "errMsg", errMsg));
        }
    }

    public void setSuccess(JijinExDictDTO item) {
        Logger.info(JijinExDictService.class, String.format("create daixiao jijin success fundCode=[%s]", item.getFundCode()));
        jijinExDictRepository.updateJijinExDict(MapUtils.buildKeyValueMap("id", item.getId(), "status", RecordStatus.DISPACHED.name(),"errMsg","SUCCESS"));
    }
    
    /**
     * 构造费率
     * @param fundCode
     * @param feeType
     * @return
     */
    public JijinExFeeDTO createFee(String fundCode,String feeType){
    	JijinExFeeDTO fee = new JijinExFeeDTO();
    	fee.setBatchId(0L);
    	fee.setChangeDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
    	fee.setChargeType("前端");
    	fee.setCreatedAt(new Date());
    	fee.setCreatedBy("SYS");
    	//fee.setErrMsg("");
    	fee.setFee(new BigDecimal("0"));
    	//fee.setFeeMemo("");
    	fee.setFeeType(feeType);
    	fee.setFundCode(fundCode);
    	//fee.setHoldPeriodUnit("");
    	fee.setIsValid(1);
    	//fee.setLowerLimitAmount();
    	//fee.setLowerLimitHoldYear();
    	fee.setStatus(RecordStatus.NO_USED.name());
    	fee.setUpdatedAt(new Date());
    	fee.setUpdatedBy("SYS");
    	//fee.setUpperLimitAmount();
    	//fee.setUpperLimitHoldYear();
    	return fee;
    }
	
}
