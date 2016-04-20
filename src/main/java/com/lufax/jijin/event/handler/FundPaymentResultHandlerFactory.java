package com.lufax.jijin.event.handler;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.JsonHelper;
import com.lufax.jijin.event.entity.FundPaymentResultEvent;
import com.lufax.jijin.event.entity.FundPaymentResultEventParser;
import com.lufax.jijin.event.handler.impl.YLXFundPaymentResultHandler;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.ylx.fund.dto.InsuranceFundRecordDTO;
import com.lufax.jijin.ylx.fund.repository.InsuranceFundRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class FundPaymentResultHandlerFactory {

    @Autowired
    private InsuranceFundRecordRepository insuranceFundRecordRepository;
    @Autowired
    private ApplicationContext appCtx;

    private final static String CHANNEL_ID = "channelId";

    private HashMap<String, EventHandler> maps = new HashMap<String, EventHandler>();

    public EventHandler getHandler(EventContext ctx) {
        String channelId = JsonHelper.parse(ctx.getMessage()).get(CHANNEL_ID).getAsString();
        if (!"JIJIN".equals(channelId)) {
            Logger.info(this, "channelId is not jijin,msg ignore");
            return DoNothingHandler.instance;
        }
        FundPaymentResultEvent event = FundPaymentResultEventParser.parse(ctx.getMessage());
        InsuranceFundRecordDTO dto = insuranceFundRecordRepository.findByRecordId(event.getRecordId());
        if(null!=dto){
            ProductCategory productCategory = ProductCategory.convert(String.valueOf(dto.getInsuranceType()));
            EventHandler handler =  maps.get(productCategory.getCode());
            if(null!=handler) return handler;
            else
                return DoNothingHandler.instance;
        }
        else
            return DoNothingHandler.instance;
    }

    @PostConstruct
    public void init(){
        maps.put(ProductCategory.SLP.getCode(), appCtx.getBean(YLXFundPaymentResultHandler.class));
    }
    private static class DoNothingHandler extends EventHandler {

        static final DoNothingHandler instance = new DoNothingHandler();

        @Override
        public void handle(EventContext ctx) {
            Logger.warn(this, String.format("message:%s WON'T BE HANDLED BY handlers in FundPaymentResultHandlerFactory", ctx.getMessage()));
        }

    }
}
