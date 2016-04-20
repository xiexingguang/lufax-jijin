package com.lufac.jijin.test.repository;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.constant.FundType;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.mq.client.util.MapUtils;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinInfoRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JijinInfoRepository repository;

    @Test@Ignore
    public void testAddJijinInfo() {
        JijinInfoDTO dto = new JijinInfoDTO();
        ;
        dto.setFundCode(new Double(Math.random() * new Double(1000)).toString());
        dto.setInstId("yfd101");
        dto.setFundBrand("");
        dto.setFundName("");
        dto.setIsFirstPublish(1);
        dto.setFundType("");
        dto.setRiskLevel("0");
        dto.setIsBuyDailyLimit("1");
        dto.setBuyDailyLimit(new BigDecimal("50000"));
        dto.setBuyFeeRateDesc("");
        dto.setBuyFeeDiscountDesc("");
        dto.setMinInvestAmount(new BigDecimal("1000"));
        dto.setRedemptionFeeRateDesc("");
        dto.setChargeType("");
        dto.setRedemptionArrivalDay(3);
        dto.setFundOpeningType("1");
        dto.setEstablishedDate(new Date());
        dto.setDividendType("0");
        dto.setTrustee("");
        dto.setForeignId("");
        dto.setProductCategory("");
        dto.setSourceType("");
        dto.setProductCode("");
        dto.setAppliedAmount("");
        dto.setBuyStatus("");
        dto.setRedemptionStatus("");
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());
        dto.setCreatedBy("");
        dto.setUpdatedBy("");
        dto.setCollectionMode("7");
        repository.addJijinInfo(dto);
        assertNotNull(dto.getId());
    }

    @Test@Ignore
    public void testFindJijinInfo() {
        JijinInfoDTO dto = new JijinInfoDTO();
        dto.setFundCode(new Double(Math.random() * new Double(1000)).toString());
        dto.setInstId("yfd101");
        dto.setFundBrand("");
        dto.setFundName("");
        dto.setIsFirstPublish(1);
        dto.setFundType("");
        dto.setRiskLevel("0");
        dto.setIsBuyDailyLimit("1");
        dto.setBuyDailyLimit(new BigDecimal("50000"));
        dto.setBuyFeeRateDesc("");
        dto.setBuyFeeDiscountDesc("");
        dto.setMinInvestAmount(new BigDecimal("1000"));
        dto.setRedemptionFeeRateDesc("");
        dto.setChargeType("");
        dto.setRedemptionArrivalDay(3);
        dto.setFundOpeningType("1");
        dto.setEstablishedDate(new Date());
        dto.setDividendType("0");
        dto.setTrustee("");
        dto.setForeignId("");
        dto.setProductCategory("");
        dto.setSourceType("");
        dto.setProductCode("");
        dto.setAppliedAmount("");
        dto.setBuyStatus("");
        dto.setRedemptionStatus("");
        dto.setCreatedAt(new Date());
        dto.setUpdatedAt(new Date());
        dto.setCreatedBy("");
        dto.setUpdatedBy("");
        dto = repository.addJijinInfo(dto);
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("fundCode", dto.getFundCode());
        JijinInfoDTO rt = repository.findJijinInfo(condition);
        assertNotNull(rt);
        assertEquals(rt.getId(), dto.getId());
    }

    @Test@Ignore
    public void testUpdateJijinInfo() {
    	int i = repository.updateJijinInfo(MapUtils.buildKeyValueMap("fundCode","470009","productId","11122555","buyStatus","11","redemptionStatus","212312132"));
    	assertEquals(1,i);
    }

    @Test@Ignore
    public void testGet() {
        List<String> ids = repository.getDistinctInstId();
        System.out.println(ids.toString());
    }

    @Test@Ignore
    public void testUpdateJijinStatusByProductCode(){
    	int i = repository.updateJijinStatusByProductCode(MapUtils.buildKeyValueMap("productCode","11111111","buyStatus","NEW","redemptionStatus","ddddd"));
    	assertEquals(1,i);
    }
    
    @Test@Ignore
    public void testFindAll(){
    	List<JijinInfoDTO> jijins = repository.findAllJijins();
    	System.out.println(jijins.size());
    	assertTrue(jijins.size()>0);
    }
    
    @Test@Ignore
    public void testFindByType(){
    	List fundCodes = repository.findFundCodeListByFundType(FundType.CURRENCY);
    	assertNotNull(fundCodes);
    }
    
}
