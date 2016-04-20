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
 * 份额分红
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 15, 2015 9:48:41 AM
 * 
 */
@Service
public class HandleDividendSwitchDispatchJob extends BaseBatchWithLimitJob<Dividend, Long> {
	
	@Autowired
	private DividendService dividendService;

	@Override
	protected Long getKey(Dividend item) {
		return item.getId();
	}

	@Override
	protected List<Dividend> fetchList(int batchAmount) {
		return dividendService.getNewDividendsByType(Dividend.Type.SWITCH_DIVIDEND, batchAmount);
	}
	
	@Override
    protected void process(Dividend item) {
		dividendService.increaseDividend(item);
    }

}
