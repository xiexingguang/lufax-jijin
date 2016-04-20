package com.lufax.jijin.fundation.schedular;

import com.lufax.jersey.utils.Logger;
import com.lufax.kernel.zookeeper.AcquireFailedException;
import com.lufax.kernel.zookeeper.LufaxInterProcessMutex;
import com.lufax.kernel.zookeeper.ZookeeperService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

/**
 * 可限定batch最大记录集
 *
 * @param <T>
 * @param <K>
 * @author xuneng
 */
public abstract class BaseBatchWithLimitJob<T, K> {
    protected static final int BATCH_AMOUNT = 100;
    protected static final int MAX_FAIL_AMOUNT = 500;
    protected int MAX_PROCESS_AMOUNT = 1000; // default value - job can process max amount 

    protected String jobName = this.getClass().getSimpleName();
    private int count;

    @Resource
    private ZookeeperService zookeeperService;
    
    public void execute() {
    	String lockPath = this.getClass().getSimpleName();
    	LufaxInterProcessMutex mutex = zookeeperService.newInterProcessMutex("/jobs/"+lockPath); 
        try {
            mutex.acquire(2000, TimeUnit.MILLISECONDS);
            Logger.info(this, String.format("job [%s] get lock success", lockPath));
	        Logger.info(this, "the max fail amount is: " + getMaxFailAmount());
	        List<K> previousItemKeys = new ArrayList<K>();
	        Set<K> failedItemKeys = new HashSet<K>();
	        boolean finalRoundFlag = false;
	        int loopTime = 0;
	        count = 0;
	        while (true) {
	            loopTime += 1;
	            int batchAmount = getBatchAmount() + failedItemKeys.size();
	            if (count >= MAX_PROCESS_AMOUNT) break;
	            List<T> dataList = fetchList(batchAmount);
	            if (dataList.size() > 0) {
	                if (dataList.size() < batchAmount) {
	                    finalRoundFlag = true;
	                }
	                Logger.info(this, String.format("Job (name: %s, Round: %d) start to process data(size: %d)", jobName, loopTime, dataList.size()));
	                dataList = filterByPreviousFetch(dataList, previousItemKeys, failedItemKeys);
	
	                Logger.debug(this, String.format("Job (name: %s, Round: %d) previousItemKeys(size: %d): %s", jobName, loopTime, previousItemKeys.size(), previousItemKeys));
	                Logger.debug(this, String.format("Job (name: %s, Round: %d) failedItemKeys(size: %d): %s", jobName, loopTime, failedItemKeys.size(), failedItemKeys));
	
	                if (failedItemKeys.size() >= getMaxFailAmount()) {
	                    Logger.warn(this, String.format("Job (name: %s) the number of failed items exceeds the max amount, %d items failed", jobName, failedItemKeys.size()));
	                    break;
	                }
	                processList(dataList);
	                Logger.debug(this, String.format("Job (name: %s, Round: %d) finished process data(size: %d)", jobName, loopTime, dataList.size()));
	            } else {
	                Logger.debug(this, "no data found to process");
	                finalRoundFlag = true;
	            }
	
	            if (finalRoundFlag) break;
	        }
	        afterExecute(failedItemKeys);
        } catch (AcquireFailedException e) {                                      
        	Logger.info(this, String.format("job [%s] get lock failed", lockPath));
        } catch (Exception e) {                                                    
        	Logger.info(this, String.format("job [%s] get lock failed", lockPath));
        } finally {
            mutex.release(); 
            Logger.info(this, String.format("job [%s] release lock success", lockPath));
        }
    }

    protected void afterExecute(Set<K> failedItemKeys) {
    }

    private List<T> filterByPreviousFetch(List<T> dataList, List<K> previousItems, Set<K> failedItems) {
        List<K> tmpKeyList = getAllKeys(dataList);
        List<T> newList = new ArrayList<T>();
        for (T item : dataList) {
            K key = getKey(item);
            if (previousItems.contains(key)) {
                failedItems.add(key);
            } else if (!failedItems.contains(key)) {
                newList.add(item);
            }
        }
        previousItems.clear();
        previousItems.addAll(tmpKeyList);
        return newList;
    }

    private List<K> getAllKeys(List<T> dataList) {
        List<K> keyList = new ArrayList<K>();
        for (T t : dataList) {
            keyList.add(getKey(t));
        }
        return keyList;
    }

    protected abstract K getKey(T item);

    protected abstract List<T> fetchList(int batchAmount);

    protected void processList(List<T> list) {
        if (list.size() > 0) {
            for (T item : list) {
                try {
                    process(item);
                    count = count + 1;
                } catch (Exception e) {
                    Logger.error(this, String.format("Fail to process %s", jobName), e);
                }
            }
        }
    }

    protected void process(T item) {
    }

    protected int getBatchAmount() {
        return BATCH_AMOUNT;
    }

    protected int getMaxFailAmount() {
        return MAX_FAIL_AMOUNT;
    }

    public int getMAX_PROCESS_AMOUNT() {
        return MAX_PROCESS_AMOUNT;
    }

    public void setMAX_PROCESS_AMOUNT(int mAX_PROCESS_AMOUNT) {
        MAX_PROCESS_AMOUNT = mAX_PROCESS_AMOUNT;
    }
}