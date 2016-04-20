package com.lufax.jijin.fundation.service;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.fundation.constant.YebFrozenFundShowGson;
import com.lufax.jijin.fundation.constant.YebTransactionType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.gson.PaginationByPageNoGson;
import com.lufax.jijin.fundation.gson.YebTransactionHistoryGson;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.util.ApplicationContextUtils;

//@Ignore
@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinUserServiceTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	@Autowired
	JijinUserService jijinUserService;
	
	@Test
	public void testBuildAccountResponseGson() {
		System.out.println("-----------------");
		
//		System.out.println("json1:" + new Gson().toJson(jijinUserService.buildAccountResponseGson("570776")));
//		
//		System.out.println("json2:" + new Gson().toJson(jijinUserService.buildAccountResponseGson("000000", "000000")));
//		
//		System.out.println("json3:" + new Gson().toJson(jijinUserService.buildAccountResponseGson("000000", "")));
//		
//		System.out.println("json4:" + new Gson().toJson(jijinUserService.buildAccountResponseGson("000000", null)));
		
		System.out.println("++++++++++++++");
	}
	
	
	@Test
	public void testBuildJijinFrozenDetailPageGson() {
		Type type = new TypeToken<PaginationByPageNoGson<YebFrozenFundShowGson>>(){}.getType();
		
		//成功
		String retJson1 = JsonHelper.toJson(jijinUserService.buildYebFrozenFundShowPageGson(623270L, 15, 2), type);
		System.out.println("------:" + retJson1);
//		
//		//第一个sql查询失败
//		String retJson2 = JsonHelper.toJson(jijinUserService.buildYebFrozenFundShowPageGson(570776L, 15, 1), type);
//		System.out.println("------:" + retJson2);
//		
//		//第二个sql查询失败
//		String retJson3 = JsonHelper.toJson(jijinUserService.buildYebFrozenFundShowPageGson(10765396L, 15, 2), type);
//		System.out.println("------:" + retJson3);
	}
	
	@Test
	public void testBuildYebTransactionHistoryPageGson() {
		Type type = new TypeToken<PaginationByPageNoGson<YebTransactionHistoryGson>>(){}.getType();
		
//		String retJson1 = JsonHelper.toJson(jijinUserService.buildYebTransactionHistoryPageGson(10765396L, null, 15, 1), type);
//		System.out.println("------:" + retJson1);
//		
//		String retJson2 = JsonHelper.toJson(jijinUserService.buildYebTransactionHistoryPageGson(1L, Arrays.asList("01", "08"), 15, 1), type);
//		System.out.println("++++++:" + retJson2);
//		
//		String retJson3 = JsonHelper.toJson(jijinUserService.buildYebTransactionHistoryPageGson(1L, Arrays.asList("06"), 15, 1), type);
//		System.out.println("赎回------:" + retJson3);
//		
		String retJson4 = JsonHelper.toJson(jijinUserService.buildYebTransactionHistoryPageGson(584812L, Arrays.asList("00"), 15, 1), type);
		System.out.println("收益------:" + retJson4);
		
		
//		String retJson5 = JsonHelper.toJson(jijinUserService.buildYebTransactionHistoryPageGson(501612L, Arrays.asList("01", "08"), 15, 1), type);
//		System.out.println("申购------:" + retJson5);
		
//		String retJson5 = JsonHelper.toJson(jijinUserService.buildYebTransactionHistoryPageGson(623268L, Arrays.asList("01", "08"), 15, 1), type);
//		System.out.println("申购------:" + retJson5);
		
//		String retJson5 = JsonHelper.toJson(jijinUserService.buildYebTransactionHistoryPageGson(623270L, Arrays.asList("01", "08"), 15, 1), type);
//		System.out.println("申购------:" + retJson5);
	}
	
	@Autowired
	private JijinTradeRecordRepository repo;
	@Test
    public void testGetJijinTradeRecordDTOInfoByIds() {
    	List<JijinTradeRecordDTO> list = repo.getJijinTradeRecordDTOInfoByIds(Arrays.asList(7068L, 3L, 103L));
    	for(JijinTradeRecordDTO dto : list) {
    		System.out.println(dto.getId() + "," + dto.getChannel() + "," + dto.getProdCode());
    	}
    }
}
