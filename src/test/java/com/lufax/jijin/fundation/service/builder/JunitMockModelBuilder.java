package com.lufax.jijin.fundation.service.builder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.constant.TradeConfirmStatus;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinDividendDTO;
import com.lufax.jijin.fundation.dto.JijinDividendDTO.Status;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinPAFBuyAuditDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.dto.JijinThirdPaySyncDTO;
import com.lufax.jijin.fundation.dto.JijinTradeConfirmDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceAuditDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.dto.JijinWithdrawRecordDTO;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.jijin.fundation.repository.JijinDividendRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinPAFBuyAuditRepository;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.repository.JijinThirdPaySyncRepository;
import com.lufax.jijin.fundation.repository.JijinTradeConfirmRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinTradeSyncRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceAuditRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.repository.JijinWithdrawRecordRepository;
import com.lufax.jijin.fundation.service.domain.Dividend;
import com.lufax.mq.client.util.MapUtils;

@Component
public class JunitMockModelBuilder {

	@Autowired
	private JijinTradeRecordRepository jijinTradeRecordRepository;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinDividendRepository dividendRepo;
    @Autowired
    private JijinThirdPaySyncRepository jijinThirdPaySyncRepository;
	@Autowired
	protected JijinAccountRepository jijinAccountRepository;
    @Autowired
    protected JijinPAFBuyAuditRepository jijinPAFBuyAuditRepository;
    @Autowired
    private JijinTradeSyncRepository jijinTradeSyncRepository;
    @Autowired
    private JijinWithdrawRecordRepository jijinWithdrawRecordRepository;
    @Autowired
    private JijinSyncFileRepository jijinSyncFileRepository;
    @Autowired
    private JijinTradeConfirmRepository jijinTradeConfirmRepository;
    @Autowired
    private JijinUserBalanceAuditRepository jijinUserBalanceAuditRepository;

    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	public  JijinTradeRecordDTO buildTradeRecord(TradeRecordStatus status,TradeRecordType type, String fundCode,String instId,String trxDate, Long retryTimes) {

		JijinTradeRecordDTO dto = new JijinTradeRecordDTO();
		dto.setStatus(status.name());
		dto.setUserId(1L);
		dto.setAppNo("lufaxRequestNo");
		dto.setAppSheetNo("appSheetNo");
		dto.setCancelAppNo("56789101199");
		dto.setCancelAppSheetNo("8765555599");
		dto.setChargeType("A");
		dto.setContractNo("LU000001");
		dto.setDividendType("1");
		dto.setFundCode(fundCode);
		dto.setIsControversial("-1");
		dto.setReqAmount(BigDecimal.TEN);
		dto.setReqShare(BigDecimal.TEN);
		dto.setType(type);
		dto.setTrxDate(trxDate);
		dto.setTrxTime("20190923120101");
		dto.setTrxId(1l);
		dto.setFrozenCode("123456");
		dto.setChannel("PAF");
		dto.setFrozenType("NORMAL");
		dto.setPayOrderNo("payOrderNo");
		dto.setPayCancelOrderNo("payCancelOrderNo");
		dto.setNotifyAppNo("notifyAppNo");
		dto.setInstId(instId);
		dto.setIsAgreeRisk("1");
		dto.setRedeemType("0");
		dto.setUkToken("ukToken"+type.name());
		dto.setFlag("1");
		dto.setRetryTimes(retryTimes);
		jijinTradeRecordRepository.insertJijinTradeRecord(dto);

		return dto;
	}
	
	public int updateTradeRecordRetryTimes(Long id,Long retryTimes){
		return jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id",id,"retryTimes",retryTimes));
	}
	
	public JijinUserBalanceDTO buildJijinUserBalance(Long userId,String fundCode) {

		JijinUserBalanceDTO dto = new JijinUserBalanceDTO();
	    dto.setUserId(userId);
	    dto.setFundCode(fundCode);
	    dto.setFrozenShare(new BigDecimal("1220.00"));
	    dto.setShareBalance(new BigDecimal("122.23"));
	    dto.setFrozenShare(new BigDecimal("0.00"));
	    dto.setApplyingAmount(new BigDecimal("1220.00"));
	    dto.setDividendType("FUND");
	    dto.setVersion(0L);
	    dto.setDividendStatus("DONE");
	    dto.setDividendType("1");
		jijinUserBalanceRepository.insertBusJijinUserBalance(dto);

		return dto;
	}
	
	public JijinAccountDTO buildJijinUserAccount(Long userId,String instId) {

		JijinAccountDTO dto = new JijinAccountDTO();
		dto.setUserId(userId);
		dto.setInstId(instId);
		dto.setPayNo("pay_test_max");
		dto.setChannel("PAF");
		dto.setContractNo("contractNo");
		dto.setCustNo("customerNo");
		dto.setDeleted(false);
		jijinAccountRepository.insertBusJijinAccount(dto);

		return dto;
	}
	 
	/**
	 * 
	 * @param fundCode
	 * @param instId
	 * @param fundType
	 * @return
	 */
	 public JijinInfoDTO buildJijinInfo(String fundCode, String instId,String fundType) {
	        JijinInfoDTO dto = new JijinInfoDTO();
	        dto.setFundCode(fundCode);
	        dto.setInstId(instId);
	        dto.setFundBrand("");
	        dto.setFundName("");
	        dto.setIsFirstPublish(1);
	        dto.setFundType(fundType);
	        dto.setRiskLevel("0");
	        dto.setIsBuyDailyLimit("1");
	        dto.setBuyDailyLimit(new BigDecimal("50000"));
	        dto.setBuyFeeRateDesc("");
	        dto.setBuyFeeDiscountDesc("");
	        dto.setMinInvestAmount(new BigDecimal("1000"));
	        dto.setRedemptionFeeRateDesc("");
	        dto.setChargeType("");
	        dto.setRedemptionArrivalDay(3);
	        dto.setFundOpeningType("1");
	        dto.setEstablishedDate(new Date());
	        dto.setDividendType("0");
	        dto.setTrustee("");
	        dto.setForeignId("");
	        dto.setProductCategory("802");
	        dto.setSourceType("");
	        dto.setProductCode("1234");
	        dto.setProductId(12343l);
	        dto.setAppliedAmount("");
	        dto.setBuyStatus("");
	        dto.setRedemptionStatus("");
	        dto.setCreatedAt(new Date());
	        dto.setUpdatedAt(new Date());
	        dto.setCreatedBy("");
	        dto.setUpdatedBy("");
	        dto.setCollectionMode("7");
	        jijinInfoRepository.addJijinInfo(dto);
	        
	        return dto;
	    }
	 
	 public JijinDividendDTO builddividendDTO(){
		 
		JijinDividendDTO dto = new JijinDividendDTO();
		dto.setInstId("yfd101");
		dto.setAppSheetNo("app_sheet_no_insert");
		dto.setChargeType("A");
		dto.setDividendAmount(BigDecimal.valueOf(0.25));
		dto.setDividendDate(format.format(new Date()));
		dto.setDividendMode("0");
		dto.setDividendShare(BigDecimal.valueOf(1.36));
		dto.setDividendType(Dividend.Type.SWITCH_DIVIDEND.getName());
		dto.setFee(BigDecimal.valueOf(0.50));
		dto.setFileId(10001);
		dto.setFundCode("470009");
		dto.setRightDate(format.format(new Date()));
		dto.setResCode("0000");
		dto.setResMsg("success");
		dto.setStatus(Status.NEW);
		dto.setTrxDate(format.format(new Date()));
		dto.setUserId(1l);
		dividendRepo.insertJijinDividend(dto);
		 
		 return dto;
	 }
	 
	public JijinThirdPaySyncDTO buildJijinThirdPayDTO(String payType) {

		JijinThirdPaySyncDTO dto = new JijinThirdPaySyncDTO();
		dto.setFileId(1L);
		dto.setPayNo("pafNo");
		dto.setPaySerialNo("pafSerialNo");
		dto.setAppSheetNo("appSheetNo");
		dto.setAmount(BigDecimal.ONE);
		dto.setCurrency("CNY");
		dto.setPayType(payType);
		dto.setTrxTime("20150514231512");
		dto.setTrxResult("S");
		dto.setStatus(JijinThirdPaySyncDTO.Status.NEW);
		dto.setChannel("PAF");
		dto.setInstId("yfd101");

		jijinThirdPaySyncRepository.insertBusJijinThirdPaySync(dto);
		return dto;
	}
	
	public JijinPAFBuyAuditDTO buildPafBuyAudit(String appSheetNo,String distributeCode,String status,Long fileId,String trxDate,BigDecimal amount){
		
	    JijinPAFBuyAuditDTO dto = new JijinPAFBuyAuditDTO();
        dto.setCurrency("CNY");
        dto.setCustomerId("1001");
        dto.setDistributorCode(distributeCode);
        dto.setFileId(fileId);
        dto.setFundDate(trxDate);
        dto.setFundSeqId(appSheetNo);
        dto.setFundTime("20150505010101");
        dto.setFundType("fundType");
        dto.setPayAcct("payAcct");
        dto.setPayAcctName("payAcctName");
        dto.setPayBankCode("payBankCode");
        dto.setPayBankName("payBankName");
        dto.setPayOrgId("payOrgId");
        dto.setRecBankCode("recBankCode");
        dto.setRecBankName("recBankName");
        dto.setReceiveAcct("receiveAcct");
        dto.setReceiveAcctName("receiveAcctName");
        dto.setStatus(status);
        dto.setTxnAmount(amount);
        dto.setTxnDate(trxDate);
        dto.setTxnType("txnType");
        dto.setVersion(0l);

        return jijinPAFBuyAuditRepository.insertBusJijinPAFBuyAudit(dto);
	}
	
	public JijinTradeSyncDTO buildTradeSync(String appSheetNo, String businessCode,TradeConfirmStatus status){
		JijinTradeSyncDTO dto = new JijinTradeSyncDTO();
        dto.setFileId(1L);
        dto.setContractNo("contractNo");
        dto.setTaNo("tab");
        dto.setAppSheetNo(appSheetNo);
        dto.setLufaxRequestNo("lufaxRequestNo");
        dto.setFundCompanyCode("fundCompanyCode");
        dto.setTrxDate("2015-05-06");
        dto.setTrxTime("2015-05-06 10:00:00");
        dto.setTrxConfirmDate("2015-05-06 11:00:00");
        dto.setBusinessCode(businessCode);
        dto.setFundCode("470009");
        dto.setChargeType("chargeType");
        dto.setNetValue(BigDecimal.ONE);
        dto.setPurchaseAmount(BigDecimal.ONE);
        dto.setRedeemShare(BigDecimal.ONE);
        dto.setConfirmShare(BigDecimal.ONE);
        dto.setConfirmAmount(BigDecimal.ONE);
        dto.setDividentType("1");
        dto.setTradeResCode("0000");
        dto.setTradeResMemo("tradeResMemo");
        dto.setTaConfirmSign("taConfirmSign");
        dto.setBusinessFinishFlag("businessFinishFlag");
        dto.setPurchaseFee(BigDecimal.ZERO);
        dto.setStatus(status.name());
        jijinTradeSyncRepository.insertJijinTradeSync(dto);
        return dto;
		
	}
	
	public JijinThirdPaySyncDTO getThirdPayRecord(String appSheetNo){
		List<JijinThirdPaySyncDTO> jijinThirdPaySyncList =jijinThirdPaySyncRepository.findBusJijinThirdPaySync(MapUtils.buildKeyValueMap("appSheetNo",appSheetNo));
		return jijinThirdPaySyncList.get(0);
	}
	
	public JijinTradeSyncDTO getTradeSyncRecord(String appSheetNo,String fundCode){
		return jijinTradeSyncRepository.getJijinTradeSyncByAppSheetNoAndFundCode(appSheetNo, fundCode);
	}
	
	public JijinTradeRecordDTO getJijinTradeRecord(Long id){
		return jijinTradeRecordRepository.getRecordById(id);
	}
	
	/**
	 * 
	 * @param recordId
	 * @param stauts
	 * @return
	 */
	public JijinWithdrawRecordDTO buildWithdrawRecord(String recordId, String stauts){
	
		JijinWithdrawRecordDTO jijinWithdrawRecordDTO = new JijinWithdrawRecordDTO();
	    jijinWithdrawRecordDTO.setProductId(100l);
	    jijinWithdrawRecordDTO.setTradeUserId(202l);
	    jijinWithdrawRecordDTO.setTradeAccountNo("303l");
	    jijinWithdrawRecordDTO.setType(1);
	    jijinWithdrawRecordDTO.setOperateDate("20150928");
	    jijinWithdrawRecordDTO.setRequestAmount(BigDecimal.TEN);
	    jijinWithdrawRecordDTO.setRemark("");
	    jijinWithdrawRecordDTO.setVersion(0);
	    jijinWithdrawRecordDTO.setRecordId(recordId);
	    jijinWithdrawRecordDTO.setStatus(stauts);
	    jijinWithdrawRecordRepository.insertJijinWithdrawRecord(jijinWithdrawRecordDTO);
	    
	    return jijinWithdrawRecordDTO;
	}
	
	/**
	 *
	 * @param fileName
	 * @param type
	 * @param status
	 * @return
	 */
	public JijinSyncFileDTO buildJijinSyncFile(String fileName,SyncFileBizType type,SyncFileStatus status,String bizDate){
		JijinSyncFileDTO dto = new JijinSyncFileDTO();
		dto.setSourcePath("");
		dto.setFileName(fileName);
		dto.setBizType(type.name());
		dto.setBizDate(bizDate);
		dto.setCurrentLine(1l);
		dto.setRetryTimes(0l);
		dto.setStatus(status.name());
		dto =(JijinSyncFileDTO)jijinSyncFileRepository.insertBusJijinSyncFile(dto);

		return dto;
	}
	
	
	public JijinSyncFileDTO getJijinSyncFileDTO(Long id){
		return jijinSyncFileRepository.findBusJijinSyncFile(MapUtils.buildKeyValueMap("id",id));
	}
	
	
	public JijinTradeConfirmDTO buildJijinTradeConfirmDTO(String applyDate,String confirmDate,String bizType,String fundCode,
			BigDecimal amount, BigDecimal confirmShare, String tradeResCode, String stauts){
		JijinTradeConfirmDTO jijinTradeConfirmDTO = new JijinTradeConfirmDTO();
        jijinTradeConfirmDTO.setFileId(1L);
        jijinTradeConfirmDTO.setContractNo("contractNo");
        jijinTradeConfirmDTO.setApplyDate(applyDate);
        jijinTradeConfirmDTO.setLufaxRequestNo("lufaxRequestNo");
        jijinTradeConfirmDTO.setAppSheetNo("appSheetNo");
        jijinTradeConfirmDTO.setBizType(bizType);
        jijinTradeConfirmDTO.setFundCode(fundCode);
        jijinTradeConfirmDTO.setConfirmDate(confirmDate);
        jijinTradeConfirmDTO.setAmount(amount);
        jijinTradeConfirmDTO.setConfirmShare(confirmShare);
        jijinTradeConfirmDTO.setCompanyConfirmNo("companyConfirmNo");
        jijinTradeConfirmDTO.setTradeResCode(tradeResCode); //0000
        jijinTradeConfirmDTO.setTradeResMemo("success");
        jijinTradeConfirmDTO.setStatus(stauts);
        jijinTradeConfirmDTO.setPayNo("payNo");
        return (JijinTradeConfirmDTO)jijinTradeConfirmRepository.insertJijinTradeConfirm(jijinTradeConfirmDTO);
	}
	
	public JijinUserBalanceAuditDTO buildJijinUserBalanceAuditDto(Long userId, String fundCode,String instId,String bizDate,Status status,BigDecimal TotalIncome){
		JijinUserBalanceAuditDTO dto = new JijinUserBalanceAuditDTO();
		
		dto.setAvailableShare(new BigDecimal("12.21"));
		dto.setBizDate(bizDate);
		dto.setContractNo("contractNo");
		dto.setDailyIncome(new BigDecimal("1"));
		dto.setDividendType("0");
		dto.setFeeType("A");
		dto.setFileId(1234l);
		dto.setFrozenShare(new BigDecimal("2.2"));
		dto.setFundCode(fundCode);
		dto.setInstId(instId);
		dto.setTotalIncome(TotalIncome);
		dto.setTotalShare(new BigDecimal("22223"));
		dto.setUnpaiedIncome(null);
		dto.setFundType("1");
		dto.setStatus(status.name());
		dto.setUserId(userId);

		return jijinUserBalanceAuditRepository.insertBusJijinUserBalanceAudit(dto);
	}
	
	public JijinUserBalanceAuditDTO getJijinUserBalanceAuditDTO(Long id){
		return jijinUserBalanceAuditRepository.findBusJijinUserBalanceAudit(MapUtils.buildKeyValueMap("id",id));
	}
	

	
}
