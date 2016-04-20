package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExAnnounceDTO;
import com.lufax.jijin.daixiao.dto.JijinExAssetConfDTO;
import com.lufax.jijin.daixiao.gson.JijinExAnnounceGson;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;

import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by chenguang451 on 2016/1/6.
 */
@Repository
public class JijinExAnnounceRepository extends BaseRepository<JijinExAnnounceDTO> implements SyncFileRepository {

    @Override
    protected String nameSpace() {
        return "JijinExAnnounce";
    }

    public JijinExAnnounceDTO insert(JijinExAnnounceDTO dto) {
        return super.insert("insert", dto);
    }

    public void batchInsert(List<JijinExAnnounceDTO> list){
        batchInsert("insert",list);
    }

    public JijinExAnnounceDTO queryLatestRecordByFundCode(String fundCode){
        return super.query("selectLatestRecordByFundCode", MapUtils.buildKeyValueMap("fundCode", fundCode));
    }
    
    public List<JijinExAnnounceGson> getPageListByFundCodeAndStartEnd(String fundCode,int start,int end){
    	return super.queryListObject("getPageByFundCodeAndStartEnd", MapUtils.buildKeyValueMap("fundCode", fundCode,"start",start,"end",end));   			
    }
    
    public int  countTotalNumByFundCode(String fundCode){
    	Integer i = (Integer)super.queryObject("countTotalNumByFundCode", fundCode); 
    	if(null != i){
    		return i;
    	}else{
    		return 0;
    	}
    }

    @Override
    public int batchInsertDTOs(List dtos) {
        return batchInsert("insert",dtos);
    }

    public List<JijinExAnnounceDTO> findUnDispatchedAnnounce(int limit){
    	return super.queryList("findUnDispatchedAnnounce", limit);
    }
    
    public void updateIsValidByObjectIdAndMaxBatchId(String objectId,Long maxBatchId){
    	super.update("updateIsValidByObjectIdAndMaxBatchId", MapUtils.buildKeyValueMap("objectId",objectId,"maxBatchId",maxBatchId));
    }
    
    public int updateStatusAndIsValidById(Long id,Integer isValid,String status){
    	return super.update("updateStatusAndIsValidById", MapUtils.buildKeyValueMap("id",id,"isValid",isValid,"status",status));
    }
    
    public int countBatchIdIsBiggerThanThis(String objectId,Long batchId){
    	return (Integer)super.queryObject("countBatchIdIsBiggerThanThis", MapUtils.buildKeyValueMap("objectId",objectId,"batchId",batchId));
    }
    
}
