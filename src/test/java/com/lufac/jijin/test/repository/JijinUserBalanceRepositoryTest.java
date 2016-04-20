package com.lufac.jijin.test.repository;

import static org.junit.Assert.assertNotNull;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import com.google.gson.reflect.TypeToken;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.daixiao.repository.JijinExCharacterRepository;
import com.lufax.jijin.fundation.constant.BalanceDividendStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.gson.CurrencyToStockGson;
import com.lufax.jijin.fundation.gson.PaginationByPageNoGson;
import com.lufax.jijin.fundation.gson.PaginationGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.service.RedeemService;
import com.lufax.jijin.fundation.service.TransferService;

@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class JijinUserBalanceRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
	
	 @Autowired
	protected JijinUserBalanceRepository jijinUserBalanceRepository;
	 
	 
    @Test@Ignore
    public void testInsert(){
    	
//    	 JijinUserBalanceDTO dto= new JijinUserBalanceDTO();
//    	    dto.setUserId(1001L);
//    	    dto.setFundCode("470009");
//    	    dto.setShareBalance(new BigDecimal("122.23"));
//    	    dto.setFrozenShare(new BigDecimal("0.00"));
//    	    dto.setDividendType("FUND");
//    	    dto.setVersion(0L);
//    	    dto.setDividendStatus("DONE");
//    	    
//    	    jijinUserBalanceRepository.insertBusJijinUserBalance(dto);
    }
    
    @Test@Ignore
    public void testFind(){
    	 JijinUserBalanceDTO dto = jijinUserBalanceRepository.findUserBalanceByFundCode(1001l, "003201");
//    	 assertNotNull(dto);
    	 
    }
	
    
    @Test@Ignore
    public void testUpdate(){
    	
    	JijinUserBalanceDTO dto = jijinUserBalanceRepository.findUserBalanceByFundCode(1001l, "003201");
        dto.setFrozenAmount(new BigDecimal("12.00"));	
    	 int result = jijinUserBalanceRepository.updateFundAccount(dto);
//    	 assertNotNull(dto);
    	 
    }
    @Test@Ignore
    public void testUpdateUserBalanceDividendStatus(){
    	jijinUserBalanceRepository.updateUserBalanceDividendStatusAndType(5L, "470009", BalanceDividendStatus.MODIFYING.name(),null);
    }
    
//    @Test
//    public void testx(){
//       Map<String, JijinUserBalanceDTO> map = (Map<String, JijinUserBalanceDTO>)jijinUserBalanceRepository.findFundCodeShareBalance(608047L, BigDecimal.valueOf(1000), "11464");
//       System.out.println(map.keySet());
//       if(map.keySet().contains("11464")) {
//           map.remove("11464");
//       }
//       System.out.println(map.keySet());
//    }
	
    @Autowired
    JijinExCharacterRepository jijinExCharacterRepository;
    @Autowired
    JijinInfoRepository jijinInfoRepository;
    @Autowired
    TransferService transferService;
    
    @Test
    public void testJijinUserBalanceRepository(){
//        System.out.println(jijinUserBalanceRepository.isShareBalanceFit(597468L, "18382", BigDecimal.valueOf(1000)));
//        System.out.println(jijinUserBalanceRepository.isShareBalanceFit(597468L, "18382", BigDecimal.valueOf(3000)));
//        System.out.println(jijinUserBalanceRepository.isShareBalanceFit(608047L, "11464", BigDecimal.valueOf(1000)));
//        List<String> list = jijinUserBalanceRepository.findFundCodeByUserAmount(608047L);
        
//        System.out.println(list);
        
//        Map<String, JijinInfoDTO> map = (Map<String, JijinInfoDTO>)jijinInfoRepository.findByFundCodes(Arrays.asList("95449"), "STOCK");
//        for(String key : map.keySet()) {
//            System.out.println(key + "------------------->" + map.get(key).getFundName() + "," + map.get(key).getProductCode());
//        }
//        List<JijinUserBalanceDTO> l = jijinUserBalanceRepository.findFundCodeShareBalance(608047L, BigDecimal.valueOf(1000), Arrays.asList("11464"), 0, 15);
//        for(JijinUserBalanceDTO j : l) {
//            System.out.println("xxxxxxxxxx" + j.getShareBalance() + "," + j.getFundCode());
//        }
    }
    
    @Test
    public void testTransferService(){
        Type type = new TypeToken<PaginationGson<CurrencyToStockGson>>(){}.getType();
        
//        //成功
        String retJson1 = JsonHelper.toJson(transferService.getConvertedCurrency2StockPage(667795L, "593342", BigDecimal.valueOf(20), 1, 15, 300), type);
        System.out.println("1------:" + retJson1);
        
//        //input parameter：amount=null
//        String retJson2 = JsonHelper.toJson(transferService.getConvertedCurrency2StockPage(608047L, "11464", null, 1, 15, 300), type);
//        System.out.println("2------:" + retJson2);
//        
//        //input parameter：fundCode=""
//        String retJson3 = JsonHelper.toJson(transferService.getConvertedCurrency2StockPage(608047L, "", BigDecimal.valueOf(1000), 1, 15, 300), type);
//        System.out.println("3------:" + retJson3);
////        
//        //第1个sql
//        String retJson4 = JsonHelper.toJson(transferService.getConvertedCurrency2StockPage(608047L, "14884", BigDecimal.valueOf(1000), 1, 15, 300), type);
//        System.out.println("4------:" + retJson4);
//        
//        //第2个sql
//        String retJson5 = JsonHelper.toJson(transferService.getConvertedCurrency2StockPage(608047L, "703449", BigDecimal.valueOf(1000), 1, 15, 300), type);
//        System.out.println("5------:" + retJson5);
//        
//        //第3个sql:不存在的用户(用户在bus_jijin_user_info表中无对应的用户信息)
//        String retJson6 = JsonHelper.toJson(transferService.getConvertedCurrency2StockPage(608048L, "593342", BigDecimal.valueOf(1000), 1, 15, 300), type);
//        System.out.println("6------:" + retJson6);
//        
//        //申购金额>持有金额的基金
//        String retJson7 = JsonHelper.toJson(transferService.getConvertedCurrency2StockPage(608047L, "11464", BigDecimal.valueOf(1000000), 1, 15, 300), type);
//        System.out.println("7------:" + retJson7);
//        
//        //第5个sql，查询是否是货基
//        String retJson8 = JsonHelper.toJson(transferService.getConvertedCurrency2StockPage(608047L, "11464", BigDecimal.valueOf(100000), 1, 15, 300), type);
//        System.out.println("8------:" + retJson8);
    }
    
//    @Test
//    public void testInsertTmp(){
//        
//         JijinUserBalanceDTO dto = new JijinUserBalanceDTO();
//            dto.setUserId(646070L);
//            dto.setFundCode("392109");
//            dto.setShareBalance(new BigDecimal("3000.23"));
//            dto.setFrozenShare(new BigDecimal("0.00"));
//            dto.setDividendType("FUND");
//            dto.setVersion(0L);
//            dto.setDividendStatus("DONE");
//            
//            jijinUserBalanceRepository.insertBusJijinUserBalance(dto);
//    }
}
