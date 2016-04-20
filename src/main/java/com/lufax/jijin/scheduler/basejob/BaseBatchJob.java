package com.lufax.jijin.scheduler.basejob;

import com.lufax.jijin.base.utils.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BaseBatchJob<T, K> {

    protected static final int BATCH_AMOUNT = 100;
    protected static final int MAX_FAIL_AMOUNT = 1000;
    protected String jobName = this.getClass().getSimpleName();

    public void execute() {
        Logger.info(this, "the execute amount is: " + getBatchAmount());
        Logger.info(this, "the max fail amount is: " + getMaxFailAmount());
        List<K> previousItemKeys = new ArrayList<K>();
        Set<K> failedItemKeys = new HashSet<K>();
        boolean finalRoundFlag = false;
        int loopTime = 0;

        while (true) {
            loopTime += 1;
            int batchAmount = getBatchAmount() + failedItemKeys.size();
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
                } catch (Exception e) {                                 //todo add more logger info
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
}
