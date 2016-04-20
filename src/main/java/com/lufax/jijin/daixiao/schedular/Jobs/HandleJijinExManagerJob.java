package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExManagerDTO;
import com.lufax.jijin.daixiao.repository.JijinExManagerRepository;
import com.lufax.jijin.daixiao.service.JijinExManagerService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExManagerJob extends BaseBatchWithLimitJob<JijinExManagerDTO, Long> {

    @Autowired
    private JijinExManagerRepository jijinExManagerRepository;
    @Autowired
    private JijinExManagerService jijinExManagerService;


    @Override
    protected Long getKey(JijinExManagerDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinExManagerDTO> fetchList(int maxNum) {
        return jijinExManagerRepository.getJijinExManagersByStatus(RecordStatus.NEW.name(), maxNum);
    }


    @Override
    protected void process(JijinExManagerDTO jijinExManagerDTO) {
        try {
            jijinExManagerService.handleJijinExManager(jijinExManagerDTO);
        } catch (Exception e) {
            Logger.warn(this, String.format("handle jijinExManager occurred exception ,id [%s]", jijinExManagerDTO.getId()));
            String errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() >= 500) {
                jijinExManagerRepository.updateJijinExManager(MapUtils.buildKeyValueMap("id", jijinExManagerDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg.substring(0, 500)));
            } else {
                jijinExManagerRepository.updateJijinExManager(MapUtils.buildKeyValueMap("id", jijinExManagerDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg));
            }
        }

    }
}
