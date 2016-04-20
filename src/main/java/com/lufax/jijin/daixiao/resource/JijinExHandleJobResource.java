package com.lufax.jijin.daixiao.resource;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.schedular.Jobs.*;
import com.lufax.jijin.scheduler.SchedulerJob;
import com.lufax.jijin.scheduler.SchedulerJobService;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@Path("/fundation/ex/handle/job")
public class JijinExHandleJobResource {

    @InjectParam
    private SchedulerJobService schedulerJobService;
    @InjectParam
    private HandleJijinExGradeJob handleJijinExGradeJob;
    @InjectParam
    private HandleJijinExInfoJob handleJijinExInfoJob;
    @InjectParam
    private HandleJijinExManagerJob handleJijinExManagerJob;
    @InjectParam
    private HandleJijinExScopeJob handleJijinExScopeJob;
    @InjectParam
    private HandleJijinExRiskGradeJob handleJijinExRiskGradeJob;
    @InjectParam
    private HandleJijinExFundTypeJob handleJijinExFundTypeJob;
    @InjectParam
    private HandleJijinExGoodSubjectJob handleJijinExGoodSubjectJob;
    @InjectParam
    private HandleJijinExHotSubjectJob handleJijinExHotSubjectJob;
    @InjectParam
    private HandleJijinExDictJob handleJijinExDictJob;
    @InjectParam
    private HandleJijinNetValueJob handleJijinNetValueJob;
    @InjectParam
    private HandleJijinExFxPerformJob handleJijinExFxPerformJob;
    @InjectParam
    private HandleJijinExBuyLimitJob handleJijinExBuyLimitJob;
    @InjectParam
    private HandleJijinExDiscountJob handleJijinExDiscountJob;
    @InjectParam
    private HandleJijinExIncomeModeJob handleJijinExIncomeModeJob;
    @InjectParam
    private HandleJijinExFeeJob handleJijinExFeeJob;
    @InjectParam
    private HandleJijinExSellDayCountJob handleJijinExSellDayCountJob;
    @InjectParam
    private HandleJijinExMfPerformJob handleJijinExMfPerformJob;
    @InjectParam
    private HandleJijinExNetValueJob handleJijinExNetValueJob;
    @InjectParam
    private HandleJijinExYieldRateJob handleJijinExYieldRateJob;
    @InjectParam
    private HandleJijinExAnnounceJob handleJijinExAnnounceJob;


    /**
     * 处理基金评级信息,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-grade")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExGradeJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExGradeJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExGradeJob! ");
                handleJijinExGradeJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExGradeJob.execute();
                Logger.info(this, "End the handleJijinExGradeJob! ");
            }
        });
    }


    /**
     * 处理基金基本信息,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-info")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExInfoJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExInfoJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExInfoJob! ");
                handleJijinExInfoJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExInfoJob.execute();
                Logger.info(this, "End the handleJijinExInfoJob! ");
            }
        });
    }

    /**
     * 处理基金经理信息,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-manager")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExManagerJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExManagerJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExManagerJob! ");
                handleJijinExManagerJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExManagerJob.execute();
                Logger.info(this, "End the handleJijinExManagerJob! ");
            }
        });
    }

    /**
     * 处理基金规模信息,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-scope")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinScopeJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExScopeJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExScopeJob! ");
                handleJijinExScopeJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExScopeJob.execute();
                Logger.info(this, "End the handleJijinExScopeJob! ");
            }
        });
    }
    
    /**
     * 处理基金风险等级,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-risk-grade")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExRiskGradeJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExRiskGradeJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExRiskGradeJob! ");
                handleJijinExRiskGradeJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExRiskGradeJob.execute();
                Logger.info(this, "End the handleJijinExRiskGradeJob! ");
            }
        });
    }
    
    /**
     * 处理基金类型,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-fund-type")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExFundTypeJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExFundTypeJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinFundTypeJob! ");
                handleJijinExFundTypeJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExFundTypeJob.execute();
                Logger.info(this, "End the handleJijinFundTypeJob! ");
            }
        });
    }
    
    /**
     * 处理基金精选主题,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-good-subject")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExGoodSubjectJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExGoodSubjectJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExGoodSubjectJob! ");
                handleJijinExGoodSubjectJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExGoodSubjectJob.execute();
                Logger.info(this, "End the handleJijinExGoodSubjectJob! ");
            }
        });
    }
    
    /**
     * 处理基金人气方案,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-hot-subject")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExHotSubjectJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExHotSubjectJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExHotSubjectJob! ");
                handleJijinExHotSubjectJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExHotSubjectJob.execute();
                Logger.info(this, "End the handleJijinExHotSubjectJob! ");
            }
        });
    }
    
    /**
     * 处理基金供销名录,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-dict")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExDictJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExDictJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExDictJob! ");
                handleJijinExDictJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExDictJob.execute();
                Logger.info(this, "End the handleJijinExDictJob! ");
            }
        });
    }
    /**
     * 处理基金状态数据（数据来源为代销公司的07净值文件）,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-daixiao-jijin-statust")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinNetValueJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinNetValueJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinNetValueJob! ");
                handleJijinNetValueJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinNetValueJob.execute();
                Logger.info(this, "End the handleJijinNetValueJob! ");
            }
        });
    }

    /**
     * 处理基金申认购数据（数据来源为代销公司的01净值文件）,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-buy-limit")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinBuyLimitJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExBuyLimitJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExBuyLimitJob! ");
                handleJijinExBuyLimitJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExBuyLimitJob.execute();
                Logger.info(this, "End the handleJijinExBuyLimitJob! ");
            }
        });
    }
    /**
     * 处理基金优惠汇率数据（数据来源为代销公司的02文件）,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-discount")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinDiscountJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExDiscountJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExDiscountJob! ");
                handleJijinExDiscountJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExDiscountJob.execute();
                Logger.info(this, "End the handleJijinExDiscountJob! ");
            }
        });
    }

    /**
     * 处理基金默认分红方式数据（数据来源为代销公司的03文件）,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-income-mode")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinIncomeModeJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExIncomeModeJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExIncomeModeJob! ");
                handleJijinExIncomeModeJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExIncomeModeJob.execute();
                Logger.info(this, "End the handleJijinExIncomeModeJob! ");
            }
        });
    }

    /**
     * 处理基金赎回到帐如期数据（数据来源为代销公司的04文件）,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-sell-daycount")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinSellDayCountJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExSellDayCountJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExSellDayCountJob! ");
                handleJijinExSellDayCountJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExSellDayCountJob.execute();
                Logger.info(this, "End the handleJijinExSellDayCountJob! ");
            }
        });
    }

    /**
     * 处理基金费率数据（数据来源为代销公司的05文件）,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-fee")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinFeeJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExFeeJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExFeeJob! ");
                handleJijinExFeeJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExFeeJob.execute();
                Logger.info(this, "End the handleJijinExFeeJob! ");
            }
        });
    }

    /**

     * 处理基金净值增长率数据）,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-mf-perform")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExMfPerformJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExMfPerformJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExMfPerformJob! ");
                handleJijinExMfPerformJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExMfPerformJob.execute();
                Logger.info(this, "End the handleJijinExMfPerformJob! ");
            }
        });
    }
    
    /*
     * 处理基金净值数据（数据来源为代销公司的06文件）,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-ex-net-value")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExNetValueJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExNetValueJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExNetValueJob! ");
                handleJijinExNetValueJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExNetValueJob.execute();
                Logger.info(this, "End the handleJijinExNetValueJob! ");
            }
        });
    }

    /**

     * 处理指数涨跌幅娄据,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-fx-perform")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExfxPerformJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExfxPerformJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExFxPerformJob! ");
                handleJijinExFxPerformJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExFxPerformJob.execute();
                Logger.info(this, "End the handleJijinExFxPerformJob! ");
            }
        });
    }
    
    /**

     * 处理七日年化、万份收益,每10分钟处理2000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-yield-rate")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExYieldRateJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExYieldRateJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExYieldRateJob! ");
                handleJijinExYieldRateJob.setMAX_PROCESS_AMOUNT(2000); // set max process file number per round
                handleJijinExYieldRateJob.execute();
                Logger.info(this, "End the handleJijinExYieldRateJob! ");
            }
        });
    }
    
    /**

     * 处理基金公告信息
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-ex-announce")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinExAnnounceJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest){
    	
    	return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExAnnounceJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExAnnounceJob! ");
                handleJijinExAnnounceJob.setMAX_PROCESS_TIME(59*60*1000);
                handleJijinExAnnounceJob.execute();
                Logger.info(this, "End the handleJijinExAnnounceJob! ");
            }
        });
    }
}
