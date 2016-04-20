package com.lufax.jijin.daixiao.repository;

import java.util.List;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.daixiao.dto.JijinExRiskGradeDTO;
import com.lufax.mq.client.util.MapUtils;
import com.site.lookup.util.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

/**
 * 风险等级
 *
 * @author chenqunhui
 */
@Repository
public class JijinExRiskGradeRepository extends BaseRepository<JijinExRiskGradeDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExRiskGrade";
    }

    public JijinExRiskGradeDTO insertJijinExRiskGrade(JijinExRiskGradeDTO dto) {
        return super.insert("insertJijinExRiskGrade", dto);
    }


    public List<JijinExRiskGradeDTO> getJijinExRiskGradeByFundCode(String fundCode) {
        return super.queryList("getJijinExRiskGradeByFundCode", fundCode);
    }

    public JijinExRiskGradeDTO getLastestJijinExRiskGradeByFundCode(String fundCode) {
        List<JijinExRiskGradeDTO> list=  super.queryList("getLastestJijinExRiskGradeByFundCode", fundCode);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

	
	public void batchInsertJijinExRiskGrade(List<JijinExRiskGradeDTO> list){
        batchInsert("insertJijinExRiskGrade",list);
    }
	
	public List<JijinExRiskGradeDTO> getUndispachedRiskGrade(int limit){
		return super.queryList("getUndispachedRiskGrade",MapUtils.buildKeyValueMap("limit", limit));
	}
	
	public int updateJijinExRiskGradeStatus(Long id,String status,String errMsg){
		if(StringUtils.isNotEmpty(errMsg) && errMsg.length()>1000){
    		errMsg = errMsg.substring(0, 1000);
    	}
		return super.update("updateJijinExRiskGradeStatus", MapUtils.buildKeyValueMap("id",id,"status",status,"errMsg",errMsg));
	}
}
