/**
 * 
 */
package com.lufax.jijin.fundation.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinIncreaseDTO;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 6, 2015 3:30:50 PM
 * 
 */
@Repository
public class JijinIncreaseRepository extends BaseRepository<JijinIncreaseDTO>{

	@Override
	protected String nameSpace() {
		return "BusJijinIncrease";
	}
	
	public JijinIncreaseDTO insertBusJijinIncrease(JijinIncreaseDTO dto) {
        return super.insert("insertBusJijinIncrease", dto);
    }

	public JijinIncreaseDTO findBusJijinIncrease(Map condition) {
        return super.query("findBusJijinIncrease", condition);
    }
	
    public List<JijinIncreaseDTO> findBusJijinIncreases(Map condition) {
        return super.queryList("findBusJijinIncrease", condition);
    }

}
