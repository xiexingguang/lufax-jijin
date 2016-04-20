package com.lufax.jijin.daixiao.service;

import com.google.gson.Gson;

import static org.junit.Assert.*;

import com.lufax.jijin.daixiao.gson.JijinDaixiaoInfoGson;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinDaixiaoServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;

    @Test@Ignore
    public void testGet() {
        JijinDaixiaoInfoGson jijinDaixiaoInfoGson = jijinDaixiaoInfoService.getJijinDaixiaoInfoGsonByProductId("110020");
        System.out.println(new Gson().toJson(jijinDaixiaoInfoGson));
    }
    
    @Test@Ignore
    public void testCreateProductCode(){
    	String productCode = jijinDaixiaoInfoService.createProductCode();
    	System.out.println("productCode="+productCode);
    	assertNotNull(productCode);
    }

}
