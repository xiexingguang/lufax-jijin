package com.lufac.jijin.test.repository;

import static org.junit.Assert.*;

import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.repository.JijinTradeSyncRepository;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinTradeSyncRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinTradeSyncRepository repository;


    @Test@Ignore
    public void testInsert() {
        JijinTradeSyncDTO dto = new JijinTradeSyncDTO();
        dto.setFileId(1L);
        dto.setContractNo("a");
        dto.setTaNo("tab");
        dto.setAppSheetNo("appSheetNo");
        dto.setLufaxRequestNo("lufaxRequestNo");
        dto.setFundCompanyCode("fundCompanyCode");
        dto.setTrxDate("2015-05-06");
        dto.setTrxTime("2015-05-06 10:00:00");
        dto.setTrxConfirmDate("2015-05-06 11:00:00");
        dto.setBusinessCode("businessCode");
        dto.setFundCode("fundCode");
        dto.setChargeType("chargeType");
        dto.setNetValue(BigDecimal.ONE);
        dto.setPurchaseAmount(BigDecimal.TEN);
        dto.setRedeemShare(BigDecimal.ZERO);
        dto.setConfirmShare(BigDecimal.ZERO);
        dto.setConfirmAmount(BigDecimal.TEN);
        dto.setDividentType("dividentType");
        dto.setTradeResCode("tradeResCode");
        dto.setTradeResMemo("tradeResMemo");
        dto.setTaConfirmSign("taConfirmSign");
        dto.setBusinessFinishFlag("businessFinishFlag");
        dto.setPurchaseFee(BigDecimal.ZERO);
        dto.setStatus("new");
        repository.insertJijinTradeSync(dto);
    }

    @Test@Ignore
    public void testBatchInsert() {
        List<String> businessCodes = new ArrayList<String>();
        businessCodes.add("businessCode");
        List<JijinTradeSyncDTO> dtos = repository.getUnDispatchRecords("new", 10, businessCodes);
        System.out.println(dtos.size());
    }
    @Test@Ignore
    public void testUpdateJijinTradeSyncStatusById(){
    	JijinTradeSyncDTO dto = new JijinTradeSyncDTO();
        dto.setFileId(1L);
        dto.setContractNo("a");
        dto.setTaNo("tab");
        dto.setAppSheetNo("appSheetNo");
        dto.setLufaxRequestNo("lufaxRequestNo");
        dto.setFundCompanyCode("fundCompanyCode");
        dto.setTrxDate("2015-05-06");
        dto.setTrxTime("2015-05-06 10:00:00");
        dto.setTrxConfirmDate("2015-05-06 11:00:00");
        dto.setBusinessCode("businessCode");
        dto.setFundCode("fundCode");
        dto.setChargeType("chargeType");
        dto.setNetValue(BigDecimal.ONE);
        dto.setPurchaseAmount(BigDecimal.TEN);
        dto.setRedeemShare(BigDecimal.ZERO);
        dto.setConfirmShare(BigDecimal.ZERO);
        dto.setConfirmAmount(BigDecimal.TEN);
        dto.setDividentType("dividentType");
        dto.setTradeResCode("tradeResCode");
        dto.setTradeResMemo("tradeResMemo");
        dto.setTaConfirmSign("taConfirmSign");
        dto.setBusinessFinishFlag("businessFinishFlag");
        dto.setPurchaseFee(BigDecimal.ZERO);
        dto.setStatus("new");
        dto = (JijinTradeSyncDTO)repository.insertJijinTradeSync(dto);
    	int rt = repository.updateJijinTradeSyncStatusById(dto.getId(), "DISPATCHED");
    	assertEquals(rt, 1);
    }

    @Test@Ignore
    public void testGet() {
        JijinTradeSyncDTO jijinTradeSyncDTO =  repository.getJijinTradeSyncByAppSheetNoAndFundCode("20150625000086","110026");
        System.out.println(jijinTradeSyncDTO.getAppSheetNo());
    }

    @Test@Ignore
    public void testUpdate(){
        JijinTradeSyncDTO dto = new JijinTradeSyncDTO();
        dto.setFileId(1L);
        dto.setContractNo("a");
        dto.setTaNo("tab");
        dto.setAppSheetNo("appSheetNo");
        dto.setLufaxRequestNo("lufaxRequestNo");
        dto.setFundCompanyCode("fundCompanyCode");
        dto.setTrxDate("2015-07-06");
        dto.setTrxTime("2015-07-06 10:00:00");
        dto.setTrxConfirmDate("2015-07-06 11:00:00");
        dto.setBusinessCode("144");
        dto.setFundCode("fundCode");
        dto.setChargeType("chargeType");
        dto.setNetValue(BigDecimal.ONE);
        dto.setPurchaseAmount(BigDecimal.TEN);
        dto.setRedeemShare(BigDecimal.ZERO);
        dto.setConfirmShare(BigDecimal.ZERO);
        dto.setConfirmAmount(BigDecimal.TEN);
        dto.setDividentType("0");
        dto.setTradeResCode("0000");
        dto.setTradeResMemo("");
        dto.setTaConfirmSign("");
        dto.setBusinessFinishFlag("");
        dto.setPurchaseFee(BigDecimal.ZERO);
        dto.setStatus("new");
        dto.setTaConfirmSign("test");
        dto = (JijinTradeSyncDTO)repository.insertJijinTradeSync(dto);
        int rt = repository.updateJijinTradeSyncStatusAndMemoById(dto.getId(), "DIS","XXX");
        assertEquals(rt, 1);
    }

}
