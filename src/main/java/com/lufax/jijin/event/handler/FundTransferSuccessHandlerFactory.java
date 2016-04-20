package com.lufax.jijin.event.handler;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.event.handler.impl.YLXFundTransferSuccessHandler;
import com.lufax.jijin.product.constant.ProductCategory;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.base.utils.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class FundTransferSuccessHandlerFactory {

    @Autowired
    private ProductRepository productRepository;

    private final String PRODUCT_ID = "productId";
    private final HashMap<String, EventHandler> maps = new HashMap<String, EventHandler>();

    @Autowired
    private ApplicationContext appCtx;

    public EventHandler getHandler(EventContext context){

        Long productId = JsonHelper.parse(context.getMessage()).get(PRODUCT_ID).getAsLong();
        ProductDTO productDTO = productRepository.getById(productId);
        if(null == productDTO) return DoNothingHandler.instance;
        EventHandler eventHandler = maps.get(productDTO.getProductCategory());
        if(null != eventHandler)
            return eventHandler;
        else
            return DoNothingHandler.instance;
    }

    @PostConstruct
    private void init(){
        maps.put(ProductCategory.SLP.getCode(), appCtx.getBean(YLXFundTransferSuccessHandler.class));
    }

    private static class DoNothingHandler extends EventHandler {

        static final DoNothingHandler instance = new DoNothingHandler();

        @Override
        public void handle(EventContext ctx) {
            Logger.warn(this, String.format("message:%s WON'T BE HANDLED BY handlers in ProductCancelEventHandlerFactory", ctx.getMessage()));
        }

    }
}
