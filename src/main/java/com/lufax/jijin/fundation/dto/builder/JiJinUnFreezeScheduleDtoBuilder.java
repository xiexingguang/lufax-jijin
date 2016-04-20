package com.lufax.jijin.fundation.dto.builder;

import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUnFreezeScheduleDTO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by wuxiujun685 on 2016/3/11.
 */
public class JiJinUnFreezeScheduleDtoBuilder {
    /**
     *
     * @param jijinTradeRecordDTO
     * @param map
     * @return
     */
    public static JijinUnFreezeScheduleDTO buildUnFreezeScheduleDto(JijinTradeRecordDTO jijinTradeRecordDTO, Map map){
        JijinUnFreezeScheduleDTO jijinUnFreezeScheduleDTO = new JijinUnFreezeScheduleDTO();
        jijinUnFreezeScheduleDTO.setUnfreezeDate(map.get("unfreezeDate").toString());
        jijinUnFreezeScheduleDTO.setTotalCount((Long) map.get("totalCount"));//没保存一次总量加1
        jijinUnFreezeScheduleDTO.setTotalUnfreezeCount((Long) map.get("totalUnfreezeCount"));//没保存一次总量加1
        jijinUnFreezeScheduleDTO.setTotalFreezeShare((BigDecimal) map.get("totalFreezeShare"));
        jijinUnFreezeScheduleDTO.setTotalUnFreezeShare(BigDecimal.ZERO);
        jijinUnFreezeScheduleDTO.setVersion(1L);
        return  jijinUnFreezeScheduleDTO;
    }

}
