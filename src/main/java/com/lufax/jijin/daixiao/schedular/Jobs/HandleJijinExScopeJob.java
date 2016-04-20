package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExScopeDTO;
import com.lufax.jijin.daixiao.repository.JijinExScopeRepository;
import com.lufax.jijin.daixiao.service.JijinExScopeService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExScopeJob extends BaseBatchWithLimitJob<JijinExScopeDTO, Long> {

    @Autowired
    private JijinExScopeRepository jijinExScopeRepository;
    @Autowired
    private JijinExScopeService jijinExScopeService;


    @Override
    protected Long getKey(JijinExScopeDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinExScopeDTO> fetchList(int maxNum) {
        return jijinExScopeRepository.getJijinExScopeByStatusAndMaxNum(RecordStatus.NEW.name(), maxNum);
    }

    @Override
    protected void process(JijinExScopeDTO jijinExScopeDTO) {
        try {
            jijinExScopeService.handleJijinExScope(jijinExScopeDTO);
        } catch (Exception e) {
            Logger.warn(this, String.format("handle jijinExScope occurred exception ,id [%s]", jijinExScopeDTO.getId()));
            String errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() >= 500) {
                jijinExScopeRepository.updateJijinExScope(MapUtils.buildKeyValueMap("id", jijinExScopeDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg.substring(0, 500)));
            } else {
                jijinExScopeRepository.updateJijinExScope(MapUtils.buildKeyValueMap("id", jijinExScopeDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg));
            }
        }
    }
}
