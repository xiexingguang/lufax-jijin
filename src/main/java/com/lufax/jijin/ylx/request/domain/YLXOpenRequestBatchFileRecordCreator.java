package com.lufax.jijin.ylx.request.domain;

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.ylx.batch.domain.*;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXOpenRequestDetailRepository;
import com.lufax.jijin.ylx.util.YlxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class YLXOpenRequestBatchFileRecordCreator {
    @Autowired
    protected YLXOpenRequestDetailRepository ylxOpenRequestDetailRepository;
    @Autowired
    protected YLXBatchRepository ylxBatchRepository;
    @Autowired
    protected YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    protected JijinAppProperties jijinAppProperties;
    @Autowired
    protected TradeDayService tradeDayService;

    @Transactional
    public void insertBatchFile(YLXBatchDTO batch){

        int FILE_SIZE = Integer.valueOf(jijinAppProperties.getYlxFileMaxSize());
        Logger.info(this,String.format("FILE_SIZE %s",FILE_SIZE));
        //create file name and insert batch file
        long count = ylxOpenRequestDetailRepository.countOpenRequestBybatchId(batch.getId());
        Logger.info(this, String.format("start to create open request file for %s,record count:%s",batch.getId(),count));
        if(count!=0){
            long fileNumLoop =0l;// real file num is count/file_size +1
            if(count%FILE_SIZE==0){
                fileNumLoop = count/FILE_SIZE+1;
            }else if(count%FILE_SIZE>0){
                fileNumLoop = count/FILE_SIZE+2;
            }

            List<YLXBatchFileDTO> files = new ArrayList<YLXBatchFileDTO>();

            for(int i=1;i<fileNumLoop;i++){
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                String target = format.format(batch.getTargetDate());
//				Date today = new Date();
//		    	today = DateUtils.startOfDay(today);
                String num = BatchFileContentUtil.getStrSeq(i);
                String fileName = "account_open_ljs_op_"+target+"_"+num+".txt";
                YLXBatchFileDTO file = new YLXBatchFileDTO();
                file.setBatchId(batch.getId());
                file.setFileName(fileName);
                file.setStatus(YLXBatchFileStatus.init.name());
                file.setOrgCode(YlxConstants.SELL_ORG_CODE);
                file.setTrxDate(batch.getTargetDate());
                file.setVersion(YlxConstants.VERSION);
                file.setCurrentLine(0L);
                files.add(file);
            }

            ylxBatchFileRepository.batchInsert(files);
            //update heart beat and next sub step
            ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                    YLXBatchStatus2.REQUEST_DATA_PREPARED.name(), null);

        }else{
            //set job complete directly
            ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                    YLXBatchStatus2.COMPLETE.name(),
                    null);
            //把对应的CONFIRM BATCH 置为COMPLETE状态
            YLXBatchDTO dto = new YLXBatchDTO();

            Date targetDate = batch.getTargetDate();
            dto.setStatus(YLXBatchStatus2.COMPLETE.name());
            dto.setTargetDate(targetDate);
            dto.setTriggerDate(tradeDayService.getNextTradeDayAsDate(targetDate));
            dto.setCutOffId(batch.getCutOffId());
            dto.setType(YLXBatchType.YLX_SLP_OPEN_CONFIRM.name());
            dto.setReqBatchId(batch.getId());
            ylxBatchRepository.insertYLXBatchDTO(dto);
        }
        Logger.info(this, String.format("end to create open request file for %s",batch.getId()));
    }
}
