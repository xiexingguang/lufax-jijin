package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.daixiao.constant.RecordStatus;
import com.lufax.jijin.daixiao.dto.JijinExGradeDTO;
import com.lufax.jijin.daixiao.repository.JijinExGradeRepository;
import com.lufax.jijin.daixiao.service.JijinExGradeService;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HandleJijinExGradeJob extends BaseBatchWithLimitJob<JijinExGradeDTO, Long> {

    @Autowired
    private JijinExGradeRepository jijinExGradeRepository;
    @Autowired
    private JijinExGradeService jijinExGradeService;


    @Override
    protected Long getKey(JijinExGradeDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinExGradeDTO> fetchList(int maxNum) {
        return jijinExGradeRepository.getJijinExGradesByStatusAndMaxNumAndRatingInterval(RecordStatus.NEW.name(), maxNum,"3");
    }

    @Override
    protected void process(JijinExGradeDTO jijinExGradeDTO) {
        try {
            jijinExGradeService.handleJijinExGrade(jijinExGradeDTO);
        } catch (Exception e) {
            Logger.warn(this, String.format("handle jijinExGrade info occurred exception ,id [%s]", jijinExGradeDTO.getId()));
            String errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() >= 500) {
                jijinExGradeRepository.updateJijinExGrade(MapUtils.buildKeyValueMap("id", jijinExGradeDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg.substring(0, 500)));
            } else {
                jijinExGradeRepository.updateJijinExGrade(MapUtils.buildKeyValueMap("id", jijinExGradeDTO.getId(), "status", RecordStatus.FAILED.name(), "errorMsg", errorMsg));
            }

        }
    }
}
