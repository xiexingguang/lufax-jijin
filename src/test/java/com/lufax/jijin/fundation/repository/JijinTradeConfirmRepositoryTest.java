package com.lufax.jijin.fundation.repository;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.constant.DHBizType;
import com.lufax.jijin.fundation.dto.JijinTradeConfirmDTO;

@Ignore
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinTradeConfirmRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinTradeConfirmRepository repository;

    @Test@Ignore
    public void testInsert() {
        JijinTradeConfirmDTO jijinTradeConfirmDTO = new JijinTradeConfirmDTO();
        jijinTradeConfirmDTO.setFileId(1L);
        jijinTradeConfirmDTO.setContractNo("contractNo");
        jijinTradeConfirmDTO.setApplyDate("20150923");
        jijinTradeConfirmDTO.setLufaxRequestNo("lufaxRequestNo");
        jijinTradeConfirmDTO.setAppSheetNo("appSheetNo");
        jijinTradeConfirmDTO.setBizType("01");
        jijinTradeConfirmDTO.setFundCode("000111");
        jijinTradeConfirmDTO.setConfirmDate("20150923");
        jijinTradeConfirmDTO.setAmount(new BigDecimal(100));
        jijinTradeConfirmDTO.setConfirmShare(new BigDecimal(100));
        jijinTradeConfirmDTO.setCompanyConfirmNo("companyConfirmNo");
        jijinTradeConfirmDTO.setTradeResCode("0000");
        jijinTradeConfirmDTO.setTradeResMemo("success");
        jijinTradeConfirmDTO.setStatus("NEW");
        repository.insertJijinTradeConfirm(jijinTradeConfirmDTO);
    }


    @Test@Ignore
    public void testSearch(){
        List<String> bizTypes = new ArrayList<String>();
        bizTypes.add(DHBizType.REDEEM.getCode());
        bizTypes.add(DHBizType.PURCHASE.getCode());
        List<JijinTradeConfirmDTO> list =  repository.getUnDispatchConfirms("NEW",100,bizTypes);
        System.out.println(list.size());
    }


    @Test@Ignore
    public void testUpdate(){
        List<String> bizTypes = new ArrayList<String>();
        bizTypes.add(DHBizType.REDEEM.getCode());
        bizTypes.add(DHBizType.PURCHASE.getCode());
        List<JijinTradeConfirmDTO> list =  repository.getUnDispatchConfirms("NEW",100,bizTypes);
        JijinTradeConfirmDTO jijinTradeConfirmDTO  = list.get(0);
        repository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(),"UNMATCH");
    }
}
