package com.lufax.jijin.fundation.dto.builder;

import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.JijinFreezeType;
import com.lufax.jijin.fundation.dto.JijinFreezeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinTradeReconDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by wuxiujun685 on 2016/3/10.
 */
public class JijinFreezeRecordDtoBuilder {
    /***
     *
     * @param jijinTradeRecordDTO
     * @param map
     * @return JijinFreezeRecordDTO
     */
    public  static JijinFreezeRecordDTO buildFreezeRecordDto(JijinTradeRecordDTO jijinTradeRecordDTO, Map map){
        JijinFreezeRecordDTO jijinFreezeRecordDTO = new JijinFreezeRecordDTO();
        jijinFreezeRecordDTO.setAppNo(jijinTradeRecordDTO.getAppNo());
        jijinFreezeRecordDTO.setAppSheetNo(jijinTradeRecordDTO.getAppSheetNo());
        jijinFreezeRecordDTO.setBuyConfirmDate(map.get("trxConfirmDate").toString());
        jijinFreezeRecordDTO.setFreezeShare(new BigDecimal(map.get("freezeShare").toString()));
        jijinFreezeRecordDTO.setFundCode(jijinTradeRecordDTO.getFundCode());
        jijinFreezeRecordDTO.setUnfreezeDate(map.get("unfreezeDate").toString());
        jijinFreezeRecordDTO.setUserId(jijinTradeRecordDTO.getUserId());
        jijinFreezeRecordDTO.setUserBalanceId((Long)map.get("userBalanceId"));
        jijinFreezeRecordDTO.setFreezeType(Integer.parseInt(JijinFreezeType.MARKET.getCode()));

        return jijinFreezeRecordDTO;
    }
}
