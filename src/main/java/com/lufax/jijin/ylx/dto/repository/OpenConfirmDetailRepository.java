package com.lufax.jijin.ylx.dto.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.ylx.dto.OpenConfirmDetailDTO;
@Repository
public class OpenConfirmDetailRepository extends BusdataBaseRepository<OpenConfirmDetailDTO> {

	@Override
	protected String nameSpace() {
		return "YLX_OPEN_CONFIRM_DETAIL";
	}
	
	public List<OpenConfirmDetailDTO> getYlxOpenConfirmsByInternalTrxId(List<Long> ids) {
        return super.queryList("getYlxOpenConfirmsByInternalTrxId", MapUtils.buildKeyValueMap("ids", ids));
    }

}
