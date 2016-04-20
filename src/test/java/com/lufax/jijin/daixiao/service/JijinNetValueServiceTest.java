package com.lufax.jijin.daixiao.service;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.daixiao.gson.JijinUpdateGson;
import com.lufax.jijin.daixiao.gson.JijinUpdateStatusGson;
import com.lufax.jijin.daixiao.schedular.Jobs.HandleJijinNetValueJob;
import com.lufax.jijin.fundation.constant.JijinStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinNetValueServiceTest extends AbstractJUnit4SpringContextTests{

	//private JijinNetValueService service;
	@Autowired
	private HandleJijinNetValueJob job;
	@Autowired
	private JijinInfoRepository  info;
	@Autowired
	private JijinDaixiaoInfoService jijinDaixiaoInfoService;
	
	@Test@Ignore
	public void testHandleJijinNetValue(){
		//job.execute();
		//service.handleJijinNetValue(dto)
		long totalTime = 0;
		long totalCount = 0;
		long errorCount = 0;
		long successCount = 0;
		long failCount= 0;
		List<JijinInfoDTO> list= info.findAllJijins();
		for(JijinInfoDTO dto : list){
			for(int i=0;i<1;i++){
				   JijinUpdateStatusGson st = new JijinUpdateStatusGson();
		           st.setBeNewStatus(JijinStatus.BUY_STATUS_PUR_OPEN);
		           st.setCode(dto.getProductCode());
		           st.setBeOperationType("0");
		           long s = System.currentTimeMillis();
		           try{
		        	   BaseGson bs = jijinDaixiaoInfoService.updateJijinDaixiaoStatus(st); 
		        	   JijinUpdateGson jijinUpdateGson = new JijinUpdateGson();
		               jijinUpdateGson.setCode(dto.getProductCode());
		               jijinUpdateGson.setTrustee(dto.getTrustee());//托管人
		               jijinUpdateGson.setFundBrand(dto.getFundBrand());//管理人对应基金品牌
		               jijinUpdateGson.setDisplayName(dto.getFundName());//名称,传简称@槐远0820
		               jijinUpdateGson.setFundManagerName(dto.getFundManager());//管理人
		               jijinUpdateGson.setIsBuyDailyLimit(dto.getIsBuyDailyLimit());
		               jijinUpdateGson.setBuyDailyLimit(dto.getBuyDailyLimit());
		               jijinUpdateGson.setMinInvestAmount(dto.getMinInvestAmount());
		               jijinUpdateGson.setMaxInvestAmount(dto.getMaxInvestAmount());
		               jijinUpdateGson.setIncreaseInvestAmount(dto.getIncreaseInvestAmount());
		               jijinUpdateGson.setSubType(dto.getFundType());
		               jijinUpdateGson.setDividendMethod(dto.getDividendType());
		               BaseGson createJijinInfoResultGson = jijinDaixiaoInfoService.updateProduct(jijinUpdateGson);
		        	   if(createJijinInfoResultGson.getRetCode().equals("000")){
		        		   successCount++;
		        	   }else{
		        		   failCount++;
		        	   }
		           }catch(Exception e){
		        	   errorCount++;
		           }
		           
		           totalTime += (System.currentTimeMillis() - s);
		           totalCount++;
		           System.out.println("总计"+totalCount+"次");
			   }
		}
		System.out.println("耗时"+totalTime+"ms");
		System.out.println("总计"+totalCount+"次");
		System.out.println("成功"+successCount+"次");
		System.out.println("失败"+failCount+"次");
		System.out.println("异常"+errorCount+"次");
		System.out.println("time/count = "+totalTime/totalCount);
		System.out.println("error% = "+errorCount/totalCount*100+"%");
	}
}
