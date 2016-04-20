package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExAssetConfDTO;
import com.lufax.jijin.daixiao.dto.JijinExIndustryConfDTO;
import com.lufax.jijin.daixiao.dto.JijinExStockConfDTO;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenguang451 on 2016/1/6.
 */
@Repository
public class JijinExStockConfRepository extends BaseRepository<JijinExStockConfDTO> implements SyncFileRepository {

    @Override
    protected String nameSpace() {
        return "JijinExStockConf";
    }

    public JijinExStockConfDTO insert(JijinExStockConfDTO dto) {
        return super.insert("insert", dto);
    }

    public void batchInsert(List<JijinExStockConfDTO> list){
        batchInsert("insert",list);
    }

    public List<JijinExStockConfDTO> queryLatestStockConfRecordsByFundCode(String fundCode, String endDate, long batchId){
        return super.queryList("queryLatestStockConfRecordsByFundCode", MapUtils.buildKeyValueMap("fundCode", fundCode,"endDate",endDate, "batchId", batchId));
    }

    @Override
    public int batchInsertDTOs(List dtos) {
        return batchInsert("insert",dtos);
    }
    
    public JijinExStockConfDTO getStockConfLatestEndDateAndBatchIdByFundCode(String fundCode){
    	return (JijinExStockConfDTO) super.queryObject("getStockConfLatestEndDateAndBatchIdByFundCode", fundCode);
    }
}
