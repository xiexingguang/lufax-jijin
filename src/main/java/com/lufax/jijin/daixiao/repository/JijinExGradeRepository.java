package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.EmptyChecker;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExGradeDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 评级
 *
 * @author chenqunhui
 */
@Repository
public class JijinExGradeRepository extends BaseRepository<JijinExGradeDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExGrade";
    }

    public JijinExGradeDTO insertJijinExGrade(JijinExGradeDTO jijinExGradeDTO) {
        return super.insert("insertJijinExGrade", jijinExGradeDTO);
    }

    public int updateJijinExGrade(Map condition) {
        return super.update("updateJijinExGrade", condition);
    }

    public List<JijinExGradeDTO> getJijinExGradeByFundCode(String fundCode) {
        return super.queryList("getJijinExGradeByFundCode", fundCode);
    }


    public List<JijinExGradeDTO> getJijinExGradeByFundCodeAndRatingInterval(String fundCode, String ratingInterval, String ratingGagency) {
        return super.queryList("getJijinExGradeByFundCodeAndRatingInterval", MapUtils.buildKeyValueMap("fundCode", fundCode, "ratingInterval", ratingInterval, "ratingGagency", ratingGagency));
    }

    public void batchInsertDTOs(List dtos) {
        super.batchInsert("insertJijinExGrade", dtos);
    }

    public JijinExGradeDTO getJijinExGradeById(Long id) {
        return (JijinExGradeDTO) queryObject("getJijinExGradeById", id);
    }

    public List<JijinExGradeDTO> getJijinExGradesByStatusAndMaxNumAndRatingInterval(String status, int maxNum, String ratingInterval) {
        return super.queryList("getJijinExGradesByStatusAndMaxNumAndRatingInterval", MapUtils.buildKeyValueMap("status", status, "maxNum", maxNum, "ratingInterval", ratingInterval));
    }

    /**
     * 获取评级机构最新的评级
     *
     * @param fundCode
     * @param ratingGagency
     * @return
     */
    public JijinExGradeDTO getLatestJijinExGradeByFundCodeAndRatingGagency(String fundCode, String ratingGagency, String ratingInterval,Integer isValid) {
    	List<JijinExGradeDTO> list = super.queryList("getLatestJijinExGradeByFundCodeAndRatingGagency", MapUtils.buildKeyValueMap("fundCode", fundCode, "ratingGagency", ratingGagency, "ratingInterval", ratingInterval,"isValid",isValid));
    	if(EmptyChecker.isEmpty(list)){
    		return null;
    	}else{
    		return list.get(0);
    	}
    }

    public Integer countNumberOfAfterRateDate(String fundCode, String rateDate, String ratingInterval,String ratingGagency) {
        return (Integer) super.queryObject("countNumberOfAfterRateDate", MapUtils.buildKeyValueMap("fundCode", fundCode, "rateDate", rateDate, "ratingInterval", ratingInterval,"ratingGagency",ratingGagency));
    }

    /**
     * 将相同日期、相同评级、fundCode的有效数据更新为无效
     * @param id
     * @param fundCode
     * @param rateDate
     * @param ratingInterval
     * @return
     */
    public int updateSameFundCodeDateIntervalRecordToNotVaild(Long id, String fundCode, String rateDate, String ratingInterval,String ratingGagency) {
        return super.update("updateSameFundCodeDateIntervalRecordToNotVaild", MapUtils.buildKeyValueMap("id", id, "fundCode", fundCode, "rateDate", rateDate, "ratingInterval", ratingInterval,"ratingGagency",ratingGagency));
    }

}
