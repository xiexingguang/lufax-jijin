/**
 * 
 */
package com.lufax.jijin.fundation.schedular;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.fundation.service.DividendService;
import com.lufax.jijin.fundation.service.domain.Dividend;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 18, 2015 2:11:08 PM
 * 
 */
@Service
public class HandleDividendCashDispatchJob extends BaseBatchWithLimitJob<Dividend, Long> {
    
    @Autowired
    private DividendService service;

	@Override
	protected Long getKey(Dividend item) {
		return item.getId();
	}

	@Override
	protected List<Dividend> fetchList(int batchAmount) {
		return service.getNewDividendsByType(Dividend.Type.CASH, batchAmount);
	}
	
	@Override
    protected void process(Dividend dividend) {
		service.increaseDividend(dividend);
    }

}
