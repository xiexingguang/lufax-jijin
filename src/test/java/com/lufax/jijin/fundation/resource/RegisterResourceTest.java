package com.lufax.jijin.fundation.resource;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.JijinAppProperties;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class RegisterResourceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
    private JijinAppProperties props;

	@Test@Ignore
	public void testInitSaleCodesMap() {
		String s = props.getSaleCodeMap();
		//{"key2":"2","key1":"1"}
		//{"yfd":"211","htf":"247"}
		System.out.println("s is:" + s);
//		String s = "{\"yfd\":\"211\",\"htf\":\"247\"}";
		Map saleCodes = new Gson().fromJson(s, Map.class);
		for(Object key : saleCodes.keySet()){
			System.out.println(saleCodes.get(key));
		}
		assertTrue(saleCodes.size()>0);
	}

}
