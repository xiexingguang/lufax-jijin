package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExYieldRateDTO;
import com.lufax.jijin.daixiao.repository.JijinExYieldRateRepository;
import com.lufax.jijin.daixiao.service.JijinExYieldRateService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExYieldRateJob extends BaseBatchWithLimitJob<JijinExYieldRateDTO, Long> {

    @Autowired
    private JijinExYieldRateRepository jijinExYieldRateRepository;
    @Autowired
    private JijinExYieldRateService jijinExYieldRateService;


    @Override
    protected Long getKey(JijinExYieldRateDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinExYieldRateDTO> fetchList(int maxNum) {
        return jijinExYieldRateRepository.getUnDispatchedJijinExYieldRate(maxNum);
    }


    @Override
    protected void process(JijinExYieldRateDTO jijinExYieldRateDTO) {
        try {
            jijinExYieldRateService.handleJijinExYieldRate(jijinExYieldRateDTO);
        } catch (Exception e) {
            Logger.warn(this, String.format("handle jijinExYieldRate occurred exception ,id [%s]", jijinExYieldRateDTO.getId()));
            String errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() >= 500) {
                jijinExYieldRateRepository.updateJijinExYieldRateStatus(jijinExYieldRateDTO.getId(),RecordStatus.FAILED.name(), null,errorMsg.substring(0, 500));
            } else {
                jijinExYieldRateRepository.updateJijinExYieldRateStatus(jijinExYieldRateDTO.getId(),RecordStatus.FAILED.name(), null,errorMsg);
            }
        }

    }
}
