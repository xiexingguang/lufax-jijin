package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.YebFrozenFundShowGson;
import com.lufax.jijin.fundation.constant.YebTransactionType;
import com.lufax.jijin.fundation.dto.JijinFrozenDetailDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.gson.AccountResponseGson;
import com.lufax.jijin.fundation.gson.PaginationByPageNoGson;
import com.lufax.jijin.fundation.gson.YebTransactionHistoryGson;
import com.lufax.jijin.fundation.repository.JijinFrozenDetailRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceAuditRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;

@Service
public class JijinUserService {
	
	@Autowired
	private JijinUserBalanceRepository userBalanceRepo;
	
	@Autowired
	private JijinUserBalanceAuditRepository userBalanceAuditRepo;
	
	@Autowired
	private JijinFrozenDetailRepository jijinFrozenDetailRepository;
	
	@Autowired 
	private JijinTradeLogRepository jijinTradeLogRepository;
	
	@Autowired
	private JijinTradeRecordRepository jijinTradeRecordRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	private static final List<String> YEB_TRANSACTION_TYPE_LIST = Arrays.asList(YebTransactionType.SELL_FREEZE.getCode(),
            YebTransactionType.SELL_SUCCESS_UNFREEZE.getCode(), YebTransactionType.SELL_FAIL_UNFREEZE.getCode());

	private static final String STATUS_DISPATCHED = "DISPATCHED";
	private static final String STATUS_SUCCESS = "SUCCESS";
	
	private static final String FUND_CODE = "000379"; 
	
	private static final Map<List<String>, List<String>> TRADE_RECORD_TYPE_MAP = new HashMap<List<String>, List<String>>(3);
	
	static {
		//收益
		TRADE_RECORD_TYPE_MAP.put(Arrays.asList("00"), Arrays.asList(TradeRecordType.CURRENCY_INCOME.name()));
		//申购；认购是APPLY
		TRADE_RECORD_TYPE_MAP.put(Arrays.asList("01", "08"), Arrays.asList(TradeRecordType.PURCHASE.name()));
		//赎回
		TRADE_RECORD_TYPE_MAP.put(Arrays.asList("06"), Arrays.asList(TradeRecordType.REDEEM.name()));
	}
	
	private static final String MIDWAY = "MIDWAY";
	private static final String EMPTY_PRODUCT_CODE = "EMPTY_PRODUCT_CODE";
	
    public AccountResponseGson buildAccountResponseGson(String userIdStr) {
		long userId = Long.valueOf(userIdStr);
		//第一次与数据库交互
        JijinUserBalanceDTO jijinUserBalance = queryJijinUserBalanceDTO(FUND_CODE, userId);  
        if (null == jijinUserBalance) {
            Logger.info(this, String.format("JijinUserBalanceDTO is null, [userId: %s] [fundCode: %s] ", userIdStr, FUND_CODE));
//            return new AccountResponseGson("99", "userBalance not exists");
            return new AccountResponseGson("0", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }
        
        //第二次与数据库交互
        BigDecimal allIncomeAmount = userBalanceAuditRepo.
        		sumAmountByCondition(MapUtils.buildKeyValueMap("fundCode",FUND_CODE, "userId",userId, "status",STATUS_DISPATCHED));
		
		return new AccountResponseGson(String.valueOf(jijinUserBalance.getId()),
				jijinUserBalance.getFrozenShare(), allIncomeAmount, jijinUserBalance.getShareBalance());
    }

	private JijinUserBalanceDTO queryJijinUserBalanceDTO(String fundCode, long userId) {
		return userBalanceRepo.findUserBalanceByFundCode(userId, fundCode);
	}
    
    public PaginationByPageNoGson<YebFrozenFundShowGson> buildYebFrozenFundShowPageGson(
    		Long userId, int pageLimit, int pageNo) {
        Logger.info(this, String.format("The typeList is [%s] !", JsonHelper.toJson(YEB_TRANSACTION_TYPE_LIST)));

    	//第一次与数据库交互
    	JijinUserBalanceDTO jijinUserBalanceDTO = queryJijinUserBalanceDTO(FUND_CODE, userId);
    	if (null == jijinUserBalanceDTO) {
    		String msg = String.format("JijinUserBalanceDTO is null, [userId: %s] [fundCode: %s] ", userId, FUND_CODE);
            Logger.warn(this, msg);
            return new PaginationByPageNoGson<YebFrozenFundShowGson>(pageLimit, 0, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE);
    	}
    	
    	Long dtoId = jijinUserBalanceDTO.getId();
    	//第二次与数据库交互
    	int recordCount = (int)jijinFrozenDetailRepository.findCountByUserIdAndType(dtoId, YEB_TRANSACTION_TYPE_LIST);
    	//总记录数数/每页显示的条数=总页数 
    	int pageCount = (recordCount<=0) ? 0 : ((0==recordCount%pageLimit) ? recordCount/pageLimit : (recordCount/pageLimit)+1);
    	//第三次与数据库交互
    	List<JijinFrozenDetailDTO> jijinFrozenDetailDTOs = ((pageNo>=1) && (pageNo<=pageCount)) ?
    			jijinFrozenDetailRepository.findFrozenDetailByIdAndType(dtoId, YEB_TRANSACTION_TYPE_LIST, pageLimit, pageNo-1) : null;
    	if(null == jijinFrozenDetailDTOs) {
    		String msg = String.format("jijinFrozenDetailDTOs is null, [userId: %s] [fundCode: %s] [dtoId: %s] ", userId, FUND_CODE, dtoId);
            Logger.warn(this, msg);
    		return new PaginationByPageNoGson<YebFrozenFundShowGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE);
    	}
    	
    	List<YebFrozenFundShowGson> pageRecords = new ArrayList<YebFrozenFundShowGson>(jijinFrozenDetailDTOs.size());
        for (JijinFrozenDetailDTO jijinFrozenDetailDTO : jijinFrozenDetailDTOs) {
           if(null != jijinFrozenDetailDTO) {
        	   pageRecords.add(new YebFrozenFundShowGson(jijinFrozenDetailDTO));
           }
        }
        
        Collections.sort(pageRecords, new Comparator<YebFrozenFundShowGson>() {
			public int compare(YebFrozenFundShowGson o1, YebFrozenFundShowGson o2) {
				//如果要按照降序排序,则o1 小于o2，返回1（正数），相等返回0，01大于02返回-1（负数）
				return o1.getId()==o2.getId() ? 0 : (o1.getId()<o2.getId() ? 1 : -1);
			}
		});
    	
        String msg = "Query JIJIN Frozen Detail success";
		Logger.info(this, msg);
    	return new PaginationByPageNoGson<YebFrozenFundShowGson>(pageLimit, recordCount, pageNo, pageRecords, msg, ConstantsHelper.RET_CODE_SUCCESS);
    }

    public PaginationByPageNoGson<YebTransactionHistoryGson> buildYebTransactionHistoryPageGson(
            Long userId, List<String> txTypeCodeList, int pageLimit, int pageNo) {
        int recordCount = 0;
        List<JijinTradeLogDTO> transactionHistoryList = null;
        List<String> txTypeList = null;

        //入参检查
        if(pageNo<1 || null==txTypeCodeList || !TRADE_RECORD_TYPE_MAP.keySet().contains(txTypeCodeList)) {
            String msg = String.format("parameter error input params are: [userId=%s, txTypeList=%s, pageLimit=%s, pageNo=%s]", userId, txTypeList, pageLimit, pageNo);
            Logger.warn(this, msg);
            return generateRetGsonByCondition(pageLimit, pageNo, recordCount, msg, transactionHistoryList, txTypeList);
        }

        try {
            txTypeList = TRADE_RECORD_TYPE_MAP.get(txTypeCodeList);
            //第一次与数据库交互
            recordCount = (int)jijinTradeLogRepository.countByAccountAndTypeList(userId, txTypeList, FUND_CODE, STATUS_SUCCESS);
            int pageCount = (recordCount<=0) ? 0 : ((0==recordCount%pageLimit) ? recordCount/pageLimit : (recordCount/pageLimit)+1);
            //第二次与数据库交互
            transactionHistoryList = ((pageNo>=1) && (pageNo<=pageCount)) ?
                    jijinTradeLogRepository.findPaginationByAccountAndTypeList(userId, txTypeList,
                            FUND_CODE, STATUS_SUCCESS, pageLimit, pageNo-1) : null;
            if(CollectionUtils.isEmpty(transactionHistoryList)) {
                String msg = String.format("Query Yeb Transaction history has no result from DB, [userId=%s, txTypeList=%s, pageLimit=%s, pageNo=%s]", userId, txTypeList, pageLimit, pageNo);
                Logger.warn(this, msg);
                return generateRetGsonByCondition(pageLimit, pageNo, recordCount, msg, transactionHistoryList, txTypeList);
            }

            List<JijinTradeLogDTO> finalJijinTradeLogDTOList = new ArrayList<JijinTradeLogDTO>(transactionHistoryList.size());
            Map<JijinTradeLogDTO, Long> idTradeRecordIdMap = new LinkedHashMap<JijinTradeLogDTO, Long>(transactionHistoryList.size());
            for (JijinTradeLogDTO jijinTradeLogDTO : transactionHistoryList) {
                //数据库约束决定tradeRecordId不可能为null
                if(!idTradeRecordIdMap.containsKey(jijinTradeLogDTO)) {
                    idTradeRecordIdMap.put(jijinTradeLogDTO, jijinTradeLogDTO.getTradeRecordId());
                }
            }
            if(idTradeRecordIdMap.isEmpty()) {
                String msg = String.format("No JijinTradeRecord id found, [userId=%s, txTypeList=%s, pageLimit=%s, pageNo=%s]", userId, txTypeList, pageLimit, pageNo);
                Logger.warn(this, msg);
                return generateRetGsonByCondition(pageLimit, pageNo, recordCount, msg, transactionHistoryList, txTypeList);
            }

            //第三次与数据库交互
            List<JijinTradeRecordDTO> jijinTradeRecordDTOs = jijinTradeRecordRepository.getJijinTradeRecordDTOInfoByIds(idTradeRecordIdMap.values());
            if(CollectionUtils.isEmpty(jijinTradeRecordDTOs)) {
                String msg = String.format("jijinTradeRecordDTOs has no result from DB, ids=%s", idTradeRecordIdMap.values());
                Logger.warn(this, msg);
                return generateRetGsonByCondition(pageLimit, pageNo, recordCount, msg, transactionHistoryList, txTypeList);
            }

            Map<JijinTradeLogDTO, String> codeMap = new LinkedHashMap<JijinTradeLogDTO, String>(jijinTradeRecordDTOs.size());
            for(JijinTradeRecordDTO jijinTradeRecordDTO : jijinTradeRecordDTOs) {
                Iterator<JijinTradeLogDTO> it = idTradeRecordIdMap.keySet().iterator();
                while(it.hasNext()) {
                    JijinTradeLogDTO jijinTradeLogDTO = it.next();
                    jijinTradeLogDTO.setChannel(jijinTradeRecordDTO.getChannel());
                    if(jijinTradeLogDTO.getTradeRecordId().equals(jijinTradeRecordDTO.getId())) {
                        // 判断prodCode是否为空，不为空走下面逻辑，为空问产品经理?
                        if(StringUtils.isBlank(jijinTradeRecordDTO.getProdCode())) {
                        	
                        	if(TradeRecordType.PURCHASE == jijinTradeRecordDTO.getType()){
                        		if("LBO".equals(jijinTradeRecordDTO.getChannel())){
                            		jijinTradeLogDTO.setRemark("申购转入");
                                    finalJijinTradeLogDTOList.add(jijinTradeLogDTO);
                        		}else{
                            		jijinTradeLogDTO.setRemark("收款购买");
                                    finalJijinTradeLogDTOList.add(jijinTradeLogDTO);
                        		}
                        	}else if(TradeRecordType.REDEEM == jijinTradeRecordDTO.getType()){
                        		jijinTradeLogDTO.setRemark("赎回至陆金所账户");
                                finalJijinTradeLogDTOList.add(jijinTradeLogDTO);
                        	}
                            it.remove();
                        }else{   	
                        	if(null==jijinTradeRecordDTO.getRemark()){
                        		codeMap.put(jijinTradeLogDTO, jijinTradeRecordDTO.getProdCode());
                        	}else{
                           	 	jijinTradeLogDTO.setRemark(jijinTradeRecordDTO.getRemark());
                           	 	finalJijinTradeLogDTOList.add(jijinTradeLogDTO);
                        	}
                        }
                    }
                }
            }
            idTradeRecordIdMap.clear();

            
            if(codeMap.isEmpty()) {
                String msg = String.format("All productCodes are empty, [userId=%s, txTypeList=%s, pageLimit=%s, pageNo=%s]", userId, txTypeList, pageLimit, pageNo);
                Logger.warn(this, msg);
                return generateRetGsonByCondition(pageLimit, pageNo, recordCount, msg, transactionHistoryList, txTypeList);
            }

            //第四次与数据库交互
            List<ProductDTO> productDTOs = productRepository.getByCodes(codeMap.values());
            if(CollectionUtils.isEmpty(productDTOs)) {
                String msg = String.format("productDTOs has no result from DB, codes=%s", codeMap.values());
                Logger.warn(this, msg);
                return generateRetGsonByCondition(pageLimit, pageNo, recordCount, msg, transactionHistoryList, txTypeList);
            }
            for(ProductDTO productDTO : productDTOs) {
                Iterator<Map.Entry<JijinTradeLogDTO, String>> it = codeMap.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry<JijinTradeLogDTO, String> entry = it.next();
                    if(entry.getValue().equals(productDTO.getCode())) {

                    	if(entry.getKey().getType() == TradeRecordType.PURCHASE  || entry.getKey().getType() == TradeRecordType.CURRENCY_INCOME){
                    		entry.getKey().setRemark(productDTO.getDisplayName() + productDTO.getCode() +"收款购买");
                    	}else if(entry.getKey().getType() == TradeRecordType.REDEEM){
                            if(MIDWAY.equals(entry.getKey().getChannel())) {
                            	entry.getKey().setRemark("支付赎回，用于支付" + productDTO.getDisplayName() + productDTO.getCode());
                            } else {
                            	entry.getKey().setRemark("赎回至陆金所账户");
                            }
                    	}
                        finalJijinTradeLogDTOList.add(entry.getKey());
                    }
                }
            }
            codeMap.clear();
            
            if(CollectionUtils.isEmpty(finalJijinTradeLogDTOList)) {
                String msg = String.format("finalJijinTradeLogDTOList is empty, [userId=%s, txTypeList=%s, pageLimit=%s, pageNo=%s]", userId, txTypeList, pageLimit, pageNo);
                Logger.warn(this, msg);
                return generateRetGsonByCondition(pageLimit, pageNo, recordCount, msg, transactionHistoryList, txTypeList);
            }

            String msg = "Query Yeb Transaction history success";
            Logger.info(this, msg);
            return generateRetGsonByCondition(pageLimit, pageNo, recordCount, msg, finalJijinTradeLogDTOList, txTypeList);
        } catch (Exception e) {
            String msg = String.format("unknown exception occur. error msg:[%s]", e.getMessage());
            Logger.error(this, msg);
            return generateRetGsonByCondition(pageLimit, pageNo, recordCount, msg, transactionHistoryList, txTypeList);
        }
    }

    private PaginationByPageNoGson<YebTransactionHistoryGson> generateRetGsonByCondition(int pageLimit,
            int pageNo, int recordCount, String msg, List<JijinTradeLogDTO> transactionHistoryList, List<String> txTypeList) {
        if(CollectionUtils.isEmpty(transactionHistoryList)) {
            return new PaginationByPageNoGson<YebTransactionHistoryGson>(pageLimit,
                    recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE);
        }

        List<YebTransactionHistoryGson> pageRecords = new ArrayList<YebTransactionHistoryGson>(transactionHistoryList.size());
        for (JijinTradeLogDTO jijinTradeLogDTO : transactionHistoryList) {
            if(null==jijinTradeLogDTO) {
                continue ;
            }

            String remark = jijinTradeLogDTO.getRemark();
            if(txTypeList.contains(TradeRecordType.CURRENCY_INCOME.name())) {        //收益分支-收益
                jijinTradeLogDTO.setRemark("收益");
            } else if(txTypeList.contains(TradeRecordType.PURCHASE.name())) {        //认/申购分支-转入
               //donothing
            	//jijinTradeLogDTO.setRemark(remark + "收款购买");
            } else if(txTypeList.contains(TradeRecordType.REDEEM.name())) {          //赎回分支-转出
            	//do nothing
            }

            pageRecords.add(new YebTransactionHistoryGson(jijinTradeLogDTO));
        }

        Collections.sort(pageRecords, new Comparator<YebTransactionHistoryGson>() {
            public int compare(YebTransactionHistoryGson o1, YebTransactionHistoryGson o2) {
                //如果要按照降序排序,则o1 小于o2，返回1（正数），相等返回0，01大于02返回-1（负数）
                return o1.getId()==o2.getId() ? 0 : (o1.getId()<o2.getId() ? 1 : -1);
            }
        });

        return new PaginationByPageNoGson<YebTransactionHistoryGson>(pageLimit,
                recordCount, pageNo, pageRecords, msg, ConstantsHelper.RET_CODE_SUCCESS);
    }
}
