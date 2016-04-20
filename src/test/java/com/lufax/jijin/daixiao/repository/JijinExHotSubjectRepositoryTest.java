package com.lufax.jijin.daixiao.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;


@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinExHotSubjectRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
    @Test@Ignore
    public void testJijinExRiskGrade() {
    }   
}
