package com.lufax.jijin.fundation.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.base.utils.StringUtils;
import com.lufax.jijin.fundation.dto.BaseNumDTO;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;
import com.lufax.mq.client.util.MapUtils;

@Repository
public class JijinUserBalanceRepository extends BaseRepository<JijinUserBalanceDTO>{

    protected String nameSpace(){
        return "BusJijinUserBalance";
    }

    public Object insertBusJijinUserBalance(JijinUserBalanceDTO jijinUserBalanceDTO) {
        return super.insert("insertBusJijinUserBalance", jijinUserBalanceDTO);
    }
    
    public JijinUserBalanceDTO findBusJijinUserBalance(Map condition) {
        return super.query("findBusJijinUserBalance", condition);
    }

    public List<JijinUserBalanceDTO> findBusJijinUserBalanceList(Map condition) {
        return super.queryList("findBusJijinUserBalance", condition);
    }

    public JijinUserBalanceDTO findUserBalanceByFundCode(long userId, String fundCode) {
		return super.query("findBusJijinUserBalance", MapUtils.buildKeyValueMap("userId",userId,"fundCode",fundCode));
    }
    
    // 有乐观锁
    public int updateBusJijinUserBalance(Map condition) {
        return super.update("updateBusJijinUserBalance", condition);
    }
    
    // 有乐观锁
    public int updateFundAccount(JijinUserBalanceDTO dto) {
    	return update("updateBusJijinUserBalanceDTO", dto);
    }
    //无乐观锁
    public int updateUserBalanceDividendStatusAndType(Long userId,String fundCode,String dividendStatus,String dividendType){
    	Map<String,Object> param = new HashMap<String,Object>();
    	param.put("userId", userId);
    	param.put("fundCode",fundCode);
    	if(StringUtils.isNotBlank(dividendStatus)){
    		param.put("dividendStatus", dividendStatus);
    	}
    	if(StringUtils.isNotBlank(dividendType)){
    		param.put("dividendType", dividendType);
    	}
    	return update("updateUserBalanceDividendStatusAndType",param);
    }
    
    public Map<String, ?> findFundCodeByUserId(long userId) {
        return super.queryForMap("findFundCode", 
                MapUtils.buildKeyValueMap("userId",userId), "fundCode");
    }
    
    public List<JijinUserBalanceDTO> findFundCodeShareBalance(long userId, BigDecimal amount, Collection<String> fundCodes, int pageIndex, int pageLimit) {
        return super.selectForListPagination("findFundCodeShareBalanceByPage", MapUtils.buildKeyValueMap("userId",userId,
                "amount",amount, "fundCodes", new ArrayList<String>(new HashSet<String>(fundCodes))), pageIndex, pageLimit);
    }
}
