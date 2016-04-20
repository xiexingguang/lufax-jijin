package com.lufax.jijin.daixiao.service;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.gson.Gson;
import com.lufax.jijin.daixiao.constant.JijinExFundBizEnum;
import com.lufax.jijin.daixiao.dto.JijinExSellLimitDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.UserBalanceResultGson;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExSellLimitServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinExSellLimitService jijinExSellLimitService;

    @Test@Ignore
    public void testRecordFileSync() throws Exception {
        JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
        jijinSyncFileDTO.setId(373);
        jijinSyncFileDTO.setFileName("/home/NiuZhanJun/WorkLufax/Projects/30_jijindaixiao/sampleFiles/wind_20150806010000_15.txt");
        jijinSyncFileDTO.setCurrentLine(1l);
        jijinExSellLimitService.recordFileSync(jijinSyncFileDTO);
        assertTrue(1 == 1);
    }

    @Test@Ignore
    public void testGetLatestSellLimitDtoByFundCode() throws Exception {
        String fundCode="B00001";
        //赎回限制信息
        JijinExSellLimitDTO latestSellLimitDto = jijinExSellLimitService.getLatestSellLimitDtoByFundCode(fundCode, JijinExFundBizEnum.赎回.getBizCode());
        UserBalanceResultGson userBalanceResultGson = new UserBalanceResultGson();
        if (latestSellLimitDto != null) {
            userBalanceResultGson.setSingleRedeemMinAmount(latestSellLimitDto.getSingleSellMinAmount());
            userBalanceResultGson.setSingleRedeemMaxAmount(latestSellLimitDto.getSingleSellMaxAmount());
            userBalanceResultGson.setBizCode(JijinExFundBizEnum.赎回.getBizCode());
            userBalanceResultGson.setBizName(JijinExFundBizEnum.赎回.toString());
        } else {
            userBalanceResultGson.setSingleRedeemMinAmount(BigDecimal.ZERO);
            userBalanceResultGson.setSingleRedeemMaxAmount(new BigDecimal(Long.MAX_VALUE));
            userBalanceResultGson.setBizCode(JijinExFundBizEnum.赎回.getBizCode());
            userBalanceResultGson.setBizName(JijinExFundBizEnum.赎回.toString());
        }
        System.out.println(new Gson().toJson(userBalanceResultGson));
        assertTrue(latestSellLimitDto != null);
    }
}