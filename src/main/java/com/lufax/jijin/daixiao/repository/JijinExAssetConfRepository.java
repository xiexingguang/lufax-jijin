package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExAssetConfDTO;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chenguang451 on 2016/1/6.
 */
@Repository
public class JijinExAssetConfRepository extends BaseRepository<JijinExAssetConfDTO> implements SyncFileRepository {

    @Override
    protected String nameSpace() {
        return "JijinExAssetConf";
    }

    public JijinExAssetConfDTO insert(JijinExAssetConfDTO dto) {
        return super.insert("insert", dto);
    }

    public void batchInsert(List<JijinExAssetConfDTO> list){
        batchInsert("insert",list);
    }


    public JijinExAssetConfDTO getLatestAssetConfByFundCode(String fundCode){
    	return (JijinExAssetConfDTO)super.queryObject("getLatestAssetConfByFundCode", fundCode);
    }

    @Override
    public int batchInsertDTOs(List dtos) {
        return batchInsert("insert",dtos);
    }
}
