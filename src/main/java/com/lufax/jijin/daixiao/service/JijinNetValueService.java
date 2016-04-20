package com.lufax.jijin.daixiao.service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.gson.JijinUpdateStatusGson;
import com.lufax.jijin.fundation.constant.FundSaleCode;
import com.lufax.jijin.fundation.constant.JijinStatus;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.exception.ListAppException;
import com.lufax.jijin.fundation.gson.BaseGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.repository.JijinNetValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 处理净值文件里的基金状态（只处理代销的数据）
 *
 * @author chenqunhui
 */
@Service
public class JijinNetValueService {

    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;
    @Autowired
    private JijinNetValueRepository jijinNetValueRepository;

    /**
     * 处理lfex代销基金净值文件（07）的数据，只取其中的基金状态数据
     *
     * @param dto
     * @throws ListAppException
     */
    @Transactional
    public void handleJijinNetValue(JijinNetValueDTO dto) throws ListAppException {
        String buyStatus = "";
        String redStatus = "";
        if (dto.getFundStatus().equals("0")) {
            buyStatus = JijinStatus.BUY_STATUS_PUR_OPEN;
            redStatus = JijinStatus.REDEMPTION_STATU_RED_OPEN;
        } else if (dto.getFundStatus().equals("1")) {
            buyStatus = JijinStatus.BUY_STATUS_SUB_OPEN;
            redStatus = JijinStatus.REDEMPTION_STATU_CLOSE;
        } else if (dto.getFundStatus().equals("4")) {
            buyStatus = JijinStatus.BUY_STATUS_PUR_CLOSE;
            redStatus = JijinStatus.REDEMPTION_STATU_CLOSE;
        } else if (dto.getFundStatus().equals("5")) {
            buyStatus = JijinStatus.BUY_STATUS_PUR_CLOSE;
            redStatus = JijinStatus.REDEMPTION_STATU_RED_OPEN;
        } else if (dto.getFundStatus().equals("6")) {
            buyStatus = JijinStatus.BUY_STATUS_PUR_OPEN;
            redStatus = JijinStatus.REDEMPTION_STATU_CLOSE;
        } else if (dto.getFundStatus().equals("9")) {
            buyStatus = JijinStatus.BUY_STATUS_CLOSE;
            redStatus = JijinStatus.REDEMPTION_STATU_CLOSE;
        }
        JijinInfoDTO info = jijinInfoRepository.findJijinInfoByFundCode(dto.getFundCode());
        if (null == info) {
            jijinNetValueRepository.updateJijinNetValueStatus(dto.getId(), RecordStatus.NO_USED.name());
            return;
        }

        //直销数据不使用
        if (!info.getInstId().equals(FundSaleCode.LFX.getInstId())) {
            jijinNetValueRepository.updateJijinNetValueStatus(dto.getId(), RecordStatus.NO_USED.name());
            return;
        }

        if (!info.getBuyStatus().equals(buyStatus) && !buyStatus.equals("")) {
            //修改申购状态
            JijinUpdateStatusGson st = new JijinUpdateStatusGson();
            st.setBeNewStatus(buyStatus);
            st.setCode(info.getProductCode());
            st.setBeOperationType("0");
            BaseGson bs = jijinDaixiaoInfoService.updateJijinDaixiaoStatus(st);
            if ("000".equals(bs.getRetCode())) {
                Logger.error(this, String.format("handle net value job call list update status sevice error,productCode=[%s],buyStatus=[%s], retCode =[%s] retMsg=[%s]", info.getProductCode(), buyStatus, bs.getRetCode(), bs.getRetMessage()));
                throw new ListAppException();
            }
        }
        if (!info.getRedemptionStatus().equals(redStatus) && !redStatus.equals("")) {
            //修改赎回状态
            JijinUpdateStatusGson st = new JijinUpdateStatusGson();
            st.setBeNewStatus(redStatus);
            st.setCode(info.getProductCode());
            st.setBeOperationType("1");
            BaseGson bs = jijinDaixiaoInfoService.updateJijinDaixiaoStatus(st);
            if ("000".equals(bs.getRetCode())) {
                Logger.error(this, String.format("handle net value job call list update status sevice error,productCode=[%s],redStatus=[%s], retCode =[%s] retMsg=[%s]", info.getProductCode(), redStatus, bs.getRetCode(), bs.getRetMessage()));
                throw new ListAppException();
            }
        }
        jijinNetValueRepository.updateJijinNetValueStatus(dto.getId(), RecordStatus.DISPACHED.name());
    }


}
