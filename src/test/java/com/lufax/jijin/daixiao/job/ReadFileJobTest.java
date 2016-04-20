package com.lufax.jijin.daixiao.job;

import com.lufax.jijin.daixiao.schedular.tools.DxBatchJobTemplate;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static org.junit.Assert.assertEquals;

/**
 * Created by chenguang451 on 2016/1/7.
 */
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class ReadFileJobTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DxBatchJobTemplate worker;

    @Test
    @Ignore
    public void testReadAssetConf() {
        int result = worker.execute("schedular/daixiao/assetconf.avsc");
        assertEquals(1, result);
    }

    @Test
    @Ignore
    public void testReadIndustryConf() {
        int result = worker.execute("schedular/daixiao/industryconf.avsc");
        assertEquals(1, result);
    }

    @Test
    @Ignore
    public void testReadStockConf() {
        int result = worker.execute("schedular/daixiao/stockconf.avsc");
        assertEquals(1, result);
    }

    @Test
    @Ignore
    public void testReadBondConf() {
        int result = worker.execute("schedular/daixiao/bondconf.avsc");
        assertEquals(1, result);
    }


    @Test
    @Ignore
    public void testReadMaPerf() {
        int result = worker.execute("schedular/daixiao/maperf.avsc");
        assertEquals(1, result);
    }

    @Test
    @Ignore
    public void testReadAnnounce() {
        int result = worker.execute("schedular/daixiao/announce.avsc");
        assertEquals(1, result);
    }

    @Test
    @Ignore
    public void testReadCharacter() {
        int result = worker.execute("schedular/daixiao/character.avsc");
        assertEquals(1, result);
    }


}
