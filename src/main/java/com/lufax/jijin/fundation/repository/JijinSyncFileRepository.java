package com.lufax.jijin.fundation.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

@Repository
public class JijinSyncFileRepository extends BaseRepository<JijinSyncFileDTO>{

    protected String nameSpace(){
        return "BusJijinSyncFile";
    }

    public Long getNextVal(){
        return (Long)super.queryObject("getNextVal");
    }

    public Object insertBusJijinSyncFile(JijinSyncFileDTO busJijinSyncFileDTO) {
        return super.insert("insertBusJijinSyncFile", busJijinSyncFileDTO);
    }
    
    public int batchInsertBusJijinSyncFile(List<JijinSyncFileDTO> busJijinSyncFileDTOs) {
        return super.batchInsert("insertBusJijinSyncFile", busJijinSyncFileDTOs);
    }
    
    public JijinSyncFileDTO findBusJijinSyncFile(Map condition) {
        return super.query("findBusJijinSyncFile", condition);
    }

    public List<JijinSyncFileDTO> findBusJijinSyncFileList(Map condition) {
        return super.queryList("findBusJijinSyncFile", condition);
    }

    public int updateBusJijinSyncFile(Map condition) {
        return super.update("updateBusJijinSyncFile", condition);
    }
    
    public int updateBusJijinSyncFileStatus(long id, SyncFileStatus status, String comment){
    	 return super.update("updateBusJijinSyncFile", MapUtils.buildKeyValueMap("id", id, "status", status.name(),"memo", comment));
    }
}
