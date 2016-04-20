package com.lufax.jijin.daixiao.schedular.Jobs;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.constant.DaiXiaoFileTypeEnum;
import com.lufax.jijin.daixiao.service.JijinExFeeService;
import com.lufax.jijin.daixiao.service.JijinExFileService;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.fundation.schedular.BaseBatchWithLimitJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@Service
public class ScanJijinExFileJob extends BaseBatchWithLimitJob<JijinSyncFileDTO, Long> {
    @Autowired
    private JijinSyncFileRepository jijinSyncFielRepository;

    @Autowired
    private JijinExFileService jijinExFileService;

    @Override
    protected Long getKey(JijinSyncFileDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinSyncFileDTO> fetchList(int batchAmount) {
        return jijinSyncFielRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("bizTypes", DaiXiaoFileTypeEnum.getTypeList(), "status", SyncFileStatus.INIT.name(), "limit", batchAmount));
    }

    @Override
    public void process(JijinSyncFileDTO jijinSyncFileDTO) {
        try {
            Logger.info(this, String.format("Start to read file :[id: %s] [fileName : %s]", jijinSyncFileDTO.getId(), jijinSyncFileDTO.getFileName()));
            //扫描文件并下载到NAS
            jijinExFileService.scanFile(jijinSyncFileDTO);
            Logger.info(this, String.format("Success read file :[id: %s] [fileName : %s]", jijinSyncFileDTO.getId(), jijinSyncFileDTO.getFileName()));
        } catch (Exception e) {
            Logger.error(this, String.format("Read file :[id: %s] [fileName : %s] failed !", jijinSyncFileDTO.getId(), jijinSyncFileDTO.getFileName()));
            jijinSyncFielRepository.updateBusJijinSyncFile(MapUtils.buildKeyValueMap("id", jijinSyncFileDTO.getId(), "status", SyncFileStatus.NOT_EXIST.name(), "memo", e.getClass().getName()));
        }
    }
}
