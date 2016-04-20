package com.lufax.jijin.daixiao.repository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.daixiao.dto.JijinExFundTypeDTO;
import com.site.lookup.util.StringUtils;


/**
 * 基金类型
 *
 * @author chenqunhui
 */

@Repository
public class JijinExFundTypeRepository  extends BaseRepository<JijinExFundTypeDTO>{

	@Override
	protected String nameSpace() {
		return "BusJijinExFundType";
	}
	
	public JijinExFundTypeDTO inserJijinExFundType(JijinExFundTypeDTO dto){
		return super.insert("insertJijinExFundType", dto);
	}
	public void batchInserJijinExFundType(List<JijinExFundTypeDTO> list){
	        batchInsert("insertJijinExFundType",list);
	}

    public List<JijinExFundTypeDTO> getJijinExFundTypeByFundCode(String fundCode) {
        return super.queryListObject("getJijinExFundTypeByFundCode", MapUtils.buildKeyValueMap("fundCode",fundCode));
    }

    public JijinExFundTypeDTO getLastestJijinExFundTypeByFundCode(String fundCode) {
        List<JijinExFundTypeDTO> list = super.queryListObject("getLastestJijinExFundTypeByFundCode", MapUtils.buildKeyValueMap("fundCode",fundCode));
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取未处理的记录
     * @param limit
     * @return
     */
    public List<JijinExFundTypeDTO> getUnDispachedJijinExFundType(int limit){
    	return super.queryList("getUnDispachedJijinExFundType", MapUtils.buildKeyValueMap("limit",limit));
    }
    
    /**
     * 更新记录状态
     * @param id
     * @param status
     * @return
     */
    public int updateJijinExFundTypeStatus(Long id,String status,String errMsg){
    	if(StringUtils.isNotEmpty(errMsg) && errMsg.length()>1000){
    		errMsg = errMsg.substring(0, 1000);
    	}
    	return super.update("updateJijinExFundTypeStatus", MapUtils.buildKeyValueMap("id",id,"status",status,"errMsg",errMsg));
    			
    }
}
