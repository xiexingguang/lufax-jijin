package com.lufax.jijin.ylx.job;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.scheduler.basejob.BaseJob;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.request.service.YLXOpenRequestSendFileService;
import com.lufax.jijin.ylx.request.service.YLXSendFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class YLXOpenRequestFileSendJob extends BaseJob<YLXBatchDTO,Long> {
    @Autowired
    private YLXOpenRequestSendFileService ylxSendFileService;
    @Autowired
    private YLXBatchRepository ylxBatchRepository;
    @Override
    protected List<YLXBatchDTO> fetchList(int batchAmount) {
        return ylxSendFileService.getBatchSendFileRecords(YLXBatchType.YLX_SLP_OPEN_REQUEST, YLXBatchStatus2.REQUEST_FILE_CREATED,BatchRunStatus.IDLE);
    }

    @Override
    protected int getBatchAmount() {
        return 0;
    }

    @Override
    protected void processList(List<YLXBatchDTO> list) {
        for(YLXBatchDTO batchDTO : list){
            try {
                ylxBatchRepository.updateBatchRunStatusById(batchDTO.getId(), BatchRunStatus.ONGOING);
                if(ylxSendFileService.sendFiles(batchDTO)){
                    ylxSendFileService.updateToSuccessAndInsertConfirm(batchDTO);
                }else{
                    ylxSendFileService.updateToFail(batchDTO,null);
                }
            }catch (Exception e){
                Logger.error(this, String.format("YLXOpenRequestFileSendJob failed,batchId:%s", batchDTO.getId()),e);
                ylxSendFileService.updateToFail(batchDTO,e.getMessage());
            }finally {
                ylxBatchRepository.updateBatchRunStatusById(batchDTO.getId(),BatchRunStatus.IDLE);
            }
        }
    }
}
