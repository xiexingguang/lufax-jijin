package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.codehaus.plexus.util.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.gson.PurchaseOrderResultGson;
import com.lufax.jijin.fundation.gson.PurchaseResultGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;
import com.lufax.jijin.fundation.service.domain.FileHolder;
import com.lufax.jijin.service.MqService;

@Ignore
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class PurchaseServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private EventService eventService;
    @Autowired
    private JijinGatewayRemoteService jijinGatewayRemoteService;
    @Autowired
    private AccountAppCallerService accountAppCallerService;
    @Autowired
    private PurchaseService purchaseService;
	@Autowired
	private JunitMockModelBuilder junitMockModelBuilder;
    @Autowired
    private MqService mqService;



    @Before
    public void setUp() {
        jijinGatewayRemoteService = mock(JijinGatewayRemoteService.class);
        accountAppCallerService = mock(AccountAppCallerService.class);
        mqService = mock(MqService.class);
        eventService.setAccountAppCallerService(accountAppCallerService);
        eventService.setJijinGatewayRemoteService(jijinGatewayRemoteService);
        purchaseService.setMqService(mqService);

		junitMockModelBuilder.buildJijinInfo("470009","yfd101","stock");
		junitMockModelBuilder.buildJijinUserBalance(1l,"470009");
		junitMockModelBuilder.buildJijinUserAccount(1l,"yfd101");
    }

    @Test
    public void testDoPurchaseSuccess() {
        GWResponseGson res = new GWResponseGson();
        res.setRetCode("000");
        PurchaseOrderResultGson gs = new PurchaseOrderResultGson();
        gs.setAppSheetSerialNo("appSheetSerialNotest");
        gs.setErrorCode("0000");
        res.setContent(JsonHelper.toJson(gs));
        when(jijinGatewayRemoteService.buy(anyString(), anyString())).thenReturn(res);
        JijinTradeRecordDTO record =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.INIT,TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);

        eventService.doPurchase(record.getId());
        
        record = junitMockModelBuilder.getJijinTradeRecord(record.getId());
        assertTrue("SUBMIT_SUCCESS".equals(record.getStatus()));
    
    }



    @Test
    public void testDoPurchaseFail() {
        GWResponseGson res = new GWResponseGson();
        res.setRetCode("000");
        PurchaseOrderResultGson gs = new PurchaseOrderResultGson();
        gs.setAppSheetSerialNo("appSheetSerialNotest");
        gs.setErrorCode("0001");
        res.setContent(JsonHelper.toJson(gs));
        BaseResponseGson baseResponseGson = new BaseResponseGson();
        baseResponseGson.setRetCode("123");
        baseResponseGson.setRetMessage("");

        when(accountAppCallerService.unfreezeFund(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(baseResponseGson);
        when(jijinGatewayRemoteService.buy(anyString(), anyString())).thenReturn(res);
        
        JijinTradeRecordDTO record =junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.INIT,TradeRecordType.PURCHASE,"470009","yfd101","20990101", 0l);
        eventService.doPurchase(record.getId());
        record = junitMockModelBuilder.getJijinTradeRecord(record.getId());
        assertTrue("FAIL".equals(record.getStatus()));

    }
    
    @Test
    public void testDahuaPurchase(){
    	
		Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mqService).sendJijinRecordSuccessMsg(Mockito.anyLong());  

		PurchaseResultGson result =purchaseService.orderPurchase("470009", "1", BigDecimal.TEN, "PURCHASE", "-12343", "2322", "1", "NORMAL","prodCode","PAF");
		
		assertTrue("00".equals(result.getRetCode()));
    }
    
    @Test
    public void testDahuaGeneratePurchaseFileWithEmpty(){
    			 
		JijinSyncFileDTO testFile =junitMockModelBuilder.buildJijinSyncFile("lfx201_testFile", SyncFileBizType.PAF_BUY_AUDIT, SyncFileStatus.READ_SUCCESS, "20150301");
		junitMockModelBuilder.buildPafBuyAudit("appSheetNo", "211", "DISPATCHED", testFile.getId(), "20150301", BigDecimal.TEN);
		junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.NOTIFY_SUCCESS, TradeRecordType.PURCHASE, "470009", "yfd101", "20150301", 0l);
		 
		FileHolder fileHolder = new FileHolder();
		fileHolder.setFileName("purchase_testFile");
		fileHolder.setFileAbsolutePath("file/");
		purchaseService.generatePurchaseFile(fileHolder, "20150301", "bizType");
		
		FileUtils.removePath("file/purchase_testFile");
    }

    @Test
    public void testDahuaGeneratePurchaseFileWithRecord(){
    			 
		JijinSyncFileDTO testFile =junitMockModelBuilder.buildJijinSyncFile("lfx201_testFile", SyncFileBizType.PAF_BUY_AUDIT, SyncFileStatus.READ_SUCCESS, "20150301");
		junitMockModelBuilder.buildPafBuyAudit("appSheetNo", "211", "DISPATCHED", testFile.getId(), "20150301", BigDecimal.TEN);
		junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.NOTIFY_SUCCESS, TradeRecordType.PURCHASE, "470009", "lfx201", "20150301", 0l);
		 
		FileHolder fileHolder = new FileHolder();
		fileHolder.setFileName("purchase_testFile");
		fileHolder.setFileAbsolutePath("file/");
		purchaseService.generatePurchaseFile(fileHolder, "20150301", "bizType");
		
		FileUtils.removePath("file/purchase_testFile");		
    }
    
    @Test
    public void testLackParameter(){

        PurchaseResultGson result =purchaseService.orderPurchase("", "", null, "", "", "", "", "","","PAF");
        assertTrue("09".equals(result.getRetCode()));
    }
    
}
