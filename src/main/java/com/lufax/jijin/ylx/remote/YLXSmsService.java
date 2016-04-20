package com.lufax.jijin.ylx.remote;

import com.lufax.jijin.base.constant.SmsTemplate;
import com.lufax.jijin.base.dto.BizParametersDTO;
import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.sysFacade.gson.result.SendSmsResultGson;
import com.lufax.jijin.sysFacade.service.ExtInterfaceService;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class YLXSmsService {

	@Resource
	private ExtInterfaceService extInterfaceService;

	@Resource
	private BizParametersRepository bizParametersRepository;

    @Resource
    private JijinAppProperties jijinAppProperties;


	private String convertTargetDate(Date targetDate) {
		return new SimpleDateFormat("MM月dd日").format(targetDate);
	}

	public SendSmsResultGson sendYlxWithdrawSuccessMessage(Date targetDate, String term, BigDecimal amount) {
		List<String> mobileNos = getYlxOperatorList();

		String strTargetDate = convertTargetDate(targetDate);
		String strAmount = amount.toString();
		Logger.info(this, String.format("send Ylx Withdraw Success Message, date term: %s term, amount: %s ",
				strTargetDate, strAmount));

		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		for (String mobileNo : mobileNos) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("targetDate", strTargetDate);
			params.put("term", term);
			params.put("amount", strAmount);
			contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_WITHDRAW_COMPLETE,
					params));
		}
		return extInterfaceService.sendSms(contentList);
	}

	public SendSmsResultGson sendYlxWithdrawFailedMessage(Date targetDate, String term) {
		List<String> mobileNos = getYlxOperatorList();

		String strTargetDate = convertTargetDate(targetDate);
		Logger.info(this, String.format("send Ylx Withdraw Failed Message, date term: %s term.", strTargetDate));

		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		for (String mobileNo : mobileNos) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("targetDate", strTargetDate);
			params.put("term", term);
			contentList.add(extInterfaceService.generateSmsContentMap(mobileNo,
					SmsTemplate.YLX_WITHDRAW_MONEY_NO_ENOUGH, params));
		}
		return extInterfaceService.sendSms(contentList);
	}

	public SendSmsResultGson sendYlxRequestFilePushFailedMessage(Date targetDate, String term) {
		List<String> mobileNos = getYlxOperatorList();

		String strTargetDate = convertTargetDate(targetDate);
		Logger.info(this,
				String.format("send Ylx Request File Push Failed Message, date term: %s term.", strTargetDate));

		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		for (String mobileNo : mobileNos) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("targetDate", convertTargetDate(targetDate));
			params.put("term", term);
			contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_SEND_BUY_REQ_FILE_FAIL,
					params));
		}
		return extInterfaceService.sendSms(contentList);
	}

    public SendSmsResultGson sendYlxHandleConfirmFileFailed(YLXBatchDTO batchDTO) {
        List<String> mobileNos = getYlxOperatorList();

        Logger.info(this,String.format("sendYlxHandleConfirmFileFailed,  batchId:",batchDTO.getId()));

        List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
        for (String mobileNo : mobileNos) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("targetDate", DateUtils.formatDate(batchDTO.getTargetDate()));
            params.put("triggerDate", DateUtils.formatDate(batchDTO.getTriggerDate()));
            params.put("warningTime", jijinAppProperties.getYlxPullConfirmTimeout());
            params.put("type", YLXBatchType.valueOf(batchDTO.getType()).getDesc());
            contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_HANDLE_CONFIRM_FILE_FAIL,
                    params));
        }
        return extInterfaceService.sendSms(contentList);
    }

    public SendSmsResultGson sendYlxPullConfirmFileFailed(YLXBatchDTO batchDTO) {
        List<String> mobileNos = getYlxOperatorList();

        Logger.info(this,String.format("sendYlxPullConfirmFileFailed, batchId:",batchDTO.getId()));

        List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
        for (String mobileNo : mobileNos) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("targetDate", DateUtils.formatDate(batchDTO.getTargetDate()));
            params.put("triggerDate", DateUtils.formatDate(batchDTO.getTriggerDate()));
            params.put("warningTime", jijinAppProperties.getYlxPullConfirmTimeout());
            params.put("type", YLXBatchType.valueOf(batchDTO.getType()).getDesc());
            contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_PULL_CONFIRM_FILE_FAIL,
                    params));
        }
        return extInterfaceService.sendSms(contentList);
    }

	private List<String> getYlxOperatorList() {
		BizParametersDTO result = bizParametersRepository.findByCode("fa.ylx.operator.mobile.list");
		return Arrays.asList(result.getValue().split(","));
	}
	
	/**
	 * 富赢增长认购成功，产品成立发送提示短消息
	 * @param mobileNo
	 * @param realName
	 * @param productName
	 * @param investmentAmount
	 * @param fundShare
	 */
	public void sendBuySuccessMessageToCustomer(String mobileNo, String realName,String productName,BigDecimal investmentAmount,BigDecimal fundShare){
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("realName", realName);
		params.put("productName", productName);
		params.put("investmentAmount", investmentAmount.toString());
		params.put("fundShare", fundShare.toString());
		contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_BUY_SUCCESS_MESSAGE_TEMPLATE,
				params));
		extInterfaceService.sendSmsToCustomers(contentList);
	}
	
	/**
	 * 富赢增长申购成功发送提示短消息
	 * @param mobileNo
	 * @param userName
	 * @param purchaseDate
	 * @param productName
	 * @param amount
	 * @param fundShare
	 * @param unitPrice
	 * @param commissionFee
	 */
	public void sendPurchaseSuccessMessageToCustomer(String mobileNo, String userName,Date purchaseDate,String productName,BigDecimal amount,BigDecimal fundShare,BigDecimal unitPrice,BigDecimal commissionFee){
		Logger.info(this, String.format("send purchase success to user[username:%s,mobileNo:%s,purchaseDate:%s,productName:%s,amount:%s,fundShare:%s,unitPrice:%s,commissionFee:%s]", userName, mobileNo,purchaseDate,productName,amount,fundShare,unitPrice,commissionFee));
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName", userName);
		params.put("purchaseDate", DateUtils.formatDate(purchaseDate));
		params.put("productName", productName);
		params.put("amount", amount.toString());
		params.put("fundShare", fundShare.toString());
		params.put("unitPrice", unitPrice.toString());
		params.put("commissionFee", commissionFee.toString());
		contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_PURCHASE_SUCCESS_MESSAGE,
				params));
		
		extInterfaceService.sendSmsToCustomers(contentList);
	}
	/**
	 * 富赢增长申购失败发送提示短消息
	 * @param mobileNo
	 * @param userName
	 * @param purchaseDate
	 * @param productName
	 * @param amount
	 * @param refundDate
	 */
	public void sendPurchaseFailMessageToCustomer(String mobileNo, String userName,Date purchaseDate,String productName,BigDecimal amount,Date refundDate){
		Logger.info(this, String.format("send purchase fail to user[username:%s,mobileNo:%s,purchaseDate:%s,productName:%s,amount:%s,refundDate:%s]", userName, mobileNo,purchaseDate,productName,amount,refundDate));
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName", userName);
		params.put("purchaseDate", DateUtils.formatDate(purchaseDate));
		params.put("amount", amount.toString());
		params.put("productName", productName);
		params.put("refundDate", DateUtils.formatDate(refundDate));
		contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_PURCHASE_FAIL_MESSAGE,
				params));
		
		extInterfaceService.sendSmsToCustomers(contentList);
	}

	/**
	 * 富赢增长赎回成功发送提示短消息
	 * @param mobileNo
	 * @param userName
	 * @param redeemDate
	 * @param productName
	 * @param fundShare
	 * @param unitPrice
	 * @param amount
	 * @param commissionFee
	 */
	public void sendRedeemSuccessMessageToCustomer(String mobileNo, String userName,Date redeemDate,String productName,BigDecimal fundShare,BigDecimal unitPrice,BigDecimal amount,BigDecimal commissionFee){
		Logger.info(this, String.format("send redeem success to user[username:%s,mobileNo:%s,redeemDate:%s,productName:%s,amount:%s,fundShare:%s,unitPrice:%s,commissionFee:%s]", userName, mobileNo,redeemDate,productName,amount,fundShare,unitPrice,commissionFee));
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName", userName);
		params.put("redeemDate", DateUtils.formatDate(redeemDate));
		params.put("productName", productName);
		params.put("fundShare", fundShare.toString());
		params.put("unitPrice", unitPrice.toString());
		params.put("amount", amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		params.put("commissionFee", commissionFee.toString());
		contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_REDEEM_SUCCESS_MESSAGE,
				params));
		
		extInterfaceService.sendSmsToCustomers(contentList);
	}

	/**
	 * 
	 * @param mobileNo
	 * @param userName
	 * @param redeemDate
	 * @param fundShare
	 * @param productName
	 */
	public void sendRedeemFailMessageToCustomer(String mobileNo, String userName,Date redeemDate,BigDecimal fundShare,String productName){
		Logger.info(this, String.format("send redeem fail to user[username:%s,mobileNo:%s,redeemDate:%s,productName%s,fundShare:%s]", userName, mobileNo,redeemDate,productName,fundShare));
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName", userName);
		params.put("redeemDate", DateUtils.formatDate(redeemDate));
		params.put("fundShare", fundShare.toString());
		params.put("productName", productName);
		contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_REDEEM_FAIL_MESSAGE,
				params));
		
		extInterfaceService.sendSmsToCustomers(contentList);
	}
	
	/**
	 * 产品开放 发送短信
	 * @param mobileNo
	 * @param userName
	 * @param redeemDate
	 * @param fundShare
	 * @param productName
	 */
	public void sendProductOpenMessageToCustomer(String mobileNo, String userName,String productName,Date purchaseDate){
		Logger.info(this, String.format("send product open message to user[username:%s,mobileNo:%s,purchaseDate:%s,productName:%s]", userName, mobileNo,purchaseDate,productName));
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName", userName);
		params.put("purchaseDate", DateUtils.formatDate(purchaseDate));
		params.put("productName", productName);
		contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_PRODUCT_STATUS_OPEN_MESSAGE,
				params));
		extInterfaceService.sendSmsToCustomers(contentList);
	}
	/**
	 * 净值对不上，发短信
	 */
	public SendSmsResultGson sendYlxProfitError(String fundName , String thirdAccount, BigDecimal my, BigDecimal other) {
		List<String> mobileNos = getYlxOperatorList();

		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();
		for (String mobileNo : mobileNos) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("date", new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			params.put("fundName", fundName);
			params.put("thirdCustomerAccount", thirdAccount);
			params.put("ljsShare", my.toPlainString());
			params.put("ylxShare", other.toPlainString());
			contentList.add(extInterfaceService.generateSmsContentMap(mobileNo, SmsTemplate.YLX_PROFIT_ERROR, params));
		}
		return extInterfaceService.sendSms(contentList);
	}
}
