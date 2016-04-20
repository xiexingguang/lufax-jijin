package com.lufax.jijin.fundation.repository;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.dto.TradeDaysDTO;
import com.lufax.jijin.base.repository.ChinaKongTradeDaysRepository;


@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class ChinaKongTradeDaysRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ChinaKongTradeDaysRepository chinaKongTradeDaysRepository;

    @Test
    public void testGet() throws Exception {
    	
    	TradeDaysDTO dto = chinaKongTradeDaysRepository.getLastTradeDay(new Date());

        assertTrue(null!=dto);
    }

}