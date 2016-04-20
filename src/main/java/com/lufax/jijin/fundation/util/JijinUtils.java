package com.lufax.jijin.fundation.util;

import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;

public class JijinUtils {


    public static boolean isHuoji(JijinInfoDTO infoDto) {
        try {
            if (null == infoDto) {
                return false;
            }
            return infoDto.getFundType().toLowerCase().equals("currency");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否为代销基金
     *
     * @param infoDto
     * @return
     */
    public static boolean isDaixiao(JijinInfoDTO infoDto) {
        if (null == infoDto || null == infoDto.getInstId()) {
            return false;
        }
        return infoDto.getInstId().toLowerCase().endsWith("lfx201");
    }

    public static boolean isDahua(String instId) {
        return FundSaleCode.DHC.getInstId().equals(instId);
    }

}
