package com.lufax.jijin.trade.service;

import com.google.gson.Gson;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.repository.FundLoanRequestRepository;
import com.lufax.jijin.sysFacade.gson.result.BaseResultDTO;
import com.lufax.jijin.sysFacade.service.FundService;
import com.lufax.jijin.user.domain.BankAccountDetailGson;
import com.lufax.jijin.user.domain.CheckTradePasswordGson;
import com.lufax.jijin.user.dto.SLPRedeemResult;
import com.lufax.jijin.user.service.UserService;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.confirm.service.RedeemConfirmHandler;
import com.lufax.jijin.ylx.dto.YLXAccountTransferRecordDTO;
import com.lufax.jijin.ylx.dto.YLXFundBalanceDTO;
import com.lufax.jijin.ylx.dto.YLXSellConfirmDetailDTO;
import com.lufax.jijin.ylx.dto.YLXSellRequestDetailDTO;
import com.lufax.jijin.ylx.dto.YLXTransactionHistoryDTO;
import com.lufax.jijin.ylx.dto.repository.YLXAccountTransferRecordRepository;
import com.lufax.jijin.ylx.dto.repository.YLXFundBalanceRepository;
import com.lufax.jijin.ylx.dto.repository.YLXSellRequestDetailRepository;
import com.lufax.jijin.ylx.dto.repository.YLXTransactionHistoryRepository;
import com.lufax.jijin.ylx.enums.YLXSellRequestStatus;
import com.lufax.jijin.ylx.enums.YLXTradeDetailType;
import com.lufax.jijin.ylx.enums.YLXTransactionHistoryType;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordDTO;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordHisDTO;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordHisRepository;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordRepository;
import com.lufax.jijin.ylx.util.InsuranceFundRecordStatus;
import com.lufax.jijin.ylx.util.InsuranceFundRecordType;
import com.lufax.jijin.ylx.util.YlxConstants;
import com.lufax.jijin.base.constant.FaInsuranceAccountTransferRecordStatus;
import com.lufax.jijin.base.constant.InstructionType;
import com.lufax.jijin.base.constant.SLPAccountTransferRecordStatus;
import com.lufax.jijin.base.repository.InstructionNoGenerator;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.ListUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.dao.FundLoanRequestDAO;
import com.lufax.jijin.dto.FundLoanRequestDTO;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.site.lookup.util.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service
public class YlxFundRedeemService{

	protected static BigDecimal HUNDRED = BigDecimal.valueOf(100);
	protected static BigDecimal KW = BigDecimal.valueOf(20000000);
	protected static final int ROWNUM = 500; // batchSize
	@Autowired
	private YLXProfitService profit;
	@Autowired
	private ProductRepository productDao;
	@Autowired
	private UserService userService;
	@Autowired
    private YLXFundBalanceRepository ylxFundBalanceRepository;
	@Autowired
	private TradeDayService tradeDayService;
	@Autowired
	private FundLoanRequestDAO loanrequestDao;
	@Autowired
	private YLXSellRequestDetailRepository ylxSellRequestDetailRepository;
	@Autowired
	private FundLoanRequestRepository fundLoanRequestRepository;
	@Autowired
	private InstructionNoGenerator instructionNoGenerator;
	@Autowired
	private InsuranceFundRecordRepository insuranceFundRecordRepository;
    @Autowired
    private InsuranceFundRecordHisRepository insuranceFundRecordHisRepository;
    @Autowired
    private FundService fundService;
    @Autowired
    private YLXAccountTransferRecordRepository accTransferDao;
    @Autowired
    private YLXTransactionHistoryRepository ylxTransactionHistoryRepository;
    @Autowired
    private YlxFundRedeemTransactionalService transactionDealer;
    
    public String redeem(BigDecimal redeemAmount, String tradePassword, Integer productId, long userId){
    	if(redeemAmount.compareTo(BigDecimal.ZERO) == 0){
    		return new Gson().toJson(new SLPRedeemResult(ResourceResponseCode.REDEEM_SUBMIT_FAIL,"赎回失败"));
        }
    	
    	//校验用户交易密码
        CheckTradePasswordGson checkTradePasswordGson = userService.checkTradePwd(Long.valueOf(userId), tradePassword, "5");
        if (!checkTradePasswordGson.isSuccess()) {
        	Logger.info(this, String.format("check trade password not success [userId: %s]!", userId));
        	SLPRedeemResult result = new SLPRedeemResult(ResourceResponseCode.REDEEM_PWD_WRONG, "密码错误");
        	if("08".equals(checkTradePasswordGson.getResultId())){
        		result.setRet_code(ResourceResponseCode.REDEEM_PWD_LOCK);
        		result.setRet_msg("帐号被锁");
        	}
        	result.setMemo(checkTradePasswordGson.getResultMsg());
    		result.setLockHours(checkTradePasswordGson.getLockHours());
        	result.setLockRange(checkTradePasswordGson.getLockRange());
        	result.setMaxErrorTime(checkTradePasswordGson.getMaxErrorTime());
        	result.setErrorTime(checkTradePasswordGson.getErrorTime());
            return new Gson().toJson(result);
        }
        
        ProductDTO product = productDao.getById(productId);
        
        List<YLXFundBalanceDTO> ylxFundBalances = ylxFundBalanceRepository.findYlxFundBalanceByUserIdAndProductCode(MapUtils.buildKeyValueMap("userId", userId, "productCode", product.getCode()));
    	if(CollectionUtils.isEmpty(ylxFundBalances)){
    		Logger.info(this, String.format("no fund_balance [userId: %s]!", userId));
            return new Gson().toJson(new SLPRedeemResult(ResourceResponseCode.REDEEM_AMOUNT_NOT_CORRECT,"未开户"));
    	}
        
    	YLXFundBalanceDTO ylxFundBalance = ylxFundBalances.get(0);
    	
    	if(ylxFundBalance.getFundShare().compareTo(HUNDRED) <= 0){
    		redeemAmount = ylxFundBalance.getFundShare();
    	}else if(ylxFundBalance.getFundShare().compareTo(redeemAmount) < 0){
    		Logger.info(this, String.format("not enougn fundshare [userId: %s]!", userId));
            return new Gson().toJson(new SLPRedeemResult(ResourceResponseCode.REDEEM_AMOUNT_NOT_ENOUGH,"余额不足"));
    	}
    	if(StringUtils.isEmpty(ylxFundBalance.getThirdAccount())){
    		Logger.info(this, String.format("ylxFundBalance.getThirdAccount is null; [userId: %s]!", userId));
            return new Gson().toJson(new SLPRedeemResult(ResourceResponseCode.REDEEM_SUBMIT_FAIL,"没有第三方客户号"));
    	}
    	
    	Date tradeDate = tradeDayService.getTargetTradeDay(new Date());
    	
    	FundLoanRequestDTO loanRequest = loanrequestDao.getFundLoanRequestByProductCode(product.getCode());
    	YLXSellRequestDetailDTO insertDto = new YLXSellRequestDetailDTO();
    	insertDto.setBatchId(-1L);
    	insertDto.setInternalTrxId(0L - gen.getNext());
    	insertDto.setTrxDate(DateUtils.startOfDay(tradeDate));
    	insertDto.setTrxTime(tradeDate);
    	insertDto.setBankAccount(String.valueOf(userId));
    	insertDto.setThirdAccount(ylxFundBalance.getThirdAccount());//
        insertDto.setThirdAccountType("01");
        insertDto.setThirdCustomerAccount(ylxFundBalance.getThirdCustomerAccount());//
        insertDto.setProdCode(loanRequest.getThirdCompanyCode());//
    	insertDto.setSellType(YlxConstants.REDEEM_TYPE);
        insertDto.setFundShare(redeemAmount);
        insertDto.setPrincipal(BigDecimal.ZERO);
        insertDto.setInterest(BigDecimal.ZERO);
        insertDto.setCurrency(YlxConstants.CURRENCY_RMB);
        insertDto.setInternalSellType(YLXTradeDetailType.REDEEM.name());
        insertDto.setProductCategory(product.getProductCategory());
        insertDto.setProductCode(product.getCode());
        insertDto.setProductId(product.getId());
        insertDto.setUserId(userId);
        insertDto.setStatus(YLXSellRequestStatus.SELLING.name());
        
    	try{
    		transactionDealer.handleRedeem(ylxFundBalance, redeemAmount, insertDto);
    	}catch(Exception e){
    		Logger.info(this, String.format("handleRedeem error, try again [userId: %s]!", ylxFundBalance.getUserId()));
    		return new Gson().toJson(new SLPRedeemResult(ResourceResponseCode.REDEEM_SUBMIT_FAIL,"赎回失败"));
    	}
    	
    	return new Gson().toJson(new SLPRedeemResult(ResourceResponseCode.SUCCESS,"赎回成功"));
    } 

    @Transactional
	public void createFundRecord(List<YLXSellRequestDetailDTO> sells, YLXBatchDTO batchDTO) {
		
		/*Map<Long, List<List<Long>>> sellGroupLists = new HashMap<Long, List<List<Long>>>();
		Map<Long, List<BigDecimal>> sellGroupAmouts = new HashMap<Long, List<BigDecimal>>();
		
		splitByKw(sells, sellGroupLists, sellGroupAmouts);

        Logger.info(this,String.format("sellGroupLists: %s",new Gson().toJson(sellGroupLists)));
        Logger.info(this,String.format("sellGroupAmouts: %s",new Gson().toJson(sellGroupAmouts)));

        for(Long productId : sellGroupLists.keySet()){
			ProductDTO product = productDao.getById(productId);
			List<BigDecimal> sellamount = sellGroupAmouts.get(productId);
			FundLoanRequestDTO fundLoanRequestDTO = fundLoanRequestRepository.getFundLoanRequestByProductCode(product.getCode());
	        BankAccountDetailGson bankAccountDetailGson = userService.bankAccountDetail(fundLoanRequestDTO.getLoaneeUserId());
	        
			List<List<Long>> selllist = sellGroupLists.get(productId);
			List<InsuranceFundRecordDTO> fundList = new ArrayList<InsuranceFundRecordDTO>();
			
			for(int i=0; i < selllist.size(); i++){
				InsuranceFundRecordDTO fundRecord = generateFundRecord(product, sellamount.get(i), productId, batchDTO, i , selllist.size(), fundLoanRequestDTO.getSpvUserId(), bankAccountDetailGson.getId());
				if(fundRecord != null){
					fundList.add(fundRecord);
				}
			}
			transactionDealer.insertInsuranceFundData(fundList, selllist);
		}*/


        HashMap<Long, List<AmountWithSellRequestHolder>> productIdHolderList = new HashMap<Long, List<AmountWithSellRequestHolder>>();
        splitByKw(sells,productIdHolderList);

        Logger.info(this,String.format("productIdHolderList: %s",new Gson().toJson(productIdHolderList)));
        //Logger.info(this,String.format("sellGroupAmouts: %s",new Gson().toJson(sellGroupAmouts)));

        for(Long productId : productIdHolderList.keySet()){
            ProductDTO product = productDao.getById(productId);
            List<AmountWithSellRequestHolder> holderList = productIdHolderList.get(productId);
            FundLoanRequestDTO fundLoanRequestDTO = fundLoanRequestRepository.getFundLoanRequestByProductCode(product.getCode());
            BankAccountDetailGson bankAccountDetailGson = userService.bankAccountDetail(fundLoanRequestDTO.getLoaneeUserId());
            for(int i=0; i < holderList.size(); i++){
                InsuranceFundRecordDTO fundRecord = generateFundRecord(product, holderList.get(i).getAmount(), productId, batchDTO, i , holderList.size(), fundLoanRequestDTO.getSpvUserId(), bankAccountDetailGson.getId());
                holderList.get(i).setFundRecordDTO(fundRecord);
            }
            transactionDealer.insertInsuranceFundData(holderList);
        }
	}
	
	/*private void splitByKw(List<YLXSellRequestDetailDTO>  sells, final Map<Long, List<List<Long>>> sellGroupLists , final Map<Long, List<BigDecimal>> sellGroupAmouts){
		Map<Long, List<YLXSellRequestDetailDTO>>  sellsGroupMap = new HashMap<Long, List<YLXSellRequestDetailDTO>>();
		for (YLXSellRequestDetailDTO dto : sells) {
        	if (!sellsGroupMap.containsKey(dto.getProductId())) {
        		sellsGroupMap.put(dto.getProductId(), new ArrayList<YLXSellRequestDetailDTO>(1000));
            }
        	List<YLXSellRequestDetailDTO> subList = sellsGroupMap.get(dto.getProductId());
        	subList.add(dto);
        }
		for(Long productId : sellsGroupMap.keySet()){
			BigDecimal tempAmount = BigDecimal.ZERO;
			List<Long> tempList = new ArrayList<Long>(1000);
			
			List<List<Long>> sellLists = new ArrayList<List<Long>>();
			List<BigDecimal> sellAmounts = new ArrayList<BigDecimal>();	
			
			sellGroupLists.put(productId, sellLists);
			sellGroupAmouts.put(productId, sellAmounts);
			
			Iterator<YLXSellRequestDetailDTO> sell_it = sellsGroupMap.get(productId).iterator();
			while(sell_it.hasNext()){
				YLXSellRequestDetailDTO sell = sell_it.next();	
				if(tempAmount.add(sell.getPrincipal()).compareTo(KW) > 0){
                    if(CollectionUtils.isNotEmpty(tempList)) {
                        sellLists.add(tempList);
                        sellAmounts.add(tempAmount);
                    }
					
					tempList = new ArrayList<Long>(1000);
					tempAmount = sell.getPrincipal();
					tempList.add(sell.getId());
				}else{
					tempAmount = tempAmount.add(sell.getPrincipal());
					tempList.add(sell.getId());
				}
			}
			if(!tempList.isEmpty()){
				sellLists.add(tempList);
				sellAmounts.add(tempAmount);
			}
		}
	}*/

    private void splitByKw(List<YLXSellRequestDetailDTO>  sells, final Map<Long, List<AmountWithSellRequestHolder>> productIdHolderList){
        Map<Long, List<YLXSellRequestDetailDTO>>  sellsGroupMap = new HashMap<Long, List<YLXSellRequestDetailDTO>>();
        for (YLXSellRequestDetailDTO dto : sells) {
            if (!sellsGroupMap.containsKey(dto.getProductId())) {
                sellsGroupMap.put(dto.getProductId(), new ArrayList<YLXSellRequestDetailDTO>(1000));
            }
            List<YLXSellRequestDetailDTO> subList = sellsGroupMap.get(dto.getProductId());
            subList.add(dto);
        }
        for(Long productId : sellsGroupMap.keySet()){
            BigDecimal tempAmount = BigDecimal.ZERO;
            List<Long> tempList = new ArrayList<Long>(1000);
            productIdHolderList.put(productId,new ArrayList<AmountWithSellRequestHolder>());

            Iterator<YLXSellRequestDetailDTO> sell_it = sellsGroupMap.get(productId).iterator();
            while(sell_it.hasNext()){
                YLXSellRequestDetailDTO sell = sell_it.next();
                if(tempAmount.add(sell.getPrincipal()).compareTo(KW) > 0){
                    if(CollectionUtils.isNotEmpty(tempList)) {
                        AmountWithSellRequestHolder holder = new AmountWithSellRequestHolder();
                        holder.setAmount(tempAmount);
                        holder.setRequestIds(tempList);
                        productIdHolderList.get(productId).add(holder);
                    }

                    tempList = new ArrayList<Long>(1000);
                    tempAmount = sell.getPrincipal();
                    tempList.add(sell.getId());
                }else{
                    tempAmount = tempAmount.add(sell.getPrincipal());
                    tempList.add(sell.getId());
                }
            }
            if(!tempList.isEmpty()){
                AmountWithSellRequestHolder holder = new AmountWithSellRequestHolder();
                holder.setAmount(tempAmount);
                holder.setRequestIds(tempList);
                productIdHolderList.get(productId).add(holder);
            }
        }
    }

    public static class AmountWithSellRequestHolder {
        private BigDecimal amount;
        private List<Long> requestIds;
        private InsuranceFundRecordDTO fundRecordDTO;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public List<Long> getRequestIds() {
            return requestIds;
        }

        public void setRequestIds(List<Long> requestIds) {
            this.requestIds = requestIds;
        }

        public InsuranceFundRecordDTO getFundRecord() {
            return fundRecordDTO;
        }

        public void setFundRecordDTO(InsuranceFundRecordDTO fundRecordDTO) {
            this.fundRecordDTO = fundRecordDTO;
        }
    }
	private InsuranceFundRecordDTO generateFundRecord(ProductDTO product , BigDecimal bigDecimal, Long productId , YLXBatchDTO batchDTO, int rechargeBatchId, int totalBatch,long fromUserId, String toCardId) {
		InsuranceFundRecordDTO fundRecord = new InsuranceFundRecordDTO();
		fundRecord.setAmount(bigDecimal);
		fundRecord.setProductId(productId);
		fundRecord.setType(InsuranceFundRecordType.RECHARGE.getCode());
		fundRecord.setStatus(InsuranceFundRecordStatus.RECHARGE_NEW.getCode());
		fundRecord.setRecordId(String.format("%s%s", YlxConstants.FUND_RECORD_PREFIX, instructionNoGenerator.generate(InstructionType.RECHARGE)));
		fundRecord.setFromUserId(fromUserId);
		fundRecord.setToCardId(toCardId);
		fundRecord.setInsuranceType(Integer.valueOf(product.getProductCategory()));
		fundRecord.setFundDate(String.format("%s-%s", DateUtils.formatDate(batchDTO.getTargetDate()), rechargeBatchId));
		fundRecord.setRemark(String.format("%s-%s-(%s/%s)", batchDTO.getId(), product.getCode(), rechargeBatchId, totalBatch));
		Logger.info(this,String.format("fund record for productId:%s,recordId:%s",fundRecord.getProductId(),fundRecord.getRecordId()));
		return fundRecord;
	}
	
	public void rechargeForCompany(InsuranceFundRecordDTO insuranceFundRecordDTO){
        BaseResultDTO baseResultDTO = fundService.rechargeForCompany(insuranceFundRecordDTO.getFromUserId(), insuranceFundRecordDTO.getRecordId(), insuranceFundRecordDTO.getAmount(), insuranceFundRecordDTO.getToCardId(), "养老险实力派对公代扣");
        if (baseResultDTO.isSuccess()) {
            Logger.info(this, String.format("apply withdraw success for YLX InsuranceFundRecordId :[%s]", insuranceFundRecordDTO.id()));
            insuranceFundRecordRepository.updateInsuranceFundRecordStatusById(InsuranceFundRecordStatus.RECHARGE_PROCESSING.getCode(), insuranceFundRecordDTO.id());
            insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(insuranceFundRecordDTO, InsuranceFundRecordStatus.RECHARGE_PROCESSING.getCode()));
        } else {
            insuranceFundRecordHisRepository.insertFaInsuranceFundRecordHis(new InsuranceFundRecordHisDTO(insuranceFundRecordDTO, InsuranceFundRecordStatus.RECHARGE_FAIL.getCode()));
            Logger.info(this, String.format("call fund failed, withdraw company failed for YLX InsuranceFundRecordId:[%s]", insuranceFundRecordDTO.id()));
        }
    }
	@Autowired
	private InstructionNoGenerator gen;
	@Autowired
	private RedeemConfirmHandler redeemHandler;
	public void rechargeForUser(InsuranceFundRecordDTO insuranceFundRecordDTO) {
		List<YLXSellRequestDetailDTO> sellList = ylxSellRequestDetailRepository.getYLXSellRequestDTOsByRecordIdandStatus(insuranceFundRecordDTO.getRecordId(), Integer.MAX_VALUE, YLXSellRequestStatus.CONFIRMED.getCode()); 
		List<YLXAccountTransferRecordDTO> dtoList = new ArrayList<YLXAccountTransferRecordDTO>();
		for(YLXSellRequestDetailDTO sell : sellList){
			YLXAccountTransferRecordDTO dto = new YLXAccountTransferRecordDTO(
					gen.generateBusinessNo(),
					insuranceFundRecordDTO.getFromUserId(),
					sell.getUserId(),
					TransactionType.EXPENSE_REDEMPTION.name(),
					TransactionType.INCOME_REDEMPTION.name(),
					sell.getPrincipal(),
					String.format("赎回转出 %s", insuranceFundRecordDTO.getProductDisPlayName()),
					String.format("%s", insuranceFundRecordDTO.getProductDisPlayName()),
					FaInsuranceAccountTransferRecordStatus.NEW.getCode(),
					"JIJIN");
			dto.setSellRequestId(sell.getId());
			dtoList.add(dto);
		}
		for(List<YLXAccountTransferRecordDTO> partList : ListUtils.split(dtoList, ROWNUM)){
			List<Long> sellRequestIds = new ArrayList<Long>(ROWNUM);
			for(YLXAccountTransferRecordDTO dto : partList){
				sellRequestIds.add(dto.getSellRequestId());
			}
			transactionDealer.handleBatchFaSlpAccountTransferRecord(partList, sellRequestIds);
		}
	}
	
	@Transactional
	public void rechargeUpdateUser(YLXAccountTransferRecordDTO transfer, YLXSellRequestDetailDTO sell, YLXFundBalanceDTO ylxFundBalance, YLXSellConfirmDetailDTO ylxSellConfirmDTO){
		ylxFundBalance.setTotalSellAmount(ylxFundBalance.getTotalSellAmount().add(sell.getPrincipal()));
		ylxFundBalance.setFrozenFundShare(ylxFundBalance.getFrozenFundShare().subtract(sell.getFundShare()));
		ylxFundBalance.setFundShare(ylxFundBalance.getFundShare().add(sell.getFundShare().subtract(ylxSellConfirmDTO.getConfirmFundShare())));
		int num = ylxFundBalanceRepository.updateYlxFundBalance(MapUtils.buildKeyValueMap("frozenFundShare", ylxFundBalance.getFrozenFundShare(),"fundShare",ylxFundBalance.getFundShare(),"totalSellAmount",ylxFundBalance.getTotalSellAmount(),"id", ylxFundBalance.getId(), "version", ylxFundBalance.getVersion()));
		if(num > 0){
			accTransferDao.updateYLXAccountTransferRecordStatusById(transfer.getId(), SLPAccountTransferRecordStatus.SUCCESS);
			ylxSellRequestDetailRepository.updateSellRequestJustStatusById(sell.getId(), YLXSellRequestStatus.SUCCESS.name());
            YLXTransactionHistoryDTO ylxTransactionHistoryDTO = new YLXTransactionHistoryDTO();
            ylxTransactionHistoryDTO.setSerialNo(sell.getId());
            ylxTransactionHistoryDTO.setUserId(Long.valueOf(sell.getBankAccount()));
            ylxTransactionHistoryDTO.setProductCode(sell.getProductCode());
            ylxTransactionHistoryDTO.setRemark("赎回成功");
            ylxTransactionHistoryDTO.setTransactionType(YLXTransactionHistoryType.REDEEM.name());
            ylxTransactionHistoryDTO.setFundShare(ylxSellConfirmDTO.getConfirmFundShare());
            ylxTransactionHistoryDTO.setTransactionAmount(ylxSellConfirmDTO.getAmount());
            ylxTransactionHistoryDTO.setCreatedAt(new Date());
            ylxTransactionHistoryDTO.setUpdatedAt(new Date());
            ylxTransactionHistoryDTO.setCreatedBy("SYS");
            ylxTransactionHistoryDTO.setUpdatedBy("SYS");
            ylxTransactionHistoryDTO.setVersion(0L);
            ylxTransactionHistoryRepository.insertYlxOpTransactionHistory(ylxTransactionHistoryDTO);
		}else{
			throw new RuntimeException("rechargeUpdateUser version conflict. userid : " + sell.getBankAccount());
		}
	}
}
