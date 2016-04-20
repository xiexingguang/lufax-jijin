package com.lufax.jijin.fundation.resource;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.ConstantsHelper;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.JijinAppProperties;
import com.lufax.jijin.base.utils.JijinInstIdPlatMerchantIdMapHolder;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.service.JijinDaixiaoInfoService;
import com.lufax.jijin.fundation.constant.DHFileType;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.dto.JijinInfoDTO;
import com.lufax.jijin.fundation.dto.JijinNetValueDTO;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.gson.CreateJijinInfoResultGson;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.schedular.BatchJobTemplate;
import com.lufax.jijin.fundation.schedular.CancelRedeemCompensationJob;
import com.lufax.jijin.fundation.schedular.DividendTypeModifyJob;
import com.lufax.jijin.fundation.schedular.HandleDHTradeDispatchJob;
import com.lufax.jijin.fundation.schedular.HandleDividendCashDispatchJob;
import com.lufax.jijin.fundation.schedular.HandleDividendSwitchDispatchJob;
import com.lufax.jijin.fundation.schedular.HandleIncomeDispatchJob;
import com.lufax.jijin.fundation.schedular.HandleJijinSpecialDispatchJob;
import com.lufax.jijin.fundation.schedular.HandleJijinTradeReconJob;
import com.lufax.jijin.fundation.schedular.HandlePAFBuyAuditDispatchJob;
import com.lufax.jijin.fundation.schedular.HandlePAFRedeemDispatchJob;
import com.lufax.jijin.fundation.schedular.HandlePaidInAdvanceRedeemJob;
import com.lufax.jijin.fundation.schedular.HandlePurchaseDispatchJob;
import com.lufax.jijin.fundation.schedular.HandleRedeemDispatchJob;
import com.lufax.jijin.fundation.schedular.HandleTZeroRedeemJob;
import com.lufax.jijin.fundation.schedular.PurchaseRefundJob;
import com.lufax.jijin.fundation.schedular.ReadJijinBuyAuditFileJob;
import com.lufax.jijin.fundation.schedular.ReadJijinDHNetValueJob;
import com.lufax.jijin.fundation.schedular.ReadJijinDHProfitFileJob;
import com.lufax.jijin.fundation.schedular.ReadJijinDHPurchaseFileJob;
import com.lufax.jijin.fundation.schedular.ReadJijinDHRedeemFileJob;
import com.lufax.jijin.fundation.schedular.ReadJijinDHTransferFileJob;
import com.lufax.jijin.fundation.schedular.ReadJijinRedeemAuditFileJob;
import com.lufax.jijin.fundation.schedular.ReadJijinTradeSyncFileJob;
import com.lufax.jijin.fundation.schedular.ReadJijinUserBalanceAuditFileJob;
import com.lufax.jijin.fundation.schedular.ReadPAFBuyAuditFileJob;
import com.lufax.jijin.fundation.schedular.ReadPAFSyncFileJob;
import com.lufax.jijin.fundation.schedular.RedeemCompensationJob;
import com.lufax.jijin.fundation.schedular.SendBillFileToLfexJob;
import com.lufax.jijin.fundation.schedular.SendPromotionStatusToRedisJob;
import com.lufax.jijin.fundation.schedular.SyncFileLineFilter;
import com.lufax.jijin.fundation.schedular.SyncFileLineProcessor;
import com.lufax.jijin.fundation.schedular.*;
import com.lufax.jijin.fundation.service.IncreaseService;
import com.lufax.jijin.fundation.service.JijinDahuaRedisService;
import com.lufax.jijin.fundation.service.JijinInfoService;
import com.lufax.jijin.fundation.service.JijinWithdrawService;
import com.lufax.jijin.fundation.service.PurchaseService;
import com.lufax.jijin.fundation.service.RedeemService;
import com.lufax.jijin.fundation.service.ScanService;
import com.lufax.jijin.fundation.service.domain.FileHolder;
import com.lufax.jijin.fundation.util.JijinUtils;
import com.lufax.jijin.scheduler.SchedulerJob;
import com.lufax.jijin.scheduler.SchedulerJobService;
import com.lufax.jijin.service.MqService;
import com.sun.jersey.api.core.InjectParam;

/**
 * 基金直连定时任务入口
 *
 * @author xuneng
 */
@Path("/fundation/job")
public class JobResource {

    @InjectParam
    private SchedulerJobService schedulerJobService;
    @InjectParam
    private ScanService scanService;
    @InjectParam
    private ReadPAFSyncFileJob readPAFRedeemSyncFileJob;
    @InjectParam
    private DividendTypeModifyJob dividendTypeModifyJob;
    @InjectParam
    private ReadJijinTradeSyncFileJob readJijinTradeSyncFileJob;
    @InjectParam
    private HandlePurchaseDispatchJob handlePurchaseDispatchJob;
    @InjectParam
    private JijinAppProperties jijinAppProperties;
    @InjectParam
    private TradeDayService tradeDayService;
    @InjectParam
    private JijinInfoRepository jijinInfoRepository;
    @InjectParam
    private HandleDividendSwitchDispatchJob handleDividendSwitchDispatchJob;
    @InjectParam
    private HandleDividendCashDispatchJob handleDividendCashDispatchJob;
    @InjectParam
    private HandlePAFRedeemDispatchJob handlePAFRedeemDispatchJob;
    @InjectParam
    private HandleRedeemDispatchJob handleRedeemDispatchJob;
    @InjectParam
    private RedeemCompensationJob redeemCompensationJob;
    @InjectParam
    private CancelRedeemCompensationJob cancelRedeemCompensationJob;
    @InjectParam
    private JijinInstIdPlatMerchantIdMapHolder jijinInstIdPlatMerchantIdMapHolder;
    @InjectParam
    private BatchJobTemplate readWorker;
    @InjectParam
    private PurchaseRefundJob purchaseRefundJob;
    @InjectParam
    private IncreaseService increaseService;
    @InjectParam
    private MqService mqService;
    @InjectParam
    private ReadPAFBuyAuditFileJob readPafBuyAuditFileJob;
    @InjectParam
    private ReadJijinBuyAuditFileJob readJijinBuyAuditFileJob;
    @InjectParam
    private ReadJijinRedeemAuditFileJob readJijinRedeemAuditFileJob;
    @InjectParam
    private ReadJijinUserBalanceAuditFileJob readJijinUserBalanceFileJob;
    @InjectParam
    private HandlePAFBuyAuditDispatchJob handlePAFBuyAuditDispatchJob;
    @InjectParam
    private BizParametersRepository bizParametersRepository;
    @InjectParam
    private HandleDHTradeDispatchJob handleDHTradeDispatchJob;
    @InjectParam
    private ReadJijinDHRedeemFileJob readJijinDHRedeemFileJob;
    @InjectParam
    private ReadJijinDHPurchaseFileJob readJijinDHPurchaseFileJob;
    @InjectParam
    private HandleIncomeDispatchJob handleDaixiaoIncomeDispatchJob;
    @InjectParam
    private HandleTZeroRedeemJob handleTZeroRedeemJob;
    @InjectParam
    private JijinWithdrawService jijinWithdrawService;
    @InjectParam
    private HandleJijinTradeReconJob handleJijinTradeReconJob;
    @InjectParam
    private JijinInfoService jijinInfoService;
    @InjectParam
    private JijinDaixiaoInfoService jijinDaixiaoInfoService;
    @InjectParam
    private ReadJijinDHProfitFileJob readJijinDHProfitFileJob;
    @InjectParam
    private HandleJijinSpecialDispatchJob handleJijinSpecialDispatchJob;
    @InjectParam
    private ReadJijinDHTransferFileJob readJijinDHTransferFileJob;
    @InjectParam
    private ReadJijinDHNetValueJob readJijinDHNetValueJob;
    @InjectParam
    private PurchaseService purchaseService;
    @InjectParam
    private RedeemService redeemService;
    @InjectParam
    private SendBillFileToLfexJob sendBillFileToLfexJob;
    @InjectParam
    private SendPromotionStatusToRedisJob sendPromotionStatusToRedisJob;
    @InjectParam
    private JijinDahuaRedisService jijinDahuaRedisService;
    @InjectParam
    private HandlePaidInAdvanceRedeemJob handlePaidInAdvanceRedeemJob;
    @InjectParam
    private HandleJijinUnfreezeJob handleJijinUnfreezeJob;


    /**
     * 0 0/5 * * * ?
     * 扫描基金交易对账文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/scan-jijin-trade-sync")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String scanJijinTradeSync(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "scanJijinTradeSync") {
            @Override
            public void execute() {
                Date date = new Date();
                if (tradeDayService.isTradeDay(date)) {
                    Logger.info(this, "Start scan jijin trade sync file!");
                    List<String> instIds = jijinInfoRepository.getDistinctInstId();
                    String dateStr = DateUtils.formatDateAsString(date);
                    List<FileHolder> fileHolderList = new ArrayList<FileHolder>();
                    for (String instId : instIds) {
                        FileHolder fileHolder = new FileHolder();
                        fileHolder.setFileName(instId + "_" + dateStr + "_04.txt");
                        fileHolder.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/upload/" + dateStr + "/");
                        fileHolderList.add(fileHolder);
                    }

                    scanService.yiTiaoLong(fileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.TRADE.name());
                    Logger.info(this, "End scan jijin trade sync file!");
                }
            }
        });

    }

    /**
     * 0 0/5 * * * ?
     * 处理基金交易申购(认购)对账文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-purchase-dispatch")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePurchaseDispatchJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePurchaseDispatchJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start to dispatch purchase!");
                handlePurchaseDispatchJob.setMAX_PROCESS_TIME(5*59*1000);
                handlePurchaseDispatchJob.execute();
                Logger.info(this, "End dispatch purchase!");
            }
        });

    }

    /**
     * 0 0/5 * * * ?
     * 处理申购失败退款
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/purchase-refund")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String purchaseRefundJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "purchaseRefundJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the purchaseRefundJob! ");
                purchaseRefundJob.setMAX_PROCESS_TIME(5*59*1000);
                purchaseRefundJob.execute();
                Logger.info(this, "End the purchaseRefundJob! ");
            }
        });
    }


    /**
     * 0 0/15 * * * ?
     * 读取申购认购文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-trade-sync-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinTradeSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinTradeSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinTradeSyncFileJob! ");
                readJijinTradeSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinTradeSyncFileJob.execute();
                Logger.info(this, "End the readJijinTradeSyncFileJob! ");
            }
        });
    }


    /**
     * 0 0/5 * * * ?
     * 扫描平安付赎回，现金分红,申购退款 代付文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/scan-paf-sync")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String scanPAFSync(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "scanPAFSync") {
            @Override
            public void execute() {
                Logger.info(this, "Start scan paf recon file!");
                Date date = new Date();
                if (tradeDayService.isTradeDay(date)) {
                    String targetDate = DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT);


                    String rootPath = jijinAppProperties.getJijinNasRootDir();
                    String filePath = rootPath + "upload/" + targetDate + "/";

                    List<String> instIds = jijinInfoRepository.getDistinctInstId();
                    List<FileHolder> fileHolderList = new ArrayList<FileHolder>();
                    for (String instId : instIds) {
                        String platMerchantId = jijinInstIdPlatMerchantIdMapHolder.getPlatMerchantId(instId);
                        FileHolder fileHolder = new FileHolder();
                        fileHolder.setFileAbsolutePath(filePath);
                        String fileName = "FUND_RESULT_P_" + platMerchantId + "_" + DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT) + "_1.txt";
                        fileHolder.setFileName(fileName);
                        fileHolderList.add(fileHolder);
                    }

                    scanService.yiTiaoLong(fileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.PAF_REDEEM_DIVIDEND.name());
                    Logger.info(this, "End scan paf recon file!");
                }
            }
        });

    }


    /**
     * 0 0/5 * * * ?
     * 扫描基金产品行情文件(净值)
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/scan-jijin-netvalue-sync")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String scanJijinNetValueSync(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ScanJob(runId, runCallbackDest, "scanJijinNetValueSync", SyncFileBizType.NETVALUE));
    }

    /**
     * 0 0/15 * * * ?
     * 读取基金产品行情(净值)文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-netvalue-sync-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinNetValueSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        final List<String> zhixiaoFundCodeList = jijinInfoRepository.findZhixiaoFundCodeList();
        return schedulerJobService.executeThread(new ReadJob(runId, runCallbackDest, "readJijinNetValueSyncFileJob", "netvalue.avsc",
                        new SyncFileLineFilter() {
                            @Override
                            public boolean shouldFiltered(Object dto, JijinSyncFileDTO file) {
                                if (dto instanceof JijinNetValueDTO) {
                                    //如果是代销的文件，过滤掉直销的数据
                                    if (file.getFileName().contains("lfx201") && zhixiaoFundCodeList.contains(((JijinNetValueDTO) dto).getFundCode())) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                                return false;

                            }
                        },
                        new SyncFileLineProcessor() {
                            @Override
                            public void processLine(Object line) {
                                if (line instanceof JijinNetValueDTO) {
                                    JijinNetValueDTO netValue = (JijinNetValueDTO) line;
                                    netValue.setStatus("NEW");
                                    //通过开关判断是否需要发消息
                                    String switchTag = bizParametersRepository.findValueByCode(ConstantsHelper.JIJIN_DAIXIAO_SEND_NET_VALUE_MSG);
                                    JijinInfoDTO jijinInfoDTO = jijinInfoRepository.findJijinInfoByFundCode(netValue.getFundCode());
                                    //基金代销上线后不再发送消息，直接使用wind数据
                                    if ("on".equals(switchTag)) {
                                        if (jijinInfoDTO != null && !jijinInfoDTO.getInstId().equals("lfx201")) {
                                            mqService.sendJijinNetValue(netValue);
                                            if (!JijinUtils.isHuoji(jijinInfoDTO)) {
                                                //如果是货基，不计算净值增长率
                                                try {
                                                    increaseService.calculateFundIncrease(netValue);
                                                } catch (Exception e) {
                                                    Logger.warn(this, e.getMessage(), e);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Logger.warn(this, "readJijinNetValueSyncFileJob send mq meet error object type, is " + line.getClass().getName());
                                }
                            }
                            @Override 
                            public void setPropertiesBeforeInsertToDB(Object line) {
                                if (line instanceof JijinNetValueDTO) {
                                	((JijinNetValueDTO) line).setStatus("NEW");
                                }
                            }
                            
                        },
                        10)
        );
    }


    /**
     * 0 0/5 * * * ?
     * 扫描基金产品分红明细文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/scan-jijin-dividend-sync")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String scanJijinDividendSync(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ScanJob(runId, runCallbackDest, "scanJijinDividendSync", SyncFileBizType.DIVIDEND));
    }

    /**
     * 0 0/15 * * * ?
     * 读取基金产品分红明细文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-dividend-sync-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinDividendSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new ReadJob(runId, runCallbackDest, "readJijinDividendSyncFileJob", "dividend.avsc", null, null, 10));
    }


    /**
     * 0 0/5 * * * ?
     * 处理基金份额分红
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-jijin-dividend-switch-dispatch")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleDividendSwitchDispatchJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinDividendSwitchDispatchJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleDividendSwitchDispatchJob! ");
                handleDividendSwitchDispatchJob.setMAX_PROCESS_AMOUNT(2000);
                handleDividendSwitchDispatchJob.execute();
                Logger.info(this, "End the handleDividendSwitchDispatchJob! ");
            }
        });
    }

    /**
     * 0 0/5 * * * ?
     * 处理基金现金分红
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-jijin-dividend-cash-dispatch")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleDividendCashDispatchJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinDividendCashDispatchJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleDividendCashDispatchJob! ");
                handleDividendCashDispatchJob.setMAX_PROCESS_AMOUNT(1500);
                handleDividendCashDispatchJob.execute();
                Logger.info(this, "End the handleDividendCashDispatchJob! ");
            }
        });
    }


    /**
     * 0 0/15 * * * ?
     * 读取平安付赎回，现金分红代付文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-paf-sync-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readPAFSyncFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readPAFSyncFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readPAFSyncFileJob! ");
                readPAFRedeemSyncFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readPAFRedeemSyncFileJob.execute();
                Logger.info(this, "End the readPAFSyncFileJob! ");
            }
        });
    }


    /**
     * 0 0/5 * * * ?
     * 处理平安付赎回代发
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-paf-redeem-dispatch")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePAFRedeemDispatchJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePAFRedeemDispatchJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handlePAFRedeemDispatchJob! ");
                handlePAFRedeemDispatchJob.setMAX_PROCESS_TIME(5*59*1000);
                handlePAFRedeemDispatchJob.execute();
                Logger.info(this, "End the handlePAFRedeemDispatchJob! ");
            }
        });
    }

    /**
     * 0 0/5 * * * ?
     * 处理基金公司赎回确认
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-jijin-redeem-dispatch")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleRedeemDispatchJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleRedeemDispatchJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleRedeemDispatchJob! ");
                handleRedeemDispatchJob.setMAX_PROCESS_TIME(5*59*1000);
                handleRedeemDispatchJob.execute();
                Logger.info(this, "End the handleRedeemDispatchJob! ");
            }
        });
    }


    /**
     * 0 0/5 * * * ?
     * 赎回请求发生网络异常，补偿重试
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-redeem-compensation")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String redeemCompensationJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "redeemCompensationJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the redeemCompensationJob! ");
                redeemCompensationJob.execute();
                Logger.info(this, "End the redeemCompensationJob! ");
            }
        });
    }

    /**
     * 0/5 * * * * ?
     * 处理T+0赎回请求
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-t0-redeem")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleT0RedeemJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleTZeroRedeemJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleTZeroRedeemJob! ");
                handleTZeroRedeemJob.setMAX_PROCESS_TIME(4000);
                handleTZeroRedeemJob.execute();
                Logger.info(this, "End the handleTZeroRedeemJob! ");
            }
        });
    }


    /**
     * 0 0/5 * * * ?
     * 赎回撤单发生网络异常，补偿重试
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-cancel-redeem-compensation")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String cancelRedeemCompensationJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "cancelRedeemCompensationJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the cancelRedeemCompensationJob! ");
                cancelRedeemCompensationJob.execute();
                Logger.info(this, "End the cancelRedeemCompensationJob! ");
            }
        });
    }


    /**
     * 0 0/10 * * * ?
     * 回写基金公司对分红方式修改申请的处理结果
     */
    @GET
    @Path("/handle-dividend-modify-result")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String dividendTypeModifyJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "dividendTypeModifyJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the dividendTypeModifyJob! ");
                //dividendTypeModifyJob.setMAX_PROCESS_AMOUNT(100);
                dividendTypeModifyJob.execute();
                Logger.info(this, "End the dividendTypeModifyJob! ");
            }
        });
    }

    /**
     * The scan jijin file job
     *
     * @author chenguang E-mail:chenguang451@pingan.com.cn
     * @version create time:May 20, 2015 7:13:34 PM
     */
    class ScanJob extends SchedulerJob {
        private SyncFileBizType type;

        protected ScanJob(String runId, String runCallbackDest,
                          String jobDescription, SyncFileBizType type) {
            super(runId, runCallbackDest, jobDescription);
            this.type = type;
        }

        @Override
        public void execute() {
            Logger.info(this, String.format("Start scan %s file!", type.name()));
            Date date = new Date();
            if (tradeDayService.isTradeDay(date)) {
                List<String> instIds = jijinInfoRepository.getDistinctInstId();
                String dateStr = DateUtils.formatDateAsString(date);
                List<FileHolder> fileHolderList = new ArrayList<FileHolder>();
                for (String instId : instIds) {
                    FileHolder fileHolder = new FileHolder();
                    fileHolder.setFileName(instId + "_" + dateStr + "_" + type.getFilePrefix() + ".txt");
                    fileHolder.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/upload/" + dateStr + "/");
                    fileHolderList.add(fileHolder);
                }
                scanService.yiTiaoLong(fileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), type.name());
            }
            Logger.info(this, String.format("End of scan %s file!", type.name()));
        }
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
            readWorker.execute("schedular/" + this.schema, filter, processor, processLimit);
            Logger.info(this, "End the " + schema + "! ");
        }
    }


    /**
     * 0 0/5 * * * ?
     * <p/>
     * download/20150724/{247}_20150724_to_bank_fund_txn_10.txt  - PAF
     * upload/20150724/{247}_20150724_to_bank_fund_txn_10.txt  - JIJIN
     * upload/20150724/{247}_20150724_to_bank_fund_txn_12.txt  - JIJIN
     * upload/20150724/{247}_20150724_06.txt  - JIJIN
     * <p/>
     * 扫描对账文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/scan-audit-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String scanAuditFile(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "scanJijinAuditJob") {
            @Override
            public void execute() {
                Date date = new Date();
                if (tradeDayService.isTradeDay(date)) {
                    Logger.info(this, "Start scan jijin audit file!");
                    List<String> instIds = jijinInfoRepository.getDistinctInstId();
                    String dateStr = DateUtils.formatDateAsString(date);
                    List<FileHolder> pafFileHolderList = new ArrayList<FileHolder>();
                    List<FileHolder> jijinBuyFileHolderList = new ArrayList<FileHolder>();
                    List<FileHolder> jijinRedeemFileHolderList = new ArrayList<FileHolder>();
                    List<FileHolder> jijinUserBalanceFileHolderList = new ArrayList<FileHolder>();

                    for (String instId : instIds) {

                        String saleCode = jijinInstIdPlatMerchantIdMapHolder.getFundSaleCode(instId);

                        FileHolder fileHolder1 = new FileHolder();
                        fileHolder1.setFileName(saleCode + "_" + dateStr + "_to_bank_fund_txn_10.txt");
                        fileHolder1.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/download/" + dateStr + "/");

                        pafFileHolderList.add(fileHolder1);

                        FileHolder fileHolder2 = new FileHolder();
                        fileHolder2.setFileName(saleCode + "_" + dateStr + "_to_bank_fund_txn_10.txt");
                        fileHolder2.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/upload/" + dateStr + "/");

                        jijinBuyFileHolderList.add(fileHolder2);


                        FileHolder fileHolder3 = new FileHolder();
                        fileHolder3.setFileName(saleCode + "_" + dateStr + "_to_bank_fund_txn_12.txt");
                        fileHolder3.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/upload/" + dateStr + "/");

                        jijinRedeemFileHolderList.add(fileHolder3);

                        FileHolder fileHolder4 = new FileHolder();
                        if (!"dh103".equals(instId)) {
                            fileHolder4.setFileName(instId + "_" + dateStr + "_06.txt");
                            fileHolder4.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/upload/" + dateStr + "/");
                        }

                        jijinUserBalanceFileHolderList.add(fileHolder4);

                    }

                    scanService.yiTiaoLong(pafFileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.PAF_BUY_AUDIT.name());
                    scanService.yiTiaoLong(jijinBuyFileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.JIJIN_BUY_AUDIT.name());
                    scanService.yiTiaoLong(jijinRedeemFileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.JIJIN_REDEEM_AUDIT.name());
                    scanService.yiTiaoLong(jijinUserBalanceFileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.JIJIN_BALANCE_AUDIT.name());

                    Logger.info(this, "End scan jijin audit file!");
                }
            }
        });

    }

    /**
     * 0 0/10 * * * ?
     * 读取平安付认申购文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-paf-buy-audit-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readPAFBuyAuditFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readPAFBuyAuditFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readPAFBuyAuditFileJob! ");
                readPafBuyAuditFileJob.setMAX_PROCESS_AMOUNT(3);
                readPafBuyAuditFileJob.execute();
                Logger.info(this, "End the readPAFBuyAuditFileJob! ");
            }
        });
    }


    /**
     * 0 0/10 * * * ?
     * 读取基金公司认申购文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-buy-audit-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinBuyAuditFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinBuyAuditFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinBuyAuditFileJob! ");
                readJijinBuyAuditFileJob.setMAX_PROCESS_AMOUNT(3);
                readJijinBuyAuditFileJob.execute();
                Logger.info(this, "End the readJijinBuyAuditFileJob! ");
            }
        });
    }


    /**
     * 0 0/10 * * * ?
     * 读取基金公司赎回对账文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-redeem-audit-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinRedeemAuditFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinRedeemAuditFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinRedeemAuditFileJob! ");
                readJijinRedeemAuditFileJob.setMAX_PROCESS_AMOUNT(3);
                readJijinRedeemAuditFileJob.execute();
                Logger.info(this, "End the readJijinRedeemAuditFileJob! ");
            }
        });
    }


    /**
     * 0 0/10 * * * ?
     * 读取基金公司用户持仓文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-user-balance-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinUserBalanceFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinUserBalanceFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinUserBalanceFileJob! ");
                readJijinUserBalanceFileJob.setMAX_PROCESS_AMOUNT(2);
                readJijinUserBalanceFileJob.execute();
                Logger.info(this, "End the readJijinUserBalanceFileJob! ");
            }
        });
    }

    /**
     * 0 0/10 * * * ?
     * 清销认申购帐务
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-paf-buy-audit-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePAFBuyAuditDispatchJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePAFBuyAuditDispatchJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handlePAFBuyAuditDispatchJob! ");
                handlePAFBuyAuditDispatchJob.setMAX_PROCESS_TIME(10 * 59 * 1000);
                handlePAFBuyAuditDispatchJob.execute();
                Logger.info(this, "End the handlePAFBuyAuditDispatchJob! ");
            }
        });
    }

    /**
     * 0 0/10 * * * ?
     * 异常处理
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-jijin-special-dispatch")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinSpecialDispatchJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinSpecialDispatchJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinSpecialDispatchJob! ");
                handleJijinSpecialDispatchJob.setMAX_PROCESS_AMOUNT(3000);
                handleJijinSpecialDispatchJob.execute();
                Logger.info(this, "End the handleJijinSpecialDispatchJob! ");
            }
        });
    }

    

    /**
     * 0 0/10 * * * ?  (scheduler设定为2小时超时，job本身1小时超时)
     * 代销货基每日收益同步（按交易日同步）
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-daixiao-daily-income")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleDaixiaoIncomeDispatchJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleDaixiaoIncomeDispatchJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleDaixiaoIncomeDispatchJob! ");
                handleDaixiaoIncomeDispatchJob.execute();
                Logger.info(this, "End the handleDaixiaoIncomeDispatchJob! ");
            }
        });
    }

    /**
     * 0 0/10 * * * ?
     * 扫描大华基金文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/scan-jijin-dh-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String scanJijinDhFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "scanJijinDhFileJob") {
            @Override
            public void execute() {
                Date date = new Date();
                if (tradeDayService.isTradeDay(date)) {
                    Logger.info(this, "Start scan jijin dh file!");
                    String dateStr = DateUtils.formatDateAsString(date);
                    List<FileHolder> payInList = new ArrayList<FileHolder>();
                    List<FileHolder> payOutList = new ArrayList<FileHolder>();
                    FileHolder fileHolder = new FileHolder();
                    fileHolder.setFileName("dh103_"+ dateStr +"_"+ "22.zip");
                    fileHolder.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + "dh103" + "/upload/" + dateStr + "/");
                    payOutList.add(fileHolder);

                    FileHolder fileHolder1 = new FileHolder();
                    fileHolder1.setFileName("dh103_"+ dateStr +"_"+ "21.zip");
                    fileHolder1.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + "dh103" + "/upload/" + dateStr + "/");
                    payInList.add(fileHolder1);

                    scanService.yiTiaoLongForZIP(payOutList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), DHFileType.DH_PAY_OUT.name());
                    scanService.yiTiaoLongForZIP(payInList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), DHFileType.DH_PAY_IN.name());
                    Logger.info(this, "End scan jijin dh file!");
                }
            }
        });
    }


    /**
     * 0 0/10 * * * ?
     * 扫描大华基金文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/scan-jijin-dh-file-daily")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String scanJijinDhFileDailyJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "scanJijinDhFileDailyJob") {
            @Override
            public void execute() {
                Date date = new Date();

                Logger.info(this, "Start scan jijin dh daily file!");
                String dateStr = DateUtils.formatDateAsString(date);
                List<FileHolder> profitList = new ArrayList<FileHolder>();
                List<FileHolder> netValueList = new ArrayList<FileHolder>();
                List<FileHolder> transferList = new ArrayList<FileHolder>();

                FileHolder fileHolder = new FileHolder();
                fileHolder.setFileName("dh103_" + dateStr + "_23.zip");
                fileHolder.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + "dh103" + "/upload/" + dateStr + "/");
                profitList.add(fileHolder);

                FileHolder fileHolder1 = new FileHolder();
                fileHolder1.setFileName("dh103_" + dateStr + "_24.zip");
                fileHolder1.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + "dh103" + "/upload/" + dateStr + "/");
                netValueList.add(fileHolder1);

                FileHolder fileHolder2 = new FileHolder();
                fileHolder2.setFileName("dh103_" + dateStr + "_26.zip");
                fileHolder2.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + "dh103" + "/upload/" + dateStr + "/");
                transferList.add(fileHolder2);

                scanService.yiTiaoLongForZIP(profitList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), DHFileType.DH_PROFIT.name());
                scanService.yiTiaoLongForZIP(netValueList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), DHFileType.DH_NET_VALUE.name());
                scanService.yiTiaoLongForZIP(transferList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), DHFileType.DH_TRANSFER.name());

                Logger.info(this, "End scan jijin dh daily file!");

            }
        });
    }

    
    /**
     * 0 0/10 * * * ?
     * 读取大华每日收益文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-dahua-daily-income")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinDHProfitFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinDHProfitFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinDHProfitFileJob! ");
                readJijinDHProfitFileJob.setMAX_PROCESS_AMOUNT(2);
                readJijinDHProfitFileJob.execute();
                Logger.info(this, "End the readJijinDHProfitFileJob! ");
            }
        });
    }

    /**
     * 0 0/10 * * * ?
     * 读取大华划款明细文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-dh-transfer-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinDHTransferFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinDHTransferFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinDHTransferFileJob! ");
                readJijinDHTransferFileJob.setMAX_PROCESS_AMOUNT(2);
                readJijinDHTransferFileJob.execute();
                Logger.info(this, "End the readJijinDHTransferFileJob! ");
            }
        });
    }
    

    /**
     * 0 0/15 * * * ?
     * 读取大华赎回确认文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-dh-redeem-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinDHRedeemFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinDHRedeemFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinDHRedeemFileJob! ");
                readJijinDHRedeemFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinDHRedeemFileJob.execute();
                Logger.info(this, "End the readJijinDHRedeemFileJob! ");
            }
        });
    }

    /**
     * 0 0/15 * * * ?
     * 读取大华申购确认文件
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-dh-purchase-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinDHPurchaseFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinDHPurchaseFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the readJijinDHPurchaseFileJob! ");
                readJijinDHPurchaseFileJob.setMAX_PROCESS_AMOUNT(10); // set max process file number per round
                readJijinDHPurchaseFileJob.execute();
                Logger.info(this, "End the readJijinDHPurchaseFileJob! ");
            }
        });
    }

    /**
     * 0 0/5 * * * ?
     * 处理转出确认文件(大华基金直连货基使用)
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-dh-trade-dispatch")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleDHTradeDispatchJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleDHTradeDispatchJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start to dispatch dh trade!");
                handleDHTradeDispatchJob.setMAX_PROCESS_AMOUNT(2000);
                handleDHTradeDispatchJob.execute();
                Logger.info(this, "End dispatch dh trade!");
            }
        });

    }

    /**
     * 0 0/5 * * * ?
     * 扫描大华货基实体户并发起对公代扣
     *
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/recharge-dahua")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String rechargeDahua(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "rechargeDahuaJob") {
            @Override
            public void execute() {
                jijinWithdrawService.createWithdrawRecord();
            }
        });

    }
    
    /**
     * 0 0/5 * * * ?
     * 定时扫描大华货基垫资户，对比警戒线，并修改大帐户状态
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-dahua-account-status")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleDaHuaAccountStatus(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleDaHuaAccountStatusJob") {
            @Override
            public void execute() {
                jijinWithdrawService.handleDaHuaAccountStatus();
            }
        });

    }
    
    
    /**
     * 0 0/5 * * * ?
     * 对账并生成交易文件给大华
     * 
     * job本身只运行1小时，schedule超时为2小时
     * 
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-jijin-trade-recon")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinTradeReconJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinTradeReconJob") {
            @Override
            public void execute() {
            	handleJijinTradeReconJob.execute();
            }
        });

    }

	/**
	 * 新增大华货基
	 * @return
	 */
	@GET
	@Path("/create-dahua")
	@Produces(value = MediaType.APPLICATION_JSON)
	public String createDahuaJijinInfo(){
		JijinInfoDTO dto = new JijinInfoDTO();
		dto.setAppliedAmount("999999999999");
		//dto.setBuyDailyLimit();
		dto.setBuyFeeDiscountDesc("0");
		dto.setBuyStatus("PUR_OPEN");
		dto.setChargeType("A");
		dto.setCollectionMode("6");
		dto.setDividendType("0");
		try {
			dto.setEstablishedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2013-12-03"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dto.setForeignId("");
		dto.setFundBrand("");
		dto.setFundCode("000379");
		dto.setFundName("平安大华日增利货币市场基金");
		dto.setFundOpeningType("1");
		dto.setFundType("CURRENCY");
		dto.setInstId("dh103");
		dto.setIsBuyDailyLimit("0");
		dto.setIsFirstPublish(0);
		dto.setMinInvestAmount(new BigDecimal("0.01"));
		dto.setProductCategory("802");
		//dto.setProductCode("0000164");
		dto.setRedemptionArrivalDay(2);
		dto.setRedemptionFeeRateDesc("0");
		dto.setRedemptionStatus("RED_OPEN");
		dto.setRiskLevel("1");
		dto.setSourceType("8");
		dto.setTrustee("平安银行");
		StringBuffer sb = new StringBuffer();
		sb.append("投资目标：在严格控制基金资产投资风险和保持基金资产较高流动性的基础上，力争获得超越业绩比较基准的稳定回报。").append("/r/n");
		sb.append("投资范围：本基金投资于法律法规及监管机构允许投资的金融工具，包括现金，通知存款，短期融资券，一年以内（含一年）的银行定期存款、大额存单，期限在一年以内（含一年）的债券回购，期限在一年以内（含一年）的中央银行票据，剩余期限在397天以内（含397天）的债券、资产支持证券、中期票据，中国证监会及/或中国人民银行认可的其他具有良好流动性的金融工具。").append("/r/n");
		sb.append("业绩比较基准：同期七天通知存款利率（税后）。").append("/r/n");
		sb.append("风险收益特征：本基金的风险和预期收益低于股票型基金、混合型基金、债券型基金。");
		dto.setFundIntroduce(sb.toString());
		dto.setFundManager("投研总监孙健");
		dto.setFundManagerIntroduce("基金经理介绍：孙健，基金经理。曾任湘财证券有限责任公司资产管理部投资经理，太平资产管理有限公司组合投资经理，摩根士丹利华鑫基金管理有限公司货币基金经理，银华货币市场证券投资基金、银华信用债券型证券投资基金基金经理。2011年9月加入平安大华基金公司，任投资研究部固定收益研究员，现担任平安大华保本混合型证券投资基金基金经理、平安大华添利债券型证券投资基金基金经理、平安大华日增利货币市场基金基金经理、平安大华新鑫先锋混合型证券投资基金基金经理、平安大华智慧中国灵活配置混合型证券投资基金基金经理、平安大华鑫享混合型证券投资基金基金经理");
		String productCode = jijinDaixiaoInfoService.createProductCode();
        dto.setProductCode(productCode);
		CreateJijinInfoResultGson gson = jijinInfoService.addJiJinInfo(dto);
		Logger.info(this, JsonHelper.toJson(gson));
		return JsonHelper.toJson(gson);
	}

    /**
     * 0 0/10 * * * ?
     * 读取大华基金净值文件job
     * 
     * 
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/read-jijin-dahua-net-value")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String readJijinDHNetValueJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "readJijinDHNetValueJob") {
            @Override
            public void execute() {
            	Logger.info(this, "readJijinDHNetValueJob start");
            	readJijinDHNetValueJob.execute();
            	Logger.info(this, "readJijinDHNetValueJob end");
            }
        });

    }


    /**
     * 生成申购对账文件(提供给lfex使用)
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/create-lfx-purchase-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String createLfxPurchaseFile(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "createLfxPurchaseFile") {
            @Override
            public void execute() {
                Date date = new Date();
                if (tradeDayService.isTradeDay(date)) {
                    Logger.info(this, "Start create lfx purchase file!");
                    String dateStr = DateUtils.formatDateAsString(date);
                    FileHolder fileHolder= new FileHolder();
                    fileHolder.setFileName("lfx201" + "_" + dateStr + "_28.txt");
                    fileHolder.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + "lfx201" + "/download/" + dateStr + "/");
                    purchaseService.generatePurchaseFile(fileHolder, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.JIJIN_LFX_PURCHASE_RESULT.name());
                    Logger.info(this, "end create lfx purchase file!");
                }
            }
        });

    }


    /**
     * 生成申购对账文件(提供给lfex使用)
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/create-lfx-redeem-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String createLfxRedeemFile(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "createLfxRedeemFile") {
            @Override
            public void execute() {
                Date date = new Date();
                if (tradeDayService.isTradeDay(date)) {
                    Logger.info(this, "Start create lfx redeem file!");
                    String dateStr = DateUtils.formatDateAsString(date);
                    FileHolder fileHolder= new FileHolder();
                    fileHolder.setFileName("lfx201" + "_" + dateStr + "_29.txt");
                    fileHolder.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + "lfx201" + "/download/" + dateStr + "/");
                    redeemService.generateRedeemFile(fileHolder, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.JIJIN_LFX_REDEEM_RESULT.name());
                    Logger.info(this, "end create lfx redeem file!");
                }
            }
        });

    }
    /**
     * 生成开户的对帐文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/create-lfx-register-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String createRegisterFileToLfex(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
    	return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "sendRegisteFileToLfexJob") {
    		 @Override
             public void execute() {
    			 sendBillFileToLfexJob.execute(SyncFileBizType.JIJIN_LFX_REGISTER_RESULT.name(), new Date());
    		 }
    	});
    }
    
    /**
     * 生成分红修改的对帐文件
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/create-lfx-dividend-file")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String createDividendFileToLfex(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
    	return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "sendDividendFileToLfexJob") {
    		 @Override
             public void execute() {
    			 sendBillFileToLfexJob.execute(SyncFileBizType.JIJIN_LFX_DIVIDEND_RESULT.name(), new Date());
    		 }
    	});
    }

    /**
     * 将基金活动信息写入redis
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/send-promotion-status-to-redis")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String sendPromotionStatusToRedis(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
    	return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "sendPromotionStatusToRedisJob") {
   		 	@Override
            public void execute() {
	   			Logger.info(this, "Start sendPromotionStatusToRedis job!");
	   			sendPromotionStatusToRedisJob.execute();
	   			Logger.info(this, "End sendPromotionStatusToRedis job!");
   		 }
   	});
    }
    
    /**
     * 0/3 * * * * ?
     * 大华货基初始化redis(陆金所虚拟热点账户)
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/init-dahua-redis")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String initDahuaRedis(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
    	return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "initDahuaRedisJob") {
   		 	@Override
            public void execute() {
	   			Logger.info(this, "Start initDahuaRedisJob job - 000379!");
	   			jijinDahuaRedisService.initRedis(JijinConstants.DAHUA_FUND_CODE);
	   			Logger.info(this, "End initDahuaRedisJob  job  - 000379!");
   		 }
   	});
    }
    
    /**
     * 0 0 0 * * ?
     * 每日生成大华中间户redis镜像
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/gen-dahua-redis-sanpshot")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String genDahuaRedisSnapshot(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
    	return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "genDahuaRedisSnapshotJob") {
   		 	@Override
            public void execute() {
	   			Logger.info(this, "Start genDahuaRedisSnapshotJob job - 000379!");
	   			int result =jijinDahuaRedisService.genRedisSnapshot();
	   			if(result<0){// retry 小概率事件，但是还是值得一试的，增加强壮性
	   				Logger.info(this, "retry genDahuaRedisSnapshotJob job - 000379!");
	   				jijinDahuaRedisService.genRedisSnapshot();
	   			}
	   			Logger.info(this, "End genDahuaRedisSnapshotJob  job  - 000379!");
   		 }
   	});
    }  
    
    /**
     * 0/3 * * * * ?
     * 轮询处理 先行垫付的赎回记录
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/handle-paid-in-advance-redeem")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handlePaidInAdvanceRedeemJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
    	return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handlePaidInAdvanceRedeemJob") {
   		 	@Override
            public void execute() {
	   			Logger.info(this, "Start handle-paid-in-advance-redeem job");
	   			handlePaidInAdvanceRedeemJob.execute();
	   			Logger.info(this, "End handle-paid-in-advance-redeem  job ");
   		 }
   	});
    }
    
	/**
	 * 0 0/15 * * * ?
	 * 解冻份额JOB
	 */
	@GET
    @Path("/handle-jijin-unfreeze")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String handleJijinUnfreezeJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinUnfreezeJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinUnfreezeJob! ");
                handleJijinUnfreezeJob.setMAX_PROCESS_TIME(5* 59 * 1000);
                handleJijinUnfreezeJob.execute();
                Logger.info(this, "End the handleJijinUnfreezeJob! ");
            }
        });
    }

}
