package com.lufax.jijin.fundation.dto.builder;

import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.JijinFreezeStatus;
import com.lufax.jijin.fundation.constant.JijinFreezeType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUnFreezeRecordDTO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by wuxiujun685 on 2016/3/11.
 */
public class JijinUnFreezeRecordDtoBuilder {
    /**
     * 认/申购确认时，如果是搞活动的基金，且需要冻结份额，此收集解冻信息
     * @param jijinTradeRecordDTO
     * @param map
     * @return jijinUnFreezeRecordDTO
     */
    public  static JijinUnFreezeRecordDTO  buildUnFreezeRecordDto(JijinTradeRecordDTO jijinTradeRecordDTO, Map map){
        JijinUnFreezeRecordDTO jijinUnFreezeRecordDTO = new JijinUnFreezeRecordDTO();
        jijinUnFreezeRecordDTO.setAppNo(jijinTradeRecordDTO.getAppNo());
        jijinUnFreezeRecordDTO.setAppSheetNo(jijinTradeRecordDTO.getAppSheetNo());
        jijinUnFreezeRecordDTO.setUnfreezeDate(map.get("unfreezeDate").toString());
        jijinUnFreezeRecordDTO.setUnfreezeShare(new BigDecimal(map.get("freezeShare").toString()));
        jijinUnFreezeRecordDTO.setUserId(jijinTradeRecordDTO.getUserId());
        jijinUnFreezeRecordDTO.setUserBalanceId((Long)map.get("userBalanceId"));
        jijinUnFreezeRecordDTO.setFundCode(jijinTradeRecordDTO.getFundCode());
        jijinUnFreezeRecordDTO.setStatus(JijinFreezeStatus.FROZEN.name());
        jijinUnFreezeRecordDTO.setFreezeType(Integer.parseInt(JijinFreezeType.MARKET.getCode()));
        jijinUnFreezeRecordDTO.setVersion(1l);
        return  jijinUnFreezeRecordDTO;
    }
}
