package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExYieldRateDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 年化收益
 *
 * @author chenqunhui
 */
@Repository
public class JijinExYieldRateRepository extends BaseRepository<JijinExYieldRateDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExYieldRate";
    }

    public JijinExYieldRateDTO insertJijinExYieldRate(JijinExYieldRateDTO dto) {
        return super.insert("insertJijinExYieldRate", dto);
    }
    

    public void batchInsertJijinExYieldRate(List<JijinExYieldRateDTO> list) {
        batchInsert("insertJijinExYieldRate", list);
    }
    
    public int updateJijinExYieldRateStatus(Long id, String status, Integer isValid, String errMsg) {
    	return super.update("updateJijinExYieldRateStatus", MapUtils.buildKeyValueMap("id", id, "status", status, "isValid", isValid, "errMsg", errMsg));
    }
    /**
    * 查询该fundCode下指定日期之后的数据，如果有数据，本条记录将不会推送到product
    *
    * @param fundCode
    * @param endDate
    * @return
    */
	public Integer countNumberOfAfterDate(String fundCode, String endDate) {
		return (Integer) super.queryObject("countNumberOfAfterDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "endDate", endDate));
	}
    /**
     * 将相同日期、fundCode的有效数据更新为无效
     *
     * @param id
     * @param fundCode
     * @param date
     * @return
     */
    public int updateSameFundCodeDateRecordToNotVaild(Long id, String fundCode, String endDate) {
        return super.update("updateSameFundCodeDateRecordToNotVaild", MapUtils.buildKeyValueMap("id", id, "fundCode", fundCode, "endDate", endDate));
    }
    
    public List<JijinExYieldRateDTO> getUnDispatchedJijinExYieldRate(int limit) {
        return super.queryList("getUnDispatchedJijinExYieldRate", limit);
    }
    
    public JijinExYieldRateDTO getLastestJijinExYieldRateByFundCode(String fundCode) {
        return (JijinExYieldRateDTO)super.queryObject("getLastJijinExYieldRateDTOByFundCode", fundCode);
    }

    public List<JijinExYieldRateDTO> getYieldRateDtoByFundCodeAndEndDate(String fundCode,String endDate){
        Map map = MapUtils.buildKeyValueMap("fundCode", fundCode, "endDate", endDate);
        return super.queryList("getYieldRateDtoByFundCodeAndEndDate", map);
    }

    public List<JijinExYieldRateDTO> getYieldRateByPage(String fundCode,int start, int end){
    	return super.queryList("getYieldRateByPage", MapUtils.buildKeyValueMap("fundCode",fundCode,"start",start,"end",end));
    }
}


