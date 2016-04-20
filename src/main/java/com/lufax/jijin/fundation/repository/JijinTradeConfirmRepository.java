package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinTradeConfirmDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JijinTradeConfirmRepository extends BaseRepository<JijinTradeConfirmDTO> {

    protected String nameSpace() {
        return "JijinTradeConfirm";
    }

    public Object insertJijinTradeConfirm(JijinTradeConfirmDTO jijinTradeConfirmDTO) {
        return super.insert("insertJijinTradeConfirm", jijinTradeConfirmDTO);
    }

    public List<JijinTradeConfirmDTO> getUnDispatchConfirms(String status, int maxNum,List<String>  bizTypes) {
        return super.queryList("getUnDispatchConfirms", MapUtils.buildKeyValueMap("status", status, "maxNum", maxNum, "bizTypes", bizTypes));
    }

    public void batchInsertJijinTradeConfirm(List jijinTradeConfirmDTOs) {
        super.batchInsert("insertJijinTradeConfirm", jijinTradeConfirmDTOs);
    }

    public int updateJijinTradeConfirmStatusById(long id, String status) {
        return super.update("updateJijinTradeConfirmStatusById", MapUtils.buildKeyValueMap("id", id, "status", status));
    }
}
