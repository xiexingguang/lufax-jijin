package com.lufax.jijin.fundation.service;

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.*;
import com.lufax.jijin.fundation.constant.*;
import com.lufax.jijin.fundation.dto.*;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.gson.PurchaseOrderResultGson;
import com.lufax.jijin.fundation.gson.PurchaseResultGson;
import com.lufax.jijin.fundation.repository.*;
import com.lufax.jijin.fundation.service.domain.FileHolder;
import com.lufax.jijin.fundation.util.PurchaseFileContentUtil;
import com.lufax.jijin.service.MqService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinAccountRepository jijinAccountRepository;
    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private MqService mqService;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;
    @Autowired
    private TradeDayService tradeDayService;
    @Autowired
    private JijinPAFBuyAuditRepository jijinPAFBuyAuditRepository;
    @Autowired
    private JijinyT0SyncService jijinyT0SyncService;
    @Autowired
    private JijinyT1SyncService jijinyT1SyncService;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;

    private static final int ROWNUM = 500;

    public MqService getMqService() {
        return mqService;
    }

    public void setMqService(MqService mqService) {
        this.mqService = mqService;
    }

    /**
     * 落地大华基金申购请求信息
     *
     * @param fundCode
     * @param userId
     * @param amount
     * @param type
     * @param transactionId
     * @param frozenCode
     * @param isAgreeRisk
     * @param frozenType
     * @param prodCode
     * @param channel    - "YEB,LBO"
     * @return
     */
    @Transactional
    public PurchaseResultGson orderPurchase(String fundCode, String userId, BigDecimal amount, String type, String transactionId, String frozenCode, String isAgreeRisk, String frozenType,String prodCode,String channel) {

        PurchaseResultGson purchaseResultGson = new PurchaseResultGson();

        String retMsg = "";
        if (StringUtils.isBlank(fundCode)) {
            retMsg = "fundCode is empty|";
        }
        if (StringUtils.isBlank(userId)) {
            retMsg = retMsg + "userId is empty|";
        }
        if (null == amount) {
            retMsg = retMsg + "amount is empty|";
        }
        if (StringUtils.isBlank(type)) {
            retMsg = retMsg + "type is empty|";
        }
        if (StringUtils.isBlank(transactionId)) {
            retMsg = retMsg + "transactionId is empty|";
        }
        if (StringUtils.isBlank(frozenCode)) {
            retMsg = retMsg + "frozenCode is empty|";
        }
        if (StringUtils.isBlank(isAgreeRisk)) {
            retMsg = retMsg + "isAgreeRisk is empty|";
        }
        if (StringUtils.isBlank(frozenType)) {
            retMsg = retMsg + "frozenType is empty|";
        }
        
        purchaseResultGson.setRetMessage(retMsg);

        if (!StringUtils.isBlank(purchaseResultGson.getRetMessage())) {
            purchaseResultGson.setRetCode("09");
            return purchaseResultGson;
        }

        JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", fundCode));
        if (null == jijinInfoDTO) {
            Logger.info(this, String.format("jijinInfo is null,fundCode [%s]", fundCode));
            purchaseResultGson.setRetMessage("jijinInfo not exists");
            purchaseResultGson.setRetCode("99");
            return purchaseResultGson;
        }

        JijinAccountDTO jijinAccountDTO = jijinAccountRepository.findActiveAccount(Long.valueOf(userId), jijinInfoDTO.getInstId(), "PAF");
        if (jijinAccountDTO == null) {
            Logger.info(this, String.format("jijinAccount is null,userId [%s]", userId));
            purchaseResultGson.setRetMessage("jijinAccount is null,");
            purchaseResultGson.setRetCode("99");
            return purchaseResultGson;
        }

        JijinTradeRecordDTO jijinTradeRecordDTO = jijinTradeRecordRepository.getRecordByTrxIdAndTypeChannel(Long.valueOf(transactionId), type,channel);
        if (jijinTradeRecordDTO != null) {
            Logger.info(this, String.format("jijinTradeRecord is exists,transactionId [%s]", transactionId));
            purchaseResultGson.setRetMessage("jijinTradeRecord is exists");
            purchaseResultGson.setRetCode("00");
            return purchaseResultGson;
        }

        String bizType = type.equals(TradeRecordType.PURCHASE.name()) ? "022" : "020";
        String appNo = sequenceService.getSerialNumber(bizType);
        jijinTradeRecordDTO = new JijinTradeRecordDTO();
        jijinTradeRecordDTO.setUserId(Long.valueOf(userId));
        jijinTradeRecordDTO.setFundCode(fundCode);
        jijinTradeRecordDTO.setStatus(TradeRecordStatus.INIT.name());
        jijinTradeRecordDTO.setType(TradeRecordType.valueOf(type));
        jijinTradeRecordDTO.setReqAmount(amount);
        jijinTradeRecordDTO.setContractNo(jijinAccountDTO.getContractNo());
        jijinTradeRecordDTO.setAppNo(appNo);
        jijinTradeRecordDTO.setChargeType("A");
        jijinTradeRecordDTO.setTrxId(Long.valueOf(transactionId));
        jijinTradeRecordDTO.setFrozenCode(frozenCode);
        jijinTradeRecordDTO.setFrozenType(frozenType);
        jijinTradeRecordDTO.setChannel(channel); // "YEB","LBO","PAF"
        jijinTradeRecordDTO.setDividendType(jijinInfoDTO.getDividendType());
        jijinTradeRecordDTO.setInstId(jijinInfoDTO.getInstId());
        jijinTradeRecordDTO.setIsAgreeRisk(isAgreeRisk);
        jijinTradeRecordDTO.setFlag("1");
        jijinTradeRecordDTO.setIsControversial("-1");
        jijinTradeRecordDTO.setUkToken(channel + transactionId);
        JijinTradeRecordDTO result = jijinTradeRecordRepository.insertJijinTradeRecord(jijinTradeRecordDTO);
        //落地成功后发送消息给自己消费 接着做下单处理
        //落地成功后发送消息给自己消费 ,通过MQ做调用基金，调用paf，调用基金
        if ("dh103".equals(jijinInfoDTO.getInstId())) {
            mqService.sendCurrencyJijinBuyMQ(jijinTradeRecordDTO.getId());
        } else {
            mqService.sendJijinBuyMQ(jijinTradeRecordDTO.getId());
        }

        purchaseResultGson.setTradeRecordId(result.getId());
        purchaseResultGson.setRetCode("00");
        purchaseResultGson.setRetMessage("order purchase success");
        return purchaseResultGson;

    }

    /**
     * 三步曲处理T0货币基金认申购请求
     * 没有异常则三部曲一次完成
     * 如有有异常，则通过MQ重试
     *
     * @param jijinTradeRecordDTO
     */
    public void buyCurrencyJijin(JijinTradeRecordDTO jijinTradeRecordDTO) {

        String currentStatus = jijinTradeRecordDTO.getStatus();
        if (TradeRecordStatus.INIT.name().equals(currentStatus)) {
            Logger.info(this, String.format("[currency jijin] start call jijin buy, recordId: [%s]", jijinTradeRecordDTO.getId()));
            jijinyT0SyncService.callT0JijinPurchase(jijinTradeRecordDTO);
            Logger.info(this, String.format("[currency jijin] start call jijin buy, recordId: [%s]", jijinTradeRecordDTO.getId()));
        }

        if (TradeRecordStatus.SUBMIT_SUCCESS.name().equals(jijinTradeRecordDTO.getStatus())) {
            Logger.info(this, String.format("[currency jijin] start call paf withdraw, recordId: [%s]", jijinTradeRecordDTO.getId()));
            jijinyT0SyncService.doT0WithdrawByPAF(jijinTradeRecordDTO);
            Logger.info(this, String.format("[currency jijin] end call paf withdraw, recordId: [%s]", jijinTradeRecordDTO.getId()));
        }

        if (TradeRecordStatus.WITHDRAW_SUCCESS.name().equals(jijinTradeRecordDTO.getStatus())) {
            Logger.info(this, String.format("[currency jijin] start call jijin notify, recordId: [%s]", jijinTradeRecordDTO.getId()));
            jijinyT0SyncService.buyApplyT0Notify(jijinTradeRecordDTO);
            Logger.info(this, String.format("[currency jijin] end call jijin notify, recordId: [%s]", jijinTradeRecordDTO.getId()));
        }
    }


    /**
     * 三步曲处理T1认申购请求
     * 没有异常则三部曲一次完成
     * 如有有异常，则通过MQ重试
     *
     * @param jijinTradeRecordDTO
     */
    public void buyJijin(JijinTradeRecordDTO jijinTradeRecordDTO) {

        String currentStatus = jijinTradeRecordDTO.getStatus();
        if (TradeRecordStatus.INIT.name().equals(currentStatus)) {
            Logger.info(this, String.format("start call jijin buy, recordId: [%s]", jijinTradeRecordDTO.getId()));
            jijinyT1SyncService.callT1JijinPurchase(jijinTradeRecordDTO);
            Logger.info(this, String.format("end call jijin buy, recordId: [%s]", jijinTradeRecordDTO.getId()));
        }

        if (TradeRecordStatus.SUBMIT_SUCCESS.name().equals(jijinTradeRecordDTO.getStatus())) {
            Logger.info(this, String.format("start call paf withdraw, recordId: [%s]", jijinTradeRecordDTO.getId()));
            jijinyT1SyncService.doT1WithdrawByPAF(jijinTradeRecordDTO);
            Logger.info(this, String.format("end call paf withdraw, recordId: [%s]", jijinTradeRecordDTO.getId()));
        }

        if (TradeRecordStatus.WITHDRAW_SUCCESS.name().equals(jijinTradeRecordDTO.getStatus())) {
            Logger.info(this, String.format("start call jijin notify, recordId: [%s]", jijinTradeRecordDTO.getId()));
            jijinyT1SyncService.buyApplyT1Notify(jijinTradeRecordDTO);
            Logger.info(this, String.format("end call jijin notify, recordId: [%s]", jijinTradeRecordDTO.getId()));
        }
    }

    public void generatePurchaseFile(FileHolder fileHolder, String bizDate, String bizType) {

        //首先判断当天的平安付划款明细是否有处理完成
        List<JijinSyncFileDTO> pafBuyAuditList = jijinSyncFileRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("bizType", SyncFileBizType.PAF_BUY_AUDIT, "status", SyncFileStatus.READ_SUCCESS, "bizDate", bizDate));
        if (CollectionUtils.isEmpty(pafBuyAuditList)) {
            Logger.info(this, String.format("pafBuyAudit file not read finish,do nothing"));
            return;
        }
        Long lfxAuditFileId = 0L;
        for (JijinSyncFileDTO jijinSyncFileDTO : pafBuyAuditList) {
            if (jijinSyncFileDTO.getFileName().indexOf("lfx201") != -1) {
                lfxAuditFileId = jijinSyncFileDTO.getId();
                break;
            }
        }

        if (lfxAuditFileId.compareTo(0L) == 0) {
            Logger.info(this, String.format("pafBuyAudit file not exist,do nothing"));
            return;
        }

        //判断平安付划款明细是否有数据
        PafBuyAuditCountDTO countDto = jijinPAFBuyAuditRepository.countByFileIdAndStatus(lfxAuditFileId, "");
        if (countDto != null && countDto.getTotal() != 0) {
            PafBuyAuditCountDTO pafBuyAuditCountDTO = jijinPAFBuyAuditRepository.countByFileIdAndStatus(lfxAuditFileId, "NEW");
            if (pafBuyAuditCountDTO != null && pafBuyAuditCountDTO.getTotal() != 0) {
                Logger.info(this, String.format("pafBuyAudit file not dispatch finish,do nothing"));
                return;
            }
        }

        List<JijinSyncFileDTO> list = new ArrayList<JijinSyncFileDTO>();

        String filePathAndName = fileHolder.getFileAbsolutePath() + fileHolder.getFileName();

        JijinSyncFileDTO existFile = jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("fileName", filePathAndName));
        if (existFile == null) {
            JijinSyncFileDTO dto = new JijinSyncFileDTO();
            dto.setBizDate(bizDate);
            dto.setBizType(bizType);
            dto.setCurrentLine(1l);
            dto.setFileName(filePathAndName);
            dto.setRetryTimes(0l);
            dto.setStatus(SyncFileStatus.INIT.name());
            list.add(dto);
        }

        if (!CollectionUtils.isEmpty(list))
            jijinSyncFileRepository.batchInsertBusJijinSyncFile(list);

        list = jijinSyncFileRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("status", SyncFileStatus.INIT.name(), "bizType", bizType));


        String lastTradeDay = tradeDayService.getLastTradeDay(DateUtils.parseDate(bizDate, DateUtils.DATE_STRING_FORMAT));
        Date createFrom = DateUtils.parseDate(lastTradeDay + "150000", DateUtils.XINBAO_MOBILENO_SEQUENCE_FORMATE);
        Date createTo = DateUtils.parseDate(bizDate + "150000", DateUtils.XINBAO_MOBILENO_SEQUENCE_FORMATE);
        List<String> types = Arrays.asList("PURCHASE", "APPLY");
        for (JijinSyncFileDTO targetFile : list) {


            JijinTradeRecordCountDTO count = jijinTradeRecordRepository.countTradeRecordByCreatedAtAndTypes(createFrom, createTo, "lfx201", types);
            if (count.getTotal() == 0) {
                try {
                    genFile(targetFile.getFileName(), createFrom, createTo, count, true, types);
                    jijinSyncFileRepository.updateBusJijinSyncFileStatus(targetFile.getId(), SyncFileStatus.WRITE_SUCCESS, "今日无交易");
                } catch (IOException e) {
                    Logger.error(this, "代销申购文件生成失败，文件名：" + targetFile.getFileName());
                    jijinSyncFileRepository.updateBusJijinSyncFileStatus(targetFile.getId(), SyncFileStatus.WRITE_FAIL, "IO exception,生成文件失败");
                }
            } else {
                try {
                    genFile(targetFile.getFileName(), createFrom, createTo, count, false, types);
                    jijinSyncFileRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", targetFile.getId(), "status", SyncFileStatus.WRITE_SUCCESS.name(), "currentLine", count.getTotal() + 2));
                } catch (IOException e) {
                    Logger.error(this, "代销申购文件生成失败，文件名：" + targetFile.getFileName());
                    jijinSyncFileRepository.updateBusJijinSyncFileStatus(targetFile.getId(), SyncFileStatus.WRITE_FAIL, "IO exception,生成文件失败");
                }
            }

        }

    }

    private void genFile(String fileName, Date createFrom, Date createTo, JijinTradeRecordCountDTO countDto, boolean isEmpty, List<String> types) throws IOException {

        long time = System.currentTimeMillis();
        Logger.info(this, String.format("start gen lfx purchase file,start time: [%s]ms", time));
        String tmpFileName = fileName + ".tmp";
        File tmpFile = FileUtils.createEmptyFile(tmpFileName);

        String header = PurchaseFileContentUtil.createPurchaseFileHeader(countDto.getTotal());
        String columnName = PurchaseFileContentUtil.createPurchaseFileColumnName();

        FileUtils.appendContent(tmpFile, header, "UTF-8");
        FileUtils.appendContent(tmpFile, columnName, "UTF-8");

        if (!isEmpty) {
            int start = 1;
            int end = countDto.getTotal();
            while (start <= end) {

                int targetEnd = 0;
                targetEnd = start + ROWNUM;

                List<JijinTradeRecordDTO> dtos = jijinTradeRecordRepository.getRecordsByCreatedAtAndTypes(createFrom, createTo, start, targetEnd, "lfx201", types);
                String content = "";
                for (JijinTradeRecordDTO dto : dtos) {
                    content = PurchaseFileContentUtil.createPurchaseFileContent(dto);
                    FileUtils.appendContent(tmpFile, content, "UTF-8");
                }
                start = targetEnd;
            }
        }

        tmpFile.renameTo(new File(fileName));
        Logger.info(this, String.format("end gen lfx purchase file,spend [%s]ms", System.currentTimeMillis() - time));
    }

    @Transactional
    public Boolean batchInsertForZhuanGou(JijinUserBalanceDTO userBalanceDTO, JijinTradeRecordDTO jijinTradeRecordDTO, PurchaseOrderResultGson purchaseOrderResultGson) {
        userBalanceDTO.setShareBalance(userBalanceDTO.getShareBalance().subtract(jijinTradeRecordDTO.getReqAmount()));
        if (null == userBalanceDTO.getFrozenShare()) {
            userBalanceDTO.setFrozenShare(jijinTradeRecordDTO.getReqAmount());
        } else {
            userBalanceDTO.setFrozenShare(userBalanceDTO.getFrozenShare().add(jijinTradeRecordDTO.getReqAmount()));
        }
        int result = jijinUserBalanceRepository.updateFundAccount(userBalanceDTO);
        if (result != 1) {
            return false;
        }
        jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", jijinTradeRecordDTO.getId(), "errorCode", purchaseOrderResultGson.getErrorCode(), "trxTime", purchaseOrderResultGson.getTransactionTime(), "trxDate", purchaseOrderResultGson.getTransactionDate(), "appSheetNo", purchaseOrderResultGson.getAppSheetSerialNo(), "status", TradeRecordStatus.SUBMIT_SUCCESS.name()));
        JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(userBalanceDTO, purchaseOrderResultGson.getTransactionDate(), new BigDecimal("0"), jijinTradeRecordDTO.getReqAmount(), "转购下单成功,冻结份额", BalanceHistoryBizType.转购下单成功, jijinTradeRecordDTO.getId());
        jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
        return true;
    }

}
