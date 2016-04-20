package com.lufax.jijin.daixiao.resource;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.schedular.Jobs.*;
import com.lufax.jijin.daixiao.schedular.tools.DxBatchJobTemplate;
import com.lufax.jijin.fundation.schedular.BatchJobTemplate;
import com.lufax.jijin.fundation.schedular.SyncFileLineFilter;
import com.lufax.jijin.fundation.schedular.SyncFileLineProcessor;
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
@Path("/fundation/ex/read/job")
public class JijinExReadJobResource {
    @InjectParam
    private SchedulerJobService schedulerJobService;
    @InjectParam
    private ReadJijinExBuyLimitSyncFileJob readJijinExBuyLimitSyncFileJob;
    @InjectParam
    private ReadJijinExDiscountSyncFileJob readJijinExDiscountSyncFileJob;
    @InjectParam
    private ReadJijinExIncomeModeSyncFileJob readJijinExIncomeModeSyncFileJob;
    @InjectParam
    private ReadJijinExFeeSyncFileJob readJijinExFeeSyncFileJob;
    @InjectParam
    private ReadJijinExSellDayCountSyncFileJob readJijinExSellDayCountSyncFileJob;
    @InjectParam
    private ReadJijinExSellLimitSyncFileJob readJijinExSellLimitSyncFileJob;
    @InjectParam
    private ReadJijinExFundTypeSyncFileJob readJijinExFundTypeSyncFileJob;
    @InjectParam
    private ReadJijinExFxPerformSyncFileJob readJijinExFxPerformSyncFileJob;
    @InjectParam
    private ReadJijinExGoodSubjectSyncFileJob readJijinExGoodSubjectSyncFileJob;
    @InjectParam
    private ReadJijinExHotSubjectSyncFileJob readJijinExHotSubjectSyncFileJob;
    @InjectParam
    private ReadJijinExMfPerformSyncFileJob readJijinExMfPerformSyncFileJob;
    @InjectParam
    private ReadJijinExRiskGradeSyncFileJob readJijinExRiskGradeSyncFileJob;
    @InjectParam
    private ReadJijinExManagerSyncFileJob readJijinExManagerSyncFileJob;
    @InjectParam
    private ReadJijinExScopeSyncFileJob readJijinExScopeSyncFileJob;
    @InjectParam
    private ReadJijinExGradeSyncFileJob readJijinExGradeSyncFileJob;
    @InjectParam
    private ReadJijinExInfoSyncFileJob readJijinExInfoSyncFileJob;
    @InjectParam
    private ReadJijinExDividendSyncFileJob readJijinExDividendSyncFileJob;
    @InjectParam
    private ReadJijinExNetValueSyncFileJob readJijinExNetValueSyncFileJob;
    @InjectParam
    private ReadJijinExYieldRateSyncFileJob readJijinExYieldRateSyncFileJob;
    @InjectParam
    private DxBatchJobTemplate readWorker;



    @GET
    @Path("/read-buy-limit-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readExBuyLimitSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readExBuyLimitSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readExBuyLimitSyncFileJob! ");
                readJijinExBuyLimitSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExBuyLimitSyncFileJob.execute();
                Logger.info(this, "End the readExBuyLimitSyncFileJob! ");
            }
        });
    }

    @GET
    @Path("/read-discount-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readExDiscountSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readExDiscountSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readExDiscountSyncFileJob! ");
                readJijinExDiscountSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExDiscountSyncFileJob.execute();
                Logger.info(this, "End the readExDiscountSyncFileJob! ");
            }
        });
    }

    @GET
    @Path("/read-fee-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readExFeeSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readExFeeSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readExFeeSyncFileJob! ");
                readJijinExFeeSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExFeeSyncFileJob.execute();
                Logger.info(this, "End the readExFeeSyncFileJob! ");
            }
        });
    }

    @GET
    @Path("/read-income-mode-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readExIncomeModeSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readExIncomeModeSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readExIncomeModeSyncFileJob! ");
                readJijinExIncomeModeSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExIncomeModeSyncFileJob.execute();
                Logger.info(this, "End the readExIncomeModeSyncFileJob! ");
            }
        });
    }

    @GET
    @Path("/read-sell-daycount-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readExSellDayCountSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readExSellDayCountSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readExSellDayCountSyncFileJob! ");
                readJijinExSellDayCountSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExSellDayCountSyncFileJob.execute();
                Logger.info(this, "End the readExSellDayCountSyncFileJob! ");
            }
        });
    }

    @GET
    @Path("/read-sell-limit-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readExSellLimitSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readExSellLimitSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readExSellLimitSyncFileJob! ");
                readJijinExSellLimitSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExSellLimitSyncFileJob.execute();
                Logger.info(this, "End the readExSellLimitSyncFileJob! ");
            }
        });
    }
    
    @GET
    @Path("/read-fund-type-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readExFundTypeSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExFundTypeSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExFundTypeSyncFileJob! ");
                readJijinExFundTypeSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExFundTypeSyncFileJob.execute();
                Logger.info(this, "End the readJijinExFundTypeSyncFileJob! ");
            }
        });
    }
    
    @GET
    @Path("/read-fx-perform-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExFxPerformSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExFxPerformSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExFxPerformSyncFileJob! ");
                readJijinExFxPerformSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExFxPerformSyncFileJob.execute();
                Logger.info(this, "End the readJijinExFxPerformSyncFileJob! ");
            }
        });
    }
    
    @GET
    @Path("/read-good-subject-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExGoodSubjectSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExGoodSubjectSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExGoodSubjectSyncFileJob! ");
                readJijinExGoodSubjectSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExGoodSubjectSyncFileJob.execute();
                Logger.info(this, "End the readJijinExGoodSubjectSyncFileJob! ");
            }
        });
    }
    
    @GET
    @Path("/read-hot-subject-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExHotSubjectSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExHotSubjectSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExHotSubjectSyncFileJob! ");
                readJijinExHotSubjectSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExHotSubjectSyncFileJob.execute();
                Logger.info(this, "End the readJijinExHotSubjectSyncFileJob! ");
            }
        });
    }
    
    @GET
    @Path("/read-mf-perform-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExMfPerformSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExMfPerformSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExMfPerformSyncFileJob! ");
                readJijinExMfPerformSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExMfPerformSyncFileJob.execute();
                Logger.info(this, "End the readJijinExMfPerformSyncFileJob! ");
            }
        });
    }
    
    @GET
    @Path("/read-risk-grade-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExRiskGradeSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExRiskGradeSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExRiskGradeSyncFileJob! ");
                readJijinExRiskGradeSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExRiskGradeSyncFileJob.execute();
                Logger.info(this, "End the readJijinExRiskGradeSyncFileJob! ");
            }
        });
    }


    /**
     * 0 0/30 * * * ?
     * 读取基金经理文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-manager-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExManagerSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExManagerSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExManagerSyncFileJob! ");
                readJijinExManagerSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExManagerSyncFileJob.execute();
                Logger.info(this, "End the readJijinExManagerSyncFileJob! ");
            }
        });
    }


    /**
     * 0 0/30 * * * ?
     * 读取基金规模文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-scope-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExScopeSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExScopeSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExScopeSyncFileJob! ");
                readJijinExScopeSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExScopeSyncFileJob.execute();
                Logger.info(this, "End the readJijinExScopeSyncFileJob! ");
            }
        });
    }

    /**
     * 0 0/30 * * * ?
     * 读取基金评级文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-grade-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExGradeSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExGradeSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExGradeSyncFileJob! ");
                readJijinExGradeSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExGradeSyncFileJob.execute();
                Logger.info(this, "End the readJijinExGradeSyncFileJob! ");
            }
        });
    }

    /**
     * 0 0/30 * * * ?
     * 读取基金基本信息文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-info-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExInfoSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExInfoSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExInfoSyncFileJob! ");
                readJijinExInfoSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExInfoSyncFileJob.execute();
                Logger.info(this, "End the readJijinExInfoSyncFileJob! ");
            }
        });
    }

    /**
     * 0 0/30 * * * ?
     * 读取基金历史分红文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-dividend-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExDividendSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExDividendSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExDividendSyncFileJob! ");
                readJijinExDividendSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExDividendSyncFileJob.execute();
                Logger.info(this, "End the readJijinExDividendSyncFileJob! ");
            }
        });
    }


    /**
     * 0 0/30 * * * ?
     * 读取基金净值文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-net-value-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExNetValueSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExNetValueSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExNetValueSyncFileJob! ");
                readJijinExNetValueSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExNetValueSyncFileJob.execute();
                Logger.info(this, "End the readJijinExNetValueSyncFileJob! ");
            }
        });
    }
    
    /**
     * 0 0/15 * * * ?
     * 读取基金年化收益文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-yield-rate-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExYieldRateSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinExYieldRateSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinExYieldRateSyncFileJob! ");
                readJijinExYieldRateSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinExYieldRateSyncFileJob.execute();
                Logger.info(this, "End the readJijinExYieldRateSyncFileJob! ");
            }
        });
    }


    /**
     * 0 0/15 * * * ?
     * 读取基金资产配置文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-asset-conf-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExAssetConfFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ReadJob(runId, runCallbackDest, "readJijinExAssetConfSyncFileJob", "assetconf.avsc", null, null, 10));
    }

    /**
     * 0 1/15 * * * ?
     * 读取基金行业配置文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-industry-conf-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExIndustryConfFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ReadJob(runId, runCallbackDest, "readJijinExIndustryConfSyncFileJob", "industryconf.avsc", null, null, 10));
    }


    /**
     * 0 2/15 * * * ?
     * 读取基金持股配置文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-stock-conf-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExStockConfFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ReadJob(runId, runCallbackDest, "readJijinExStockConfSyncFileJob", "stockconf.avsc", null, null, 10));
    }

    /**
     * 0 3/15 * * * ?
     * 读取基金持劵配置文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-bond-conf-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExBondConfFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ReadJob(runId, runCallbackDest, "readJijinExBondConfSyncFileJob", "bondconf.avsc", null, null, 10));
    }


    /**
     * 0 4/15 * * * ?
     * 读取基金经理业绩表现文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-ma-perf-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExMaPerfFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ReadJob(runId, runCallbackDest, "readJijinExMaPerfSyncFileJob", "maperf.avsc", null, null, 10));
    }

    /**
     * 0 5/15 * * * ?
     * 读取基金公告文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-announce-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExAnnounceFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ReadJob(runId, runCallbackDest, "readJijinExAnnounceFileJob", "announce.avsc", null, null, 10));
    }

    /**
     * 0 6/15 * * * ?
     * 读取基金属性文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-character-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinExCharacterFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ReadJob(runId, runCallbackDest, "readJijinExCharacterFileJob", "character.avsc", null, null, 10));
    }






    /**
     * The read file job
     *
     * @author chenguang E-mail:chenguang451@pingan.com.cn
     * @version create time:May 20, 2015 7:15:28 PM
     */
    class ReadJob extends SchedulerJob {
        private String schema;
        private SyncFileLineProcessor processor;
        private int processLimit;
        private SyncFileLineFilter filter;

        protected ReadJob(String runId, String runCallbackDest,
                          String jobDescription, String schema) {
            super(runId, runCallbackDest, jobDescription);
            this.schema = schema;
        }

        protected ReadJob(String runId, String runCallbackDest,
                          String jobDescription, String schema, SyncFileLineFilter filter,
                          SyncFileLineProcessor processor,
                          int limit) {
            super(runId, runCallbackDest, jobDescription);
            this.schema = schema;
            this.processor = processor;
            this.processLimit = limit;
            this.filter = filter;
        }

        @Override
        public void execute() {
            Logger.info(this, "Start the " + schema + "! ");
            readWorker.execute("schedular/daixiao/" + this.schema, filter, processor, processLimit);
            Logger.info(this, "End the " + schema + "! ");
        }
    }
}
