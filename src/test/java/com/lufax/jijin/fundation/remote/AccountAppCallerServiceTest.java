package com.lufax.jijin.fundation.remote;

import com.google.gson.Gson;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.gson.StandardTransferParamGson;
import com.lufax.jijin.fundation.gson.StandardTransferResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by NiuZhanJun on 10/29/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
@Ignore
public class AccountAppCallerServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private AccountAppCallerService accountAppCallerService;

    @Test
    public void testPlusMoney() throws Exception {
        BaseResponseGson baseResponseGson = accountAppCallerService.plusMoney(BigDecimal.ONE, "JIJIN", 123l, "", "9999999", "test", "1111111111");
        System.out.println(new Gson().toJson(baseResponseGson));
    }

    @Test
    public void testTransferMoney() throws Exception {
        Long fromUserId = 12345l;
        Long toUserId = 12346l;
        String fromTransactionType = "EXPENSE_REDEMPTION";
        String toTransactionType = "INCOME_REDEMPTION";
        BigDecimal transferredAmount = new BigDecimal(100);
        String fromRemark = "赎回：";
        String toRemark = "赎回：";
        Long recordId = 1234567890l;
        String channelId = "JIJIN";
        //转账请求Gson
        StandardTransferParamGson standardTransferParam = new StandardTransferParamGson(fromUserId, toUserId, fromTransactionType, toTransactionType, transferredAmount, fromRemark, toRemark, recordId, channelId);

        StandardTransferResponse standardTransferResponse = accountAppCallerService.transferMoney(standardTransferParam);
        System.out.println(new Gson().toJson(standardTransferResponse));
    }
}