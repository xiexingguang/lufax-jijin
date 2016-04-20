package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.JijinExFundBizEnum;
import com.lufax.jijin.daixiao.dto.*;
import com.lufax.jijin.daixiao.gson.JijinDaixiaoInfoGson;
import com.lufax.jijin.daixiao.repository.*;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExProductServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExBuyLimitRepository jijinExBuyLimitRepository;
    @Autowired
    private JijinExDiscountRepository jijinExDiscountRepository;
    @Autowired
    private JijinExDictRepository jijinExDictRepository;
    @Autowired
    private JijinExDividendRepository jijinExDividendRepository;
    @Autowired
    private JijinExFeeRepository jijinExFeeRepository;
    @Autowired
    private JijinExFundTypeRepository jijinExFundTypeRepository;
    @Autowired
    private JijinExFxPerformRepository jijinExFxPerformRepository;
    @Autowired
    private JijinExGoodSubjectRepository jijinExGoodSubjectRepository;
    @Autowired
    private JijinExGradeRepository jijinExGradeRepository;
    @Autowired
    private JijinExHotSubjectRepository jijinExHotSubjectRepository;
    @Autowired
    private JijinExIncomeModeRepository jijinExIncomeModeRepository;
    @Autowired
    private JijinExInfoRepository jijinExInfoRepository;
    @Autowired
    private JijinExManagerRepository jijinExManagerRepository;
    @Autowired
    private JijinExMfPerformRepository jijinExMfPerformRepository;
    @Autowired
    private JijinExNetValueRepository jijinExNetValueRepository;
    @Autowired
    private JijinExRiskGradeRepository jijinExRiskGradeRepository;
    @Autowired
    private JijinExScopeRepository jijinExScopeRepository;
    @Autowired
    private JijinExSellDayCountRepository jijinExSellDayCountRepository;
    @Autowired
    private JijinExSellLimitRepository jijinExSellLimitRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;

    private String fundCode = "479977";
    private String status = "DISPATCHED";
    private Long batchId = Long.valueOf(DateUtils.formatDateAsCmsDrawSequence(new Date()));


    @Test@Ignore
    public void testCreateProduct() {
        //先确认fundCode是否正确
        //if(fundCode.equals("479977"))return;
        for (int i = 0; i < 1000; i++) {
            System.out.println("=======================start create produt " + i + "==============");
            prepare();
            boolean newDict=createDict();
            if(!newDict)continue;
            createBuyLimit();
            createDiscount();
            createFee();
            createIncomeMode();
            createSellLimit();
            createSellDayCount();
            createFundType();
            createDividend();
            createFxPerform();
            createGoodSubject();
            createGrade();
            createHotSubject();
            createInfo();
            createManager();
            createMfPerform();
            createNetValue();
            createRiskGrade();
            createScope();
            System.out.println("=======================create produt " + i + " success==============");
        }

        assertTrue(1 == 1);
    }

    public void prepare() {
        fundCode = getRandom(900000, 999999);
        batchId = jijinSyncFileRepository.getNextVal();
    }

    public boolean createDict() {
        try {
            JijinExDictDTO jijinExDictDTO = new JijinExDictDTO();
            jijinExDictDTO.setFundCode(fundCode);
            jijinExDictRepository.insertJijinExDict(jijinExDictDTO);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test@Ignore
    public void createBuyLimit() {
        JijinExBuyLimitDTO jijinExBuyLimitDTO = new JijinExBuyLimitDTO();
        jijinExBuyLimitDTO.setFundCode(fundCode);
        jijinExBuyLimitDTO.setStatus(status);
        jijinExBuyLimitDTO.setBatchId(batchId);
        jijinExBuyLimitDTO.setIsValid(1);
        jijinExBuyLimitDTO.setRaisedAmount(getRandomBD(100, 999));
        jijinExBuyLimitDTO.setAddInvestMaxAmount(getRandomBD(1000000, 9999999));
        jijinExBuyLimitDTO.setAddInvestMinAmount(getRandomBD(10, 200));
        jijinExBuyLimitDTO.setBizCode(JijinExFundBizEnum.申购.getBizCode());
        jijinExBuyLimitDTO.setFirstInvestMaxAmount(getRandomBD(1000000, 9999999));
        jijinExBuyLimitDTO.setFirstInvestMinAmount(BigDecimal.TEN);
        jijinExBuyLimitDTO.setInvestDailyLimit(BigDecimal.ZERO);
        jijinExBuyLimitDTO.setSingleInvestMaxAmount(getRandomBD(1000000, 9999999));
        jijinExBuyLimitDTO.setSingleInvestMinAmount(getRandomBD(1, 10));
        jijinExBuyLimitRepository.insertJijinExBuyLimit(jijinExBuyLimitDTO);
    }

    @Test@Ignore
    public void createDiscount() {
        //申购费率
        JijinExDiscountDTO jijinExDiscountDTO = new JijinExDiscountDTO();
        jijinExDiscountDTO.setFundCode(fundCode);
        jijinExDiscountDTO.setStatus(status);
        jijinExDiscountDTO.setBatchId(batchId);
        jijinExDiscountDTO.setIsValid(1);

        jijinExDiscountDTO.setBizCode("22");
        jijinExDiscountDTO.setSetValue(BigDecimal.TEN);
        jijinExDiscountDTO.setSetType("0");
        jijinExDiscountDTO.setFixedMaxAmount(getRandomBD(1000000, 9999999));
        jijinExDiscountDTO.setFixedMinAmount(getRandomBD(1, 10));
        jijinExDiscountDTO.setDiscountMode("0");
        jijinExDiscountDTO.setFeePloy("1");
        jijinExDiscountDTO.setFixedRate(new BigDecimal(0.5));
        jijinExDiscountDTO.setValidDate("20150818");

        jijinExDiscountRepository.insertJijinExDiscount(jijinExDiscountDTO);

    }

    @Test@Ignore
    public void createFee() {
        //申购费率
        JijinExFeeDTO jijinExFeeDTO = new JijinExFeeDTO();
        jijinExFeeDTO.setFundCode(fundCode);
        jijinExFeeDTO.setStatus(status);
        jijinExFeeDTO.setBatchId(batchId);
        jijinExFeeDTO.setIsValid(1);

        jijinExFeeDTO.setHoldPeriodUnit("1");
        jijinExFeeDTO.setFeeType("22");
        jijinExFeeDTO.setChargeType("前端");
        jijinExFeeDTO.setLowerLimitAmount(getRandomBD(1, 10));
        jijinExFeeDTO.setUpperLimitAmount(getRandomBD(1000000, 9999999));
        jijinExFeeDTO.setLowerLimitHoldYear(getRandomBD(1, 9));
        jijinExFeeDTO.setUpperLimitHoldYear(BigDecimal.TEN);
        jijinExFeeDTO.setFee(new BigDecimal(50));
        jijinExFeeDTO.setFeeMemo("");
        jijinExFeeDTO.setChangeDate("20150818");
        jijinExFeeRepository.insertJijinExFee(jijinExFeeDTO);

        //==============================
        //赎回费率
        jijinExFeeDTO = new JijinExFeeDTO();
        jijinExFeeDTO.setFundCode(fundCode);
        jijinExFeeDTO.setStatus(status);
        jijinExFeeDTO.setBatchId(batchId);
        jijinExFeeDTO.setIsValid(1);

        jijinExFeeDTO.setHoldPeriodUnit("1");
        jijinExFeeDTO.setFeeType("24");
        jijinExFeeDTO.setChargeType("前端");
        jijinExFeeDTO.setLowerLimitAmount(getRandomBD(1, 10));
        jijinExFeeDTO.setUpperLimitAmount(getRandomBD(1000000, 9999999));
        jijinExFeeDTO.setLowerLimitHoldYear(getRandomBD(1, 9));
        jijinExFeeDTO.setUpperLimitHoldYear(BigDecimal.TEN);
        jijinExFeeDTO.setFee(new BigDecimal(50));
        jijinExFeeDTO.setFeeMemo("");
        jijinExFeeDTO.setChangeDate("20150818");
        jijinExFeeRepository.insertJijinExFee(jijinExFeeDTO);


    }

    @Test@Ignore
    public void createIncomeMode() {
        JijinExIncomeModeDTO jijinExIncomeModeDTO = new JijinExIncomeModeDTO();
        jijinExIncomeModeDTO.setFundCode(fundCode);
        jijinExIncomeModeDTO.setStatus(status);
        jijinExIncomeModeDTO.setBatchId(batchId);
        jijinExIncomeModeDTO.setIsValid(1);
        jijinExIncomeModeDTO.setMinHoldShareCount(getRandomBD(1, 10));
        jijinExIncomeModeDTO.setIncomeMode("1");
        jijinExIncomeModeRepository.insertJijinExIncomeMode(jijinExIncomeModeDTO);
    }

    @Test@Ignore
    public void createSellLimit() {
        JijinExSellLimitDTO jijinExSellLimitDTO = new JijinExSellLimitDTO();
        jijinExSellLimitDTO.setFundCode(fundCode);
        jijinExSellLimitDTO.setStatus(status);
        jijinExSellLimitDTO.setBatchId(batchId);
        jijinExSellLimitDTO.setIsValid(1);
        jijinExSellLimitDTO.setBizCode(JijinExFundBizEnum.赎回.getBizCode());
        jijinExSellLimitDTO.setSingleSellMinAmount(getRandomBD(1, 100));
        jijinExSellLimitDTO.setSingleSellMaxAmount(getRandomBD(1000000, 9999999));
        jijinExSellLimitRepository.insertJijinExSellLimit(jijinExSellLimitDTO);
    }

    @Test@Ignore
    public void createSellDayCount() {
        JijinExSellDayCountDTO jijinExSellDayCountDTO = new JijinExSellDayCountDTO();
        jijinExSellDayCountDTO.setFundCode(fundCode);
        jijinExSellDayCountDTO.setStatus(status);
        jijinExSellDayCountDTO.setBatchId(batchId);
        jijinExSellDayCountDTO.setIsValid(1);
        jijinExSellDayCountDTO.setSellConfirmDayCount(3l);
        jijinExSellDayCountRepository.insertJijinExSellDayCount(jijinExSellDayCountDTO);
    }

    @Test@Ignore
    public void createFundType() {
        JijinExFundTypeDTO jijinExFundTypeDTO = new JijinExFundTypeDTO();
        jijinExFundTypeDTO.setFundCode(fundCode);
        jijinExFundTypeDTO.setStatus(status);
        jijinExFundTypeDTO.setBatchId(batchId);
        jijinExFundTypeDTO.setFundType("1");
        jijinExFundTypeRepository.inserJijinExFundType(jijinExFundTypeDTO);
    }

    @Test@Ignore
    public void createDividend() {
        JijinExDividendDTO jijinExDividendDTO = new JijinExDividendDTO();
        jijinExDividendDTO.setFundCode(fundCode);
        jijinExDividendDTO.setBatchId(batchId);
        jijinExDividendDTO.setIsValid(1);
        jijinExDividendDTO.setAnnDate("20150818");
        jijinExDividendDTO.setCurrencyCode("CNY");
        jijinExDividendDTO.setDivDate("20150818");
        jijinExDividendDTO.setDivEdexDate("20150818");
        jijinExDividendDTO.setDividendDate("20150818");
        jijinExDividendDTO.setExDate("20150818");
        jijinExDividendDTO.setPerDividend(getRandomBD(10, 999).divide(new BigDecimal(100)));
        jijinExDividendDTO.setRecordDate("20150818");
        jijinExDividendRepository.insertJijinExDividend(jijinExDividendDTO);
    }

    @Test@Ignore
    public void createFxPerform() {
        //JijinExFxPerformDTO jijinExFxPerformDTO=new JijinExFxPerformDTO();
        //jijinExFxPerformRepository.insertJijinExFxPerform(jijinExFxPerformDTO);
    }

    @Test@Ignore
    public void createGoodSubject() {
        JijinExGoodSubjectDTO jijinExGoodSubjectDTO = new JijinExGoodSubjectDTO();
        jijinExGoodSubjectDTO.setFundCode(fundCode);
        jijinExGoodSubjectDTO.setStatus(status);
        jijinExGoodSubjectDTO.setBatchId(batchId);
        jijinExGoodSubjectDTO.setSubjectIndex(1l);
        jijinExGoodSubjectDTO.setSubjectName("碉堡了的基金");
        jijinExGoodSubjectRepository.insertJijinExGoodSubject(jijinExGoodSubjectDTO);
    }

    @Test@Ignore
    public void createGrade() {
        JijinExGradeDTO jijinExGradeDTO = new JijinExGradeDTO();
        jijinExGradeDTO.setFundCode(fundCode);
        jijinExGradeDTO.setStatus(status);
        jijinExGradeDTO.setBatchId(batchId);
        jijinExGradeDTO.setIsValid(1);
        jijinExGradeDTO.setFundType("三星");
        jijinExGradeDTO.setRateDate("20150101");
        jijinExGradeDTO.setStarLevel(getRandom(1, 5));
        jijinExGradeDTO.setRatingGagency("银河");
        jijinExGradeDTO.setRatingInterval(getRandom(1, 5));
        jijinExGradeRepository.insertJijinExGrade(jijinExGradeDTO);
    }

    @Test@Ignore
    public void createHotSubject() {
        JijinExHotSubjectDTO jijinExHotSubjectDTO = new JijinExHotSubjectDTO();
        jijinExHotSubjectDTO.setFundCode(fundCode);
        jijinExHotSubjectDTO.setStatus(status);
        jijinExHotSubjectDTO.setBatchId(batchId);
        jijinExHotSubjectDTO.setSubjectName("热的发烫的基金");
        jijinExHotSubjectDTO.setSubjectIndex(1l);
        jijinExHotSubjectRepository.insertJijinExHotSubject(jijinExHotSubjectDTO);
    }

    @Test@Ignore
    public void createInfo() {
        JijinExInfoDTO jijinExInfoDTO = new JijinExInfoDTO();
        jijinExInfoDTO.setFundCode(fundCode);
        jijinExInfoDTO.setStatus(status);
        jijinExInfoDTO.setBatchId(batchId);
        jijinExInfoDTO.setIsValid(1);
        jijinExInfoDTO.setBenchMark("benchMark");
        jijinExInfoDTO.setCompanyCode("comCode");
        jijinExInfoDTO.setCompanyFullName("comFullName");
        jijinExInfoDTO.setCustodianBank("bank");
        jijinExInfoDTO.setFullName("Jijin-Full-" + fundCode);
        jijinExInfoDTO.setInvestType("investType");
        jijinExInfoDTO.setManagementComp("mamCom");
        jijinExInfoDTO.setManagementFee(new BigDecimal(1));
        jijinExInfoDTO.setName("Jijin-" + fundCode);
        jijinExInfoDTO.setTrusteeFee(new BigDecimal(2));
        jijinExInfoDTO.setSetupDate("20150723");
        jijinExInfoRepository.insertJijinExInfo(jijinExInfoDTO);
    }

    @Test@Ignore
    public void createManager() {
        JijinExManagerDTO jijinExManagerDTO = new JijinExManagerDTO();
        jijinExManagerDTO.setFundCode(fundCode);
        jijinExManagerDTO.setStatus(status);
        jijinExManagerDTO.setBatchId(batchId);
        jijinExManagerDTO.setIsValid(1);
        jijinExManagerDTO.setManager("槐远");
        jijinExManagerDTO.setResume("碉堡了的基金经理");
        jijinExManagerRepository.insertJijinExManager(jijinExManagerDTO);
    }

    @Test@Ignore
    public void createMfPerform() {
        JijinExMfPerformDTO jijinExMfPerformDTO = new JijinExMfPerformDTO();
        jijinExMfPerformDTO.setFundCode(fundCode);
        jijinExMfPerformDTO.setStatus(status);
        jijinExMfPerformDTO.setBatchId(batchId);
        jijinExMfPerformDTO.setIsValid(1);

        jijinExMfPerformDTO.setPerformanceDay("20150820");
        jijinExMfPerformDTO.setBenefitDaily(getRandomRate4());
        jijinExMfPerformDTO.setBenefitOneWeek(getRandomRate4());
        jijinExMfPerformDTO.setBenefitOneMonth(getRandomRate4());
        jijinExMfPerformDTO.setBenefitSixMonth(getRandomRate4());
        jijinExMfPerformDTO.setBenefitThreeMonth(getRandomRate4());
        jijinExMfPerformDTO.setBenefitOneYear(getRandomRate4());
        jijinExMfPerformDTO.setBenefitTwoYear(getRandomRate4());
        jijinExMfPerformDTO.setBenefitThreeYear(getRandomRate4());
        jijinExMfPerformDTO.setBenefitThisYear(getRandomRate4());
        jijinExMfPerformDTO.setBenefitTotal(getRandomRate4());
        jijinExMfPerformRepository.insertJijinExMfPerfom(jijinExMfPerformDTO);
    }

    @Test@Ignore
    public void createNetValue() {
        JijinExNetValueDTO jijinExNetValueDTO = new JijinExNetValueDTO();
        jijinExNetValueDTO.setFundCode(fundCode);
        jijinExNetValueDTO.setStatus(status);
        jijinExNetValueDTO.setBatchId(batchId);
        jijinExNetValueDTO.setIsValid(1);
        jijinExNetValueDTO.setNetValue(getRandomBD(101, 500).divide(new BigDecimal(100)));
        jijinExNetValueDTO.setTotalNetValue(getRandomBD(10, 99));
        jijinExNetValueDTO.setEndDate("20150818");
        jijinExNetValueRepository.insertJijinExNetValue(jijinExNetValueDTO);
    }

    @Test@Ignore
    public void createRiskGrade() {
        JijinExRiskGradeDTO jijinExRiskGradeDTO = new JijinExRiskGradeDTO();
        jijinExRiskGradeDTO.setFundCode(fundCode);
        jijinExRiskGradeDTO.setStatus(status);
        jijinExRiskGradeDTO.setBatchId(batchId);
        jijinExRiskGradeDTO.setRiskGrade("2");
        jijinExRiskGradeRepository.insertJijinExRiskGrade(jijinExRiskGradeDTO);
    }

    @Test@Ignore
    public void createScope() {
        JijinExScopeDTO jijinExScopeDTO = new JijinExScopeDTO();
        jijinExScopeDTO.setFundCode(fundCode);
        jijinExScopeDTO.setStatus(status);
        jijinExScopeDTO.setBatchId(batchId);
        jijinExScopeDTO.setIsValid(1);
        jijinExScopeDTO.setFundShare(getRandomBD(1000000, 9999999));
        jijinExScopeDTO.setPurchaseShare(getRandomBD(1000000, 9999999));
        jijinExScopeDTO.setRedeemShare(getRandomBD(1000000, 9999999));
        jijinExScopeDTO.setStartDate("20150101");
        jijinExScopeDTO.setReportDate("20150801");
        jijinExScopeRepository.insertJijinExScope(jijinExScopeDTO);
    }

    private String getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s + "";
    }

    private BigDecimal getRandomBD(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return new BigDecimal(s);
    }

    private BigDecimal getRandomRate2() {
        Random random = new Random();
        int s = random.nextInt(99) % (99 - 1 + 1) + 1;
        return new BigDecimal(s).divide(new BigDecimal(100));
    }

    private BigDecimal getRandomRate4() {
        Random random = new Random();
        int s = random.nextInt(9999) % (9999 - 1 + 1) + 1;
        return new BigDecimal(s).divide(new BigDecimal(10000));
    }

}
