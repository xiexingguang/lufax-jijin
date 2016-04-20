package com.lufax.jijin.fundation.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.service.builder.JunitMockModelBuilder;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinPromotionServiceTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private JijinPromotionService jijinPromotionService;
    @Autowired
    private JunitMockModelBuilder junitMockModelBuilder;
    
	@Before
	public void setUp() {

//		junitMockModelBuilder.buildJijinInfo("470009","yfd101","stock");
//		junitMockModelBuilder.buildJijinUserBalance(1l,"470009");

	}
	
	@Test
	public void test(){
//		jijinPromotionService.getPromotionStatus()	;
	}

}
