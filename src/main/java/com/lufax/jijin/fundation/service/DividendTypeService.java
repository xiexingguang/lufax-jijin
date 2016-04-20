package com.lufax.jijin.fundation.service;



import java.util.Date;

import com.lufax.jijin.fundation.constant.*;
import com.lufax.jijin.fundation.service.domain.JiJinDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.constant.BalanceDividendStatus;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.gson.request.GWDividendModifyRequestGson;
import com.lufax.jijin.fundation.remote.gson.response.GWDividendModifyResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseCode;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinSequenceRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.util.JijinUtils;
import com.lufax.mq.client.util.MapUtils;


/**
 * 基金分红方式修改service
 *
 * @author chenqunhui
 */
@Service
public class DividendTypeService {

	@Autowired
	private JijinTradeRecordRepository jijinTradeRecordRepository;
	
	@Autowired
	private JijinGatewayRemoteService jijinGatewayRemoteService;

	@Autowired
	private JijinUserBalanceRepository jijinUserBalanceRepository;
	
	@Autowired
	private JijinAccountRepository jijinAccountRepository;
	
	@Autowired
	private JijinInfoRepository jijinInfoRepository;
	
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private JijinSequenceRepository jijinSequenceRepository;
	@Autowired
	private JiJinDateUtil jiJinDateUtil;
	public void setJijinGatewayRemoteService(
			JijinGatewayRemoteService jijinGatewayRemoteService) {
		this.jijinGatewayRemoteService = jijinGatewayRemoteService;
	}

	/**
	 * 申请修改分红方式
	 * @param userId
	 * @param fundCode
	 * @param dividendType
     * @return
     */
	public BaseGson applyModifyDividendTypeRequest(Long userId,String fundCode,String dividendType){
		BaseGson baseGson= new BaseGson();    
		if(!"0".equals(dividendType) && !"1".equals(dividendType)){
			baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_PARAM_IS_EMPTY);
            baseGson.setRetMessage("分红方式错误");
            Logger.info(this, String.format("apply update dividend type failed, ApplicationNo [%s].", dividendType));
		}
		//流水号
		String appNo = sequenceService.getSerialNumber(JijinBizType.DIVIDEND_MODIFY_APPLY.getCode());
		//查出基金的信息,需要使用instId
		JijinInfoDTO jijinInfo = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode",fundCode));
		if(null == jijinInfo){
			baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_PARAM_IS_EMPTY);
            baseGson.setRetMessage("基金编码错误");
            Logger.info(this, String.format("apply update dividend type failed.errmsg='基金编码错误', ApplicationNo [%s].", appNo));
            return baseGson;
		}else if(JijinUtils.isHuoji(jijinInfo)){
			baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_NOT_ALLOWED);
            baseGson.setRetMessage("货币基金不允许修改分红方式");
            Logger.info(this, String.format("apply update dividend type failed.errmsg='货币基金不允许修改分红方式', ApplicationNo [%s].", appNo));
            return baseGson;
		}
		
		//查出account
		JijinAccountDTO account = jijinAccountRepository.findActiveAccount(userId, jijinInfo.getInstId(), "PAF");
		if(null == account){
			baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_PARAM_IS_EMPTY);
            baseGson.setRetMessage("用户未持有该基金");
            Logger.info(this, String.format("apply update dividend type failed errMsg='用户未持有该基金', ApplicationNo [%s].", appNo));
            return baseGson;
		}
		JijinUserBalanceDTO balance = jijinUserBalanceRepository.findUserBalanceByFundCode(userId, fundCode);
		if(null == balance){
			baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_PARAM_IS_EMPTY);
            baseGson.setRetMessage("用户未持有该基金");
            Logger.info(this, String.format("apply update dividend type failed errMsg='用户未持有该基金', ApplicationNo [%s].", appNo));
            return baseGson;
		}
		//查询是否有未完成的分红修改记录，如果有，不允许再次修改
		int count = jijinTradeRecordRepository.countModifyingDividendTradeRecord(userId, fundCode);
		if(count >0){
			baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_APPLY_REPEAT);
            baseGson.setRetMessage("不允许重复修改分红方式，请等待上一次修改确认后再进行");
            Logger.info(this, String.format("apply update dividend type failed errMsg='重复修改分红方式，请等待上次的确认结果完成进行', ApplicationNo [%s].", appNo));
            return baseGson;
		}
		
		//调用 jijin-GW分红修改的接口
		GWResponseGson  gwResponse = callJijinGwModifyDividend(appNo,jijinInfo.getInstId(), account.getContractNo(),fundCode,dividendType);

		if(GWResponseCode.SUCCESS.equals(gwResponse.getRetCode())){
			//调用接口成功,转换出返回值
			GWDividendModifyResponseGson gwResult = new Gson().fromJson(gwResponse.getContent(), GWDividendModifyResponseGson.class);
			if("0000".equals(gwResult.getErrorCode())){
				//如果申请修改成功，将blanace的状态设置为分红修改中,只更新状态，不更新类型
				jijinUserBalanceRepository.updateUserBalanceDividendStatusAndType(userId,fundCode,BalanceDividendStatus.MODIFYING.name(),null);
				baseGson.setRetCode(ResourceResponseCode.SUCCESS);
				Logger.info(this, String.format("apply update dividend type success, ApplicationNo [%s].", appNo));
			}else{
				baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_JIJIN_COMPANY_RETURN_FAIL);
	            baseGson.setRetMessage(gwResult.getErrorMessage());
	            Logger.info(this, String.format("apply update dividend type failed,jijin company responseCode = [%s], responseMsg=[%s] ApplicationNo [%s].",gwResult.getErrorCode(),gwResult.getErrorMessage(), appNo));
			}
			//保存tradeRecord
			this.saveTradeRecord(appNo,fundCode,dividendType,account,gwResponse,gwResult,jijinInfo.getFundType());
			
		}else if(GWResponseCode.LACK_CLIENT_CONFIG.equals(gwResponse.getRetCode()) 
				||GWResponseCode.SIGN_FAIL.equals(gwResponse.getRetCode())
				||GWResponseCode.VALIDATE_SIGN_FAIL.equals(gwResponse.getRetCode())){
			//接口未通,不需要落地trade_record
			baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_GW_ERROR);
            baseGson.setRetMessage(gwResponse.getRetMessage());
            Logger.info(this, String.format("call GW failed when update dividend type, ApplicationNo [%s],jijin GW responseCode [%s],response Msg=[%s].", appNo,gwResponse.getRetCode(),gwResponse.getRetMessage()));
		}else if(GWResponseCode.JIJIN_RESPONSE_ERROR.equals(gwResponse.getRetCode())
				||GWResponseCode.RUNTIME_ERROR.equals(gwResponse.getRetCode())){
			//接口调用失败,需要落地trade_record
			GWDividendModifyResponseGson gwResult = new GWDividendModifyResponseGson();
			//TODO 这里要确保 GWResponse失败的code 与 jijin公司api返回的成功code要不一致才行
			gwResult.setErrorCode(gwResponse.getRetCode());
			gwResult.setErrorMessage("Error in jijin company api");
			this.saveTradeRecord(appNo,fundCode,dividendType,account,gwResponse,gwResult,jijinInfo.getFundType());
			baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_JIJIN_COMPANY_ERROR);
            baseGson.setRetMessage(gwResponse.getRetMessage());
            Logger.info(this, String.format("call jijin company api failed when update dividend type, ApplicationNo [%s],jijin GW responseCode [%s] responseMsg=[%s].", appNo,gwResponse.getRetCode(),gwResponse.getRetMessage()));
		}else{
			//如果GW的返回码无法识别，判断为失败
			baseGson.setRetCode(ResourceResponseCode.DIVIDEND_MODIFY_GW_ERROR);
            baseGson.setRetMessage(gwResponse.getRetMessage());
		}
		return baseGson;
    }
	
	

	/**
	 * 调用jijin-GW修改分红方式
	 * @param appNo 		流水号
	 * @param instId		基金公司标识
	 * @param contractNo 	客户在基金公司的账号
	 * @param fundCode 		基金代码
	 * @param dividendType  分红方式
	 * @return
	 */
	private GWResponseGson  callJijinGwModifyDividend(String appNo,String instId,String contractNo,String fundCode,String dividendType){
		GWDividendModifyRequestGson gwRequest = new GWDividendModifyRequestGson(appNo,contractNo, fundCode,dividendType,instId);
		return jijinGatewayRemoteService.applyModifyDividend(instId,JsonHelper.toJson(gwRequest));
	}


    /**
	 * 落地trade_record记录
	 * @param appNo
	 * @param fundCode
	 * @param dividendType
	 * @param account
	 * @param gwResponse
	 * @param gwResult
	 * @param fundType
     * @return jijinTradeRecordDTO
     */
	private JijinTradeRecordDTO saveTradeRecord(String appNo,String fundCode,String dividendType,JijinAccountDTO account,GWResponseGson  gwResponse,GWDividendModifyResponseGson gwResult,String fundType){
		Long sequence = jijinSequenceRepository.findJijinSequence();
        String ukToken = "JIJIN" + sequence;
		JijinTradeRecordDTO jijinTradeRecordDTO = new JijinTradeRecordDTO();
		//（1）记录request参数
		jijinTradeRecordDTO.setUserId(account.getUserId());
	    jijinTradeRecordDTO.setFundCode(fundCode);
	    jijinTradeRecordDTO.setContractNo(account.getContractNo());
	    jijinTradeRecordDTO.setAppNo(appNo);
        jijinTradeRecordDTO.setType(TradeRecordType.DIVIDEND_MODIFY);
        jijinTradeRecordDTO.setChargeType("A");
        jijinTradeRecordDTO.setDividendType(dividendType);
        jijinTradeRecordDTO.setCreatedAt(new Date());
        jijinTradeRecordDTO.setCreatedBy("SYS");
        jijinTradeRecordDTO.setChannel(account.getChannel());
        jijinTradeRecordDTO.setInstId(account.getInstId());
        jijinTradeRecordDTO.setTrxId(sequence);
        jijinTradeRecordDTO.setUkToken(ukToken);
        //（2）记录调用的返回状态
        if(null != gwResult){
        	jijinTradeRecordDTO.setTrxDate(gwResult.getTransactionDate());
            jijinTradeRecordDTO.setTrxTime(gwResult.getTransactionTime());
            jijinTradeRecordDTO.setAppSheetNo(gwResult.getAppSheetSerialNo());
            jijinTradeRecordDTO.setErrorCode(gwResult.getErrorCode());
            jijinTradeRecordDTO.setErrorMsg(gwResult.getErrorMessage());
            if("0000".equals(gwResult.getErrorCode())){
	        	//申请修改成功
	        	jijinTradeRecordDTO.setStatus("SUBMIT_SUCCESS");
			}else{
				//申请失败或其他异常情况,在trade_record中记录失败
	        	jijinTradeRecordDTO.setStatus("SUBMIT_FAIL");
			}
        }else{
        	jijinTradeRecordDTO.setStatus("SUBMIT_FAIL");
        	jijinTradeRecordDTO.setErrorCode(gwResponse.getRetCode());
            jijinTradeRecordDTO.setErrorMsg(gwResponse.getRetMessage());
        }
		if("0000".equals(gwResult.getErrorCode())) {//成功再保存数据
			String expectConfirmDate = jiJinDateUtil.getConfirmDate(TradeRecordType.DIVIDEND_MODIFY.name(), gwResult.getTransactionTime(),
					fundType);
			jijinTradeRecordDTO.setExpectConfirmDate(expectConfirmDate);
		}
        //(3)写入database
        return jijinTradeRecordRepository.insertJijinTradeRecord(jijinTradeRecordDTO);
	}



}
