package com.lufax.jijin.event.handler.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.event.entity.YLXOpProductResultEvent;
import com.lufax.jijin.event.entity.YLXOpProductResultEventParser;
import com.lufax.jijin.event.handler.EventContext;
import com.lufax.jijin.event.handler.EventHandler;
import com.lufax.jijin.product.dto.ProductDTO;
import com.lufax.jijin.product.repository.ProductRepository;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.enums.YLXBuyRequestStatus;

@Component
public class YXLOPProductCancelHandler extends EventHandler{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
    
    private static final int ROWNUM = 500; 

    /* (non-Javadoc)
     * @see com.lufax.jijin.event.handler.EventHandler#handle(com.lufax.jijin.event.handler.EventContext)
     * 产品取消，把所有的认购确认记录翻转为失败
     */
    @Override
    public void handle(EventContext ctx) {
        Logger.info(this, String.format("Event of [YLX-OP-Product-cancel][message: %s] received.", ctx.getMessage()));


        YLXOpProductResultEvent event = YLXOpProductResultEventParser.parse(ctx.getMessage());
        ProductDTO dto = productRepository.getById(event.getProductId());
        if (null == dto) {
            Logger.warn(this, String.format("ProductDTO not exists , event ignore. recordId [%s]", event.getProductId()));
            return;
        }
        Logger.info(this, String.format("Event of [Update Buy requests of success]"));
        List<YLXBuyRequestDetailDTO> buyRequestDetailDTOs = null;
        do{
        	buyRequestDetailDTOs = ylxBuyRequestDetailRepository.getYLXBuyRequestDTOsByStatus(dto.getId(), ROWNUM, YLXBuyRequestStatus.SUCCESS.name());
        	if(null!= buyRequestDetailDTOs){
        		for(YLXBuyRequestDetailDTO ylxBuyRequestDetailDTO : buyRequestDetailDTOs){
            		ylxBuyRequestDetailDTO.setStatus(YLXBuyRequestStatus.FAIL.name());
            	}
            	ylxBuyRequestDetailRepository.batchUpdate(buyRequestDetailDTOs);
        	}
        } while (CollectionUtils.isNotEmpty(buyRequestDetailDTOs));
       
        Logger.info(this, String.format("Event of [YLX-OP-Product-cancel][message: %s] handled.", ctx.getMessage()));
    }
}
