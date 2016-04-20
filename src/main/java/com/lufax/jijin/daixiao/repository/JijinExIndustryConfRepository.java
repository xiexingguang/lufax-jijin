package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExAssetConfDTO;
import com.lufax.jijin.daixiao.dto.JijinExIndustryConfDTO;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenguang451 on 2016/1/6.
 */
@Repository
public class JijinExIndustryConfRepository extends BaseRepository<JijinExIndustryConfDTO> implements SyncFileRepository {

    @Override
    protected String nameSpace() {
        return "JijinExIndustryConf";
    }

    public JijinExIndustryConfDTO insert(JijinExIndustryConfDTO dto) {
        return super.insert("insert", dto);
    }

    public void batchInsert(List<JijinExIndustryConfDTO> list){
        batchInsert("insert",list);
    }


    @Override
    public int batchInsertDTOs(List dtos) {
        return batchInsert("insert",dtos);
    }
    
    public JijinExIndustryConfDTO getIndustryLatestEndDateAndBatchIdByFundCode(String fundCode){
    	 return (JijinExIndustryConfDTO) super.queryObject("getIndustryLatestEndDateAndBatchIdByFundCode", fundCode);
    }
    
    public List<JijinExIndustryConfDTO> queryLatestRecordsByFundCodeAndEndDateAndBatchId(String fundCode,String endDate, long batchId){
        return super.queryList("queryLatestIndustryRecordsByFundCodeAndEndDateAndBatchId", MapUtils.buildKeyValueMap("fundCode", fundCode,"endDate",endDate,"batchId", batchId));
    }
}
