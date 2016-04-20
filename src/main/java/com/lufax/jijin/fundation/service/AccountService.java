/**
 *
 */
package com.lufax.jijin.fundation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lufax.jijin.base.utils.JijinInstIdPlatMerchantIdMapHolder;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.dto.JijinExCharacterDTO;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinRegisteRecordDTO;
import com.lufax.jijin.fundation.dto.JijinRegisteRecordDTO.RegisteStatus;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.remote.JijinGatewayRemoteService;
import com.lufax.jijin.fundation.remote.LjbGateWayRemoteService;
import com.lufax.jijin.fundation.remote.RequestResponseHelper;
import com.lufax.jijin.fundation.remote.gson.paf.request.PAFRegisterRequest;
import com.lufax.jijin.fundation.remote.gson.paf.response.PAFRegisterResponse;
import com.lufax.jijin.fundation.remote.gson.request.GWRegisterRequestGson;
import com.lufax.jijin.fundation.remote.gson.request.RegiserExtension;
import com.lufax.jijin.fundation.remote.gson.response.GWRegisterResponseGson;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseCode;
import com.lufax.jijin.fundation.remote.gson.response.GWResponseGson;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinRegisteRecordRepository;
import com.lufax.jijin.fundation.service.domain.Account;
import com.lufax.jijin.user.domain.UserInfoGson;
import com.lufax.jijin.user.service.UserService;
import com.lufax.mq.client.util.MapUtils;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 6, 2015 7:01:20 PM
 */
@Service
public class AccountService {

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private JijinGatewayRemoteService jijinGatewayService;

    @Autowired
    private LjbGateWayRemoteService pafGatewayService;

    @Autowired
    private RequestResponseHelper helper;

    @Autowired
    private JijinInstIdPlatMerchantIdMapHolder jijinInstIdPlatMerchantIdMapHolder;
    
    @Autowired
    JijinAccountRepository accountRepo;

    @Autowired
    JijinRegisteRecordRepository recordRepo;

    @Autowired
    JijinInfoRepository jijinRepo;

    @Autowired
    private UserService userService;

    public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Transactional
    public boolean saveRegisterAccount(JijinRegisteRecordDTO request) {
        boolean result = false;
        if (request.registerSuccess()) {
            JijinAccountDTO account = buildAccountDTO(request);
            accountRepo.insertBusJijinAccount(account);
        }

        //no matter register success or fail, save the request record
        recordRepo.insertBJijinRegisteRecord(request);
        Logger.info(this, String.format("Registe account of [%s] [%s] finish.", request.getAppNo(), request.registerSuccess()));
        result = true;
        return result;
    }

    /**
     * 只有大陆/台湾/澳门用户可以注册(代销需要判断,直连不判断)
     * @param userId
     * @param instId
     * @return
     */
    public boolean checkUserForPurchase(Long userId,String instId) {
    	UserInfoGson userInfoGson = userService.getUserInfo(userId);
        if("lfx201".equals(instId)){
            return StringUtils.isBlank(userInfoGson.getNationality())|| "01".equals(userInfoGson.getNationality()) || "02".equals(userInfoGson.getNationality()) || "03".equals(userInfoGson.getNationality());
        }else{
        	//直销不允许港澳台
        	if(StringUtils.isBlank(userInfoGson.getNationality())){
        		//内地可以申购
        		return true;
        	}else{
        		return false;
        	}
        }
    }

    private JijinAccountDTO buildAccountDTO(JijinRegisteRecordDTO request) {
        JijinAccountDTO account = new JijinAccountDTO();
        account.setContractNo(request.getContractNo());
        account.setCustNo(request.getCustNo());
        account.setInstId(request.getInstId());
        account.setPayNo(request.getPayNo());
        account.setUserId(request.getUserId());
        account.setChannel("PAF");
        return account;
    }
    
    /**
     * 判断用户是否可以购买该基金
     * @param userId
     * @return
     */
    public boolean checkUserForProd(Long userId,JijinExCharacterDTO character) {
    	//归属地区（01香港 02澳门 03台湾 04外籍）
    	UserInfoGson userInfoGson = userService.getUserInfo(userId);
        	
    	if(null==character){
    		return StringUtils.isBlank(userInfoGson.getNationality()); //默认只有大陆能投资
    	}else{
    		if(StringUtils.isBlank(userInfoGson.getNationality())){
    			return true;
    		}else if("01".equals(userInfoGson.getNationality())){
    			return "1".equals(character.getIsHongkong ());
    		}else if("02".equals(userInfoGson.getNationality())){
    			return "1".equals(character.getIsMacao());
    		}else if("03".equals(userInfoGson.getNationality())){
    			return "1".equals(character.getIsTaiwan());
    		}
    		return false;
    	}
    }

    

    public Account findAccount(long userId, String fundCode) {
        JijinInfoDTO jijin = jijinRepo.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", fundCode));
        if (jijin != null) {
            JijinAccountDTO dto = accountRepo.findActiveAccount(userId, jijin.getInstId(), "PAF");
            if (dto != null) {
                Account account = buildAccount(dto);
                return account;
            }
        }
        return null;
    }

    public boolean checkUser(long userId, String instId) {
        JijinAccountDTO dto = accountRepo.findActiveAccount(userId, instId, "PAF");
        return dto != null;
    }


    public String findJijinId(String fundCode) {
        JijinInfoDTO jijin = jijinRepo.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", fundCode));
        if (jijin != null) {
            return jijin.getInstId();
        } else {
            return null;
        }
    }
    
    @Transactional
    public BaseGson processRegister(String fundCode, Long userId) {
        BaseGson response = new BaseGson();
        try {
            UserInfoGson userInfo = userService.getUserInfo(userId);
            String instId = this.findJijinId(fundCode);

            JijinRegisteRecordDTO request = new JijinRegisteRecordDTO();
            request.setUserId(userId);
            request.setAppNo(sequenceService.getSerialNumber(JijinBizType.REGISTER.getCode()));
            request.setInstId(instId);

            String errorMessage = "";
            if (instId == null) {
                errorMessage = "instId not valid";
                processError(response, request, errorMessage, ResourceResponseCode.INST_ID_NOT_VALID);
            } else if (validUserId(userInfo,instId) == false) {
                errorMessage = "idNO not valid";
                processError(response, request, errorMessage, ResourceResponseCode.USER_ID_NOT_VALID);
            } else if (this.checkUser(userId, instId)) {
                errorMessage = "user register before";
                processError(response, request, errorMessage, ResourceResponseCode.SUCCESS);
            } else if (!this.checkUserForPurchase(userId,instId)) {
                errorMessage = "user can not register";
                processError(response, request, errorMessage, ResourceResponseCode.USER_CAN_NOT_REGISTER);
            } else {
                String payErrorMsg = registerPayAccount(request, userInfo);
                String fundErrorMsg = registerFundAccount(request, userInfo, fundCode);

                if (request.registerJijinSuccess()) {
                    response.setRetCode(ResourceResponseCode.SUCCESS);
                    request.setErrorCode(ResourceResponseCode.SUCCESS);
                    request.setErrorMsg("success");
                } else {
                    request.setStatus(RegisteStatus.FAIL);
                    if (request.registerPaySuccess() == false) {
                        response.setRetCode(ResourceResponseCode.REGISTER_PAY_FAIL);
                        response.setRetMessage("Register pay fail since "
                                + payErrorMsg);
                        request.setErrorCode(ResourceResponseCode.REGISTER_PAY_FAIL);
                        request.setErrorMsg("Register pay fail since "
                                + payErrorMsg);
                    } else {
                        response.setRetCode(ResourceResponseCode.REGISTER_JIJIN_FAIL);
                        response.setRetMessage("Register jijin fail since "
                                + fundErrorMsg);
                        request.setErrorCode(ResourceResponseCode.REGISTER_JIJIN_FAIL);
                        request.setErrorMsg("Register jijin fail since "
                                + fundErrorMsg);
                    }
                }
            }

            boolean dbResult = this.saveRegisterAccount(request);
            if (dbResult == false) {
                response.setRetCode(ResourceResponseCode.REGISTER_DB_FAIL);
                response.setRetMessage("Register pay fail since db meet exception");
            }
        } catch (Exception e) {
            Logger.warn(this, e.getMessage(), e);
            response.setRetCode(ResourceResponseCode.FAIL_WITH_EXCEPTION);
            response.setRetMessage(e.getMessage());
        }
        return response;
    }

    private Account buildAccount(JijinAccountDTO dto) {
        Account account = new Account();
        account.setChannel(dto.getChannel());
        account.setContractNo(dto.getContractNo());
        account.setCustNo(dto.getCustNo());
        account.setInstId(dto.getInstId());
        account.setPayNo(dto.getPayNo());
        account.setUserId(dto.getUserId());
        return account;
    }
    
    private void processError(BaseGson response, JijinRegisteRecordDTO request, String errorMessage, String errorCode) {
        Logger.warn(this, errorMessage);
        buildFailRecord(errorMessage, request, errorCode);
        response.setRetCode(errorCode);
        response.setRetMessage(errorMessage);
    }
    
    private void buildFailRecord(String errorMessage, JijinRegisteRecordDTO request, String errorCode) {
        request.setInstId("0");
        request.setPayNo("0");
        request.setCustNo("0");
        request.setContractNo("0");
        request.setChannel("0");
        request.setStatus(RegisteStatus.FAIL);
        request.setErrorCode(errorCode);
        request.setErrorMsg(errorMessage);
    }

    /**
     * Only valid the user identity card NO's length is 18
     *
     * @param userInfo
     * @return
     */
    private boolean validUserId(UserInfoGson userInfo,String instId) {
        //台湾澳门不判断长度
        if(instId.equals("lfx201")){
            if ("02".equals(userInfo.getNationality()) || "03".equals(userInfo.getNationality())) {
                return true;
            }
        }
        String idNO = userInfo.getIdNo();
        if (StringUtils.isNotBlank(idNO)) {
            if (idNO.length() == 18) {
                return true;
            } else {
                Logger.warn(this, "The user's idNO length is not 18.");
            }
        } else {
            Logger.warn(this, "The user's idNO is empty.");
        }
        return false;
    }

    /**
     * call pay gateway
     *
     * @param dto
     * @return
     */
    private String registerPayAccount(JijinRegisteRecordDTO dto, UserInfoGson user) {


        if (null == jijinInstIdPlatMerchantIdMapHolder.getFundSaleCode(dto.getInstId())) {
            dto.setStatus(RegisteStatus.FAIL);
            return String.format("Can't find %s 's sale code and could not create the request!", dto.getInstId());
        }
        //build request
        PAFRegisterRequest request = new PAFRegisterRequest();
        request.setUserId(dto.getUserId());
        request.setIdNo(user.getIdNo());
        if(dto.getInstId().equals("lfx201")){
            //idType为O对应平安符S P对应T 1对应I 剩下的暂时未做处理
            if(user.getIdType().equals("O")){
                request.setIdType("S");
            }else if(user.getIdType().equals("P")){
                request.setIdType("T");
            }else if(user.getIdType().equals("1")){
                request.setIdType("I");
            }else{
                request.setIdType(user.getIdType());
            }
        }else{
            request.setIdType("I");
        }
        request.setInstId(dto.getInstId());
        request.setMobile(user.getMobileNo());
        request.setName(user.getName());
        request.setSequence(dto.getAppNo());
        request.setFundCode(jijinInstIdPlatMerchantIdMapHolder.getFundSaleCode(dto.getInstId()));
        request.setMerchantId(jijinInstIdPlatMerchantIdMapHolder.getPlatMerchantId("lufax"));
        Logger.info(this,String.format("call paf register request is [%s]",JsonHelper.toJson(request)));
        GWResponseGson res = pafGatewayService.register(JsonHelper.toJson(request));
        if (res.isSuccess()) {
            PAFRegisterResponse regRes = JsonHelper.fromJson(res.getContent(), PAFRegisterResponse.class);
            if (regRes.isSuccess()) {
                dto.setPayNo(regRes.getContractNO());
                dto.setChannel("PAF");
                return "";
            } else {
                dto.setStatus(RegisteStatus.FAIL);
                return regRes.getRespMsg();
            }
        } else {
            dto.setStatus(RegisteStatus.FAIL);
            return res.getRetMessage();
        }
    }

    /**
     * call jijin gateway
     *
     * @param dto
     * @return
     */
    private String registerFundAccount(JijinRegisteRecordDTO dto, UserInfoGson user, String fundCode) {
        if(!dto.registerPaySuccess()) {
            return "";
        }

        GWRegisterRequestGson request = new GWRegisterRequestGson();
        request = helper.buildGWBaseRequest(dto.getInstId(), dto.getAppNo(), request);
        request.setApplicationNo(dto.getAppNo());
        request.setCertificateNo(user.getIdNo());
        if(dto.getInstId().equals("lfx201")){
            if(user.getIdType().equals("O")){
                request.setCertType("4");
            }else if(user.getIdType().equals("P")){
                request.setCertType("A");
            }else{
                request.setCertType(transferIdType(user.getIdType()));
            }
        }else{
            request.setCertType(transferIdType(user.getIdType()));
        }
        request.setInvestorName(user.getName());
        request.setMobilePhone(user.getMobileNo());
        request.setBankId(String.valueOf(dto.getUserId()));
        request.setCdCard(dto.getPayNo());

        if ("lfx201".equals(dto.getInstId())) {
            RegiserExtension ex = new RegiserExtension();
            ex.setFundCode(fundCode);
            ex.setRiskLevel(user.getRiskVerifyStatus());
            request.setExtension(JsonHelper.toJson(ex));
        }

//        GWResponseGson res = jijinGatewayService.register(dto.getInstId(), JsonHelper.toJson(request));
//        if (res.isSuccess()) {
//            GWRegisterResponseGson regRes = JsonHelper.fromJson(res.getContent(), GWRegisterResponseGson.class);
//            if (regRes.isSuccess()) {
//                dto.setCustNo(regRes.getCustNo());
//                dto.setContractNo(regRes.getContractNo());
//                dto.setStatus(RegisteStatus.SUCCESS);
//                return "";
//            } else {
//                dto.setStatus(RegisteStatus.FAIL);
//                return regRes.getErrorMessage();
//            }
//
//        } else {
//            dto.setStatus(RegisteStatus.FAIL);
//            return res.getRetMessage();
//        }
        
        final int maxRetryTimes = 2;
        int retryTimes = 0;
        do {
            GWResponseGson res = jijinGatewayService.register(dto.getInstId(), JsonHelper.toJson(request));
            if (GWResponseCode.SUCCESS.equalsIgnoreCase(res.getRetCode())) {
                
                GWRegisterResponseGson regRes = JsonHelper.fromJson(res.getContent(), GWRegisterResponseGson.class);
                if ("0000".equalsIgnoreCase(regRes.getErrorCode())) {
                    dto.setCustNo(regRes.getCustNo());
                    dto.setContractNo(regRes.getContractNo());
                    dto.setStatus(RegisteStatus.SUCCESS);
                    return "";
                }
                
                if("9999".equalsIgnoreCase(regRes.getErrorCode()) && retryTimes<maxRetryTimes) {
                    ++retryTimes;
                    continue;
                } 
                
                dto.setStatus(RegisteStatus.FAIL);
                return regRes.getErrorMessage();
            }
            
            if(GWResponseCode.RUNTIME_ERROR.equalsIgnoreCase(res.getRetCode()) && retryTimes<maxRetryTimes) {
                ++retryTimes;
                continue;
            }
            
            dto.setStatus(RegisteStatus.FAIL);
            return res.getRetMessage();
        } while(true);
    }

    private String transferIdType(String type){
        if(type.equalsIgnoreCase("1")){   //身份证
            return "0";
        }else{
            //默认身份证
            return "0";
        }
    }

}
