package com.lufax.jijin.daixiao.repository;

import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;


@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExRiskGradeRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
    @Test@Ignore
    public void testJijinExRiskGrade() {
    }   

}
