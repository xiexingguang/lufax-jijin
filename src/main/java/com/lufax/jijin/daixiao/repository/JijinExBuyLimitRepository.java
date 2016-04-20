package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExBuyLimitDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 认申购起点
 * @author 
 *
 */
@Repository
public class JijinExBuyLimitRepository extends BaseRepository<JijinExBuyLimitDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExBuyLimit";
    }

    public JijinExBuyLimitDTO insertJijinExBuyLimit(JijinExBuyLimitDTO JijinExBuyLimitDTO) {
        return super.insert("insertJijinExBuyLimit", JijinExBuyLimitDTO);
    }

    public void batchInsertJijinExBuyLimit(List<JijinExBuyLimitDTO> list){
        batchInsert("insertJijinExBuyLimit",list);
    }

    public int updateJijinExBuyLimit(Map condition) {
        return super.update("updateJijinExBuyLimit", condition);
    }

    public List<JijinExBuyLimitDTO> getJijinExBuyLimit(Map condition) {
        return super.queryList("getJijinExBuyLimit", condition);
    }

    public JijinExBuyLimitDTO getJijinExBuyLimitById(Long id) {
        List<JijinExBuyLimitDTO> jijinExBuyLimitDTOs = super.queryList("getJijinExBuyLimit", MapUtils.buildKeyValueMap("id", id));
        if (jijinExBuyLimitDTOs != null && jijinExBuyLimitDTOs.size() > 0) {
            return jijinExBuyLimitDTOs.get(0);
        } else {
            return null;
        }
    }

    public List<JijinExBuyLimitDTO> getJijinExBuyLimitByFundCodeAndBizCode(String fundCode, String bizCode) {
        return super.queryList("getJijinExBuyLimitByFundCodeAndBizCode", MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode));
    }
    /**
     * 获取最新批次的认申购限额数据
     * @param fundCode
     * @param bizCode
     * @return
     */
    public JijinExBuyLimitDTO getLatestJijinExBuyLimitByFundCodeAndBizCode(String fundCode, String bizCode){
    	 List<JijinExBuyLimitDTO> jijinExBuyLimitDTOs = super.queryList("getLatestJijinExBuyLimitByFundCodeAndBizCode", MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode));
    	 if(EmptyChecker.isEmpty(jijinExBuyLimitDTOs)){
    		 return null;
    	 }else{
    		 return jijinExBuyLimitDTOs.get(0);
    	 }
    }

    public Long getLatestBatchIdByDate(String fundCode, String bizCode) {
        return (Long) super.queryObject("getLatestBatchIdByCode", MapUtils.buildKeyValueMap("fundCode", fundCode, "bizCode", bizCode));
    }

    public List<JijinExBuyLimitDTO> getJijinExBuyLimitByBatchIdAndFundCode(Long batchId,String fundCode,String bizCode) {
        return super.queryList("getJijinExBuyLimitByBatchIdAndFundCode",MapUtils.buildKeyValueMap("batchId",batchId,"fundCode",fundCode,"bizCode", bizCode));
    }

    public List<JijinExBuyLimitDTO> getUnDispachedJijinExBuyLimit(int limit) {
        return super.queryList("getJijinExBuyLimit", MapUtils.buildKeyValueMap("limit", limit, "status", "NEW"));
    }
}
