package com.lufac.jijin.test.repository;

import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinTradeLogRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinTradeLogRepository repository;

    @Test@Ignore
    public void testInsert() {
        JijinTradeLogDTO dto = new JijinTradeLogDTO();

        dto.setUserId(1L);
        dto.setFundCode("a");
        dto.setTrxDate("20150505");
        dto.setTrxTime("20150505010101");
        dto.setTradeRecordId(1L);
        dto.setStatus("new");
        dto.setDividendType("2");
        dto.setType(TradeRecordType.PURCHASE);
        dto.setAmount(BigDecimal.ONE);
        dto.setReqShare(BigDecimal.TEN);
        repository.insertBusJijinTradeLog(dto);
    }

}
