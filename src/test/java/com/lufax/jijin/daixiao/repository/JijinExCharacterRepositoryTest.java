package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.daixiao.dto.JijinExCharacterDTO;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by chenguang451 on 2016/1/5.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExCharacterRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    private Long batchId = 0l;
    private String status = "";
    private String fundCode = "B00001";
    private String bizCode = "20";

    @Autowired
    private JijinExCharacterRepository repository;

    //@Before
    public void setUp() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        batchId = Long.valueOf(simpleDateFormat.format(new Date()));
        status = "NEW";
    }

    @Test@Ignore
    public void testInsert(){
        JijinExCharacterDTO dto = new JijinExCharacterDTO();
        dto.setBatchId(batchId);
        dto.setFundCode(fundCode);
        dto.setIsForeign("1");
        dto.setIsHongkong("1");
        dto.setIsMacao("1");
        dto.setIsMtsRedeem("1");
        dto.setIsMtsSub("1");
        dto.setIsPlan1("1");
        dto.setIsPlan2("1");
        dto.setIsPlan3("1");
        dto.setIsPreRedeem("1");
        dto.setIsPreSub("1");
        dto.setIsRealRedeem("1");
        dto.setIsStmRedeem("1");
        dto.setIsStmSub("1");
        dto.setIsTaiwan("1");
        dto.setIsValid(1L);
        dto.setStatus(status);
        dto.setTaCode("test_ta");
        repository.insertJijinExCharacter(dto);
        assertTrue(dto.getId() > 0);
    }

/*    @Test@Ignore
    public void get(){
        Long batchId = repository.getMaxBatchIdByFundCode(fundCode);
        JijinExCharacterDTO dto = repository.getByFundCodeAndBatchId(fundCode,batchId);
        System.out.println(dto);
        assertTrue(dto.getId()>0);

        dto = repository.queryLatestRecordByFundCode(fundCode);
        System.out.println(dto);
        assertTrue(dto.getId()>0);
    }*/
    
//    @Test
//    public void insertTmpData() {
//        JijinExCharacterDTO dto = new JijinExCharacterDTO();
//        dto.setIsMacao("1");
//        dto.setFundCode("593342");
//        dto.setIsMtsSub("1");
//        dto.setIsRealRedeem("1");
//        dto.setIsPlan3("1");
//        dto.setBatchId(batchId);
//        dto.setIsHongkong("1");
//        dto.setIsForeign("1");
//        dto.setIsStmSub("1");
//        dto.setIsPlan1("1");
//        dto.setTaCode("17");
//        dto.setIsPlan2("1");
//        dto.setIsTaiwan("1");
//        dto.setIsMtsRedeem("1");
//        dto.setIsPreRedeem("1");
//        dto.setIsValid(1L);
//        dto.setIsPreSub("1");
//        dto.setIsStmRedeem("1");
//        repository.insertJijinExCharacter(dto);
//        assertTrue(dto.getId() > 0);
////        {isMacao=1, setterMap=null, status=DISPATCHED, fundCode=11464, 
//        //isMtsSub=1, isRealRedeem=1, class=class com.lufax.jijin.daixiao.dto.JijinExCharacterDTO, 
//        //isPlan3=1, batchId=52765, isHongkong=1, isForeign=1, id=1, 
//                //isStmSub=1, isPlan1=1, taCode=17, isPlan2=1, isTaiwan=1, 
//        //isMtsRedeem=1, isPreRedeem=1, isValid=1, isPreSub=1, isStmRedeem=1}
//
//        
//    }
}
