package com.lufax.jijin.ylx.dto.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.dto.YLXAccountDTO;

@Repository
public class YLXAccountRepository extends BusdataBaseRepository<YLXAccountDTO> {

	@Override
	protected String nameSpace() {
		return "YLX_ACCOUNT";
	}

    @SuppressWarnings("unchecked")
    public List<Long> getUserByIds(List<Long> ids){
        return queryListObject("getUserByIds",MapUtils.buildKeyValueMap("ids",ids));

    }

	public List<YLXAccountDTO> getUserDTOsByIds(List<Long> ids){
		return queryList("getUserDTOsByIds",MapUtils.buildKeyValueMap("ids",ids));
		
	}

	public void batchInsert(List<YLXAccountDTO> accountList) {
		batchInsert("insert", accountList);
	}

}
