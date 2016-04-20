package com.lufax.jijin.fundation.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.fundation.constant.JijinConstants;
import com.lufax.jijin.fundation.dto.JijinRedeemBalDTO;
import com.lufax.jijin.fundation.remote.AccountrAppCallerService;
import com.lufax.jijin.fundation.repository.JijinRedeemBalHisRepository;
import com.lufax.jijin.fundation.repository.JijinRedeemBalRepository;
import com.lufax.jijin.fundation.service.domain.JijinDahuaRedisUtil;
import com.lufax.jijin.service.RedisCacheService;

/**
 * 
 * @author XUNENG311
 *
 */
@Service
public class JijinDahuaRedisService {
	
	@Autowired
	private JijinRedeemBalRepository jijinRedeemBalRepository;
	@Autowired
	private JijinRedeemBalHisRepository jijinRedeemBalHisRepository;
	@Autowired
	private BizParametersRepository bizParametersRepository;
	@Autowired
	private AccountrAppCallerService accountrAppCallerService;
	@Autowired
	private RedisCacheService redisCacheService ;
	@Autowired
	private JijinDahuaRedisUtil jijinDahuaRedisUtil;
	
	private static final String DAHUA_REDIS_KEY="jijin.dahua.balance";
	private static final BigDecimal HUNDREAD = new BigDecimal(100);
	
	public AccountrAppCallerService getAccountrAppCallerService() {
		return accountrAppCallerService;
	}

	public void setAccountrAppCallerService(
			AccountrAppCallerService accountrAppCallerService) {
		this.accountrAppCallerService = accountrAppCallerService;
	}
	
	/**
	 * 当镜像表不存在记录时，代表需要初始化,只做一次
	 * 初始化流程 查询虚拟户后插入redis，流水表，镜像表
	 * @param fundCode
	 */
	public void initRedis(String fundCode){
		//JijinConstants.DAHUA_FUND_CODE
		JijinRedeemBalDTO dto = 	jijinRedeemBalRepository.findBusJijinRedeemBalByFundCode(fundCode);
		if(null==dto){
			Long dahuaAccount = 0l;
	        try {
	            String bizValue = bizParametersRepository.findValueByCode(JijinConstants.JIJIN_DAHUA_ACCOUNT_ID_BIZ_PARAMETER_CODE);
	            dahuaAccount = new Long(bizValue);
	        } catch (Exception e) {
	            Logger.error(this, String.format("get dahua account exception"));
	            return;
	        }
	        BigDecimal balance = accountrAppCallerService.getCanBeUsedBalanceByUserId(dahuaAccount);
	        if (null == balance) {
	            Logger.error(this, String.format("[init dahua redis] cannot get balance from accountr-app,userId is[%s]", dahuaAccount));
	            return;
	        }
			try{
				jijinDahuaRedisUtil.initDBAndRedis(balance,fundCode);
				BigDecimal currentValue = getRedisValue();
				Logger.info(this, String.format("[init dahua redis] success , key [jijin.dahua.balance] value is [%s]", currentValue));

			}catch(Exception e){
				Logger.error(this, String.format("[init dahua redis] cannot init Balance , userId is [%s], since [%s]", dahuaAccount,e.getClass().getName()));
			}
		}
	}
	
	/**
	 * 递增或递减值
	 * @param value
	 * @return 负值代表不够了
	 */
	public long changeRedisValue(BigDecimal value){
		return redisCacheService.changeDahuaBalance(DAHUA_REDIS_KEY, new Long(value.multiply(HUNDREAD).longValue()));
	}
	
	/**
	 * 当前redis值
	 * @param value
	 * @return 负值代表不够了
	 */
	public BigDecimal getRedisValue(){
		
		long srcValue = redisCacheService.getDahuaBalance(DAHUA_REDIS_KEY);
		BigDecimal target = new BigDecimal(srcValue).divide(HUNDREAD);
		return target;
	}

	/**
	 * 重置redis值(sync)
	 * @param value
	 */
	public void putRedisValue(BigDecimal value){
		redisCacheService.putDahuaBalance(DAHUA_REDIS_KEY, value.multiply(HUNDREAD).longValue());
	}
	
	/**
	 * 每天0点生成大华中间户redis镜像
	 */
	public int genRedisSnapshot(){
		JijinRedeemBalDTO jijinRedeemBalDTO =jijinRedeemBalRepository.findBusJijinRedeemBalByFundCode(JijinConstants.DAHUA_FUND_CODE);
		if(null==jijinRedeemBalDTO) return 0;
		
		Date today = DateUtils.startOfToday();
		String endTime = DateUtils.formatDate(today, DateUtils.CMS_DRAW_SEQUENCE_FORMAT);
		BigDecimal sum =jijinRedeemBalHisRepository.findBusJijinRedeemBalHisBetweenStartEnd(jijinRedeemBalDTO.getSnapshortTime(), endTime);
		BigDecimal amount = jijinRedeemBalDTO.getAmount();
		if(null!=sum)
			amount = jijinRedeemBalDTO.getAmount().add(sum);
		return jijinRedeemBalRepository.updateBusJijinRedeemBalById(jijinRedeemBalDTO.getId(), amount, jijinRedeemBalDTO.getVersion(), endTime);

	}
}
