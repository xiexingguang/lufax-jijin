package com.lufax.jijin.ylx.request.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.ylx.batch.domain.BatchFileContentUtil;
import com.lufax.jijin.ylx.batch.domain.YLXBatchFileStatus;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.BatchRunStatus;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.util.YlxConstants;

@Service
public class YLXBuyRequestSendFileService extends YLXSendFileService{


    public void updateToFail(YLXBatchDTO batch,String msg){
        if (msg.length() >= 4000)
            msg = msg.substring(0, 3999);
        if(isFinalFail(batch.getType())) {
            ylxBatchRepository.updateYLXBatchDTOFailById(batch.id(),
                    YLXBatchStatus2.REQUEST_FILE_SEND_FAIL.name(), msg, String.valueOf(batch.id()), batch.getRetryTimes());

            List<YLXBuyRequestDetailDTO> dtos = ylxBuyRequestDetailRepository.getProductCodeByBatchId(batch.getId());

            for (YLXBuyRequestDetailDTO dto : dtos) {
                ProductDTO productDTO = productRepository.getByCode(dto.getProductCode());
                smsService.sendYlxRequestFilePushFailedMessage(batch.getTargetDate(), productDTO.getDisplayName());
            }
        }
        else{
            Logger.warn(this,String.format("YLXBuyRequestSendFileService fail batchId:%s",batch.getId()));
            ylxBatchRepository.updateYLXBatchRetryTimes(batch.id(), msg, String.valueOf(batch.id()));
        }
    }


    @Transactional
    public void updateToSuccessAndInsertConfirm(YLXBatchDTO batch){
            ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                    YLXBatchStatus2.REQUEST_FILE_SEND.name(),
                    null);
            YLXBatchDTO confirmBath = new YLXBatchDTO();

            Date targetDate = batch.getTargetDate();
            confirmBath.setStatus(YLXBatchStatus2.CONFIRM_RECEIVE.name());
            confirmBath.setTargetDate(targetDate);
            confirmBath.setTriggerDate(DateUtils.startOfDay(tradeDayService.getNextTradeDayAsDate(targetDate)));
            confirmBath.setCutOffId(batch.getCutOffId());
            confirmBath.setType(YLXBatchType.YLX_SLP_BUY_CONFIRM.name());
            confirmBath.setReqBatchId(batch.getId());
            confirmBath.setRunStatus(BatchRunStatus.IDLE);
            ylxBatchRepository.insertYLXBatchDTO(confirmBath);

            generateConfirmBatchFiles(batch,confirmBath);

    }
    private void generateConfirmBatchFiles(YLXBatchDTO requestBatch,YLXBatchDTO confirmBatch){
        List<YLXBatchFileDTO> requestFiles = ylxBatchFileRepository.getYLXBatchFileDTOsByBatchId(requestBatch.getId());
        List<YLXBatchFileDTO> confirmFiles = new ArrayList<YLXBatchFileDTO>();
        for(int i=1;i<=requestFiles.size();i++){
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String target = format.format(confirmBatch.getTargetDate());
            String num = BatchFileContentUtil.getStrSeq(i);
            String fileName = "prod_buy_confirm_ljs_op_"+target+"_"+num+".txt";
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
