package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.fundation.dto.JijinTransferDTO;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinTransferRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinTransferRepository repository;

    @Test@Ignore
    public void testInsert() {
        JijinTransferDTO jijinTransferDTO = new JijinTransferDTO();
        jijinTransferDTO.setFileId(1L);
        jijinTransferDTO.setFundCode("fundCode");
        jijinTransferDTO.setRemark("remark");
        jijinTransferDTO.setStatus("status");
        jijinTransferDTO.setTransferDate("20151102");
        jijinTransferDTO.setResultFlag("resultFlag");
        jijinTransferDTO.setAppSheetNo("appSheetNo");
        jijinTransferDTO.setChannelId("channelId");
        jijinTransferDTO.setInstId("instId");
        jijinTransferDTO.setPafOrderNo("pafOrderNo");
        jijinTransferDTO.setTransferNo("transferNo");
        jijinTransferDTO.setAmount(new BigDecimal(1));
        repository.insertJijinTransfer(jijinTransferDTO);
    }
}
