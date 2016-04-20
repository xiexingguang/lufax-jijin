package com.lufax.jijin.fundation.repository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.lufax.jijin.fundation.constant.YebTransactionType;
import com.lufax.jijin.fundation.dto.JijinFrozenDetailDTO;

//@Ignore
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinFrozenDetailRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private JijinFrozenDetailRepository jijinFrozenDetailRepository = null;
	
	@Test
	public void testInsertJijinFrozenDetail() {
		JijinFrozenDetailDTO dto = new JijinFrozenDetailDTO("04", 6109L, BigDecimal.valueOf(100.24), BigDecimal.valueOf(1000.32), BigDecimal.valueOf(1000.32), "test");
//		#transactionType#,
		dto.setTransactionType("04");
//        #userBalanceId#,
		dto.setUserBalanceId(6109L);
//        #frozenAmount#,
		dto.setFrozenAmount(BigDecimal.valueOf(100.24));
//        #unFrozenAmount#,
		dto.setUnFrozenAmount(BigDecimal.valueOf(1000.32));
//        #totalFrozenAmount#,
		dto.setTotalFrozenAmount(BigDecimal.valueOf(1000.32));
//        #remark#,
		dto.setRemark("test");
		
		jijinFrozenDetailRepository.insertJijinFrozenDetail(dto);
	}
	
	@Test
	public void testInsertJijinFrozenDetail2() {
//		JijinFrozenDetailDTO dto = new JijinFrozenDetailDTO("05", 208268L, BigDecimal.valueOf(100.24), BigDecimal.valueOf(100.25), BigDecimal.valueOf(100.26), "test");
//		
//		jijinFrozenDetailRepository.insertJijinFrozenDetail(dto);
		
		List<String> typeList = Arrays.asList(YebTransactionType.SELL_FREEZE.getCode(), 
        		YebTransactionType.SELL_SUCCESS_UNFREEZE.getCode(), YebTransactionType.SELL_FAIL_UNFREEZE.getCode());
		
		List<JijinFrozenDetailDTO> list = jijinFrozenDetailRepository.findFrozenDetailByIdAndType(6109L, typeList, 15, 0);
		System.out.println(list);
	}
}
