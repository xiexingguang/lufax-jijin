package com.lufax.jijin.fundation.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.BaseNumDTO;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;

@Repository
public class JijinTradeLogRepository extends BaseRepository<JijinTradeLogDTO> {

    protected String nameSpace() {
		return "JijinTradeLog";
    }

    public Object insertBusJijinTradeLog(JijinTradeLogDTO jijinTradeLogDTO) {
        return super.insert("insertBusJijinTradeLog", jijinTradeLogDTO);
    }

	public long countByAccountAndTypeList(Long userId, List<String> types, String fundCode, String status) {
		BaseNumDTO numDTO = ((BaseNumDTO)super.queryObject("countByUserIdAndType", 
				MapUtils.buildKeyValueMap("userId", userId, "types", types, "fundCode", fundCode, "status", status)));
        return null==numDTO ? 0L : numDTO.getNum();
	}

	public List<JijinTradeLogDTO> findPaginationByAccountAndTypeList(Long userId, 
			List<String> types, String fundCode, String status, int pageLimit, int pageIndex) {
		return selectForListPagination("findTradeLogByIdAndType", MapUtils.buildKeyValueMap("userId", userId, 
				"types", types, "fundCode", fundCode, "status", status), pageIndex, pageLimit);
	}
}
