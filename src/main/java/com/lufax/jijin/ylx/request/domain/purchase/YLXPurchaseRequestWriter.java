package com.lufax.jijin.ylx.request.domain.purchase;

import com.lufax.jijin.base.utils.FileUtils;
import com.lufax.jijin.ylx.batch.domain.BatchFileContentUtil;
import com.lufax.jijin.ylx.batch.domain.YLXBatchType;
import com.lufax.jijin.ylx.batch.dto.YLXBatchDTO;
import com.lufax.jijin.ylx.batch.dto.YLXBatchFileDTO;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.request.domain.IYLXRequestWriter;
import com.lufax.jijin.ylx.util.YlxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

@Component
public class YLXPurchaseRequestWriter implements IYLXRequestWriter {

	@Autowired
	private YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
	
	@Override
	public BigDecimal writeRequestContent(File newFile,YLXBatchFileDTO batchFile, long start, long end, long total,YLXBatchDTO batch,int fileSize){

        //计算这个文件包含的记录集总金额
        BigDecimal amount = ylxBuyRequestDetailRepository.sumAmountByBatchIdAndRownum(batch.getId(), start, end);

        FileUtils.appendHeader(newFile,YlxConstants.PURCHASE_FILE_TYPE, batchFile.getVersion(),batchFile.getOrgCode(), batchFile.getTrxDate(), total,amount);
        FileUtils.appendContent(newFile, BatchFileContentUtil.createColumnName(YLXBatchType.YLX_SLP_PURCHASE_REQUEST));


        while (start <= end) {
            long targetEnd =0;
            if(fileSize<ROWNUM){
                targetEnd = start + fileSize;
            }else{
                targetEnd =  start + ROWNUM;
            }
            List<YLXBuyRequestDetailDTO> dtos = ylxBuyRequestDetailRepository.getYLXBuyRequestDTOsByBatchId(batch.getId(), start, targetEnd);
            String content = "";
            for (YLXBuyRequestDetailDTO dto : dtos) {
                content = BatchFileContentUtil.createPurchaseRequestContent(dto, batch);
                FileUtils.appendContent(newFile, content);
            }

            start = targetEnd;
        }

        return amount;
    }

    @Override
    public long getTotalRecords(long batchId) {

        return ylxBuyRequestDetailRepository.countBuyRequestBybatchId(batchId);

    }


}
