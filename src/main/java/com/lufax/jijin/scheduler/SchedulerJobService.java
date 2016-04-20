package com.lufax.jijin.scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.lufax.jijin.base.utils.Logger;
import org.springframework.stereotype.Service;

@Service
public class SchedulerJobService {

    public static final String JOB_PENDING = "PENDING";
    public static final String JOB_FAIL="FAIL";
    public static final String JOB_DUPLICATE="DUPLICATE";
    
    private static NoDuplicateThreadExecutor threadPoolTaskExecutor = new NoDuplicateThreadExecutor();

    public String executeThread(SchedulerJob thread){
        try {
            threadPoolTaskExecutor.execute(thread);
            return JOB_PENDING;
        }catch(DuplicateException e){
        	Logger.warn(this,String.format("start same job when job(%s) is still running for %dms" ,e.getJob().getJobDescription(), System.currentTimeMillis() - e.getStartMillis()));
        	return JOB_PENDING;
        }catch (Throwable t){
            Logger.error(this,String.format("execute job fail --job class:%s",thread.getClass().getName()));
            return JOB_FAIL;
        }
    }
    
    private final static class NoDuplicateThreadExecutor extends ThreadPoolExecutor{
		
    	private ConcurrentHashMap<Runnable,Long> runningThread = new ConcurrentHashMap<Runnable,Long>();
    	
		private Thread deamonThread ;
		
		public NoDuplicateThreadExecutor() {
			super(	50, 
					Integer.MAX_VALUE, 
					60L, 
					TimeUnit.SECONDS, 
					new SynchronousQueue<Runnable>(), 
					new CallerRunsPolicy());
			
			deamonThread = new Thread(new Runnable(){
				@Override
				public void run() {
					while(true){
						try {
							Thread.sleep(1000 * 60);
							for(Runnable r : runningThread.keySet()){
								long millis = runningThread.get(r);
								SchedulerJob job = (SchedulerJob)r;
								Logger.info(this,String.format("jijin job(%s) is running for (%d)ms" ,job.getJobDescription(), System.currentTimeMillis() - millis));
							}
						} catch (Exception e) {}
					}
				}
			});
			
			deamonThread.setDaemon(true);
			deamonThread.start();
		}

		@Override
		protected void beforeExecute(Thread t, Runnable r){ 
			runningThread.put(r, System.currentTimeMillis());
		}
		@Override
		protected void afterExecute(Runnable r, Throwable t) { 
			runningThread.remove(r);
		}
		@Override
		public void execute(Runnable r){
			if(!(r instanceof SchedulerJob)){
				throw new UnsupportedOperationException();
			}
			
			if(!runningThread.containsKey(r)){
				super.execute(r);
			}else{
				long startMillis = runningThread.get(r);
				SchedulerJob job = (SchedulerJob)r;
				throw new DuplicateException(startMillis, job);
			}
		}
	}
    
    public static class DuplicateException extends RuntimeException{
    	
		private static final long serialVersionUID = 7365576805284723837L;
		
		private long startMillis;
    	private SchedulerJob job;
    	
		public DuplicateException(long startMillis, SchedulerJob job) {
			super();
			this.startMillis = startMillis;
			this.job = job;
		}

		public long getStartMillis() {
			return startMillis;
		}

		public SchedulerJob getJob() {
			return job;
		}
    }
}
