/**
 * 
 */
package com.lufax.jijin.fundation.schedular;

import java.util.List;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 14, 2015 10:55:10 AM
 * 
 */
public interface SyncFileRepository {
	
	/**
	 * Batch insert the file records into DB
	 * @param dtos
	 * @return
	 */
	public int batchInsertDTOs(List dtos);

}
