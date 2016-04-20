package com.lufax.jijin.event.resouce;


import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.event.gson.EventResultGson;
import com.lufax.jijin.event.handler.EventContext;
import com.lufax.jijin.event.handler.EventHandler;
import com.lufax.jijin.event.handler.FundPaymentResultHandlerFactory;
import com.lufax.jijin.event.handler.FundTransferSuccessHandlerFactory;
import com.lufax.jijin.event.handler.YLXOPProductChangeHandlerFactory;
import com.lufax.jijin.event.handler.YLXOPProductCloseHandlerFactory;
import com.lufax.jijin.event.handler.YXLOPProductCancelHandlerFactory;
import com.lufax.jijin.fundation.service.JijinWithdrawService;
import com.lufax.jijin.scheduler.SchedulerJob;
import com.lufax.jijin.scheduler.SchedulerJobService;
import com.sun.jersey.api.core.InjectParam;

@Path("event")
public class EventResource {

    @InjectParam
    private YLXOPProductCloseHandlerFactory ylxOPProductCloseHandlerFactory;
    @InjectParam
    private YLXOPProductChangeHandlerFactory ylxOPProductChangeHandlerFactory;
    @InjectParam
    private YXLOPProductCancelHandlerFactory ylxOPProductCancelHandlerFactory;

    @InjectParam
    private FundTransferSuccessHandlerFactory fundTransferSuccessHandlerFactory;
    @InjectParam
    private FundPaymentResultHandlerFactory fundPaymentResultHandlerFactory;
    @InjectParam
    private JijinWithdrawService jijinWithdrawService;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    @POST
    @Path("fund-transfer-success")
    @Produces(MediaType.APPLICATION_JSON)
    public String handFundTransferSuccess(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [fund-transfer-success][message: %s].", message));

        EventContext ctx = new EventContext(message);
        EventHandler handler = fundTransferSuccessHandlerFactory.getHandler(ctx);
        handler.handle(ctx);

        Logger.info(this, String.format("Event of [fund-transfer-success][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }

    @POST
    @Path("fund-withdraw-result")
    @Produces(MediaType.APPLICATION_JSON)
    public String paymentResultEvent(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [fund-withdraw-result][message: %s].", message));
        EventContext ctx = new EventContext(message);
        EventHandler handler = fundPaymentResultHandlerFactory.getHandler(ctx);
        handler.handle(ctx);
        Logger.info(this, String.format("Event of [fund-withdraw-result][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }

    @POST
    @Path("product-cancel")
    @Produces(MediaType.APPLICATION_JSON)
    public String handleYLXOpProductCancel(@FormParam("message") String message) {
        Logger.info(this, String.format("Event of [YLX-OP-Product-cancel][message: %s].", message));

        EventContext ctx = new EventContext(message);
        EventHandler handler = ylxOPProductCancelHandlerFactory.getHandler(ctx);
        handler.handle(ctx);

        Logger.info(this, String.format("Event of [YLX-OP-Product-cancel][message: %s] handled.", message));
        return new Gson().toJson(new EventResultGson("true"));
    }

    @InjectParam
    private SchedulerJobService schedulerJobService;

    @POST
    @Path("product-close")
    @Produces(MediaType.APPLICATION_JSON)
    public String handleYLXOpProductClose(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [YLX-OP-Product-close][message: %s].", message));

        String result = schedulerJobService.executeThread(new SchedulerJob("", "", String.format("product-close [%s]", message)) {
            @Override
            public void execute() {
                EventContext ctx = new EventContext(message);
                EventHandler handler = ylxOPProductCloseHandlerFactory.getHandler(ctx);
                handler.handle(ctx);
            }

            @Override
            protected void finished() {
            }
        });

        Logger.info(this, String.format("Event of [YLX-OP-Product-close][message: %s] handled. status[%s]", message, result));

        return new Gson().toJson(new EventResultGson("true"));
    }

    @POST
    @Path("product-change")
    @Produces(MediaType.APPLICATION_JSON)
    public String handleYLXOpProductChange(@FormParam("message") final String message) {
        Logger.info(this, String.format("Event of [YLX-OP-Product-change][message: %s].", message));

        String result = schedulerJobService.executeThread(new SchedulerJob("", "", String.format("product-change [%s]", message)) {
            @Override
            public void execute() {
                EventContext ctx = new EventContext(message);
                EventHandler handler = ylxOPProductChangeHandlerFactory.getHandler(ctx);
                handler.handle(ctx);
            }

            @Override
            protected void finished() {
            }
        });

        Logger.info(this, String.format("Event of [YLX-OP-Product-change][message: %s] handled. status[%s]", message, result));
        return new Gson().toJson(new EventResultGson("true"));
    }

}
