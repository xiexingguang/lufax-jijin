package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExInfoDTO;
import com.lufax.jijin.daixiao.repository.JijinExInfoRepository;
import com.lufax.jijin.daixiao.service.JijinExInfoService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExInfoJob extends BaseBatchWithLimitJob<JijinExInfoDTO, Long> {

    @Autowired
    private JijinExInfoRepository jijinExInfoRepository;
    @Autowired
    private JijinExInfoService jijinExInfoService;


    @Override
    protected Long getKey(JijinExInfoDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinExInfoDTO> fetchList(int maxNum) {
        return jijinExInfoRepository.getJijinExInfoByStatus(RecordStatus.NEW.name(), maxNum);
    }


    @Override
    protected void process(JijinExInfoDTO jijinExInfoDTO) {
        try {
            jijinExInfoService.dispatchJijinExInfo(jijinExInfoDTO);
        } catch (Exception e) {
            Logger.warn(this, String.format("handle jijinExInfo occurred error id [%s]", jijinExInfoDTO.getId()));
            String errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() >= 500) {
                jijinExInfoRepository.updateJijinExInfo(MapUtils.buildKeyValueMap("id", jijinExInfoDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg.substring(0, 500)));
            } else {
                jijinExInfoRepository.updateJijinExInfo(MapUtils.buildKeyValueMap("id", jijinExInfoDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg));
            }
        }
    }
}


