package com.lufax.jijin.daixiao.repository;

import java.util.List;
import java.util.Map;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.daixiao.dto.JijinExDictDTO;
import com.lufax.mq.client.util.MapUtils;
import org.springframework.stereotype.Repository;

@Repository
public class JijinExDictRepository extends BaseRepository<JijinExDictDTO> {

	@Override
	protected String nameSpace() {
		return "BusJijinExDict";
	}

	public JijinExDictDTO insertJijinExDict(JijinExDictDTO jijinExDictDTO) {
		return super.insert("insertJijinExDict", jijinExDictDTO);
	}

	/**
	 * 查询未被处理的代销基金目录
	 * 
	 * @param limit
	 * @return
	 */
	public List<JijinExDictDTO> getUnDispachedJijinExDictList(int limit){
		return super.queryList("getUnDispachedJijinExDictList", limit);
	}
	/**
	 * 修改代销名录的状态
	 * @param fundCode
	 * @param status
	 * @return
	 */
	public int updateJijinExDict(Map<Object,Object> condition){
		return super.update("updateJijinExDictStatusAndErrMsg", condition);
	}
}
