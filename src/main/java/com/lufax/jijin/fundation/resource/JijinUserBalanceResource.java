package com.lufax.jijin.fundation.resource;

import com.google.gson.Gson;
import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.JijinExFundBizEnum;
import com.lufax.jijin.daixiao.dto.JijinExIncomeModeDTO;
import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;
import com.lufax.jijin.daixiao.dto.JijinExSellLimitDTO;
import com.lufax.jijin.daixiao.repository.JijinExNetValueRepository;
import com.lufax.jijin.daixiao.service.JijinExIncomeModeService;
import com.lufax.jijin.daixiao.service.JijinExSellLimitService;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.ResourceResponseCode;
import com.lufax.jijin.fundation.dto.JijinAccountDTO;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.gson.JijinAccountGson;
import com.lufax.jijin.fundation.gson.JijinAccountResultGson;
import com.lufax.jijin.fundation.gson.UserBalanceResultGson;
import com.lufax.jijin.fundation.repository.JijinAccountRepository;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinNetValueRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.lufax.jijin.fundation.util.JijinUtils;
import com.lufax.mq.client.util.MapUtils;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/fundation/user")
public class JijinUserBalanceResource extends AuthenticatedRequiredRootResource {

    @InjectParam
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @InjectParam
    private JijinInfoRepository jijinInfoRepository;
    @InjectParam
    private JijinNetValueRepository jijinNetValueRepository;

    @InjectParam
    private TradeDayService tradeDayService;
    @InjectParam
    private JijinExSellLimitService jijinExSellLimitService;
    @InjectParam
    private JijinExIncomeModeService jijinExIncomeModeService;
    @InjectParam
    private JijinExNetValueRepository jijinExNetValueRepository;
    @InjectParam
    private JijinAccountRepository jijinAccountRepository;

    @POST
    @Path("/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJijinUserBalanceInfo(@FormParam("fundCode") String fundCode) {
        Logger.info(this, String.format("call /fundaction/user/balance start. fundCode [%s]", fundCode));
        Long currentUserId = currentUser();
        boolean isDaiXiao = false;//是否为代销平台销售基金

        UserBalanceResultGson userBalanceResultGson = new UserBalanceResultGson();
        if (StringUtils.isBlank(fundCode)) {
            userBalanceResultGson.setRetCode(ResourceResponseCode.FUND_CODE_EMPTY);
            userBalanceResultGson.setRetMessage("fund code is empty");
            Logger.info(this, "get  Jijin user balance account, the response is : " + new Gson().toJson(userBalanceResultGson));
            return new Gson().toJson(userBalanceResultGson);
        }
        try {

            JijinUserBalanceDTO userBalance = jijinUserBalanceRepository.findUserBalanceByFundCode(currentUserId, fundCode);

            if (null == userBalance) {
                userBalanceResultGson.setRetCode(ResourceResponseCode.ACCOUNT_NOT_EXIST);
                userBalanceResultGson.setRetMessage("account does not exist");

            } else {

                JijinInfoDTO infoDto = jijinInfoRepository.findJijinInfo(MapUtils.buildKeyValueMap("fundCode", fundCode));
                boolean isHuoji = JijinUtils.isHuoji(infoDto);
                userBalanceResultGson.setIsHuoji(isHuoji ? "1" : "0");

                //是否为代销平台销售基金
                if (infoDto != null) {
                    isDaiXiao = infoDto.getInstId().equals(FundSaleCode.LFX.getInstId());
                }

                //delete by@Neo 20150918 For:货基也统一使用份额
                /*if (userBalance.getBalance() != null) {
                    BigDecimal balance = userBalance.getBalance() == null ? new BigDecimal(0) : userBalance.getBalance();
                    BigDecimal frozen = userBalance.getFrozenAmount() == null ? new BigDecimal(0) : userBalance.getFrozenAmount();
                    userBalanceResultGson.setBalanceAmount(balance);
                    userBalanceResultGson.setFrozenAmount(frozen);
                    userBalanceResultGson.setIsHuoji("1");
                } else {
                    BigDecimal balance = userBalance.getShareBalance() == null ? new BigDecimal(0) : userBalance.getShareBalance();
                    BigDecimal frozen = userBalance.getFrozenShare() == null ? new BigDecimal(0) : userBalance.getFrozenShare();
                    userBalanceResultGson.setBalanceAmount(balance);
                    userBalanceResultGson.setFrozenAmount(frozen);
                    userBalanceResultGson.setIsHuoji("0");
                }*/
                BigDecimal balance = userBalance.getShareBalance() == null ? new BigDecimal(0) : userBalance.getShareBalance();
                BigDecimal frozen = userBalance.getFrozenShare() == null ? new BigDecimal(0) : userBalance.getFrozenShare();
                userBalanceResultGson.setBalanceAmount(balance);
                userBalanceResultGson.setFrozenAmount(frozen);

                //赎回状态
                userBalanceResultGson.setFundStatus(infoDto.getRedemptionStatus());

                //基金净值
                if (!isDaiXiao) {
                    JijinNetValueDTO netValueDto = jijinNetValueRepository.findLatestBusJijinNetValueByFundCode(fundCode);
                    if (null != netValueDto) {
                        userBalanceResultGson.setNetValue(netValueDto.getNetValue());
                    }
                } else {
                    JijinExNetValueDTO jijinExNetValueDTO = jijinExNetValueRepository.getLastJijinExNetValueByFundCode(fundCode);
                    if (null != jijinExNetValueDTO) {
                        userBalanceResultGson.setNetValue(jijinExNetValueDTO.getNetValue());
                    }
                }

                int nDay = infoDto.getRedemptionArrivalDay()+2; //  add additional 2 days
                String estimateTime = DateUtils.formatDate(tradeDayService.getRedeemEstimateDate(new Date(), nDay), DateUtils.DATE_FORMAT);
                userBalanceResultGson.setEstimateTime(estimateTime);

                userBalanceResultGson.setRetCode(ResourceResponseCode.SUCCESS);
                userBalanceResultGson.setRetMessage("success get user jijin balance account");

                //如果是直连基金，最低赎回份额和最低持有份额写死为100
                if (!isDaiXiao) {
                    userBalanceResultGson.setSingleRedeemMinAmount(new BigDecimal(100));
                    userBalanceResultGson.setMinHoldShareCount(new BigDecimal(100));
                    userBalanceResultGson.setBizCode(JijinExFundBizEnum.赎回.getBizCode());
                    userBalanceResultGson.setBizName(JijinExFundBizEnum.赎回.toString());
                } else {
                    //赎回限制信息
                    JijinExSellLimitDTO latestSellLimitDto = jijinExSellLimitService.getLatestSellLimitDtoByFundCode(fundCode, JijinExFundBizEnum.赎回.getBizCode());
                    if (latestSellLimitDto != null) {
                        BigDecimal minSellLimit = latestSellLimitDto.getSingleSellMinAmount();
                        BigDecimal maxSellLimit = latestSellLimitDto.getSingleSellMaxAmount();

                        if (minSellLimit == null) {
                            minSellLimit = BigDecimal.ZERO;
                        }
                        if (maxSellLimit == null || maxSellLimit.compareTo(BigDecimal.ZERO) == 0) {
                            maxSellLimit = new BigDecimal("999999999999");
                        }

                        userBalanceResultGson.setSingleRedeemMinAmount(minSellLimit);
                        userBalanceResultGson.setSingleRedeemMaxAmount(maxSellLimit);
                        userBalanceResultGson.setBizCode(JijinExFundBizEnum.赎回.getBizCode());
                        userBalanceResultGson.setBizName(JijinExFundBizEnum.赎回.toString());
                    } else {
                        userBalanceResultGson.setSingleRedeemMinAmount(BigDecimal.ZERO);
                        userBalanceResultGson.setSingleRedeemMaxAmount(new BigDecimal(Long.MAX_VALUE));
                        userBalanceResultGson.setBizCode(JijinExFundBizEnum.赎回.getBizCode());
                        userBalanceResultGson.setBizName(JijinExFundBizEnum.赎回.toString());
                    }

                    //最低持有份额
                    JijinExIncomeModeDTO latestIncomeModeDto = jijinExIncomeModeService.getLatestIncomeModeByFundCode(fundCode);
                    if (latestIncomeModeDto != null) {
                        userBalanceResultGson.setMinHoldShareCount(latestIncomeModeDto.getMinHoldShareCount());
                    } else {
                        userBalanceResultGson.setMinHoldShareCount(BigDecimal.ZERO);
                    }
                }

            }

        } catch (Exception e) {
            Logger.error(this, "get jijin user account balance occur Exception !", e);
            userBalanceResultGson.setRetCode(ResourceResponseCode.FAIL_WITH_EXCEPTION);
            userBalanceResultGson.setRetMessage("exception occured, fail to get Jijin user balance account");
        }

        String response = new Gson().toJson(userBalanceResultGson);
        Logger.info(this, "get  Jijin user balance account, the response is : " + response);
        return response;
    }


    @GET
    @Path("/account")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJijinUserAccountInfo(@QueryParam("userId") String userId) {
        Logger.info(this, String.format("call /fundaction/user/account start. userId [%s]", userId));
        Long currentUserId = currentUser();
        JijinAccountResultGson jijinAccountResultGson = new JijinAccountResultGson();

        if (StringUtils.isBlank(userId)) {
            jijinAccountResultGson.setRetCode("01");
            jijinAccountResultGson.setRetMessage("query userId can not be null");
            String result = new Gson().toJson(jijinAccountResultGson);
            Logger.info(this, String.format("get jijin account result is [%s] , userId [%S]", result, userId));
            return result;
        }

        if (Long.valueOf(userId).compareTo(currentUserId) != 0) {
            jijinAccountResultGson.setRetCode("01");
            jijinAccountResultGson.setRetMessage("login userId not equal query userId");
            String result = new Gson().toJson(jijinAccountResultGson);
            Logger.info(this, String.format("get jijin account result is [%s] , userId [%S]", result, userId));
            return result;
        }

        try {
            List<JijinAccountDTO> list = jijinAccountRepository.findValidAccountByUserId(currentUserId);
            List<JijinAccountGson> jijinAccountGsons = new ArrayList<JijinAccountGson>();
            for (JijinAccountDTO jijinAccountDTO : list) {
                jijinAccountGsons.add(new JijinAccountGson(jijinAccountDTO));
            }
            jijinAccountResultGson.setRetCode("00");
            jijinAccountResultGson.setRetMessage("success");
            jijinAccountResultGson.setList(jijinAccountGsons);
        } catch (Exception e) {
            Logger.error(this, "get jijin user account occur Exception !", e);
            jijinAccountResultGson.setRetCode("09");
            jijinAccountResultGson.setRetMessage("exception occured, fail to get Jijin user account");
        }

        String response = new Gson().toJson(jijinAccountResultGson);
        Logger.info(this, "get  Jijin user account, the response is : " + response);
        return response;
    }
}
