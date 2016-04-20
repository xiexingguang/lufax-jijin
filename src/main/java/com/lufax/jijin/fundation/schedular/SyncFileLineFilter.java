/**
 * 
 */
package com.lufax.jijin.fundation.schedular;

import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;

/**
 * The filter used in the batch job template, if you want to filter some lines to do not add them into DB, please
 * implement this interface
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 14, 2015 5:06:31 PM
 * 
 */
public interface SyncFileLineFilter {
	
	/**
	 * the line will be filtered when return is true
	 * @param line
	 * @return
	 */
	public boolean shouldFiltered(Object dto,JijinSyncFileDTO file);

}
