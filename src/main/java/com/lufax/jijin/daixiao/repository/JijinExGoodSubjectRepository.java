package com.lufax.jijin.daixiao.repository;

import java.util.List;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExGoodSubjectDTO;
import com.site.lookup.util.StringUtils;

import org.springframework.stereotype.Repository;


/**
 * 精选主题
 * @author chenqunhui
 *
 */
@Repository
public class JijinExGoodSubjectRepository extends BaseRepository<JijinExGoodSubjectDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExGoodSubject";
    }


    public Long getLatestBatchIdByFundCode(String fundCode){
        return (Long)super.queryObject("getLatestBatchIdByFundCode", fundCode);
    }

    public List<JijinExGoodSubjectDTO> getJijinExGoodSubjectsByBatchIdAndFundCode(Long batchId,String fundCode) {
        return super.queryList("getJijinExGoodSubjectsByBatchIdAndFundCode", MapUtils.buildKeyValueMap("batchId",batchId,"fundCode",fundCode));
    }

	public JijinExGoodSubjectDTO insertJijinExGoodSubject(JijinExGoodSubjectDTO dto){
		return super.insert("insertJijinExGoodSubject", dto);
	}
	
	public void batchInserJijinExFxPerform(List<JijinExGoodSubjectDTO> list){
        batchInsert("insertJijinExGoodSubject",list);

    }
	
	public List<JijinExGoodSubjectDTO> getUnDispachedJijinExGoodSubjectList(int limit){
		return super.queryList("getUnDispachedJijinExGoodSubjectList",MapUtils.buildKeyValueMap("limit",limit));
	}
	
	/**
	 * 批量更新同一批次的同一基金的状态
	 * @param batchId
	 * @param fundCode
	 * @param status
	 * @return
	 */
	public int batchUpdateJijinExGoodSubjectStatus(Long batchId,String fundCode,String status,String errMsg){
		if(StringUtils.isNotEmpty(errMsg) && errMsg.length()>1000){
    		errMsg = errMsg.substring(0, 1000);
    	}
		return super.update("batchUpdateJijinExGoodSubjectStatus", MapUtils.buildKeyValueMap("batchId",batchId,"fundCode",fundCode,"status",status,"errMsg",errMsg));
				
	}
}
