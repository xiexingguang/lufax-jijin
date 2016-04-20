package com.lufax.jijin.fundation.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;

@Ignore
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinTradeReconServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	private JijinTradeReconService  jijinTradeReconService;
	
	@Autowired
	private JijinPAFBuyAuditService jijinPAFBuyAuditService;
    @Autowired
    private EventService eventService;
    @Autowired
    private JunitMockModelBuilder junitMockModelBuilder;
    @Autowired
    private FundAppCallerService fundAppCallerService;
	
	@Autowired
	private JijinAppProperties jijinAppProperties;
    @Autowired
    private TradeDayService tradeDayService;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;

	
	@Before
	public void setUp() {
		eventService = mock(EventService.class);
		jijinPAFBuyAuditService.setEventService(eventService);
		fundAppCallerService = mock(FundAppCallerService.class);
		jijinPAFBuyAuditService.setFundAppCallerService(fundAppCallerService);
		junitMockModelBuilder.buildJijinInfo("300749","dh103","currency");
		junitMockModelBuilder.buildJijinUserAccount(1l, "dh103");
		junitMockModelBuilder.buildTradeRecord(TradeRecordStatus.SUCCESS,TradeRecordType.PURCHASE,"300749","dh103","20990101", 0l);
		
	}
	
	@Test
	public void test(){
		
		///nfsc/sftp_user/pafsftp/dh103/upload/20150930/dh103_20150930_25
		InputStream input =JijinTradeReconServiceTest.class.getResourceAsStream("mockfile/wind_20160106094100_20.txt");
		JijinSyncFileDTO reconfileDto = junitMockModelBuilder.buildJijinSyncFile("file/nfsc/sftp_user/pafsftp/dh103/upload/20150930/dh103_20990101_25.txt_99999999",SyncFileBizType.JIJIN_TRADE_RESULT,SyncFileStatus.INIT,"20990101");
		junitMockModelBuilder.buildPafBuyAudit("appSheetNo", "270A", "DISPATCHED", 99999999l, "20990101", BigDecimal.TEN);

		jijinTradeReconService.process(reconfileDto);
		
		assertTrue("RECON_SUCCESS".equals(junitMockModelBuilder.getJijinSyncFileDTO(reconfileDto.getId()).getStatus()));
	}
	
}
