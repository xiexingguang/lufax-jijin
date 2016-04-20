package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExMaPerfDTO;
import com.lufax.jijin.fundation.schedular.SyncFileRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chenguang451 on 2016/1/6.
 */
@Repository
public class JijinExMaPerfRepository extends BaseRepository<JijinExMaPerfDTO> implements SyncFileRepository {

    @Override
    protected String nameSpace() {
        return "JijinExMaPerf";
    }

    public JijinExMaPerfDTO insert(JijinExMaPerfDTO dto) {
        return super.insert("insert", dto);
    }

    public void batchInsert(List<JijinExMaPerfDTO> list){
        batchInsert("insert",list);
    }

    /**
     * 换pub_date desc, batch_id desc,取第一条数据
     * @param maId
     * @return
     */
    public JijinExMaPerfDTO getManagerLatestPubDateAndBatchIdByManagerId(String maId) {
        return (JijinExMaPerfDTO) super.queryObject("getManagerLatestPubDateAndBatchIdByManagerId", maId);
    }

    public List<JijinExMaPerfDTO> queryLatestMaPerRecordByManagerIdAndPubDateAndBatchId(String managerId,String pubDate,Long batchId){
        return super.queryList("queryLatestMaPerRecordByManagerIdAndPubDateAndBatchId", MapUtils.buildKeyValueMap("maId", managerId,"pubDate",pubDate,"batchId",batchId));
    }


    @Override
    public int batchInsertDTOs(List dtos) {
        return batchInsert("insert",dtos);
    }

}
