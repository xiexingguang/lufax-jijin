package com.lufac.jijin.test.repository;

import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.dto.JijinUnFreezeScheduleDTO;
import com.lufax.jijin.fundation.repository.JijinUnFreezeScheduleRepository;
import com.lufax.jijin.fundation.service.SequenceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;

/**
 * Created by wuxiujun685 on 2016/3/10.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinUnFreezeScheduleRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    protected SequenceService sequenceService;
    @Autowired
    protected JijinUnFreezeScheduleRepository repository;

    @Test
    public void testInsert() {
        JijinUnFreezeScheduleDTO JijinUnFreezeScheduleDTO = new JijinUnFreezeScheduleDTO();
        JijinUnFreezeScheduleDTO.setUnfreezeDate("20160310");
        JijinUnFreezeScheduleDTO.setTotalCount(16L);
        JijinUnFreezeScheduleDTO.setTotalFreezeShare(BigDecimal.valueOf(16.2));
        JijinUnFreezeScheduleDTO.setTotalUnFreezeShare(BigDecimal.valueOf(16.2));
        JijinUnFreezeScheduleDTO.setVersion(1l);
        JijinUnFreezeScheduleDTO.setTotalUnfreezeCount(20L);
        JijinUnFreezeScheduleDTO =  repository.insertJijinUnFreezeSchedule(JijinUnFreezeScheduleDTO);
        System.out.println(JijinUnFreezeScheduleDTO.getId());

        assert JijinUnFreezeScheduleDTO != null;
    }
}
