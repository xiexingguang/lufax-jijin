package com.lufac.jijin.test.repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.dto.JijinFreezeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinFreezeRuleDTO;
import com.lufax.jijin.fundation.repository.JijinFreezeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinFreezeRuleRepository;
import com.lufax.jijin.fundation.service.SequenceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;

/**
 * @author liudong735
 * @date 2016年03月10日
 */

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinFreezeRecordRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected SequenceService sequenceService;
    @Autowired
    protected JijinFreezeRecordRepository repository;

    @Test
    @Rollback(true)
    public void testInsert() {
        JijinFreezeRecordDTO jijinFreezeRecordDTO = new JijinFreezeRecordDTO();
        jijinFreezeRecordDTO.setAppNo(sequenceService.getSerialNumber(JijinBizType.PURCHASE.getCode()));
        jijinFreezeRecordDTO.setAppSheetNo("123");
        jijinFreezeRecordDTO.setBuyConfirmDate("20160310");
        jijinFreezeRecordDTO.setFreezeShare(BigDecimal.valueOf(18.2));
        jijinFreezeRecordDTO.setFundCode("097584");
        jijinFreezeRecordDTO.setUnfreezeDate("20160410");
        jijinFreezeRecordDTO.setUserId(615746L);
        jijinFreezeRecordDTO.setUserBalanceId(123L);
        jijinFreezeRecordDTO.setFreezeType(1);
        jijinFreezeRecordDTO = repository.insertBusJijinFreezeRecord(jijinFreezeRecordDTO);
        System.out.println(jijinFreezeRecordDTO.getId());

        assert jijinFreezeRecordDTO != null;
    }
}
