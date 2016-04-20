package com.lufax.jijin.ylx.request.domain;

import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXOpenRequestDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YLXOpenRequestRecordHandler {

    @Autowired
    protected YLXOpenRequestDetailRepository ylxOpenRequestDetailRepository;
    @Autowired
    protected YLXBatchRepository ylxBatchRepository;

    public void updateOpenRequestWithCurrentBatchId(YLXBatchDTO batch){
        ylxOpenRequestDetailRepository.updateBatchId(-1L, batch.getId(), batch.getTargetDate());
        /*ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                YLXBatchStatus.request_data_preparing.name(),
                YLXBatchSubStep.sub_step_1_3.name());
        batch.setSubNextStep(YLXBatchSubStep.sub_step_1_3.name())*/;
    }
}
