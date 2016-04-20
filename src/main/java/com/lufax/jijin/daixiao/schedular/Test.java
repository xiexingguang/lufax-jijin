package com.lufax.jijin.daixiao.schedular;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Test {
    public static void main(String[] args) { 
    	
    	Date dateTmp1 = new Date();  
        final CountDownLatch latch = new CountDownLatch(5);
        
        
        int taskSize = 5;  
        // 创建一个线程池  
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);  
        // 创建多个有返回值的任务  
        List<Future> list = new ArrayList<Future>();  
        for (int i = 0; i < taskSize; i++) {  
         Callable c = new MyCallable(String.valueOf(i), latch);  
         // 执行任务并获取Future对象  
         Future f = pool.submit(c);  
         // System.out.println(">>>" + f.get().toString());  
         list.add(f);  
        }  
        // 关闭线程池  
        pool.shutdown();  

        try {
           System.out.println("等待5个子线程执行完毕...");
           latch.await();
           System.out.println("5个子线程已经执行完毕");
           // 获取所有并发任务的运行结果  
           for (Future f : list) {  
            // 从Future对象上获取任务的返回值，并输出到控制台  
            System.out.println(">>>" + f.get().toString());  
           } 
           System.out.println("继续执行主线程");
           Date dateTmp2 = new Date();  
    	   long time = dateTmp2.getTime() - dateTmp1.getTime(); 
    	   System.out.println("主任务返回运行结果,当前任务时间【" + time + "毫秒】");
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
}

class MyCallable implements Callable<Object> {  
private String taskNum;  
private CountDownLatch latch;  
MyCallable(String taskNum,CountDownLatch latch) {  
   this.taskNum = taskNum;
   this.latch=latch;
}

public Object call() throws Exception {  
	   System.out.println(">>>" + taskNum + "任务启动");  
	   Date dateTmp1 = new Date();  
	   Thread.sleep(5000);  
	   Date dateTmp2 = new Date();  
	   long time = dateTmp2.getTime() - dateTmp1.getTime();  
	   System.out.println(">>>" + taskNum + "任务终止"); 
	   latch.countDown();
	   return taskNum + "任务返回运行结果,当前任务时间【" + time + "毫秒】";  
	} 
}
