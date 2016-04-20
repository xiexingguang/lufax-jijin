package com.lufax.jijin.fundation.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.constant.RedeemTypeEnum;
import com.lufax.jijin.fundation.dto.JijinTradeRecordCountDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.mq.client.util.MapUtils;

@Repository
public class JijinTradeRecordRepository extends BaseRepository<JijinTradeRecordDTO> {

    protected String nameSpace() {
        return "JijinTradeRecord";
    }

    public JijinTradeRecordDTO insertJijinTradeRecord(JijinTradeRecordDTO jijinTradeRecordDTO) {
        return super.insert("insertBusJijinTradeRecord", jijinTradeRecordDTO);
    }

    public int updateBusJijinTradeRecord(Map condition) {
        return super.update("updateBusJijinTradeRecord", condition);
    }

    public JijinTradeRecordDTO getRecordById(Long id) {
        return (JijinTradeRecordDTO) super.queryObject("getRecordById", id);
    }

    public JijinTradeRecordDTO getRecordByAppNo(String appNo) {
        return (JijinTradeRecordDTO) super.queryObject("getRecordByAppNo", appNo);
    }

    public List<JijinTradeRecordDTO> getUnknownTradeRecords(String type, int maxNum) {
        return super.queryList("getRecordsByStatusAndTypeWithRownum", MapUtils.buildKeyValueMap("status", "UNKNOWN", "type", type, "maxNum", maxNum));
    }

    public List<JijinTradeRecordDTO> getRecallingTradeRecords(String type, int maxNum) {
        return super.queryList("getRecordsByStatusAndTypeWithRownum", MapUtils.buildKeyValueMap("status", "RECALLING", "type", type, "maxNum", maxNum));
    }

    public List<JijinTradeRecordDTO> getT0RedeemApplyRecords(String type, String status, String instId, int maxNum) {
        Map<Object, Object> map = MapUtils.buildKeyValueMap(
                "status", status,
                "type", type,
                "instId", instId,
                "redeemType", RedeemTypeEnum.FAST.getTypeCode(),
                "maxNum", maxNum);
        return super.queryList("getRecordsByStatusAndTypeAndInstId", map);
    }
    
    public List<JijinTradeRecordDTO> getUndoT0RedeemRecords(long maxNum) {
         return super.queryList("getUndoT0RedeemRecords", maxNum);
    }

    public JijinTradeRecordDTO getRecordByAppSheetNoAndInstId(String appSheetNo, String instId) {
        return (JijinTradeRecordDTO) super.queryObject("getRecordByAppSheetNoAndInstId", MapUtils.buildKeyValueMap("appSheetNo", appSheetNo, "instId", instId));
    }

    public int countModifyingDividendTradeRecord(long userId, String fundCode) {
        return (Integer) super.queryObject("countModifyingDividendTradeRecord", MapUtils.buildKeyValueMap("userId", userId, "fundCode", fundCode));
    }

    public JijinTradeRecordDTO getRecordByTrxIdAndTypeChannel(Long trxId, String type,String channel) {
        return (JijinTradeRecordDTO) super.queryObject("getRecordByTrxIdAndTypeChannel", MapUtils.buildKeyValueMap("trxId", trxId, "type", type,"channel",channel));
    }

    public List<Long> getUnreconTradeRecords(String trxDate, String instId, Long fileId) {
        return super.queryListObject("getUnreconTradeRecords", MapUtils.buildKeyValueMap("trxDate", trxDate, "instId", instId, "fileId", fileId));
    }


    /**
     * select batch size from trade record
     */
    public List<JijinTradeRecordDTO> getRecordsByTradeDay(String bizDate, int start, int end, String instId) {
        return super.queryList("getRecordsByTradeDay",
                MapUtils.buildKeyValueMap("trxDate", bizDate,
                        "instId", instId,
                        "start", start,
                        "end", end));
    }

    public JijinTradeRecordCountDTO countSuccessTradeRecordByTradeDay(String trxDate, String instId) {
        return (JijinTradeRecordCountDTO) super.queryObject("countSuccessTradeRecordByTradeDay", MapUtils.buildKeyValueMap("trxDate", trxDate, "instId", instId));
    }

    public JijinTradeRecordCountDTO countTradeRecordByCreatedAtAndTypes(Date createFrom,Date createTo, String instId, List<String> types) {
        return (JijinTradeRecordCountDTO) super.queryObject("countTradeRecordByCreatedAtAndTypes", MapUtils.buildKeyValueMap("createFrom",createFrom,"createTo",createTo, "instId", instId, "types", types));
    }

    public List<JijinTradeRecordDTO> getRecordsByCreatedAtAndTypes(Date createFrom,Date createTo, int start, int end, String instId, List<String> types) {
        return super.queryList("getRecordsByCreatedAtAndTypes",
                MapUtils.buildKeyValueMap("createFrom",createFrom,"createTo",createTo,
                        "instId", instId,
                        "start", start,
                        "end", end, "types", types));
    }

    /**
     * 统计时间段内基金的认申购总金额
     * @param fundCode
     * @param startTime
     * @param endTime
     * @return
     */
    public BigDecimal countPurchaseAmountByFundCodeAndTime(String fundCode,Date startTime,Date endTime){
    	return (BigDecimal) super.queryObject("countPurchaseAmountByFundCodeAndTime", MapUtils.buildKeyValueMap("fundCode",fundCode,"startTime",startTime,"endTime",endTime));
    }
    
//    public Map<String, ?> getJijinTradeRecordDTOInfoByIds(List<Long> ids) {
//    	return super.queryForMap("getJijinTradeRecordDTOInfoByIds", MapUtils.buildKeyValueMap("ids", ids), "id");
//    }
    
    public List<JijinTradeRecordDTO> getJijinTradeRecordDTOInfoByIds(Collection<Long> ids) {
    	return super.queryList("getJijinTradeRecordDTOInfoByIds", MapUtils.buildKeyValueMap("ids", new ArrayList<Long>(new HashSet<Long>(ids))));
    }
    
    

    /**
     * 统计时间段内基金的赎回总金额
     * @param fundCode
     * @param startTime
     * @param endTime
     * @return
     */
    public BigDecimal countRedeemAmountByFundCodeAndTime(String fundCode,Date startTime,Date endTime,Long userId){
        return (BigDecimal) super.queryObject("countRedeemAmountByFundCodeAndTime", MapUtils.buildKeyValueMap("fundCode",fundCode,"startTime",startTime,"endTime",endTime,"userId",userId));
    }

}
