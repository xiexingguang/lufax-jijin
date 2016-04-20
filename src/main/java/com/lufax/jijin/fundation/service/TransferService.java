package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lufax.jijin.daixiao.dto.JijinExIncomeModeDTO;
import com.lufax.jijin.daixiao.repository.JijinExIncomeModeRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.dto.JijinExCharacterDTO;
import com.lufax.jijin.daixiao.repository.JijinExCharacterRepository;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.FundType;
import com.lufax.jijin.fundation.constant.JijinStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.jijin.fundation.gson.CurrencyToStockGson;
import com.lufax.jijin.fundation.gson.PaginationByPageNoGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.site.lookup.util.StringUtils;

@Service
public class TransferService {

    @Autowired
    JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    JijinInfoRepository jijinInfoRepository;
    @Autowired
    JijinExCharacterRepository jijinExCharacterRepository;
    @Autowired
    private JijinExIncomeModeRepository jijinExIncomeModeRepository;
    
    /**
     * 查询客户持有的份额超过客户申购金额的货币基金及其金额
     * @param userId
     * @param targetFundCode
     * @param inputAmount
     * @param pageNo
     * @param pageLimit
     * @param shouldPageAt
     * @return
     */
    //可以优化，即若无需分页，可在第二次与数据库交互时，直接查询用户持有的份额>客户申购金额的所有基金信息，在第五步时就无需分页，直接组装CurrencyToStockGson并将之加入到PaginationByPageNoGson即可
    public PaginationByPageNoGson<CurrencyToStockGson> getConvertedCurrency2StockPage(
            Long userId, String targetFundCode, BigDecimal inputAmount, int pageNo, int pageLimit, int shouldPageAt) {
        Map<String, Object> attachedInfoMap = new HashMap<String, Object>(2);
        attachedInfoMap.put("isMtsSub", 0);    //是否支持货转股转购入
        attachedInfoMap.put("isMtsRedeem", -1);    //是否支持货转股转购出  -1-无可用货币基金;0-有货币基金，但所有货币基金份额(金额)小于申购金额；1-支持货转股转购出
        int recordCount = 0;
        
        //检查入参
        if(StringUtils.isEmpty(targetFundCode) || null==inputAmount) {
            String msg = String.format("parameter error, "
                    + "input params are: [userId=%s, srcFundCode=%s, inputAmount=%s, pageNo=%s, pageLimit=%s, shouldPageAt=%s]", 
                    userId, targetFundCode, inputAmount, pageNo, pageLimit, shouldPageAt);
            Logger.warn(this, msg);
            return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE, attachedInfoMap);
        }
        
        //第一次与数据库交互：判断基金是否为代销基金，只有代销基金才支持转购入-出
        JijinInfoDTO jijinInfoDTO =  jijinInfoRepository.findJijinInfoByFundCode(targetFundCode);
        if(null==jijinInfoDTO || !FundSaleCode.LFX.getInstId().equals(jijinInfoDTO.getInstId())) {
            String msg = String.format("[fundCode=%s] is not lfx201", targetFundCode);
            Logger.warn(this, msg);
            return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE, attachedInfoMap);
        }
        
        if(!JijinStatus.BUY_STATUS_SUB_OPEN.equalsIgnoreCase(jijinInfoDTO.getBuyStatus()) && !JijinStatus.BUY_STATUS_PUR_OPEN.equalsIgnoreCase(jijinInfoDTO.getBuyStatus())) {
            String msg = String.format("[fundCode=%s] is not purchase open or sub open", targetFundCode);
            Logger.warn(this, msg);
            return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE, attachedInfoMap);
        }
        
        //第二次与数据库交互：检查所选购的股基是否支持货转股转购入
        JijinExCharacterDTO toCharacter = jijinExCharacterRepository.queryLatestRecordByFundCode(targetFundCode);
        if (null==toCharacter || !"1".equals(toCharacter.getIsMtsSub())) {
            String msg = String.format("[fundCode=%s] does not support currency to stock in", targetFundCode);
            Logger.warn(this, msg);
            return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE, attachedInfoMap);
        }
        attachedInfoMap.put("isMtsSub", 1);
        
        //第三次与数据库交互：查询用户持有所有基金及其的份额   -->客户申购金额的所有基金码
        @SuppressWarnings("unchecked")
        Map<String, JijinUserBalanceDTO> jijinUserBalanceDTOMap =  (Map<String, JijinUserBalanceDTO>)jijinUserBalanceRepository.findFundCodeByUserId(userId);
        if(jijinUserBalanceDTOMap.keySet().contains(targetFundCode)) {
            jijinUserBalanceDTOMap.remove(targetFundCode);
            Logger.info(this, String.format("remove id=[srcFundCode=%s] from jijinUserBalanceDTOMap", targetFundCode));
        }
        if(jijinUserBalanceDTOMap.isEmpty()) {
            String msg = String.format("jijinUserBalanceDTOMap is empty, no available currency. [userId=%s]", userId);
            Logger.info(this, msg);
            return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE, attachedInfoMap);
        }
        
        //第四次与数据库交互：从以上基金码中筛选(查询)出支持货转股转购出的所有基金码
        List<String> applicableFundCodes = jijinExCharacterRepository.queryMtsRedeemFundCodes(jijinUserBalanceDTOMap.keySet());
        if(applicableFundCodes.isEmpty()) {
            String msg = String.format("no fund supports convertCurrency2Stock(IS_MTS_REDEEM), [userId=%s, inputAmount=%s, srcFundCode=%s]", userId, inputAmount, targetFundCode);
            Logger.warn(this, msg);
            return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE, attachedInfoMap);
        }
        
        //第五次与数据库交互：通过以上基金码过滤出对应的货基的所有信息
        @SuppressWarnings("unchecked")
        Map<String, JijinInfoDTO> jijinInfoDTOMap = (Map<String, JijinInfoDTO>)jijinInfoRepository.findByFundCodesAsOut(applicableFundCodes, FundType.CURRENCY.name(), FundSaleCode.LFX.getInstId());
        if(jijinInfoDTOMap.isEmpty()) {
            String msg = String.format("jijinInfoDTOMap is empty, no funds are applicable. [userId=%s, inputAmount=%s, srcFundCode=%s]", userId, inputAmount, targetFundCode);
            Logger.warn(this, msg);
            return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE, attachedInfoMap);
        }

        //持有基金的最小持有份额map
        Map<String,BigDecimal> fundCodeAndMinHoldShareCountMap = new HashMap<String, BigDecimal>();

        //从jijinUserBalanceDTOMap移除非货基且持有金额的基金
        Iterator<String> it = jijinUserBalanceDTOMap.keySet().iterator();
        while(it.hasNext()) {
            String fundCode = it.next();
            //若用户持有的货基不包含支持货转股转购出的货基，就将之移除
            if(!jijinInfoDTOMap.keySet().contains(fundCode)) {
                it.remove();
                continue;
            }

            //webdev9238
            JijinExIncomeModeDTO jijinExIncomeModeDTO = jijinExIncomeModeRepository.getLastestJijinExIncomeModeByFundCode(fundCode);
            BigDecimal compareAmount = BigDecimal.ZERO;
            if(null!=jijinExIncomeModeDTO){
                compareAmount = jijinExIncomeModeDTO.getMinHoldShareCount();
            }
            fundCodeAndMinHoldShareCountMap.put(fundCode,compareAmount);

            //若申购金额>=用户持有的支持货转股转购出的货基的持有份额，就将之移除
            if(inputAmount.compareTo(jijinUserBalanceDTOMap.get(fundCode).getShareBalance().subtract(compareAmount))>=0) {
                it.remove();
                jijinInfoDTOMap.remove(fundCode);
                continue;
            }
        }
        if(jijinUserBalanceDTOMap.isEmpty()) {
            String msg = String.format("jijinUserBalanceDTOMap is empty, all currency amount is less than that of stock. [userId=%s]", userId);
            Logger.info(this, msg);
            attachedInfoMap.put("isMtsRedeem", 0);
            return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE, attachedInfoMap);
        }
        
        //第六次与数据库交互：组装最后的分页数据
        recordCount = jijinUserBalanceDTOMap.size();
        //若总记录数<=shouldPageAt(默认300)，且 pageNo==1(即无需分页)，则pageCount=1，pageNo=1，pageLimit=recordCount
        boolean notToPage = recordCount<=shouldPageAt && pageNo==1;
//        int pageCount = (recordCount<=0) ? 0 : ((0==recordCount%pageLimit) ? recordCount/pageLimit : (recordCount/pageLimit)+1);
//        int pageCount = (int)Math.ceil((double)recordCount / (double)pageLimit);
        int pageCount = notToPage ? 1 : (int)Math.ceil((double)recordCount / (double)pageLimit);
        pageLimit = notToPage ? recordCount : pageLimit;
//        pageNo = notToPage ? 1 : pageNo;
        List<JijinUserBalanceDTO> finalJijinUserBalanceDTOs = ((pageNo>=1) && (pageNo<=pageCount)) ?
                jijinUserBalanceRepository.findFundCodeShareBalance(userId, inputAmount, jijinInfoDTOMap.keySet(), pageNo-1, pageLimit) : null;
        
        if(CollectionUtils.isEmpty(finalJijinUserBalanceDTOs)) {
            String msg = String.format("finalJijinUserBalanceDTOs is empty. [userId=%s,inputAmount=%s,"
                    + "fundCodes=%s,pageNo=%s,pageLimit=%s]", userId, inputAmount, jijinInfoDTOMap.keySet(), pageNo, pageLimit);
            Logger.warn(this, msg);
            return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, null, msg, ConstantsHelper.RET_CODE_FAILURE, attachedInfoMap);
        }        
        
        List<CurrencyToStockGson> pageRecords = new ArrayList<CurrencyToStockGson>(applicableFundCodes.size());
        for(JijinUserBalanceDTO jijinUserBalanceDTO : finalJijinUserBalanceDTOs) {
            JijinInfoDTO tmpJijinInfoDTO = jijinInfoDTOMap.get(jijinUserBalanceDTO.getFundCode());
            if(null != tmpJijinInfoDTO) {
                pageRecords.add(new CurrencyToStockGson(jijinUserBalanceDTO, tmpJijinInfoDTO,fundCodeAndMinHoldShareCountMap.get(jijinUserBalanceDTO.getFundCode())));
            }
        }
                
        Collections.sort(pageRecords, new Comparator<CurrencyToStockGson>() {
            public int compare(CurrencyToStockGson o1, CurrencyToStockGson o2) {
                //如果要按照降序排序,则o1 小于o2，返回1（正数），相等返回0，01大于02返回-1（负数）
                return (0-o1.getShareBalance().compareTo(o2.getShareBalance()));
            }
        });
        
        String msg = "TransferService getConvertedCurrency2StockPage execute success.";
        Logger.info(this, msg);
        attachedInfoMap.put("isMtsRedeem", 1);
        return new PaginationByPageNoGson<CurrencyToStockGson>(pageLimit, recordCount, pageNo, pageRecords, msg, ConstantsHelper.RET_CODE_SUCCESS, attachedInfoMap);
    }
}
