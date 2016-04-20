package com.lufax.jijin.fundation.schedular;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.repository.BizParametersRepository;
import com.lufax.jijin.base.utils.MapUtils;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.service.RedeemService;
import com.lufax.kernel.zookeeper.AcquireFailedException;
import com.lufax.kernel.zookeeper.LufaxInterProcessMutex;
import com.lufax.kernel.zookeeper.ZookeeperService;

/**
 * 
 * 获取PAID_IN_ADVANCE记录，并根据开关发送MQ消息 
 * @author XUNENG311
 *
 */
@Service
public class HandlePaidInAdvanceRedeemJob {

    @Autowired
    private JijinTradeRecordRepository jijinTradeRecordRepository;

    @Autowired
    private RedeemService redeemService;
    @Autowired
    private ZookeeperService zookeeperService;
    @Autowired
    private BizParametersRepository bizParametersRepository;
    
    public void execute(){

		String lockPath = this.getClass().getSimpleName();
		LufaxInterProcessMutex mutex = zookeeperService.newInterProcessMutex("/jobs/" + lockPath);
		try {
			mutex.acquire(2000, TimeUnit.MILLISECONDS);
			// 捞到数据
			String value =bizParametersRepository.findValueByCode("dahua.async.redeem.switch") ;
			List<JijinTradeRecordDTO> list = new ArrayList<JijinTradeRecordDTO>();
			boolean isFine= true;
			if("00".equals(value) || "01".equals(value)){
				isFine = true;
			}else{
				//处于异常模式 “11”或空 则什么都不干
				return;
			}
			do {	
				// 开关判断,00,01 都要往外发
				if (isFine) {
					list = jijinTradeRecordRepository.getT0RedeemApplyRecords(TradeRecordType.REDEEM.name(),
						TradeRecordStatus.PAID_IN_ADVANCE.name(), "dh103", 100);

					for (JijinTradeRecordDTO record : list) {
						redeemService.sendPaidInAdvanceRedeemMQ(record);
						jijinTradeRecordRepository.updateBusJijinTradeRecord(MapUtils.buildKeyValueMap("id", record.getId(),"status",TradeRecordStatus.SENDING.name()));
					}
				}
			} while (!CollectionUtils.isEmpty(list));

			// 没捞到数据
			// 查询开关状态 ，只在开关 01时， 置成00
			if ("01".equals(value)) {
				//update 开关
				int updateResult = bizParametersRepository.updateByCode("dahua.async.redeem.switch", "00");
				if(updateResult!=1){
					Logger.warn(this,"update dahua.async.redeem.switch fail");
				}
			}

		} catch (AcquireFailedException e) {
			Logger.info(this,
					String.format("job [%s] get lock failed", lockPath));
		} catch (Exception e) {
			Logger.info(this,
					String.format("job [%s] get lock failed", lockPath));
		} finally {
			mutex.release();
			Logger.info(this,
					String.format("job [%s] release lock success", lockPath));
		}
	}
}
