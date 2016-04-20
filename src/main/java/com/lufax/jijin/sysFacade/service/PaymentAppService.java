package com.lufax.jijin.sysFacade.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.TransactionType;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.sysFacade.caller.InterfaceCallObject;
import com.lufax.jijin.sysFacade.caller.PaymentAppCaller;
import com.lufax.jijin.sysFacade.gson.BatchTransferAndFreezeGson;
import com.lufax.jijin.sysFacade.gson.BatchTxUnfreezeRequest;
import com.lufax.jijin.sysFacade.gson.FreezeInfo;
import com.lufax.jijin.sysFacade.gson.PaymentGson;
import com.lufax.jijin.sysFacade.gson.PaymentIncreaseRequest;
import com.lufax.jijin.sysFacade.gson.PaymentRequest;
import com.lufax.jijin.sysFacade.gson.PaymentWithdrawRequest;
import com.lufax.jijin.sysFacade.gson.PlusInfo;
import com.lufax.jijin.sysFacade.gson.RevokeClearInfo;
import com.lufax.jijin.sysFacade.gson.result.PaymentBatchTransferFrozenResult;
import com.lufax.jijin.sysFacade.gson.result.PaymentResult;

/**
 * 接口文档地址：http://lujs.cn/confluence/pages/viewpage.action?pageId=40665142
 * 
 * 1.解冻 
 * 2.解冻调减
 * 3.调增
 * 4.撤单
 * 5.主账户转账进银行卡
 * 
 * @author XUNENG
 *
 */
@Service
public class PaymentAppService {

	private static Logger logger = Logger.getLogger(PaymentAppService.class);
	private static final String PLUS_FREEZE_CLEAR = "account/plus-freeze-clear"; //撤单
	private static final String TRANSFER_FREEZE = "account/batch-unfreeze-transfer-freeze"; //转账（冻结）
	private static final String UNFREEZE_DECREASE_= "payment/unfreeze-subtract-clear"; //解冻调减 （带清算）
	private static final String UNFREEZE = "account/batch-tx-unfreeze-fund"; //解冻 
	private static final String CANCEL_PAY ="payment/cancel-pay"; //解冻 
	private static final String CASH_WITHDRAW ="cashier/withdraw"; //主账户转账进银行卡
	private static final String INCREASE ="account/refund-clear"; //调增入账（带清算）

	 
	@Autowired
	private PaymentAppCaller paymentAppCaller;
	
	/**
	 * T0赎回，垫资户转账到个人虚拟户并冻结
	 * @param request
	 * @return
	 */
	public PaymentBatchTransferFrozenResult transferAndFreeze(BatchTransferAndFreezeGson request){
		
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(TRANSFER_FREEZE).addFormValue("dto", new Gson().toJson(request));
		
		int count  = 1;
		PaymentBatchTransferFrozenResult rt = new PaymentBatchTransferFrozenResult();
		do{
			try{
				logger.info(String.format("call paymentApp [account/batch-unfreeze-transfer-freeze],request is [%s]", new Gson().toJson(request)));
				rt = paymentAppCaller.post(interfaceCallObject, PaymentBatchTransferFrozenResult.class);
				logger.info(String.format("call paymentApp [account/batch-unfreeze-transfer-freeze],reponse is [%s]", new Gson().toJson(rt)));
			}catch(Exception e){
				logger.error("call paymentApp url[account/batch-unfreeze-transfer-freeze] error...",e);
				rt.setStatus("PROCESSING");
			}
			count++;
		}while(count<20 && rt.isProcessing());
		
		return rt;
	}

	/**
	 * 申购 解冻调减（带清算）
	 * @param request
	 * @return
	 */
	public PaymentResult decreaseMoneyWithAudit(PaymentRequest request){
		
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(UNFREEZE_DECREASE_).addFormValue("dto", new Gson().toJson(request));
		
		int count  = 1;
		PaymentResult rt = new PaymentResult();
		do{
			try{
				logger.info(String.format("call paymentApp [payment/unfreeze-subtract-clear],request is [%s]", new Gson().toJson(request)));
				rt = paymentAppCaller.post(interfaceCallObject, PaymentResult.class);
				logger.info(String.format("call paymentApp [payment/unfreeze-subtract-clear],reponse is [%s]", new Gson().toJson(rt)));
			}catch(Exception e){
				logger.error("call paymentApp url[payment/unfreeze-subtract-clear] error...",e);
				rt.setStatus("PROCESSING");
			}
			count++;
		}while(count<20 && rt.isProcessing());
		
		return rt;
	}
	
	/**
	 * 解冻 (虚拟户)
	 * @param request
	 * @return
	 */
	public PaymentResult unfreeze(BatchTxUnfreezeRequest request){
		
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(UNFREEZE).addFormValue("dto", new Gson().toJson(request));
		
		int count  = 1;
		PaymentResult rt = new PaymentResult();
		do{
			try{
				logger.info(String.format("call paymentApp [payment/unfreeze-subtract-clear],request is [%s]", new Gson().toJson(request)));
				rt = paymentAppCaller.post(interfaceCallObject, PaymentResult.class);
				logger.info(String.format("call paymentApp [payment/unfreeze-subtract-clear],reponse is [%s]", new Gson().toJson(rt)));
			}catch(Exception e){
				logger.error("call paymentApp url[payment/unfreeze-subtract-clear] error...",e);
				rt.setStatus("PROCESSING");
			}
			count++;
		}while(count<20 && rt.isProcessing());
		
		return rt;
	}
	
	/**
	 * 解冻 (订单)
	 * @param request
	 * @return
	 */
	public PaymentResult cancelPay(PaymentRequest request){
		
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(CANCEL_PAY).addFormValue("dto", new Gson().toJson(request));
		
		int count  = 1;
		PaymentResult rt = new PaymentResult();
		do{
			try{
				logger.info(String.format("call paymentApp [payment/cancel-pay],request is [%s]", new Gson().toJson(request)));
				rt = paymentAppCaller.post(interfaceCallObject, PaymentResult.class);
				logger.info(String.format("call paymentApp [payment/cancel-pay],reponse is [%s]", new Gson().toJson(rt)));
			}catch(Exception e){
				logger.error("call paymentApp url[payment/cancel-pay] error...",e);
				rt.setStatus("PROCESSING");
			}
			count++;
		}while(count<20 && rt.isProcessing());
		
		return rt;
	}
	
	
	
	/**
	 *  调增入账（带清算）  
	 *  
	 *  赎回
	 *  申购失败退款
	 *  现金分红
	 * @param request
	 * @return
	 */
	public PaymentResult increaseMoneyWithAudit(PaymentIncreaseRequest request){
		
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(INCREASE).addFormValue("dto", new Gson().toJson(request));
		
		int count  = 1;
		PaymentResult rt = new PaymentResult();
		do{
			try{
				logger.info(String.format("call paymentApp [account/refund-clear],request is [%s]", new Gson().toJson(request)));
				rt = paymentAppCaller.post(interfaceCallObject, PaymentResult.class);
				logger.info(String.format("call paymentApp [account/refund-clear],reponse is [%s]", new Gson().toJson(rt)));
			}catch(Exception e){
				logger.error("call paymentApp url[account/refund-clear] error...",e);
				rt.setStatus("PROCESSING");
			}
			count++;
		}while(count<20 && rt.isProcessing());
		
		return rt;
	}
	
	/**
	 *  赎回取现入银行卡
	 *  主账户取现支持T+1赎回（清算调增后取现）或T+0赎回（转账后取现）
	 *  
	 * @param request
	 * @return
	 */
	public PaymentResult cashierWithdraw(PaymentWithdrawRequest request){
		
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(CASH_WITHDRAW).addFormValue("dto", new Gson().toJson(request));
		
		int count  = 1;
		PaymentResult rt = new PaymentResult();
		do{
			try{
				logger.info(String.format("call paymentApp [cashier/withdraw],request is [%s]", new Gson().toJson(request)));
				rt = paymentAppCaller.post(interfaceCallObject, PaymentResult.class);
				logger.info(String.format("call paymentApp [cashier/withdraw],reponse is [%s]", new Gson().toJson(rt)));
			}catch(Exception e){
				logger.error("call paymentApp url[cashier/withdraw] error...",e);
				rt.setStatus("PROCESSING");
			}
			count++;
		}while(count<20 && rt.isProcessing());
		
		return rt;
	}
	
	
	/**
	 * 【基金撤单】
	 * 	1.调增虚拟户；
	 *  2.冻结陆金币;
	 *  3.销账
	 * @param fundCode   基金编码 
	 * @param tradeRecordId 交易记录ID
	 * @param userId      用户ID
	 * @param totalAmount　撤单的总金额（包括陆金币）
	 * @param freezeCoinAmount　需要冻结的陆金币数量
	 * @param appSheetSerialNo  
	 * @param appSheetNo
	 * @param instId
	 * @param trxDate
	 * @return 陆金币的冻结号
	 */
	public PaymentResult canceOrderAndFrozenCoin(JijinInfoDTO info,Long tradeRecordId,Long userId, BigDecimal totalAmount,
			BigDecimal freezeCoinAmount,String appSheetSerialNo,String appSheetNo,String instId,String trxDate){
		PaymentGson request = new PaymentGson();
		request.setInstructionNo("C_"+tradeRecordId);
		request.setUserId(userId);
		request.setProductCode(info.getProductCode());
		request.setTrxDate(trxDate);
		//request.setReferenceId(String.valueOf(tradeRecordId));
		//request.setReferenceType(CANCEL_PURCHAS);//
		request.setUseCase("基金撤单");
		request.setBizId(String.valueOf(tradeRecordId));
		request.setBizType(request.getChannelId()+"_"+JijinBizType.CANCEL_ORDER.name());//bizType使用"JIJIN_"+jijinBizeType.name的形式
		//填充调增金额信息
		PlusInfo plus = new PlusInfo();
		plus.setPlusAmount(totalAmount);//订单总金额
		plus.setTransactionType(TransactionType.INCOME_CANCEL_REFUND.name());
		plus.setPlusBankRefId(appSheetNo);
		plus.setRemark("申购撤单：" + info.getFundName());
		request.setPlusInfo(plus);
		//填充冻结陆金币信息
		if(null != freezeCoinAmount && freezeCoinAmount.compareTo(new BigDecimal("0"))>0){
			FreezeInfo freezeInfo = new FreezeInfo();
			freezeInfo.setFrozenAmount(freezeCoinAmount);
			freezeInfo.setBusinessId(tradeRecordId);
			freezeInfo.setTransactionType(TransactionType.FROZEN_FUND.name());
			request.setFreezeInfo(freezeInfo);
		}
		//填充销账信息
		RevokeClearInfo revoke = new RevokeClearInfo();
		revoke.setBusinessRefNo(appSheetSerialNo);
		revoke.setOriginalBusinessRefNo(appSheetNo);
		revoke.setTradingDate(trxDate);//交易日
		revoke.setVendorCode(instId);
		request.setRevokeClearInfo(revoke);
		
		InterfaceCallObject interfaceCallObject = new InterfaceCallObject().withUrl(PLUS_FREEZE_CLEAR).addFormValue("dto", new Gson().toJson(request));
		
		int count  = 1;
		PaymentResult rt = new PaymentResult();
		do{
			try{
				logger.info(String.format("call paymentApp url[account/plus-freeze-clear],request is [%s]", new Gson().toJson(request)));
				rt = paymentAppCaller.post(interfaceCallObject, PaymentResult.class);
				logger.info(String.format("call paymentApp url[account/plus-freeze-clear],reponse is [%s]", new Gson().toJson(rt)));
			}catch(Exception e){
				logger.error("call paymentApp url[account/plus-freeze-clear] error...",e);
				rt.setStatus("PROCESSING");
			}
			count++;
		}while(count<100 && rt.isProcessing());
		
		return rt;
	}
}
