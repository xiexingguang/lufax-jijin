package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO.Status;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceAuditRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.yeb.dto.YebJJZLIncomeSyncDTO;
import com.lufax.jijin.yeb.repository.IncomeSyncStatus;
import com.lufax.jijin.yeb.repository.YebJJZLIncomeSyncRepository;

@Service
public class JijinIncomeDispatchService {


    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinUserBalanceAuditRepository jijinUserBalanceAuditRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private TradeDayService tradeDayService;
    @Autowired
    private JijinTradeLogRepository jijinTradeLogRepository;
    @Autowired
    private YebJJZLIncomeSyncRepository yebJJZLIncomeSyncRepository;

    /**
     * 代销货币基金收益分配,每交易日同步
     * PS:大华货基每自然日同步收益会在1022版本重载
     * 
     * @param jijinTradeSyncDTO
     */
    @Transactional
    public void dispatchIncome(JijinUserBalanceAuditDTO balAuditDto) {
	
    	JijinUserBalanceDTO userBalance = jijinUserBalanceRepository.findUserBalanceByFundCode(balAuditDto.getUserId(), balAuditDto.getFundCode());
    	
    	if(null == userBalance){
    		//无相关账户信息
    		balAuditDto.setStatus(Status.UNMATCH.name());
    		jijinUserBalanceAuditRepository.updateBusJijinUserBalanceAudit(balAuditDto);
    		return;
    	}
   
    	BigDecimal dailyIncome = balAuditDto.getDailyIncome();
    	BigDecimal totalIncome = balAuditDto.getTotalIncome();
    	
    	//用相邻两天的总收益之差和当日收益做校验
    	Date date = DateUtils.parseDate(balAuditDto.getBizDate(),DateUtils.DATE_STRING_FORMAT);
    	String yesterDay = "";
    	//如果是大华货基，按自然日
        if("000379".equals(balAuditDto.getFundCode())){  	
       		Calendar  c = Calendar.getInstance();
    		c.setTime(date);
    		c.add(Calendar.DATE, -1);
    		date=c.getTime();
        	yesterDay = DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT);
        }else{
        	yesterDay = tradeDayService.getLastTradeDay(date);
        }

    	JijinUserBalanceAuditDTO yesterdayAudit = jijinUserBalanceAuditRepository.findBusJijinUserBalanceAudit(MapUtils.buildKeyValueMap("fundCode",balAuditDto.getFundCode(),"userId",balAuditDto.getUserId(),"bizDate",yesterDay));
    	
    	if(null!=yesterdayAudit){
    		BigDecimal yesterTotalIncome = yesterdayAudit.getTotalIncome();
        	
        	BigDecimal delta = totalIncome.subtract(yesterTotalIncome);
        	if(delta.compareTo(dailyIncome)!=0){
        		//相邻两天的差值不等与当日收益，标志WARN
        		balAuditDto.setStatus(Status.WARN.name());
        	}else{
        		//正确无误
        		balAuditDto.setStatus(Status.DISPATCHED.name());
        	}
    	}else{
    		//正确无误
    		balAuditDto.setStatus(Status.DISPATCHED.name());
    	}
    	
    	int result1 = 0;
    	if(!Status.WARN.name().equals(balAuditDto.getStatus())){
        	userBalance.setShareBalance(userBalance.getShareBalance().add(dailyIncome));
        	result1 = jijinUserBalanceRepository.updateFundAccount(userBalance);
        	if(result1!=1){
        		return;
        		// conflict, do it later
        	}
    	}

    	int result = jijinUserBalanceAuditRepository.updateBusJijinUserBalanceAudit(balAuditDto);
    	//insert History
    	if(result ==1 && result1==1){
    		JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalance, balAuditDto.getBizDate(),dailyIncome, new BigDecimal("0"), "货币基金每日收益",BalanceHistoryBizType.货基收益,balAuditDto.getId());
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
            
            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(balAuditDto.getUserId(), balAuditDto.getFundCode(), balAuditDto.getId(), TradeRecordStatus.SUCCESS.name(),TradeRecordType.CURRENCY_INCOME,balAuditDto.getDailyIncome(),null,balAuditDto.getBizDate()+"000000", balAuditDto.getBizDate()));
            
            //如果是大华货基，需要同步信息给陆宝宝
            if("000379".equals(balAuditDto.getFundCode())){
            	YebJJZLIncomeSyncDTO ljb = new YebJJZLIncomeSyncDTO();
            	ljb.setAmount(balAuditDto.getTotalShare());
            	ljb.setFundCode(balAuditDto.getFundCode());
            	ljb.setIncomeDate(balAuditDto.getBizDate());
            	ljb.setStatus(IncomeSyncStatus.NEW.getCode());
            	ljb.setTotalYield(balAuditDto.getTotalIncome());
            	ljb.setUnpayYield(new BigDecimal("0.00"));
            	ljb.setUserId(balAuditDto.getUserId());
            	ljb.setYesterdayYield(balAuditDto.getDailyIncome());
            	
            	yebJJZLIncomeSyncRepository.insertYebIncomeSync(ljb);
            }
            
    	}else if(result ==1 && result1==0){
    		//warn, do nothing 
    	}else{
    		//乐观锁回滚
    		throw new OptimisticLockingFailureException("UserBalanceAudit Opt lock failed - sync daily income");
    	}
    }
}
