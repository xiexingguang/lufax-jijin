package com.lufax.jijin.fundation.service;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.BalanceHistoryBizType;
import com.lufax.jijin.fundation.constant.JijinFreezeStatus;
import com.lufax.jijin.fundation.dto.*;
import com.lufax.jijin.fundation.dto.builder.JiJinUnFreezeScheduleDtoBuilder;
import com.lufax.jijin.fundation.dto.builder.JijinFreezeRecordDtoBuilder;
import com.lufax.jijin.fundation.dto.builder.JijinUnFreezeRecordDtoBuilder;
import com.lufax.jijin.fundation.dto.builder.JijinUserBalenceHistoryDTOBuilder;
import com.lufax.jijin.fundation.repository.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by wuxiujun685 on 2016/3/10.
 */
@Service
public class JijinFreezeService {
    @Autowired
    private JijinFreezeRecordRepository freezeRepository;
    @Autowired
    private JijinUnFreezeRecordRepository unFreezeepository;
    @Autowired
    private JijinUnFreezeScheduleRepository scheduleRepository;
    @Autowired
    private JijinTradeSyncRepository jijinTradeSyncRepository;
    @Autowired
    private RedeemServiceUtil redeemServiceUtil;
    @Autowired
    private JijinUserBalanceRepository jijinUserBalanceRepository;
    @Autowired
    private JijinUserBalanceHistoryRepository jijinUserBalanceHistoryRepository;
    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;

    /**
     * 搞活动的基金，特殊要求，冻结
     *
     * @param jijinTradeRecordDTO
     * @param map
     * @return String
     */
    @Transactional
    public boolean freezeFund(JijinTradeRecordDTO jijinTradeRecordDTO, Map map) {
        boolean result = true;
        String unFreezeDate = this.calculationUnFreezeDate(map);
        if (StringUtils.isEmpty(unFreezeDate)) {//解冻日期为空，直接返回保存失败
            result = false;
        } else {
            map.put("unfreezeDate", unFreezeDate);
            //保存冻结信息
            JijinFreezeRecordDTO jijinFreezeRecoedDto = JijinFreezeRecordDtoBuilder.buildFreezeRecordDto(jijinTradeRecordDTO, map);
            freezeRepository.insertBusJijinFreezeRecord(jijinFreezeRecoedDto);
            //保存解冻信息
            JijinUnFreezeRecordDTO jijinUnFreezeRecoedDto = JijinUnFreezeRecordDtoBuilder.buildUnFreezeRecordDto(jijinTradeRecordDTO, map);
            unFreezeepository.insertJijinUnFreezeRecord(jijinUnFreezeRecoedDto);
            //保存冻结期信息
            JijinUnFreezeScheduleDTO scheduleDTO = scheduleRepository.getJijinUnFreezeScheduleListByUnfreezeDate(unFreezeDate);
            if (scheduleDTO == null) {//每天第一条时，这个对象为0
                map.put("totalCount", 1L);
                map.put("totalUnfreezeCount", 0L);
                map.put("totalFreezeShare", new BigDecimal(map.get("freezeShare").toString()));
                JijinUnFreezeScheduleDTO jiJinUnFreezeScheduleDto = JiJinUnFreezeScheduleDtoBuilder.buildUnFreezeScheduleDto(jijinTradeRecordDTO, map);
                scheduleRepository.insertJijinUnFreezeSchedule(jiJinUnFreezeScheduleDto);
            } else {
                scheduleRepository.updateJijinUnFreezeSchedule(MapUtils.buildKeyValueMap("totalCount", scheduleDTO.getTotalCount() + 1,
                        "totalUnfreezeCount", BigDecimal.ZERO,
                        "totalFreezeShare", scheduleDTO.getTotalFreezeShare().add(new BigDecimal(map.get("freezeShare").toString())),
                        "totalUnFreezeShare", BigDecimal.ZERO,
                        "version", scheduleDTO.getVersion(),
                        "unFreezeDate", scheduleDTO.getUnfreezeDate()));
            }
        }
        return result;
    }

    /**
     * 冻结结束，解冻
     *
     * @param recordDTO
     * @return
     */
    @Transactional
    public void unFreezeFund(JijinUnFreezeRecordDTO recordDTO) {
        String appNo = recordDTO.getAppNo();
        String appSheetNo = recordDTO.getAppSheetNo();
        String fundCode = recordDTO.getFundCode();
        String msg = "";

        JijinTradeSyncDTO jijinTradeSyncDTO = jijinTradeSyncRepository.getJijinTradeSyncByAppSheetNoAndFundCode(appSheetNo, fundCode);
        if (jijinTradeSyncDTO == null) {
            Logger.warn(this, String.format("jijin unfreeze recon: can not find jijin trade  record with sync file's app no [%s]", appSheetNo));
            msg = "trade sync 找不到记录";
        }
        JijinFreezeRecordDTO jijinFreezeRecordDTO = null;
        if (StringUtils.isEmpty(msg)) {
            jijinFreezeRecordDTO = freezeRepository.getJijinFreezeRecordByAppNo(appNo);
            if (jijinFreezeRecordDTO == null) {
                Logger.warn(this, String.format("jijin unfreeze recon: can not find jijin freeze  record  file's app no [%s]", appNo));
                msg = "freeze record 找不到记录";
            }
        }
        JijinTradeRecordDTO tradeRecordDTO = jijinTradeRecordRepository.getRecordByAppNo(recordDTO.getAppNo());
        if (tradeRecordDTO == null) {
            Logger.warn(this, String.format("jijin unfreeze result: can not find jijin trade record. app no: [%s]", appNo));
            msg = "trade record 找不到记录";
        }
        if (StringUtils.isEmpty(msg)) {
            BigDecimal confirmShare = jijinTradeSyncDTO.getConfirmShare();
            BigDecimal freezeShare = jijinFreezeRecordDTO.getFreezeShare();
            BigDecimal unFreezeShare = recordDTO.getUnfreezeShare();
            if (confirmShare.compareTo(freezeShare) != 0) {
                Logger.warn(this, String.format("jijin unfreeze recon: trade record with sync confirmShare yu  freezeShare bu deng  [%s]", appNo));
                msg = "tradesync confirmShare yu freezeShare bu deng ";
            }
            if (unFreezeShare.compareTo(freezeShare) != 0) {
                Logger.warn(this, String.format("jijin unfreeze recon: freezeRecord with sync freezeShare yu  unFreezeShare bu deng  [%s]", appNo));
                msg += "freezeRecord freezeShare yu unFreezeShare bu deng";
            }
        }
        //MSG 不为空 说明数据有错，更新状态为错误
        if (StringUtils.isNotEmpty(msg)) {
            unFreezeepository.updateJijinUnFreezeRecord(MapUtils.buildKeyValueMap("status", JijinFreezeStatus.UNMATCH.name(),
                    "version", recordDTO.getVersion(),
                    "unFreezeMsg", msg,
                    "appNo", appNo,
                    "appSheetNo", appSheetNo));
        } else {
            boolean result = redeemServiceUtil.unFrezee(recordDTO.getUnfreezeShare(), recordDTO.getUserId(), recordDTO.getFundCode());
            /*if (!result) {
                Logger.warn(this, String.format("NEED_MANUAL_HANDLE, jijin unfreeze fail. APP_NO: [%s]", appNo));
                unFreezeepository.updateJijinUnFreezeRecord(MapUtils.buildKeyValueMap("status", JijinFreezeStatus.FAIL.name(),
                        "version", recordDTO.getVersion(),
                        "unFreezeMsg", "解冻失败。",
                        "appNo", appNo,
                        "appSheetNo", appSheetNo));
                return;
            }*/
            //insert user balance history
            JijinUserBalanceDTO jijinUserBalanceDTO = jijinUserBalanceRepository.findUserBalanceByFundCode(recordDTO.getUserId(), recordDTO.getFundCode());
            JijinUserBalanceHistoryDTO jijinUserBalanceHistoryDTO = JijinUserBalenceHistoryDTOBuilder.buildUserBalanceHistoryDTO(jijinUserBalanceDTO, recordDTO.getUnfreezeDate(), recordDTO.getUnfreezeShare(), new BigDecimal("0"), "到期解冻", BalanceHistoryBizType.到期解冻, tradeRecordDTO.getId());
            jijinUserBalanceHistoryRepository.insertBusJijinUserBalanceHistory(jijinUserBalanceHistoryDTO);
            unFreezeepository.updateJijinUnFreezeRecord(MapUtils.buildKeyValueMap("status", JijinFreezeStatus.UNFROZEN.name(),
                    "version", recordDTO.getVersion(),
                    "unFreezeMsg", recordDTO.getUnfreezeMsg(),
                    "appNo", appNo,
                    "appSheetNo", appSheetNo));
            JijinUnFreezeScheduleDTO unFreezeScheduleDTO = scheduleRepository.getJijinUnFreezeScheduleListByUnfreezeDate(recordDTO.getUnfreezeDate());
            scheduleRepository.updateJijinUnFreezeSchedule(MapUtils.buildKeyValueMap(
                    "totalCount", unFreezeScheduleDTO.getTotalCount(),
                    "totalFreezeShare", unFreezeScheduleDTO.getTotalFreezeShare(),
                    "totalUnfreezeCount", unFreezeScheduleDTO.getTotalUnfreezeCount() + 1,
                    "totalUnFreezeShare", unFreezeScheduleDTO.getTotalUnFreezeShare().add(recordDTO.getUnfreezeShare()),
                    "version", unFreezeScheduleDTO.getVersion(),
                    "unFreezeDate", recordDTO.getUnfreezeDate()));
        }
    }

    /**
     * 根据确认日期和冻结天数，计算解冻日期
     *
     * @param map
     * @return
     * @throws ParseException
     */
    private String calculationUnFreezeDate(Map map) {
        Date date = DateUtils.parseDate(map.get("trxConfirmDate").toString(), DateUtils.DATE_STRING_FORMAT);
        int freezeDay = Integer.parseInt(map.get("freezeDay").toString());
        String unFreezeDate = DateUtils.formatDateAsString( DateUtils.add(date, freezeDay, Calendar.DATE));
        return unFreezeDate;
    }
}
