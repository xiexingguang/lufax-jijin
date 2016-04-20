package com.lufax.jijin.ylx.request.domain;

import com.lufax.jijin.base.utils.FileUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.ylx.batch.domain.*;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.batch.repository.YLXBatchFileRepository;
import com.lufax.jijin.ylx.batch.repository.YLXBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public abstract class YLXRequestBatchFileBaseCreator {
    @Autowired
    protected YLXBatchRepository ylxBatchRepository;
    @Autowired
    protected YLXBatchFileRepository ylxBatchFileRepository;
    @Autowired
    protected JijinAppProperties jijinAppProperties;

    protected abstract IYLXRequestWriter getWriter();

    public void createFiles(YLXBatchDTO batch){

        int FILE_SIZE = Integer.valueOf(jijinAppProperties.getYlxFileMaxSize());
        List<YLXBatchFileDTO> YLXBatchFileDTOs=ylxBatchFileRepository.getYLXBatchFileDTOsByBatchId(batch.getId());
        int FileTotalNum = YLXBatchFileDTOs.size();

        if(FileTotalNum!=0){
            long totalRecords=getWriter().getTotalRecords(batch.getId());

            for(YLXBatchFileDTO file :YLXBatchFileDTOs){
                String rootDir = jijinAppProperties.getYlxRequestRootDir();
                if(YLXBatchFileStatus.init.name().equals(file.getStatus())){
                    long id = file.id();
                    int seq = BatchFileContentUtil.getIntSeq(file.getFileName());
                    long startRow =FILE_SIZE*(seq-1)+1;
                    long endRow=0;
                    long total =0;
                    try{
                        File newFile = FileUtils.createEmptyFile(file.getFileName(), rootDir);

                        if(seq==FileTotalNum){ // means this is file is at last
                            endRow = totalRecords;
                            total = endRow-startRow+1;
                        }else{// means file is in middle
                            endRow = FILE_SIZE*seq;
                            total = FILE_SIZE;
                        }
                        BigDecimal amount = getWriter().writeRequestContent(newFile, file, startRow, endRow, total, batch, FILE_SIZE);
                        ylxBatchFileRepository.updateYLXBatchFileDTOSuccessById(id, YLXBatchFileStatus.created.name(), amount, file.getTrxDate(), total);
                    }catch(Exception e){
                        Logger.warn(this, "Creating file fail, retry！", e);
                        ylxBatchFileRepository.updateYLXBatchFileDTOStatusById(id, YLXBatchFileStatus.init.name(), file.getRetryTimes()+1,null,null);
                        throw new RuntimeException(e);
                    }
                }
            }

        }

        //update heart beat and next sub step
        /*ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                YLXBatchStatus.request_data_preparing.name(),
                YLXBatchSubStep.sub_step_1_5.name());
        batch.setSubNextStep(YLXBatchSubStep.sub_step_1_5.name());*/
        ylxBatchRepository.updateYLXBatchDTOSuccessById(batch.getId(),
                YLXBatchStatus2.REQUEST_FILE_CREATED.name(),
                null);

    }
}
