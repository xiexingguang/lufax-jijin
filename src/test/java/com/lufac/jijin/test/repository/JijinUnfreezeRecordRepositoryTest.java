package com.lufac.jijin.test.repository;

import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.dto.JijinUnFreezeRecordDTO;
import com.lufax.jijin.fundation.repository.JijinUnFreezeRecordRepository;
import com.lufax.jijin.fundation.service.SequenceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;

/**
 * Created by wuxiujun685 on 2016/3/10.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinUnfreezeRecordRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    protected SequenceService sequenceService;
    @Autowired
    protected JijinUnFreezeRecordRepository repository;

    @Test
    public void testInsert() {
        JijinUnFreezeRecordDTO jijinUnFreezeRecordDTO = new JijinUnFreezeRecordDTO();
        jijinUnFreezeRecordDTO.setAppNo(sequenceService.getSerialNumber(JijinBizType.PURCHASE.getCode()));
        jijinUnFreezeRecordDTO.setUnfreezeDate("20160310");
        jijinUnFreezeRecordDTO.setUnfreezeShare(BigDecimal.valueOf(19.2));
        jijinUnFreezeRecordDTO.setUserId(615746L);
        jijinUnFreezeRecordDTO.setUserBalanceId(123L);
        jijinUnFreezeRecordDTO.setFundCode("097584");
        jijinUnFreezeRecordDTO.setAppSheetNo("123");
        jijinUnFreezeRecordDTO.setStatus("1");
        jijinUnFreezeRecordDTO.setFreezeType(1);
        jijinUnFreezeRecordDTO.setVersion(1l);
        jijinUnFreezeRecordDTO =  repository.insertJijinUnFreezeRecord(jijinUnFreezeRecordDTO);
        System.out.println(jijinUnFreezeRecordDTO.getId());

        assert jijinUnFreezeRecordDTO != null;
    }
}
