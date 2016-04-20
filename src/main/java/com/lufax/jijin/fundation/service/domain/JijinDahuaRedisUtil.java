package com.lufax.jijin.fundation.service.domain;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.dto.JijinRedeemBalDTO;
import com.lufax.jijin.fundation.dto.JijinRedeemBalHisDTO;
import com.lufax.jijin.fundation.repository.JijinRedeemBalHisRepository;
import com.lufax.jijin.fundation.repository.JijinRedeemBalRepository;
import com.lufax.jijin.fundation.service.JijinDahuaRedisService;

@Component
public class JijinDahuaRedisUtil {
	
	@Autowired
	private JijinRedeemBalRepository jijinRedeemBalRepository;
	@Autowired
	private JijinRedeemBalHisRepository jijinRedeemBalHisRepository;
	@Autowired
	private BizParametersRepository bizParametersRepository;
	@Autowired
	private JijinDahuaRedisService jijinDahuaRedisService ;
	
	
	
	private static final String DAHUA_REDIS_KEY="jijin.dahua.balance";
	
	@Transactional
	public void initDBAndRedis(BigDecimal balance,String fundCode){
        JijinRedeemBalHisDTO jijinRedeemBalHisDTO = new JijinRedeemBalHisDTO();
        jijinRedeemBalHisDTO.setAmount(balance);
        jijinRedeemBalHisDTO.setRemark("init");
        jijinRedeemBalHisDTO =  jijinRedeemBalHisRepository.insertBusJijinRedeemBalHis(jijinRedeemBalHisDTO);

        jijinRedeemBalHisDTO= jijinRedeemBalHisRepository.findBusJijinRedeemBalHisById(jijinRedeemBalHisDTO.getId());
 
        JijinRedeemBalDTO jijinRedeemBalDTO = new JijinRedeemBalDTO();
        jijinRedeemBalDTO.setAmount(balance);
        jijinRedeemBalDTO.setFundCode(fundCode);
        jijinRedeemBalDTO.setSnapshotTime(DateUtils.formatDate(jijinRedeemBalHisDTO.getCreatedAt(), DateUtils.CMS_DRAW_SEQUENCE_FORMAT));
        jijinRedeemBalDTO.setVersion(0l);
        jijinRedeemBalRepository.insertBusJijinRedeemBal(jijinRedeemBalDTO);
        jijinDahuaRedisService.putRedisValue(balance);
	}

}
