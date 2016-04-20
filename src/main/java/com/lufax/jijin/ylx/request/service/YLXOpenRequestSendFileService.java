package com.lufax.jijin.ylx.request.service;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.ylx.batch.domain.*;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.util.YlxConstants;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class YLXOpenRequestSendFileService extends YLXSendFileService{


    public void updateToFail(YLXBatchDTO batch,String msg){
        if (msg.length() >= 4000)
            msg = msg.substring(0, 3999);
        if(isFinalFail(batch.getType()))
            ylxBatchRepository.updateYLXBatchDTOFailById(batch.id(),
                    YLXBatchStatus2.REQUEST_FILE_SEND_FAIL.name(), msg, String.valueOf(batch.id()), batch.getRetryTimes());
        else{
            ylxBatchRepository.updateYLXBatchRetryTimes(batch.id(), msg, String.valueOf(batch.id()));
        }
    }
    @Transactional
    public void updateToSuccessAndInsertConfirm(YLXBatchDTO batch){
            //如果是OPEN_REQUEST更新状态为COMPLETE
            ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                    YLXBatchStatus2.COMPLETE.name(),
                    null);
            YLXBatchDTO confirmBatch = new YLXBatchDTO();

            Date targetDate = batch.getTargetDate();
            confirmBatch.setStatus(YLXBatchStatus2.CONFIRM_RECEIVE.name());
            confirmBatch.setTargetDate(targetDate);
            confirmBatch.setTriggerDate(DateUtils.startOfDay(tradeDayService.getNextTradeDayAsDate(targetDate)));
            confirmBatch.setCutOffId(batch.getCutOffId());
            confirmBatch.setType(YLXBatchType.YLX_SLP_OPEN_CONFIRM.name());
            confirmBatch.setReqBatchId(batch.getId());
            confirmBatch.setRunStatus(BatchRunStatus.IDLE);
            ylxBatchRepository.insertYLXBatchDTO(confirmBatch);
            generateConfirmBatchFiles(batch,confirmBatch);
    }

    private void generateConfirmBatchFiles(YLXBatchDTO requestBatch,YLXBatchDTO confirmBatch){
        List<YLXBatchFileDTO> requestFiles = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchId(requestBatch.getId());
        List<YLXBatchFileDTO> confirmFiles = new ArrayList<YLXBatchFileDTO>();
        for(int i=1;i<=requestFiles.size();i++){
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String target = format.format(confirmBatch.getTargetDate());
            String num = BatchFileContentUtil.getStrSeq(i);
            String fileName = "account_open_confirm_ljs_op_"+target+"_"+num+".txt";
            YLXBatchFileDTO file = new YLXBatchFileDTO();
            file.setBatchId(confirmBatch.getId());
            file.setFileName(fileName);
            file.setStatus(YLXBatchFileStatus.init.name());
            file.setOrgCode(YlxConstants.SELL_ORG_CODE);
            file.setTrxDate(confirmBatch.getTargetDate());//上传日期，文件名里的交易日期
            file.setVersion(YlxConstants.VERSION);
            file.setCurrentLine(0L);
            confirmFiles.add(file);
        }
        ylxBatchFileRepository.batchInsert(confirmFiles);
    }
}
