package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JijinTradeSyncRepository extends BaseRepository<JijinTradeSyncDTO> {

    protected String nameSpace() {
        return "JijinTradeSync";
    }

    public Object insertJijinTradeSync(JijinTradeSyncDTO jijinTradeSyncDTO) {
        return super.insert("insertJijinTradeSync", jijinTradeSyncDTO);
    }

    public List<JijinTradeSyncDTO> getUnDispatchRecords(String status, int batchAmount, List<String> businessCodes) {
        return super.queryList("getUnDispatchRecords", MapUtils.buildKeyValueMap("status", status, "maxNum", batchAmount, "businessCodes", businessCodes));
    }

    public void batchInsertJijinTradeSync(List jijinTradeSyncDTOs) {
        super.batchInsert("insertJijinTradeSync", jijinTradeSyncDTOs);
    }

    public int updateJijinTradeSyncStatusById(long id, String status) {
        return super.update("updateJijinTradeSyncStatusById", MapUtils.buildKeyValueMap("id", id, "status", status));
    }

    public JijinTradeSyncDTO getJijinTradeSyncByAppSheetNoAndFundCode(String appSheetNo, String fundCode) {
        return (JijinTradeSyncDTO) super.queryObject("getJijinTradeSyncByAppSheetNoAndFundCode", MapUtils.buildKeyValueMap("appSheetNo", appSheetNo, "fundCode", fundCode));
    }

    public int updateJijinTradeSyncStatusAndMemoById(long id, String status, String memo) {
        return super.update("updateJijinTradeSyncStatusAndMemoById", MapUtils.buildKeyValueMap("id", id, "status", status, "memo", memo));
    }

    public JijinTradeSyncDTO getJijinTradeSyncByAppSheetNoAndFundCodeAndBusCode(String appSheetNo, String fundCode, String businessCode) {
        return (JijinTradeSyncDTO) super.queryObject("getJijinTradeSyncByAppSheetNoAndFundCodeAndBusCode", MapUtils.buildKeyValueMap("appSheetNo", appSheetNo, "fundCode", fundCode, "businessCode", businessCode));
    }
    
    public JijinTradeSyncDTO getJijinTradeSyncByAppNoAndFundCodeAndBusCode(String appNo, String fundCode, String businessCode) {
        return (JijinTradeSyncDTO) super.queryObject("getJijinTradeSyncByAppNoAndFundCodeAndBusCode", MapUtils.buildKeyValueMap("appNo", appNo, "fundCode", fundCode, "businessCode", businessCode));
    }
    
    public JijinTradeSyncDTO getJijinTradeSyncTransformed(String appSheetNo) {
        return (JijinTradeSyncDTO) super.queryObject("getJijinTradeSyncTransformed", MapUtils.buildKeyValueMap("appSheetNo", appSheetNo));
    }
}
