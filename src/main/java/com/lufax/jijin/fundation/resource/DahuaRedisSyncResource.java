package com.lufax.jijin.fundation.resource;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.dto.JijinRedeemBalDTO;
import com.lufax.jijin.fundation.dto.JijinRedeemBalHisDTO;
import com.lufax.jijin.fundation.repository.JijinRedeemBalHisRepository;
import com.lufax.jijin.fundation.repository.JijinRedeemBalRepository;
import com.lufax.jijin.fundation.service.JijinDahuaRedisService;
import com.sun.jersey.api.core.InjectParam;

/**
 * 人工同步大华中间户redis
 * @author XUNENG311
 *
 */
@Path("/fundation/dahua")
public class DahuaRedisSyncResource {
	
	@InjectParam
	private JijinRedeemBalRepository JijinRedeemBalRepository;
	@InjectParam
	private JijinRedeemBalHisRepository JijinRedeemBalHisRepository;
	@InjectParam
	private JijinDahuaRedisService jijinDahuaRedisService;
	
	@GET
	@Path("/sync-mid-account-redis")
	public String syncRedis(){
		
		JijinRedeemBalDTO jijinRedeemBalDTO = JijinRedeemBalRepository.findBusJijinRedeemBalByFundCode(JijinConstants.DAHUA_FUND_CODE);
		String snapshotTime = DateUtils.formatDate(jijinRedeemBalDTO.getUpdatedAt(), DateUtils.CMS_DRAW_SEQUENCE_FORMAT);
		List<JijinRedeemBalHisDTO> historyList= JijinRedeemBalHisRepository.findBusJijinRedeemBalHisAfterTime(snapshotTime);
		
		BigDecimal amount =BigDecimal.ZERO;
		for(JijinRedeemBalHisDTO his : historyList){
			amount = amount.add(his.getAmount());
		}		
		amount = amount.add(jijinRedeemBalDTO.getAmount());
		
		jijinDahuaRedisService.putRedisValue(amount);
		
		return "success";
	}
}
