/**
 * 
 */
package com.lufax.jijin.fundation.schedular;

/**
 * @author chenguang E-mail:chenguang451@pingan.com.cn
 * @version create time:May 20, 2015 7:37:24 PM
 * 
 */
public interface SyncFileLineProcessor {
	
	/**
	 * After read and parse the line to an object, you could do something on the object
	 * @param line
	 */
	public void processLine(Object line);
	
	/**
	 * do sth before insert to db
	 * @param line
	 */
	public void setPropertiesBeforeInsertToDB(Object line);

}
