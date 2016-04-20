package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.RatingGagencyEnum;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExGradeDTO;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExGradeRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {


    @Autowired
    protected JijinExGradeRepository repository;

    @Test@Ignore
    public void testInsert() {
        JijinExGradeDTO jijinExGradeDTO = new JijinExGradeDTO();
        jijinExGradeDTO.setFundCode("110020");
        jijinExGradeDTO.setBatchId(3L);
        jijinExGradeDTO.setStatus(RecordStatus.NEW.name());
        jijinExGradeDTO.setFundType("test");
        jijinExGradeDTO.setRateDate("20150723");
        jijinExGradeDTO.setRatingGagency("ratingGa");
        jijinExGradeDTO.setRatingInterval("ratingIn");
        jijinExGradeDTO.setStarLevel("five");
        repository.insertJijinExGrade(jijinExGradeDTO);

    }

    @Test@Ignore
    public void testUpdateJijinExGrade() {
        int a = repository.updateJijinExGrade(MapUtils.buildKeyValueMap("id", 1, "status", RecordStatus.DISPACHED.name()));
        Assert.assertTrue(a == 1);
    }

    @Test@Ignore
    public void testGetJijinExGradesByStatusAndMaxNum() {
        List<JijinExGradeDTO> list = repository.getJijinExGradesByStatusAndMaxNumAndRatingInterval(RecordStatus.DISPACHED.name(), 100,"3");
        Assert.assertTrue(list.size() > 0);
        JijinExGradeDTO dto = repository.getJijinExGradeById(1L);
        Assert.assertTrue(!dto.getCreatedBy().equals(""));

        List<JijinExGradeDTO> aList = repository.getJijinExGradeByFundCodeAndRatingInterval("590003","3", RatingGagencyEnum.银河.getGagencyCode());
        Assert.assertTrue(aList.size() > 0);

    }

    @Test@Ignore
    public void testCount(){
        Integer a = repository.countNumberOfAfterRateDate("100007","201507","3","SHANGZHENG");
        System.out.println(a);
        System.out.println(repository.updateSameFundCodeDateIntervalRecordToNotVaild(2304L,"100007","201507","3","SHANGZHENG"));
    }
}
