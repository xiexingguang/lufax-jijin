package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.sysFacade.gson.BatchTxUnfreezeRequest;
import com.lufax.jijin.sysFacade.gson.ClearInfo;
import com.lufax.jijin.sysFacade.gson.FrozenNo;
import com.lufax.jijin.sysFacade.gson.PaymentIncreaseRequest;
import com.lufax.jijin.sysFacade.gson.PaymentRequest;
import com.lufax.jijin.sysFacade.gson.SubtractInfo;
import com.lufax.jijin.sysFacade.service.PaymentAppService;


@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class PaymentAppServiceTest  extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private PaymentAppService paymentAppService;
	
	@Test
	public void test(){
		
    	PaymentRequest deRequest = new PaymentRequest("JIJIN","instructionNo","paymentOrder",
    			"FrozenCode","transactionType","UnfreezeRemark",
    			new SubtractInfo(new BigDecimal("1222"),"MinusType","MinusRemark","BusinessRefNo"),
    			new ClearInfo("BusinessRefNo","lfx201","20160324"),
    			"prodCode","20160324",
    			"referenceId","JIJIN_BUY_DECREASE","调减清算","bizId","JIJIN_BUY_DECREASE");
		
    	System.out.println(JsonHelper.toJson(deRequest));
    	
     	PaymentIncreaseRequest inRequest = new PaymentIncreaseRequest("JIJIN", "12123",
     			345l, TransactionType.INCOME_REDEMPTION.name(),
     			"businessReferenceId", "vendorCode", 
                new BigDecimal("232"), "20160324",
                 "赎回：xxxxx","20160324",
                 "prodCode","referenceId","JIJIN_REDEEM",
                "赎回入账","bizId","JIJIN_REDEEM");
     	
     	System.out.println(JsonHelper.toJson(inRequest));
    	
     	
     	List<FrozenNo>	frozenNoList = new ArrayList<FrozenNo>();
     	frozenNoList.add(new FrozenNo(12121l, "productCode", "20160325",
     			"referenceId","JIJIN_UNFREEZE","解冻","bizId","JIJIN_UNFREEZE"));
     	BatchTxUnfreezeRequest unfreezeRequest = new BatchTxUnfreezeRequest("JIJIN","InstructionNO",frozenNoList,TransactionType.UNFROZEN_FUND.name(),"解冻备注",
    			"prodCode","20160229","referenceId","JIJIN_BATCH_UNFREEZE","解冻","bizId","JIJIN_BATCH_UNFREEZE");
     	
     	System.out.println(JsonHelper.toJson(unfreezeRequest));
     	
     	
     	
        PaymentRequest cancelPayRequest = new PaymentRequest("JIJIN", "InstructionNO",
        		"frozenCode", null, TransactionType.UNFROZEN_FUND.name(),
        		"备注", null, null,null,null, "referenceId",
    			"JIJIN_UNFREEZE", null, "bizId","JIJIN_UNFREEZE");

     	System.out.println(JsonHelper.toJson(cancelPayRequest));
		
	}

}
