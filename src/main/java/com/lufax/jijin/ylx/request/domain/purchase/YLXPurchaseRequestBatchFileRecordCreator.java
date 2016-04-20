package com.lufax.jijin.ylx.request.domain.purchase;

import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.ylx.batch.domain.BatchFileContentUtil;
import com.lufax.jijin.ylx.batch.domain.YLXBatchFileStatus;
import com.lufax.jijin.ylx.batch.domain.YLXBatchStatus2;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.util.YlxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class YLXPurchaseRequestBatchFileRecordCreator {
    @Autowired
    protected YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
    @Autowired
    protected YLXBatchRepository ylxBatchRepository;
    @Autowired
    protected YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    protected JijinAppProperties jijinAppProperties;

    @Transactional
    public void insertBatchFile(YLXBatchDTO batch){


        int FILE_SIZE = Integer.valueOf(jijinAppProperties.getYlxFileMaxSize());
        Logger.info(this,String.format("FILE_SIZE %s",FILE_SIZE));
        //create file name and insert batch file
        long count = ylxBuyRequestDetailRepository.countBuyRequestBybatchId(batch.getId());
        Logger.info(this, String.format("start to create purchase request file for %s,record count:%s",batch.getId(),count));
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
                String num = BatchFileContentUtil.getStrSeq(i);
                String fileName = "prod_purchase_ljs_op_"+target+"_"+num+".txt";
                YLXBatchFileDTO file = new YLXBatchFileDTO();
                file.setBatchId(batch.getId());
                file.setFileName(fileName);
                file.setCurrentLine(0L);
                file.setStatus(YLXBatchFileStatus.init.name());
                file.setOrgCode(YlxConstants.SELL_ORG_CODE);
                file.setTrxDate(batch.getTargetDate());//上传日期，文件名里的交易日期
                file.setVersion(YlxConstants.VERSION);
                files.add(file);
            }

            ylxBatchFileRepository.batchInsert(files);
            ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                    YLXBatchStatus2.REQUEST_DATA_PREPARED.name(),null);

        }else{
            ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                    YLXBatchStatus2.COMPLETE.name(),
                    null);
        }
        Logger.info(this, String.format("end to create purchase request file for %s",batch.getId()));
    }
}
