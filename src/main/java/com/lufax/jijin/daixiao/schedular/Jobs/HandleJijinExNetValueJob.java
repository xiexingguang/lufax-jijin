package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExNetValueDTO;
import com.lufax.jijin.daixiao.repository.JijinExNetValueRepository;
import com.lufax.jijin.daixiao.service.JijinExNetValueService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExNetValueJob extends BaseBatchWithLimitJob<JijinExNetValueDTO, Long> {

    @Autowired
    private JijinExNetValueRepository jijinExNetValueRepository;
    @Autowired
    private JijinExNetValueService jijinExNetValueService;


    @Override
    protected Long getKey(JijinExNetValueDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinExNetValueDTO> fetchList(int maxNum) {
        return jijinExNetValueRepository.getJijinExNetValuesByStatus(RecordStatus.NEW.name(), maxNum);
    }


    @Override
    protected void process(JijinExNetValueDTO jijinExNetValueDTO) {
        try {
            jijinExNetValueService.handleJijinExNetValue(jijinExNetValueDTO);
        } catch (Exception e) {
            Logger.warn(this, String.format("handle jijinExNetValue occurred exception ,id [%s]", jijinExNetValueDTO.getId()));
            String errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() >= 500) {
                jijinExNetValueRepository.updateJijinExNetValue(MapUtils.buildKeyValueMap("id", jijinExNetValueDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg.substring(0, 500)));
            } else {
                jijinExNetValueRepository.updateJijinExNetValue(MapUtils.buildKeyValueMap("id", jijinExNetValueDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg));
            }
        }

    }
}
