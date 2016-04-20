package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExBondConfDTO;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenguang451 on 2016/1/6.
 */
@Repository
public class JijinExBondConfRepository extends BaseRepository<JijinExBondConfDTO> implements SyncFileRepository {

    @Override
    protected String nameSpace() {
        return "JijinExBondConf";
    }

    public JijinExBondConfDTO insert(JijinExBondConfDTO dto) {
        return super.insert("insert", dto);
    }

    public void batchInsert(List<JijinExBondConfDTO> list){
        batchInsert("insert",list);
    }

    @Override
    public int batchInsertDTOs(List dtos) {
        return batchInsert("insert",dtos);
    }
    
    /**
     * 按end_date desc, batch_id desc,取第一条数据
     * @param fundCode
     * @return
     */
    public JijinExBondConfDTO getBondConfLastEndDateAndBatchIdByFundCode(String fundCode){
    	return (JijinExBondConfDTO)super.queryObject("getBondConfLastEndDateAndBatchIdByFundCode", fundCode);
    }
    
    public List<JijinExBondConfDTO> getBondConfListByFundCodeAndEndDateAndBatchId(String fundCode,String endDate,Long batchId){
    	return super.queryList("getBondConfListByFundCodeAndEndDateAndBatchId", MapUtils.buildKeyValueMap("fundCode", fundCode,"endDate",endDate, "batchId", batchId));
    }
}
