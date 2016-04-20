package com.lufax.jijin.scheduler.basejob;

import com.lufax.jijin.base.utils.Logger;

import java.util.List;

public abstract class BaseJob<T, K> {

    protected String jobName = this.getClass().getSimpleName();

    protected abstract List<T> fetchList(int batchAmount);

    

    protected abstract void processList(List<T> list);

    protected void initJob(){};
    
    protected int getBatchAmount(){return 0;}
    
    public void execute() {
    	initJob();
        int batchAmount = getBatchAmount();
        Logger.info(this, "the execute amount is: " + getBatchAmount());
        List<T> dataList = fetchList(batchAmount);
        Logger.info(this, String.format("Job (name: %s) start to process data(size: %d)", jobName, dataList.size()));
        if (dataList != null && !dataList.isEmpty()) {
            try {
                processList(dataList);
            } catch (Exception e) {
                Logger.error(this, String.format("Fail to process %s", jobName), e);
            }
        }
    }
}
