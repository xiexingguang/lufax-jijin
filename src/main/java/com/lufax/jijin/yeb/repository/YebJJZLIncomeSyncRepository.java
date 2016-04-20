package com.lufax.jijin.yeb.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BusdataBaseRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.yeb.dto.YebJJZLIncomeSyncDTO;

@Repository
public class YebJJZLIncomeSyncRepository extends BusdataBaseRepository<YebJJZLIncomeSyncDTO> {
	
	
    public Object insertYebIncomeSync(YebJJZLIncomeSyncDTO yebJJZLIncomeSyncDTO) {
        return super.insert("insertyebJJZLIncomeSync", yebJJZLIncomeSyncDTO);
    }
	

    public List<YebJJZLIncomeSyncDTO> findYebIncomeSync(IncomeSyncStatus status, int batchAmount) {
        return super.queryList("findYebIncomeSyncByStatusWithLimit", MapUtils.buildKeyValueMap("status", status.getCode(), "maxNum", batchAmount));
    }

    public int updateYebJJZLIncomeSync(Long yebJJZLIncomeSyncId, IncomeSyncStatus status) {
        return super.update("updateYebJJZLIncomeSync", MapUtils.buildKeyValueMap("id", yebJJZLIncomeSyncId, "status", status.getCode()));
    }
    
    public YebJJZLIncomeSyncDTO findLastSuccessYebIncomeSync(Map condition) {
        return super.query("findLastSuccessYebIncomeSync", condition);
    }
    
    @Override
    protected String nameSpace() {
        return "YebJJZLIncomeSync";
    }
}
