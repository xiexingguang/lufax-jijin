package com.lufax.jijin.event.handler.impl;


import java.util.ArrayList;
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
import com.lufax.jijin.user.dto.UserDTO;
import com.lufax.jijin.user.repository.UserRepository;
import com.lufax.jijin.user.service.UserService;
import com.lufax.jijin.ylx.dto.YLXBuyRequestDetailDTO;
import com.lufax.jijin.ylx.dto.repository.YLXBuyRequestDetailRepository;
import com.lufax.jijin.ylx.remote.YLXSmsService;
import com.lufax.jijin.ylx.enums.YLXBuyRequestStatus;

@Component
public class YLXOPProductCloseHandler extends EventHandler{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private YLXBuyRequestDetailRepository ylxBuyRequestDetailRepository;
    @Autowired
    private YLXSmsService ylxSmsService;
    @Autowired
    private UserRepository userRepository;
    private static final int ROWNUM = 500; 

    /* (non-Javadoc)
     * @see com.lufax.jijin.event.handler.EventHandler#handle(com.lufax.jijin.event.handler.EventContext)
     * 批量发送短消息给认购成功的客户
     */
    public void handle(EventContext ctx) {
    	  Logger.info(this, String.format("Event of [YLX-OP-Product-Close][message: %s] received.", ctx.getMessage()));

          YLXOpProductResultEvent event = YLXOpProductResultEventParser.parse(ctx.getMessage());
          ProductDTO dto = productRepository.getById(event.getProductId());
          if (null == dto) {
              Logger.warn(this, String.format("ProductDTO not exists , event ignore. recordId [%s]", event.getProductId()));
              return;
          }
          List<YLXBuyRequestDetailDTO> buyRequests = null;
          int start = 0;
          int end = 500; 
          String productName = dto.getDisplayName();
          List<String> userIds = new ArrayList<String>();
          do{
        	  buyRequests = ylxBuyRequestDetailRepository.getYLXBuyRequestDTOsByStatusAndRowNum(dto.getId(),start,end,YLXBuyRequestStatus.SUCCESS.name());
        	  if(CollectionUtils.isNotEmpty(buyRequests)){
        		  for(YLXBuyRequestDetailDTO ylxBuyRequestDetailDTO:buyRequests){
            		  userIds.add(ylxBuyRequestDetailDTO.getBankAccount());
            	  }
            	  //批量查询用户信息
            	  List<UserDTO> userDTOs = userRepository.getUserInfoByIds(userIds);
            	  for(YLXBuyRequestDetailDTO ylxBuyRequestDetailDTO:buyRequests){
            		  for(UserDTO user : userDTOs){
            			  if(String.valueOf(user.getId()).equals(ylxBuyRequestDetailDTO.getBankAccount())){
            				  Logger.info(this, String.format("Event of [YLX-OP-Product-Close send the buy success message][user id: %s][buy request id: %s] handled.", user.getId(),ylxBuyRequestDetailDTO.getId()));
                    		  ylxSmsService.sendBuySuccessMessageToCustomer(user.getMobileNo(), user.getName(), productName, ylxBuyRequestDetailDTO.getAmount(), ylxBuyRequestDetailDTO.getFundShare());
            			  }
            		  }
            	  }
            	  start = end;
            	  end = start + ROWNUM;
        	  }
          } while(CollectionUtils.isNotEmpty(buyRequests));
          Logger.info(this, String.format("Event of [YLX-OP-Product-Close][message: %s] handled.", ctx.getMessage()));
    }


}
