package com.lufax.jijin.fundation.schedular;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.service.JijinTradeReconService;

/**
 * 交易对账，生成对账结果文件
 * @author xuneng
 *
 */
@Service
public class HandleJijinTradeReconJob extends BaseBatchWithTimeLimitJob<JijinSyncFileDTO, Long> {

   	@Autowired
	private JijinSyncFileRepository jijinSyncFielRepository;
   	@Autowired
   	private JijinTradeReconService jijinTradeReconService;

    @Override
    protected Long getKey(JijinSyncFileDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinSyncFileDTO> fetchList(int batchAmount) {      
        
        return jijinSyncFielRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("bizType", SyncFileBizType.JIJIN_TRADE_RESULT.name(), "status", SyncFileStatus.INIT.name(),"limit", batchAmount));
    }

    @Override
    protected void process(JijinSyncFileDTO jijinSyncFileDTO) {
    	
    	jijinTradeReconService.process(jijinSyncFileDTO);

    }
}
