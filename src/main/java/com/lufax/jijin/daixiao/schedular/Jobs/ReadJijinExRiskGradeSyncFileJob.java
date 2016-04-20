package com.lufax.jijin.daixiao.schedular.Jobs;

import java.util.List;

import com.lufax.jijin.base.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.DaiXiaoFileTypeEnum;
import com.lufax.jijin.daixiao.service.JijinExRiskGradeService;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;

@Service
public class ReadJijinExRiskGradeSyncFileJob extends BaseBatchWithLimitJob<JijinSyncFileDTO, Long> {

	@Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;
	@Autowired
	private JijinExRiskGradeService jijinExRiskGradeService;
	
	@Override
	protected Long getKey(JijinSyncFileDTO item) {
		return item.getId();
	}

	@Override
    protected List<JijinSyncFileDTO> fetchList(int batchAmount) {
        return jijinSyncFielRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("bizType", DaiXiaoFileTypeEnum.风险等级.getTypeName(), "status", SyncFileStatus.READY.name(), "limit", batchAmount));
    }
	
	@Override
    public void process(JijinSyncFileDTO jijinSyncFileDTO) {
        try {
            Logger.info(this, String.format("Start to read file :[id: %s] [fileName : %s]", jijinSyncFileDTO.getId(), jijinSyncFileDTO.getFileName()));
            jijinExRiskGradeService.recordFileSync(jijinSyncFileDTO);
            Logger.info(this, String.format("Success read file :[id: %s] [fileName : %s]", jijinSyncFileDTO.getId(), jijinSyncFileDTO.getFileName()));
        } catch (Exception e) {
            Logger.error(this, String.format("Read file :[id: %s] [fileName : %s] failed !", jijinSyncFileDTO.getId(), jijinSyncFileDTO.getFileName()));
            Long retryTimes = jijinSyncFileDTO.getRetryTimes();
            String errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg) && errorMsg.length() > 500) {
                errorMsg = errorMsg.substring(0, 500);
            }
            if (retryTimes < 10) {
                jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "retryTimes", retryTimes + 1, "memo", errorMsg));
            } else {
                jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", SyncFileStatus.READ_FAIL.name(), "retryTimes", retryTimes, "memo", errorMsg));
            }
        }
    }
}
