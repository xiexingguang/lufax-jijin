package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.dto.JijinWithdrawRecordDTO;
import com.lufax.jijin.fundation.remote.gson.request.GWRedeemRequestGson;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Created by NiuZhanJun on 9/28/15.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinWithdrawRecordRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private JijinWithdrawRecordRepository jijinWithdrawRecordRepository;

    @Test
    public void testInsertJijinWithdrawRecord() throws Exception {
        JijinWithdrawRecordDTO jijinWithdrawRecordDTO = new JijinWithdrawRecordDTO();
        jijinWithdrawRecordDTO.setProductId(100l);
        jijinWithdrawRecordDTO.setTradeUserId(202l);
        jijinWithdrawRecordDTO.setTradeAccountNo("303l");
        jijinWithdrawRecordDTO.setType(1);
        jijinWithdrawRecordDTO.setOperateDate("20150928");
        jijinWithdrawRecordDTO.setRequestAmount(new BigDecimal(10));
        jijinWithdrawRecordDTO.setRemark("");
        jijinWithdrawRecordDTO.setVersion(0);
        jijinWithdrawRecordDTO.setRecordId("2222");
        jijinWithdrawRecordDTO.setStatus("INIT");
        jijinWithdrawRecordRepository.insertJijinWithdrawRecord(jijinWithdrawRecordDTO);
        assertTrue(1 == 1);
    }

    @Test
    public void testUpdateJijinWithdrawRecord() throws Exception {
        Map map = MapUtils.buildKeyValueMap("id", 4, "status", "SUCCESS", "successAmount", 20, "version", 0);
        jijinWithdrawRecordRepository.updateJijinWithdrawRecord(map);
        assertTrue(1 == 1);
    }



}