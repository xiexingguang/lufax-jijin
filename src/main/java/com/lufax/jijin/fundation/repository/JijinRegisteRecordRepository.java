/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinRegisteRecordDTO;
import com.lufax.mq.client.util.MapUtils;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 5, 2015 4:44:58 PM
 * 
 */
@Repository
public class JijinRegisteRecordRepository extends BaseRepository<JijinRegisteRecordDTO>{

	@Override
	protected String nameSpace() {
		return "BusJijinRegisteRecord";
	}
	
    public JijinRegisteRecordDTO insertBJijinRegisteRecord(JijinRegisteRecordDTO record) {
        return super.insert("insertBusJijinRegisteRecord", record);
    }

    public JijinRegisteRecordDTO findJijinRegisteRecord(Map condition) {
        return super.query("findBusJijinRegisteRecord", condition);
    }
    
    public List<JijinRegisteRecordDTO> findJijinRegisteRecords(Map condition) {
        return super.queryList("findBusJijinRegisteRecord", condition);
    }

    /**
     * 分页查询开户记录
     * @param instId
     * @param createFrom 
     * @param createTo
     * @param batchNum
     * @param page 从1开始
     * @return
     */
    public List<JijinRegisteRecordDTO> batchFindRegisteList(String instId,Date createFrom,Date createTo, int batchNum, int page){
    	return super.queryList("batchFindRegisteList", MapUtils.buildKeyValueMap("instId",instId,"createFrom",createFrom,"createTo",createTo,"start",batchNum*(page-1),"end",batchNum*page));
    }
    /**
     * 统计开户记录总数
     * @param instId
     * @param createFrom
     * @param createTo
     * @return
     */
    public int countRegisteRecord(String instId,Date createFrom,Date createTo){
    	return (Integer)super.queryObject("countRegisteRecord", MapUtils.buildKeyValueMap("instId",instId,"createFrom",createFrom,"createTo",createTo));
    }
}
