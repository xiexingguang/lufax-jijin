package com.lufax.jijin.scheduler;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.SpringContextUtil;
import com.lufax.jijin.service.MqService;

public abstract class SchedulerJob implements Runnable{
    private String runId;
    private String runCallbackDest;
    private String jobDescription;
    public  String jobExeResult="00";
    protected SchedulerJob(String runId, String runCallbackDest, String jobDescription) {
        this.runId = runId;
        this.runCallbackDest = runCallbackDest;
        this.jobDescription=jobDescription;
    }

	public abstract void execute();

    /**
     * 如果有其他结果，可以覆盖该方法
     */
    protected void finished() {
       Logger.info(this, String.format("jobDescription is %s,runId is %s,runCallbackDest is %s finish -- start sending finish msg!", jobDescription, runId, runCallbackDest));
       MqService mqAppRemoteService= (MqService) SpringContextUtil.getBean(MqService.class);
       mqAppRemoteService.sendJobExeResultSuccess(runId,jobExeResult,runCallbackDest);
       Logger.info(this, String.format("jobDescription is %s,runId is %s,runCallbackDest is %s finish -- end sending finish msg ", jobDescription, runId, runCallbackDest));
    }

    @Override
    public void run() {
        try{
            execute();
        } catch (Exception e){
            jobExeResult="01";
            Logger.error(this,String.format("jobDescription is %s execute fail,error message is %s",jobDescription,e.getMessage()), e);
        }finally {
        	finished();
        }
    }
    
    @Override
    public int hashCode(){
    	return this.jobDescription.hashCode();
    }
    
    @Override
    public boolean equals(Object other){
    	if(other == null){
    		return false;
    	}else if(other instanceof SchedulerJob){
    		SchedulerJob o = (SchedulerJob)other;
    		return this.jobDescription.equals(o.jobDescription);
    	}else{
    		return false;
    	}
    }
    
	public String getJobDescription() {
		return jobDescription;
	}
}
