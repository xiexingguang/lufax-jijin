package com.lufax.jijin.fundation.remote;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by NiuZhanJun on 11/3/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class FundAppCallerServiceTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private FundAppCallerService fundAppCallerService;

    @Test
    public void testRechargeDahua() throws Exception {
        fundAppCallerService.rechargeDahua(123l, "9999999933", BigDecimal.TEN, "122222");
        assertTrue(1 == 1);
    }
}