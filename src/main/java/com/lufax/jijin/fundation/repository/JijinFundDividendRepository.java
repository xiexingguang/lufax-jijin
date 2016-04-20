/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinFundDividendDTO;
import com.lufax.mq.client.util.MapUtils;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 25, 2015 9:41:57 AM
 * 
 */
@Repository
public class JijinFundDividendRepository  extends BaseRepository<JijinFundDividendDTO>{

	@Override
	protected String nameSpace() {
		return "BusJijinFundDividend";
	}
	
    public List<JijinFundDividendDTO> findBusJijinFundDividendsByFund(String fundCode) {
        return super.queryList("findJijinFundDividend", MapUtils.buildKeyValueMap("fundCode", fundCode));
    }

}
