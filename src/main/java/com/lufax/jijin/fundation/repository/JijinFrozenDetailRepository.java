package com.lufax.jijin.fundation.repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.dto.BaseNumDTO;
import com.lufax.jijin.fundation.dto.JijinFrozenDetailDTO;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JijinFrozenDetailRepository extends BaseRepository<JijinFrozenDetailDTO> {

    protected String nameSpace() {
        return "JijinFrozenDetail";
    }

    public Object insertJijinFrozenDetail(JijinFrozenDetailDTO jijinFrozenDetailDTO) {
        return super.insert("insertJijinFrozenDetail", jijinFrozenDetailDTO);
    }

    public List<JijinFrozenDetailDTO> findFrozenDetailByIdAndType(Long userBalanceId, List<String> types, int pageLimit, int offset) {
        return selectForListPagination("findFrozenDetailByIdAndType", MapUtils.buildKeyValueMap("userBalanceId", userBalanceId, "types", types), offset, pageLimit);
    }
    
    public long findCountByUserIdAndType(Long userBalanceId, List<String> types) {
    	BaseNumDTO numDTO = ((BaseNumDTO)super.queryObject("countByUserIdAndType", MapUtils.buildKeyValueMap("userBalanceId", userBalanceId, "types", types)));
        return null==numDTO ? 0 : numDTO.getNum();
    }
}
