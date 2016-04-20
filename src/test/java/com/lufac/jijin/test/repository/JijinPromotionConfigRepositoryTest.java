package com.lufac.jijin.test.repository;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.gson.Gson;
import com.lufax.jijin.fundation.dto.JijinPromotionConfigDTO;
import com.lufax.jijin.fundation.repository.JijinPromotionConfigRepository;

@Ignore
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinPromotionConfigRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinPromotionConfigRepository jijinPromotionConfigRepository;

    @Test
    public void testFind(){
        JijinPromotionConfigDTO jijinPromotionConfigDTO= jijinPromotionConfigRepository.findJijinPromotionConfigByFundCode("123");
        System.out.println(new Gson().toJson(jijinPromotionConfigDTO));
    }

    @Test
    public void testUpdate(){
        JijinPromotionConfigDTO jijinPromotionConfigDTO= jijinPromotionConfigRepository.findJijinPromotionConfigByFundCode("123");
        jijinPromotionConfigDTO.setStatus("NEW");
        jijinPromotionConfigDTO.setMaxAmount(new BigDecimal(10000));
        jijinPromotionConfigDTO.setEndTime("20151202190000");
        int result =  jijinPromotionConfigRepository.updateJijinPromotionConfig(jijinPromotionConfigDTO);
        System.out.println(result);
    }

}
