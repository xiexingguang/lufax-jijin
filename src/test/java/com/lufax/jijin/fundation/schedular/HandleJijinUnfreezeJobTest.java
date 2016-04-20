package com.lufax.jijin.fundation.schedular;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author liudong735
 * @date 2016Äê03ÔÂ11ÈÕ
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class HandleJijinUnfreezeJobTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private HandleJijinUnfreezeJob handleJijinUnfreezeJob;

    @Test
    @Rollback(false)
    public void executeTest() {
        handleJijinUnfreezeJob.execute();
    }
}
