package com.lufax.jijin.ylx.jobresource;

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.scheduler.SchedulerJob;
import com.lufax.jijin.scheduler.SchedulerJobService;
import com.lufax.jijin.ylx.job.*;
import com.lufax.jijin.ylx.job.purchase.YLXHandlePurchaseConfirmDataJob;
import com.lufax.jijin.ylx.job.purchase.YLXPreparePurchaseConfirmDataJob;
import com.lufax.jijin.ylx.job.purchase.YLXPullPurchaseConfirmFileJob;
import com.lufax.jijin.ylx.job.purchase.YLXPurchaseRequestDataPrepareJob;
import com.lufax.jijin.ylx.job.purchase.YLXPurchaseRequestFileCreationJob;
import com.lufax.jijin.ylx.job.purchase.YLXPurchaseRequestFileSendJob;
import com.lufax.jijin.ylx.job.redeem.YLXHandleRedeemConfirmDataJob;
import com.lufax.jijin.ylx.job.redeem.YLXPrepareRedeemConfirmDataJob;
import com.lufax.jijin.ylx.job.redeem.YLXPullRedeemConfirmFileJob;
import com.lufax.jijin.ylx.job.redeem.YLXRedeemRequestDataPrepareJob;
import com.lufax.jijin.ylx.job.redeem.YLXRedeemRequestFileCreationJob;
import com.lufax.jijin.ylx.job.redeem.YLXRedeemRequestFileSendJob;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.Date;
/*
autouserlniyms9k0u
autouser0xcro9i9cv
autouser2qr98v1nti
autouseri7x9ppnqrf
autouserfif28pwjx5
 */
@Path("/jijin/ylx/job/")
public class JobResource {
	@InjectParam
	private SchedulerJobService schedulerJobService;
	@InjectParam
	private TradeDayService tradeDayService;
    @InjectParam
    private YLXBuyRequestDataPrepareJob ylxBuyRequestDataPrepareJob;
    @InjectParam
    private YLXBuyRequestFileCreationJob ylxBuyRequestFileCreationJob;
    @InjectParam
    private YLXBuyRequestFileSendJob ylxBuyRequestFileSendJob;
    @InjectParam
    private YLXOpenRequestDataPrepareJob ylxOpenRequestDataPrepareJob;
    @InjectParam
    private YLXOpenRequestFileCreationJob ylxOpenRequestFileCreationJob;
    @InjectParam
    private YLXOpenRequestFileSendJob ylxOpenRequestFileSendJob;
    @InjectParam
    private YLXPullOpenConfirmFileJob ylxPullOpenConfirmFileJob;
    @InjectParam
    private YLXPullBuyConfirmFileJob ylxPullBuyConfirmFileJob;
    @InjectParam
    private YLXFundWithdrawJob ylxFundWithdrawJob;
    @InjectParam
    private YLXFundWithdrawRecordCreationJob ylxFundWithdrawRecordCreationJob;
    @InjectParam
    private YLXPrepareOpenConfirmDataJob ylxPrepareOpenConfirmDataJob;
    @InjectParam
    private YLXPrepareBuyConfirmDataJob ylxPrepareBuyConfirmDataJob;
    @InjectParam
    private YLXHandleOpenConfirmDataJob ylxHandleOpenConfirmDataJob;
    @InjectParam
    private YLXHandleBuyConfirmDataJob ylxHandleBuyConfirmDataJob;
    
    
    /**
     * 获取实力派开户确认文件，并落地ylx_open_confirm_detail
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/pull-open-confirm-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePullOpenConfirmFileRequest(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {

        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePullOpenConfirmFileRequest") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handlePullOpenConfirmFileRequest ! ");
                ylxPullOpenConfirmFileJob.execute();
                Logger.info(this, "End the handlePullOpenConfirmFileRequest ! ");
            }
        });

    }
    
    /**
     * 获取实力派认购确认文件，并落地
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/pull-buy-confirm-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePullBuyConfirmFileRequest(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {

        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePullBuyConfirmFileRequest") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handlePullBuyConfirmFileRequest ! ");
                ylxPullBuyConfirmFileJob.execute();
                Logger.info(this, "End the handlePullBuyConfirmFileRequest ! ");
            }
        });
    }
    
    /**
     * 实力派确认文件数据落地到数据库更改request文件状态
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/open-confirm-prepare")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleOpenConfirmFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleOpenConfirmFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleYLXOPOpenConfirm file! ");
                ylxPrepareOpenConfirmDataJob.execute();
                Logger.info(this, "End the handleYLXOPOpenConfirm file! ");
            }
        });

    }
    
    /**
     * 处理实力派被落地的开户确认数据
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/open-confirm")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleOpenConfirmDataJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleOpenConfirmDataJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleYLXOPOpenConfirm data ! ");
                ylxHandleOpenConfirmDataJob.execute();
                Logger.info(this, "End the handleFundOpenConfirm data! ");
            }
        });

    }
    
    /**
     *实力派确认文件数据落地到数据库更改buy request 状态
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/buy-confirm-prepare")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleBuyConfirmFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleBuyConfirmFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleYLXBuyConfirm file! ");
                ylxPrepareBuyConfirmDataJob.execute();
                Logger.info(this, "End the handleYLXBuyConfirm! file");
            }
        });

    }
    
    /**
     *处理实力派认购申请被落地的确认数据
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/buy-confirm")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleBuyConfirmDataJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleBuyConfirmDataJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleYLXBuyConfirm data! ");
                ylxHandleBuyConfirmDataJob.execute();
                Logger.info(this, "End the handleYLXBuyConfirm! data");
            }
        });

    }

    /**
     * 准备认购申请数据
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/buy-request-data-prepare")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleBuyRequestDataPrepare(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleBuyRequestDataPrepare") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleBuyRequestDataPrepare ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                    ylxBuyRequestDataPrepareJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleBuyRequestDataPrepare,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleBuyRequestDataPrepare! ");
            }
        });

    }

    /**
     * 创建认购申请文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/buy-request-file-creation")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleBuyRequestFileCreation(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleBuyRequestFileCreation") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleBuyRequestFileCreation ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                    ylxBuyRequestFileCreationJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleBuyRequestFileCreation,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleBuyRequestFileCreation! ");
            }
        });

    }

    /**
     * 发送认购申请文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/buy-request-file-send")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleBuyRequestFileSend(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleBuyRequestFileSend") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleBuyRequestFileSend ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                    ylxBuyRequestFileSendJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleBuyRequestFileSend,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleBuyRequestFileSend! ");
            }
        });

    }


    /**
     * 准备开户申请数据
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/open-request-data-prepare")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleOpenRequestDataPrepare(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleOpenRequestDataPrepare") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleOpenRequestDataPrepare ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                    ylxOpenRequestDataPrepareJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleOpenRequestDataPrepare,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleOpenRequestDataPrepare! ");
            }
        });

    }

    /**
     * 生成开户申请文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/open-request-file-creation")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleOpenRequestFileCreation(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleOpenRequestFileCreation") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleBuyRequestFileCreation ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                    ylxOpenRequestFileCreationJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleOpenRequestFileCreation,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleOpenRequestFileCreation! ");
            }
        });
    }

    /**
     *发送开户申请文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/open-request-file-send")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleOpenRequestFileSend(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleOpenRequestFileSend") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleOpenRequestFileSend ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                    ylxOpenRequestFileSendJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleOpenRequestFileSend,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleOpenRequestFileSend! ");
            }
        });
    }

    /**
     * 生成YLXFUND对公待付记录
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/fund-record-creation")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleFundRecordCreation(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleFundRecordCreation") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleFundRecordCreation ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                    ylxFundWithdrawRecordCreationJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleFundRecordCreation,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleFundRecordCreation! ");
            }
        });

    }

    /**
     * YLXFUND对公待付
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/fund-withdraw")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleFundWithdraw(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleFundWithdraw") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleFundWithdraw ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                    ylxFundWithdrawJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleFundWithdraw,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleFundWithdraw! ");
            }
        });

    }

    //===================================== 华丽的分割线 实力派 二期 ============================================================

    @InjectParam
    private YLXProdProfitStep_1 ylxPullProdProfitFileJob;
    @InjectParam
    private YLXProdProfitStep_2 ylxPrepareProdProfitDataJob;
    
    /**
     * 同步产品净值
     */
    @GET
    @Path("/prod-profit")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String getProdProfit(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "getProdProfit") {
            @Override
            public void execute() {
                Logger.info(this, "Start the prod-profit ! ");
                ylxPullProdProfitFileJob.execute();
                ylxPrepareProdProfitDataJob.execute();
                Logger.info(this, "End the prod-profit! ");
            }
        });
    }
    
    @InjectParam
    private YLXInvestorProfitStep_2 ylxPrepareInvestorProfitDataJob;
    @InjectParam
    private YLXInvestorProfitStep_1 ylxPullInvesttorProfitFileJob;
    /**
     * 同步投资者净值
     */
    @GET
    @Path("/investor-profit")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String getInvestorProfit(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "getInvestorProfit") {
            @Override
            public void execute() {
                Logger.info(this, "Start the investor-profit ! ");
                ylxPullInvesttorProfitFileJob.execute();
                ylxPrepareInvestorProfitDataJob.execute();
                Logger.info(this, "End the investor-profit! ");
            }
        });
    }
    
    /**
     * 准备数据，ylx钱袋子  -》  用户钱袋子
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @InjectParam
    private YLXFundRechargeStep_1 ylxFundRechargeStep_1;
    @GET
    @Path("/fund-recharge-step-1")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRechargeRecord(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRechargeRecord") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleRechargeRecord ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxFundRechargeStep_1.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleRechargeRecord,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleRechargeRecord! ");
            }
        });

    }
    
    /**
     * 处理数据，让养老险把钱拿出来
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @InjectParam
    private YLXFundRechargeStep_2 ylxFundRechargeStep_2;
    @GET
    @Path("/fund-recgarge-step-2")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRechargeCompany(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRechargeCompany") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleRechargeCompany ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxFundRechargeStep_2.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleRechargeCompany,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleRechargeCompany! ");
            }
        });
    }
    
    /**
     * 处理数据，虚拟账户钱-》用户钱袋子
     * @param runId
     * @param runCallbackDest
     * @return
     */

    @InjectParam
    private YLXFundRechargeStep_3 ylxFundRechargeStep_3;
    @GET
    @Path("/fund-recharge-step-3")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRechargeMoney(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRechargeMoney") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleRechargeMoney ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxFundRechargeStep_3.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleRechargeMoney,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleRechargeMoney! ");
            }
        });
    }
    
    @InjectParam
    private YLXFundRechargeStep_4 ylxFundRechargeStep_4;
    @GET
    @Path("/fund-recharge-step-4")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRechargeMoneyForUser(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRechargeMoneyForUser") {
            @Override
            public void execute() {
                Logger.info(this, "Start the ylxFundRechargeMoneyForUserJob ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxFundRechargeStep_4.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to ylxFundRechargeMoneyForUserJob,date[%s]", new Date()));
                }
                Logger.info(this, "End the ylxFundRechargeMoneyForUserJob! ");
            }
        });
    }
    
    @InjectParam
    private YLXPurchaseRequestDataPrepareJob ylxPurchaseRequestDataPrepareJob;
    @InjectParam
    private YLXPurchaseRequestFileCreationJob ylxPurchaseRequestFileCreationJob;
    @InjectParam
    private YLXPurchaseRequestFileSendJob ylxPurchaseRequestFileSendJob;
    @InjectParam
    private YLXRedeemRequestDataPrepareJob ylxRedeemRequestDatapareJob;
    @InjectParam
    private YLXRedeemRequestFileCreationJob ylxRedeemRequestFileCreationJob;
    @InjectParam
    private YLXRedeemRequestFileSendJob ylxRedeemRequestFileSendJob;
    /**
     *准备申请申购数据
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/purchase-request-data-prepare")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePurchaseRequestDataPrepare(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePurchaseRequestDataPrepare") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handlePurchaseRequestDataPrepare ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxPurchaseRequestDataPrepareJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handlePurchaseRequestDataPrepare,date[%s]", new Date()));
                }
                Logger.info(this, "End the handlePurchaseRequestDataPrepare! ");
            }
        });
    }
    
    /**
     * 生成申购申请文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/purchase-request-file-creation")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePurchaseRequestFileCreation(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePurchaseRequestFileCreation") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handlePurchaseRequestFileCreation ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxPurchaseRequestFileCreationJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handlePurchaseRequestFileCreation,date[%s]", new Date()));
                }
                Logger.info(this, "End the handlePurchaseRequestFileCreation! ");
            }
        });
    }
    
    /**
     *发送开户申请文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/purchase-request-file-send")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePurchaseRequestFileSend(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePurchaseRequestFileSend") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handlePurchaseRequestFileSend ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxPurchaseRequestFileSendJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handlePurchaseRequestFileSend,date[%s]", new Date()));
                }
                Logger.info(this, "End the handlePurchaseRequestFileSend! ");
            }
        });
    }
    
    /**
     *准备赎回申请数据
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/redeem-request-data-prepare")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRedeemRequestDataPrepare(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRedeemRequestDataPrepare") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleRedeemRequestDataPrepare ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxRedeemRequestDatapareJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleRedeemRequestDataPrepare,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleRedeemRequestDataPrepare! ");
            }
        });
    }
    
    /**
     * 生成赎回申请文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/redeem-request-file-creation")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRedeemRequestFileCreation(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRedeemRequestFileCreation") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleRedeemRequestFileCreation ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxRedeemRequestFileCreationJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleRedeemRequestFileCreation,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleRedeemRequestFileCreation! ");
            }
        });
    }
    
    /**
     *发送开户申请文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/redeem-request-file-send")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRedeemRequestFileSend(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRedeemRequestFileSend") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleRedeemRequestFileSend ! ");
                Boolean isTradeDay = tradeDayService.isTradeDay(new Date());
                if (isTradeDay) {
                	ylxRedeemRequestFileSendJob.execute();
                } else {
                    Logger.info(this, String.format("today is not tradeDay,no need to handleRedeemRequestFileSend,date[%s]", new Date()));
                }
                Logger.info(this, "End the handleRedeemRequestFileSend! ");
            }
        });
    }
    
    @InjectParam
    private YLXPullPurchaseConfirmFileJob ylxPullPurchaseConfirmFileJob;
    @InjectParam
    private YLXPreparePurchaseConfirmDataJob ylxPreparePurchaseConfirmDataJob;
    @InjectParam
    private YLXHandlePurchaseConfirmDataJob ylxHandlePurchaseConfirmDataJob;
    @InjectParam
    private YLXPullRedeemConfirmFileJob ylxPullRedeemConfirmFileJob;
    @InjectParam
    private YLXPrepareRedeemConfirmDataJob ylxPrepareRedeemConfirmDataJob;
    @InjectParam
    private YLXHandleRedeemConfirmDataJob ylxHandleRedeemConfirmDataJob;
    
    /**
     * 获取实力派申购确认文件，并落地
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/pull-purchase-confirm-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePullPurchaseConfirmFileRequest(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {

        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePullPurchaseConfirmFileRequest") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handlePullPurchaseConfirmFileRequest ! ");
                ylxPullPurchaseConfirmFileJob.execute();
                Logger.info(this, "End the handlePullPurchaseConfirmFileRequest ! ");
            }
        });
    }
    
    /**
     * 实力派申购确认文件数据落地到数据库更改request文件状态
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/purchase-confirm-prepare")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePurchaseConfirmFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePurchaseConfirmFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleYLXOPPurchaseConfirm file! ");
                ylxPreparePurchaseConfirmDataJob.execute();
                Logger.info(this, "End the handleYLXOPPurchaseConfirm file! ");
            }
        });

    }
    
    /**
     * 处理实力派被落地的申购确认数据
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/purchase-confirm")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePurchaseConfirmDataJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePurchaseConfirmDataJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleYLXOPPurchaseConfirm data ! ");
                ylxHandlePurchaseConfirmDataJob.execute();
                Logger.info(this, "End the handleYLXOPPurchaseConfirm data! ");
            }
        });
    }
    
    /**
     * 获取实力派赎回确认文件，并落地
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/pull-redeem-confirm-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePullRedeemConfirmFileRequest(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {

        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePullRedeemConfirmFileRequest") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handlePullRedeemConfirmFileRequest ! ");
                ylxPullRedeemConfirmFileJob.execute();
                Logger.info(this, "End the handlePullRedeemConfirmFileRequest ! ");
            }
        });
    }
    
    /**
     * 实力派赎回确认文件数据落地到数据库更改request文件状态
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/redeem-confirm-prepare")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRedeemConfirmFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRedeemConfirmFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleYLXOPRedeemConfirm file! ");
                ylxPrepareRedeemConfirmDataJob.execute();
                Logger.info(this, "End the handleYLXOPRedeemConfirm file! ");
            }
        });

    }
    
    /**
     * 处理实力派被落地的赎回确认数据
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/redeem-confirm")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRedeemConfirmDataJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRedeemConfirmDataJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleYLXOPRedeemConfirm data ! ");
                ylxHandleRedeemConfirmDataJob.execute();
                Logger.info(this, "End the handleYLXOPRedeemConfirm data! ");
            }
        });
    }
    
}
