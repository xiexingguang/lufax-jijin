package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExFeeDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 费率
 * @author chenqunhui
 *
 */
@Repository
public class JijinExFeeRepository extends BaseRepository<JijinExFeeDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExFee";
    }

    public JijinExFeeDTO insertJijinExFee(JijinExFeeDTO JijinExFeeDTO) {
        return super.insert("insertJijinExFee", JijinExFeeDTO);
    }

    public void batchInsertJijinExFee(List<JijinExFeeDTO> list) {
        batchInsert("insertJijinExFee", list);
    }

    public int updateJijinExFee(Map condition) {
        return super.update("updateJijinExFee", condition);
    }

    public List<JijinExFeeDTO> getJijinExFee(Map condition) {
        return super.queryList("getJijinExFee", condition);
    }

    public JijinExFeeDTO getJijinExFeeById(Long id) {
        List<JijinExFeeDTO> jijinExFeeDTOs = super.queryList("getJijinExFee", MapUtils.buildKeyValueMap("id", id));
        if (jijinExFeeDTOs != null && jijinExFeeDTOs.size() > 0) {
            return jijinExFeeDTOs.get(0);
        } else {
            return null;
        }
    }

    public List<JijinExFeeDTO> getJijinExFeeByFundCode(String fundCode) {
        return super.queryList("getJijinExFeeByFundCode", fundCode);
    }

    public List<JijinExFeeDTO> getJijinExFeeByFundCodeAndFeeType(String fundCode, String feeType) {
        return super.queryList("getJijinExFeeByFundCodeAndFeeType", MapUtils.buildKeyValueMap("fundCode", fundCode, "feeType", feeType));
    }
    /**
     * 获取最近的批次ID
     * @param fundCode
     * @param feeType
     * @return
     */
    public Long getLatestBatchIdByFundCodeAndType(String fundCode,String feeType){
    	return (Long)super.queryObject("getLatestBatchIdByFundCodeAndType",MapUtils.buildKeyValueMap("fundCode",fundCode,"feeType",feeType));
    }

    public Long getLatestBatchIdByDate(String fundCode, String feeType, String currentDate) {
        return (Long) super.queryObject("getLatestFeeBatchIdByDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "feeType", feeType, "currentDate", currentDate));
    }

    public List<JijinExFeeDTO> getJijinExFeeByBatchIdAndFundCodeAndBizCode(Long batchId,String fundCode,String bizCode) {
        return super.queryList("getJijinExFeeByBatchIdAndFundCodeAndBizCode",MapUtils.buildKeyValueMap("batchId",batchId,"fundCode",fundCode,"feeType",bizCode));
    }

    public List<JijinExFeeDTO> getUnDispachedJijinExFee(int limit) {
        return super.queryList("getJijinExFee", MapUtils.buildKeyValueMap("limit", limit, "status", "NEW"));
    }
}
