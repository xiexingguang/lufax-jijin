package com.lufax.jijin.event.handler;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.event.entity.YLXOpProductResultEvent;
import com.lufax.jijin.event.entity.YLXOpProductResultEventParser;
import com.lufax.jijin.event.handler.impl.YXLOPProductCancelHandler;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;

@Component
public class YXLOPProductCancelHandlerFactory {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ApplicationContext appCtx;


    private HashMap<String, EventHandler> maps = new HashMap<String, EventHandler>();

    public EventHandler getHandler(EventContext ctx) {

        YLXOpProductResultEvent event = YLXOpProductResultEventParser.parse(ctx.getMessage());
        ProductDTO dto = productRepository.getById(event.getProductId());
        if(null!=dto){
        	EventHandler handler =  maps.get(dto.getProductCategory());
        	if(null!=handler) return handler;
        	else
        		return DoNothingHandler.instance;
        }
        else
        	return DoNothingHandler.instance;
    }

    @PostConstruct
    public void init(){
        maps.put(ProductCategory.SLP.getCode(), appCtx.getBean(YXLOPProductCancelHandler.class));
    }
    
    private static class DoNothingHandler extends EventHandler {

        static final DoNothingHandler instance = new DoNothingHandler();

        @Override
        public void handle(EventContext ctx) {
            Logger.warn(this, String.format("message:%s WON'T BE HANDLED BY handlers in YXLOPProductCancelHandlerFactory", ctx.getMessage()));
        }

    }
}
