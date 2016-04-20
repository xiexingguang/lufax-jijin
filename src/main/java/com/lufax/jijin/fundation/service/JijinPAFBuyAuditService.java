package com.lufax.jijin.fundation.service;


import com.lufax.jijin.base.utils.JijinInstIdPlatMerchantIdMapHolder;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.dto.*;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.remote.FundAppCallerService;
import com.lufax.jijin.fundation.remote.gson.request.*;
import com.lufax.jijin.fundation.remote.gson.response.FundResponseGson;
import com.lufax.jijin.fundation.repository.*;
import com.lufax.jijin.fundation.service.domain.JiJinDateUtil;
import com.lufax.jijin.service.MqService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class JijinPAFBuyAuditService {

    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinPAFBuyAuditRepository jijinPAFBuyAuditRepository;
    @Autowired
    private JijinInstIdPlatMerchantIdMapHolder jijinInstIdPlatMerchantIdMapHolder;
    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private FundAppCallerService fundAppCallerService;
	@Autowired
    private JijinTradeLogRepository jijinTradeLogRepository;
    @Autowired
    private MqService mqService;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private JiJinDateUtil jiJinDateUtil;

    public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
    public void setFundAppCallerService(FundAppCallerService fundAppCallerService) {
		this.fundAppCallerService = fundAppCallerService;
	}

     /**
      *  清销认申购账
     *
     * @param jijinPAFBuyAuditDTO
     */

    public void dispatchPAFBuyAudit(JijinPAFBuyAuditDTO jijinPAFBuyAuditDTO) {

        BigDecimal amount = jijinPAFBuyAuditDTO.getTxnAmount();
        String appSheetNo = jijinPAFBuyAuditDTO.getFundSeqId();
        String distributorCode = jijinPAFBuyAuditDTO.getDistributorCode();
        Map<String, String> mapping = jijinInstIdPlatMerchantIdMapHolder.jijinInstIdAndFundSaleCodeMap;

        Iterator<Entry<String, String>> iter = mapping.entrySet().iterator();
        String instId = "";
        while (iter.hasNext()) {
            Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();

            if (val.equals(distributorCode)) {
                instId = key;
                break;
            }
        }

        if (StringUtils.isEmpty(instId)) {
            Logger.error(this, "upload paf buy result fail, can not fins corresponding instId for distributorCode:" + distributorCode);
            return;
        }

        JijinTradeRecordDTO tradeRecord = jijinTradeRecordRepository.getRecordByAppSheetNoAndInstId(appSheetNo, instId);

        if (null == tradeRecord) {
            jijinPAFBuyAuditDTO.setStatus("UNMATCH");
        } else {
            if ("1".equals(tradeRecord.getFlag())) {
                JijinInfoDTO jijinInfo = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", tradeRecord.getFundCode()));
                if (TradeRecordStatus.SUBMIT_SUCCESS.name().equals(tradeRecord.getStatus())) {
                    //如果交易记录停留在submit_success状态上，说明实时调用平安付时超时了
                    //在这里做补偿操作，虚拟户解冻调减

                    // 使用清算接口做解冻调减
                    String paymentOrderNo = "";
                    String frozenCode = "";
                    if (tradeRecord.getFrozenType().equals("NORMAL")) {//区分是普通的解冻减钱还是先投资后代扣的解冻减钱
                        frozenCode = tradeRecord.getFrozenCode();
                    } else {
                        paymentOrderNo = tradeRecord.getFrozenCode();
                    }

                    String remark = "";
                    String expectConfirmDate ="";
                    if (tradeRecord.getType() == TradeRecordType.APPLY) {
                        remark = "认购：" + jijinInfo.getFundName();
                        expectConfirmDate = "新发基金待确认";
                    } else if (tradeRecord.getType() == TradeRecordType.PURCHASE) {
                        remark = "申购：" + jijinInfo.getFundName();
                    }
                    String subTransactionType = tradeRecord.getType() == TradeRecordType.APPLY ? TransactionType.EXPENSE_APPLY.name() : TransactionType.EXPENSE_PURCHASE.name();

                    PaymentInfo info = new PaymentInfo(paymentOrderNo, frozenCode, "CLEAR_SUBTRACT", TransactionType.UNFROZEN_FUND.name(), remark,
                            new SubTractInfo(tradeRecord.getAppSheetNo(), tradeRecord.getReqAmount(), subTransactionType, remark, jijinInfo.getInstId(), jijinInfo.getProductCode(), tradeRecord.getTrxDate()), null);
                    PaymentRequestGson request = new PaymentRequestGson("JIJIN", String.valueOf(tradeRecord.getId()), info);

                    boolean result = eventService.callFundAuditPayment(request,tradeRecord);

                    if (result) {
                        eventService.updateApplyingAmount(tradeRecord.getUserId(), tradeRecord.getFundCode(), tradeRecord.getReqAmount(), tradeRecord.getTrxDate(), tradeRecord.getId());
                        if (tradeRecord.getInstId().equals("dh103")) {
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "isControversial", "-1","errorMsg", "补偿解冻调减", "status", TradeRecordStatus.NOTIFY_SUCCESS.name()));
                        } else {
                            //WEBDEV-8355:基金(PC)赎回到账时间显示优化
                            //申购或认购返回预确认时，需要保持预计确认日期
                            if(StringUtils.isBlank(expectConfirmDate)){
                                expectConfirmDate = jiJinDateUtil.getConfirmDate(TradeRecordType.PURCHASE.name(),tradeRecord.getTrxTime(),
                                        jijinInfo.getFundType());
                            }
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap(
                                    "id", tradeRecord.getId(),
                                    "isControversial", "0",
                                    "errorMsg", "补偿解冻调减",
                                    "status", TradeRecordStatus.NOTIFY_SUCCESS.name(),
                                    "expectConfirmDate",expectConfirmDate));
                        }

                        mqService.sendJijinWithdrawSuccessMsg(tradeRecord.getId());
                    } else {
                        //此处报警需要给先投资后代扣的tradeRecord做资金解冻并且扣减虚拟户
                        Logger.error(this, String.format("NEED_MANUAL_CONFIRM,do buy audit call fund do unfrozen failed,tradeRecordId [%s]", tradeRecord.getId()));
                    }
                }
                //调用fund接口清算帐目
                DetailResultList detailResult = new DetailResultList(appSheetNo, amount, tradeRecord.getTrxDate(), instId, "2", jijinInfo.getProductCode());
                List<DetailResultList> list = new ArrayList<DetailResultList>();
                list.add(detailResult);
                UploadResultGson request = new UploadResultGson("JIJIN", String.valueOf(tradeRecord.getId()), list);

                boolean result = callFundAuditUploadResult(request);

                if (!result) {
                    jijinPAFBuyAuditDTO.setStatus("ERROR");
                } else {
                    jijinPAFBuyAuditDTO.setStatus("DISPATCHED");
                    //大华直连基金做补偿
                    if (tradeRecord.getInstId().equals("dh103")) {
                        if (tradeRecord.getStatus().equals(TradeRecordStatus.SUBMIT_SUCCESS.name()) || tradeRecord.getStatus().equals(TradeRecordStatus.WITHDRAW_SUCCESS.name()) || tradeRecord.getStatus().equals(TradeRecordStatus.NOTIFY_FAIL.name())) {
                            updateShareBalance(tradeRecord.getUserId(), tradeRecord.getFundCode(), tradeRecord.getReqAmount(), tradeRecord.getTrxDate(), tradeRecord.getId());
                            if("LBO".equals(tradeRecord.getChannel())){
                            	mqService.sendLBOTradeResultMsg(tradeRecord.getTrxId(), "success","PURCHASE","000");
                            }else{
                            	mqService.sendDhTradeResultMsg(tradeRecord.getTrxId(), "success");
                            } 
                            jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", tradeRecord.getId(), "status", TradeRecordStatus.SUCCESS.name()));
                            JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO(tradeRecord.getUserId(), tradeRecord.getFundCode(), tradeRecord.getId(), TradeRecordStatus.SUCCESS.name(), tradeRecord.getType(), tradeRecord.getReqAmount(), tradeRecord.getReqAmount(), tradeRecord.getTrxTime(), tradeRecord.getTrxDate());
                            jijinTradeLogDTO.setFee(new BigDecimal(0));
                            jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
                        }
                    }
                }
            } else {
                // 老接口调用的认申购，不用走清算接口
                Logger.info(this, String.format("Old trade record [%s], no need to do audit", tradeRecord.getId()));
                jijinPAFBuyAuditDTO.setStatus("DISPATCHED");
            }
        }

        jijinPAFBuyAuditRepository.updateBusJijinPAFBuyAuditDTO(jijinPAFBuyAuditDTO);
        Logger.info(this, String.format("end dispatch PAF buy audit, PAF buy audit id:[%s]", jijinPAFBuyAuditDTO.getId()));
    }


    /**
     * 认申购清账
     *
     * @param uploadResultGson
     */
    private boolean callFundAuditUploadResult(UploadResultGson uploadResultGson) {
        int retry_times = 0;
        FundResponseGson fundResponseGson = new FundResponseGson();
        boolean result = false;
        while (!result) {
            fundResponseGson = fundAppCallerService.auditUploadResult(uploadResultGson);

            if ("SUCCESS".equals(fundResponseGson.getResultStatus())) {
                result = true;
            } else if ("PROCESSING".equals(fundResponseGson.getResultStatus()) || StringUtils.isEmpty(fundResponseGson.getResultStatus())) {
                result = false;
            } else {
                Logger.info(this, String.format("This trade record [%s], audit remote service response fail, retCode [%s]", uploadResultGson.getInstructionNo(), fundResponseGson.getRetCode()));
                result = false;
                break;
            }
            retry_times = retry_times + 1;
            if (retry_times > 100 || result) break;
        }
        return result;
    }

    /**
     * 更新申购份额
     *
     * @param userId
     * @param fundCode
     * @param reqAmount
     */
    @Transactional
    public void updateShareBalance(Long userId, String fundCode, BigDecimal reqAmount, String bizDate, Long tradeRecordId) {
        int result = 0;
        int retry_times = 0;
        JijinUserBalanceDTO userBalanceDTO = new JijinUserBalanceDTO();
        while (result == 0) {
            userBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(userId, fundCode);
            userBalanceDTO.setShareBalance(userBalanceDTO.getShareBalance().add(reqAmount));
            userBalanceDTO.setApplyingAmount(userBalanceDTO.getApplyingAmount().subtract(reqAmount));
            result = jijinUserBalanceRepository.updateFundAccount(userBalanceDTO);
            retry_times = retry_times + 1;
            if (retry_times > 10) break;
        }
        // insert user balance history
        if (result == 1) {
            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalanceDTO, bizDate, reqAmount, new BigDecimal("0"), "转入申购成功份额", BalanceHistoryBizType.申购确认成功, tradeRecordId);
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
        }
    }
}
