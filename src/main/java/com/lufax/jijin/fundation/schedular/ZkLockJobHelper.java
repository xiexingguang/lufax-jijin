package com.lufax.jijin.fundation.schedular;

import java.util.concurrent.TimeUnit;

import jxl.common.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.lufax.kernel.zookeeper.AcquireFailedException;
import com.lufax.kernel.zookeeper.LufaxInterProcessMutex;
import com.lufax.kernel.zookeeper.ZookeeperService;

/**
 * 需要ZK锁的job请直接继承此类即可
 * @author chenqunhui168
 *
 */
public abstract class ZkLockJobHelper {

	private static Logger logger = Logger.getLogger(ZkLockJobHelper.class);
	
	@Autowired
	private ZookeeperService zookeeperService;
	
	public  void execute(){
		String lockPath = this.getLockPath();
    	LufaxInterProcessMutex mutex = zookeeperService.newInterProcessMutex("/jobs/"+lockPath); 
        try {
            mutex.acquire(3000, TimeUnit.MILLISECONDS);
            process();
            logger.info(String.format("job [%s] get lock success", lockPath));
        }catch (AcquireFailedException e) {                                      
        	logger.info(String.format("job [%s] get lock failed", lockPath));
        } catch (Exception e) {                                                    
        	logger.error(String.format("job [%s] execute error", this.getClass().getSimpleName()),e);
        } finally {
            mutex.release(); 
            logger.info(String.format("job [%s] release lock success", lockPath));
        }
        
	}
	
	protected abstract void  process();
	

	/**
	 * 自定义lockpath
	 * @return
	 */
	protected String getLockPath(){
		return this.getClass().getSimpleName();
	}
}
