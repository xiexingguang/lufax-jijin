package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.DHBizType;
import com.lufax.jijin.fundation.constant.TradeConfirmStatus;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.constant.TransferTypeEnum;
import com.lufax.jijin.fundation.constant.YebTransactionType;
import com.lufax.jijin.fundation.dto.JijinFrozenDetailDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinTradeConfirmDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceHistoryDTO;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.gson.BaseResponseGson;
import com.lufax.jijin.fundation.remote.AccountAppCallerService;
import com.lufax.jijin.fundation.remote.gson.request.RedeemAuditRequestGson;
import com.lufax.jijin.fundation.repository.JijinFrozenDetailRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinTradeConfirmRepository;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceHistoryRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.service.MqService;
import com.lufax.jijin.sysFacade.gson.Instruction;
import com.lufax.jijin.sysFacade.gson.PaymentIncreaseRequest;
import com.lufax.jijin.sysFacade.gson.PaymentWithdrawRequest;
import com.lufax.jijin.sysFacade.gson.PlusFreezeInstructionDetail;
import com.lufax.jijin.sysFacade.gson.result.PaymentResult;
import com.lufax.jijin.sysFacade.service.PaymentAppService;

/**
 * 大华货基申购 ，赎回的转入转出文件确认
 *
 */
@Service
public class JijinTradeConfirmService {

    @Autowired
    private JijinTradeConfirmRepository jijinTradeConfirmRepository;
    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private MqService mqService;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinTradeLogRepository jijinTradeLogRepository;
	@Autowired
    private AccountAppCallerService accountAppCallerService;
	@Autowired
	private PaymentAppService paymentAppService;
	@Autowired
	private JijinFrozenDetailRepository jijinFrozenDetailRepository;
	
    @Transactional
    public void dispatchConfirm(JijinTradeConfirmDTO jijinTradeConfirmDTO) {
        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordByAppSheetNoAndInstId(jijinTradeConfirmDTO.getAppSheetNo(), "dh103");
        if (jijinTradeRecordDTO == null) {
            Logger.info(this, String.format("can not find jijinTradeRecord by appSheetNo [%s], instId [dh103]", jijinTradeConfirmDTO.getAppSheetNo()));
            jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.id(), TradeConfirmStatus.UNMATCH.name());
            return;
        }

        	//区分T0，T1赎回	
        	if(DHBizType.REDEEM.getCode().equals(jijinTradeConfirmDTO.getBizType())){ // T0赎回
                if (!jijinTradeConfirmDTO.getTradeResCode().equals("0000")) {
                    jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "大华确认状态为失败,与交易状态不符,需要人工处理"));
                    return;
                }
                if (jijinTradeConfirmDTO.getAmount().compareTo(jijinTradeRecordDTO.getReqShare()) != 0) {
                    jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "大华确认金额与交易金额不符,需要人工处理"));
                    return;
                }

                if(!jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUCCESS.name())){
                    jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "大华确认转出成功,陆金所赎回失败,交易状态不符,需要人工处理"));
                    return;
                }
                
                jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "0"));
        	
        	}else if(DHBizType.REDEEM_T1.getCode().equals(jijinTradeConfirmDTO.getBizType())){// T1赎回
        		
                // paf的赎回资金确认文件先回来，状态已变成success，不做任何处理
                if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUCCESS.name())) {
                    Logger.warn(this, String.format("jijin redeem recon: 平安付代付文件比基金公司交易确认文件先回来了， app sheet no [%s]", jijinTradeRecordDTO.getAppSheetNo()));

                    jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                    jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "0"));
                    
                    return;
                }
        		
        		if (jijinTradeConfirmDTO.getTradeResCode().equals("0000")) {

                    //提交成功的记录， 翻转状态到waiting-money，后续资金到帐后再翻转trade record到终态success, 
        		    //如果是 资金流先到了，则补偿入账
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())
                    		|| jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.PROCESSING.name())) {
                    	
                    	JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", jijinTradeRecordDTO.getFundCode()));
                        BigDecimal amount = jijinTradeRecordDTO.getReqShare() == null ? jijinTradeRecordDTO.getReqAmount() : jijinTradeRecordDTO.getReqShare();

                        if (amount.compareTo(jijinTradeConfirmDTO.getAmount()) == 0) { //对账成功
                        	if(jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())){
                        		jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "0", "status", TradeRecordStatus.WAITING_MONEY.name(), "flag", "1"));
                        	} 

                        	 //清销系统入账要区分是转到虚拟户还是银行卡
                        	 PaymentResult response = new PaymentResult();
                        	 
                        	 if(TransferTypeEnum.card.name().equals(jijinTradeRecordDTO.getTransferType())){
                        		 response =  transferToBankCardAudit(jijinTradeRecordDTO,jijinInfoDTO);
                        	 }else{
                               	 PaymentIncreaseRequest request = new PaymentIncreaseRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
                                         jijinTradeRecordDTO.getUserId(), TransactionType.INCOME_REDEMPTION.name(),
                                         jijinTradeRecordDTO.getAppSheetNo(), jijinInfoDTO.getInstId(), 
                                         jijinTradeConfirmDTO.getConfirmShare() ,jijinTradeRecordDTO.getTrxDate(),
                                          "赎回：" + jijinInfoDTO.getFundName(),jijinTradeRecordDTO.getTrxDate(),            
                                         jijinInfoDTO.getProductCode(),jijinTradeRecordDTO.getAppSheetNo(),"JIJIN_REDEEM",
                                         "赎回入账", String.valueOf(jijinTradeRecordDTO.getId()),"JIJIN_REDEEM");
                               	response = paymentAppService.increaseMoneyWithAudit(request);
                        	 }

                             if (!"SUCCESS".equals(response.getStatus()) ){
                                 Logger.error(this, String.format("NEED_MANUAL_HANDLE,call account to do redeem audit failed,tradeRecordId [%s]", jijinTradeRecordDTO.id()));
                             }

                        } else {//对账失败
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "对账失败,提交份额和确认份额不一致"));
                        }
                        jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                        return;
                    }

                    //提交失败，交易确认为成功，需要人工干预
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_FAIL.name())) {
                    	jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), "DISPATCHED");
                        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "对账失败,提交赎回申请失败，但是交易确认返回成功,需要人工核查"));
                        return;
                    }

        		}else{
        			//赎回确认失败，解冻份额

                    //提交成功的记录 - 交易确认返回失败
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name())) {

                        //解冻并返还份额/金额，更新trade record 状态为FAIL, 插 trade log
                        BigDecimal amount = jijinTradeRecordDTO.getReqShare() == null ? jijinTradeRecordDTO.getReqAmount() : jijinTradeRecordDTO.getReqShare();
                        JijinUserBalanceDTO userBalance = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());

                        userBalance.setShareBalance(userBalance.getShareBalance().add(amount));
                        userBalance.setFrozenShare(userBalance.getFrozenShare().subtract(amount));

                        int result = jijinUserBalanceRepository.updateFundAccount(userBalance);

                        if (result == 1) {

                            //insert user balance history
                            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalance, jijinTradeRecordDTO.getTrxDate(), amount, new BigDecimal("0"), "赎回确认失败，退还冻结份额", BalanceHistoryBizType.赎回确认失败, jijinTradeRecordDTO.getId());
                            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
                            jijinTradeLogRepository.insertBusJijinTradeLog(new JijinTradeLogDTO(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode(), jijinTradeRecordDTO.getId(), TradeRecordStatus.FAIL.name(), TradeRecordType.REDEEM, null, amount, jijinTradeRecordDTO.getTrxTime(), jijinTradeRecordDTO.getTrxDate()));

                			jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "0", "status", TradeRecordStatus.FAIL.name()));
    
                            String remark = "赎回陆金宝失败，资金在陆金宝中";
            	            JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getFundCode());
            	            JijinFrozenDetailDTO jijinFrozenDetailDTO = new JijinFrozenDetailDTO(YebTransactionType.SELL_FAIL_UNFREEZE.getCode(), jijinUserBalanceDTO.getId(), null, jijinTradeRecordDTO.getReqShare(), jijinUserBalanceDTO.getFrozenShare(), remark);
            	            jijinFrozenDetailRepository.insertJijinFrozenDetail(jijinFrozenDetailDTO);
                            
                          //send MQ to yeb                          
                           if("LBO".equals(jijinTradeRecordDTO.getChannel())){
                           	mqService.sendLBOTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail","REDEEM",jijinTradeConfirmDTO.getTradeResCode());
                           }else{
                           	mqService.sendDhTradeResultMsg(jijinTradeRecordDTO.getTrxId(), "fail");
                           } 
                        }
                        return;
                    }

                    //提交失败，交易确认为失败，按道理不会有交易确认记录，现在处理成什么都不做
                    if (jijinTradeRecordDTO.getStatus().equals(TradeRecordStatus.SUBMIT_FAIL.name())) {
                    	jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                        Logger.info(this, String.format("大华 tradeSyncStatus is fail, but trade record status is not in condition do nothing tradeRecordStatus [%s],recordId [%s] ", jijinTradeRecordDTO.getStatus(), jijinTradeRecordDTO.getId()));
                        return;
                    }
        		}
        	} else { //申购
            if (!jijinTradeConfirmDTO.getTradeResCode().equals("0000")) {
                jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "大华确认状态为失败,与交易状态不符,需要人工处理"));
                return;
            }
            if (jijinTradeConfirmDTO.getAmount().compareTo(jijinTradeRecordDTO.getReqAmount()) != 0) {
                jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
                jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "1", "errorMsg", "大华确认金额与交易金额不符,需要人工处理"));
                return;
            }

            jijinTradeConfirmRepository.updateJijinTradeConfirmStatusById(jijinTradeConfirmDTO.getId(), TradeConfirmStatus.DISPATCH.name());
            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "isControversial", "0"));
        }


    }
    
    
    /**
     * 转入银行卡的入账
     * @param jijinTradeRecordDTO
     * @param jijinInfoDTO
     * @return
     */
    private PaymentResult transferToBankCardAudit(JijinTradeRecordDTO jijinTradeRecordDTO,JijinInfoDTO jijinInfoDTO ){
    	PaymentResult res = new PaymentResult();  	
    	
    	PlusFreezeInstructionDetail detail = new PlusFreezeInstructionDetail(jijinTradeRecordDTO.getReqShare(), jijinTradeRecordDTO.getAppSheetNo(),
    			jijinInfoDTO.getInstId(), jijinTradeRecordDTO.getTrxDate(), "赎回调增",
    			"赎回调增", String.valueOf(jijinTradeRecordDTO.getId()), "赎回调增",
    			String.valueOf(jijinTradeRecordDTO.getId()), "赎回调增入账",
    			"FORZEN_FUND", "赎回调增冻结",
    			String.valueOf(jijinTradeRecordDTO.getId()), "赎回调增冻结", jijinInfoDTO.getProductCode(),
    			DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_FORMAT), jijinTradeRecordDTO.getAppSheetNo(), "赎回调增",
    			"赎回调增转银行卡");
    	
    	
    	List<Instruction> prepareOperationList = new ArrayList<Instruction>();
    	Instruction ins = new Instruction("PLUS_FREEZE",JsonHelper.toJson(detail));
    	prepareOperationList.add(ins);
    	PaymentWithdrawRequest request = new PaymentWithdrawRequest("JIJIN", String.valueOf(jijinTradeRecordDTO.getId()),
    			"0", jijinTradeRecordDTO.getUserId(), jijinTradeRecordDTO.getReqShare(),
    			"CUSTOMER_WITHDRAWAL", DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_FORMAT), "陆金宝赎回进银行卡",
    			null, null, prepareOperationList, jijinInfoDTO.getProductCode(),
    			jijinTradeRecordDTO.getTrxDate(), jijinTradeRecordDTO.getAppSheetNo(), "大华赎回进银行卡",
    			"大华赎回进银行卡", String.valueOf(jijinTradeRecordDTO.getId()), "大华赎回进银行卡");
    	
    	res = paymentAppService.cashierWithdraw(request);
    	
    	return res;
    }
    
}
