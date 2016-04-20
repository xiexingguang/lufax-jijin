package com.lufac.jijin.test.repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordCountDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinTradeRecordRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    protected JijinTradeRecordRepository repository;

    @Test
    public void testInsert() {
            JijinTradeRecordDTO dto = new JijinTradeRecordDTO();
            dto.setStatus("SUBMIT_SUCCESS");
            dto.setUserId(1L);
            dto.setAppNo("12345456789");
            dto.setAppSheetNo("987654321");
            dto.setCancelAppNo("567891011");
            dto.setCancelAppSheetNo("87655555");
            dto.setChargeType("A");
            dto.setContractNo("LU000001");
            dto.setDividendType("1");
            dto.setFundCode("testCode");
            dto.setIsControversial("0");
            dto.setReqAmount(BigDecimal.ONE);
            dto.setReqShare(BigDecimal.TEN);
            dto.setType(TradeRecordType.REDEEM);
            dto.setTrxDate("20150923");
            dto.setTrxTime("20150923120101");
            dto.setTrxId(1l);
            dto.setFrozenCode("frozenCode");
            dto.setChannel("PAF");
            dto.setFrozenType("NORMAL");
            dto.setPayOrderNo("payOrderNo");
            dto.setPayCancelOrderNo("payCancelOrderNo");
            dto.setNotifyAppNo("notifyAppNo");
            dto.setInstId("dh103");
            dto.setIsAgreeRisk("0");
            dto.setRedeemType("0");
            dto.setRemark("remark");
            dto = repository.insertJijinTradeRecord(dto);
            
            assertTrue(dto.getId()>0);
    }

    @Test
    @Ignore
    public void testUpdate() {
        repository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", "131", "fundCode", "test222", "orderCompleteTime", "321123"));
    }

    @Test
    @Ignore
    public void testGetUnknown() {
        List<JijinTradeRecordDTO> dtos = repository.getUnknownTradeRecords(TradeRecordType.REDEEM.name(), 3);
        assertTrue(dtos.size() > 0);
    }

    @Test@Ignore
    public void testGetT0Redeem() {
        List<JijinTradeRecordDTO> dtos = repository.getT0RedeemApplyRecords(TradeRecordType.REDEEM.name(), TradeRecordStatus.SUBMIT_SUCCESS.name(), FundSaleCode.DHC.getInstId(), 100);
        System.out.println(dtos.size());

        assertTrue(1 == 1);
    }

    @Test
    @Ignore
    public void testGetByAppNo() {
        JijinTradeRecordDTO jijinTradeRecordDTO = repository.getRecordByAppSheetNoAndInstId("4321", "instId");
        System.out.println(jijinTradeRecordDTO.getAppNo());
    }

    @Test
    @Ignore
    public void testCountModifyingDividendTradeRecord() {
        int i = repository.countModifyingDividendTradeRecord(520101l, "470009");
        assertEquals(i, 1);
    }

    @Test
    @Ignore
    public void testGetRecordByTradeDay() {
        List<JijinTradeRecordDTO> dtos = repository.getRecordsByTradeDay("20150505", 1, 501, "yfd101");
        System.out.println(dtos.size());
    }

    @Test
    @Ignore
    public void testCountRecordByTradeDay() {
        JijinTradeRecordCountDTO dto = repository.countSuccessTradeRecordByTradeDay("20150505", "yfd101");
        System.out.println(dto.getTotal());
        System.out.println(dto.getAmount());
    }

    @Test
    @Ignore
    public void testGetUnreconRecords() {

        List<Long> dtos = repository.getUnreconTradeRecords("20150505", "dh103", 7304l);
        System.out.println(dtos.size());
    }

    @Test
    public void testGetRecordByTrxIdAndType() {

        JijinTradeRecordDTO dto = repository.getRecordByTrxIdAndTypeChannel(3L, "PURCHASE","PAF");
        System.out.println(dto.getId());
    }
    
    @Test
    public void testGetJijinTradeRecordDTOInfoByIds() {
    	List<JijinTradeRecordDTO> list = repository.getJijinTradeRecordDTOInfoByIds(Arrays.asList(7068L, 3L, 103L));
    	for(JijinTradeRecordDTO dto : list) {
    		System.out.println(dto.getId() + "," + dto.getChannel() + "," + dto.getProdCode());
    	}
    }

}
