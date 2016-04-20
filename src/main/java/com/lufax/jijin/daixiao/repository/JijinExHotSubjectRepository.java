package com.lufax.jijin.daixiao.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.daixiao.dto.JijinExHotSubjectDTO;
import com.lufax.mq.client.util.MapUtils;
import com.site.lookup.util.StringUtils;

import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 人气方案
 * @author chenqunhui
 *
 */
@Repository
public class JijinExHotSubjectRepository extends BaseRepository<JijinExHotSubjectDTO> {

    @Override
    protected String nameSpace() {
        return "BusJijinExHotSubject";
    }


    public JijinExHotSubjectDTO insertJijinExHotSubject(JijinExHotSubjectDTO dto) {
        return super.insert("insertJijinExHotSubject", dto);
    }


    public List<JijinExHotSubjectDTO> getJijinExHotSubjectsByBatchId(Long batchId) {
        return super.queryList("getJijinExHotSubjectsByBatchId", batchId);
    }
    
    public List<JijinExHotSubjectDTO> getJijinExHotSubjectsByBatchIdAndFundCode(Long batchId,String fundCode){
    	return super.queryList("getJijinExHotSubjectsByBatchIdAndFundCode", MapUtils.buildKeyValueMap("batchId",batchId,"fundCode",fundCode));
    }

    public Long getJijinExHotSubjectsBatchIdByFundCode(String fundCode) {
        return (Long) super.queryObject("getJijinExHotSubjectsBatchIdByFundCode", fundCode);
    }

    public void batchInsertJijinExHotSubject(List<JijinExHotSubjectDTO> list) {
        batchInsert("insertJijinExHotSubject", list);
    }
    
    public List<JijinExHotSubjectDTO>  getUndispachedHotSubject(int limit){
    	return super.queryList("getUndispachedHotSubject", MapUtils.buildKeyValueMap("limit",limit));
    }
    /**
     * 按batchId和fundCode更新人气方案状态
     * @param batchId
     * @param fundCode 
     * @param status
     * @return
     */
    public int  batchUpdateHotSubjectStatusByBatchIdAndFundCode(Long batchId,String fundCode,String status,String errMsg){
    	if(StringUtils.isNotEmpty(errMsg) && errMsg.length()>1000){
    		errMsg = errMsg.substring(0, 1000);
    	}
    	return super.update("batchUpdateHotSubjectStatusByBatchIdAndFundCode", MapUtils.buildKeyValueMap("batchId",batchId,"fundCode",fundCode,"status",status,"errMsg",errMsg));
    }
}
