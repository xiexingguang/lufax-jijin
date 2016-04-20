package com.lufax.jijin.event.handler;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.event.entity.YLXOpProductResultEvent;
import com.lufax.jijin.event.entity.YLXOpProductResultEventParser;
import com.lufax.jijin.event.handler.impl.YLXOPProductCloseHandler;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;

@Component
public class YLXOPProductCloseHandlerFactory {

    @Autowired
    private ProductRepository productRepository;

    private final HashMap<String, EventHandler> maps = new HashMap<String, EventHandler>();

    @Autowired
    private ApplicationContext appCtx;

    public EventHandler getHandler(EventContext context){

        YLXOpProductResultEvent event = YLXOpProductResultEventParser.parse(context.getMessage());
        Logger.info(this,String.format("product.close event:%s",new Gson().toJson(event)));
        ProductDTO productDTO = productRepository.getById(event.getProductId());
        if(null == productDTO) return DoNothingHandler.instance;
        EventHandler eventHandler = maps.get(productDTO.getProductCategory());
        if(null != eventHandler)
            return eventHandler;
        else
            return DoNothingHandler.instance;
    }

    @PostConstruct
    private void init(){
        maps.put(ProductCategory.SLP.getCode(), appCtx.getBean(YLXOPProductCloseHandler.class));
    }

    private static class DoNothingHandler extends EventHandler {

        static final DoNothingHandler instance = new DoNothingHandler();

        @Override
        public void handle(EventContext ctx) {
            Logger.warn(this, String.format("message:%s WON'T BE HANDLED BY handlers in YLXOPProductCloseHandlerFactory", ctx.getMessage()));
        }

    }
}
