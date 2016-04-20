package com.lufax.jijin.fundation.schedular;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author liudong735
 * @date 2016��03��11��
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
@Ignore
public class HandlePurchaseDispatchJobTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private HandlePurchaseDispatchJob handlePurchaseDispatchJob;

    @Test
    @Rollback(false)
    public void executeTest() {
        handlePurchaseDispatchJob.execute();
    }
}
