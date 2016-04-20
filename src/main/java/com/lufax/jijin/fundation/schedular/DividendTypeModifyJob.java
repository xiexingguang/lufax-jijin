package com.lufax.jijin.fundation.schedular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lufax.jijin.base.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lufax.jijin.fundation.constant.BalanceDividendStatus;
import com.lufax.jijin.fundation.constant.JijinBizType;
import com.lufax.jijin.fundation.constant.TradeRecordStatus;
import com.lufax.jijin.fundation.constant.TradeRecordType;
import com.lufax.jijin.fundation.dto.JijinTradeLogDTO;
import com.lufax.jijin.fundation.dto.JijinTradeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinTradeSyncDTO;
import com.lufax.jijin.fundation.repository.JijinTradeLogRepository;
import com.lufax.jijin.fundation.repository.JijinTradeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinTradeSyncRepository;
import com.lufax.jijin.fundation.repository.JijinUserBalanceRepository;
import com.site.lookup.util.StringUtils;

/**
 * 回写基金公司对分红修改申请的处理结果 job
 * @author chenqunhui
 *
 */
@Service
public class DividendTypeModifyJob extends BaseBatchWithLimitJob<JijinTradeSyncDTO, String> {

	@Autowired
	private JijinTradeSyncRepository jijinTradeSyncRepository;
	
	@Autowired
	private JijinTradeRecordRepository jijinTradeRecordRepository;
	
	@Autowired
	private JijinUserBalanceRepository jijinUserBalanceRepository;
	
	@Autowired
	private JijinTradeLogRepository  jijinTradeLogRepository;
	

	@Override
	protected List<JijinTradeSyncDTO> fetchList(int batchAmount) {
		List<String> list = new ArrayList<String>();
		list.add(JijinBizType.DIVIDEND_MODIFY_CONFIRM.getCode());
		return jijinTradeSyncRepository.getUnDispatchRecords("NEW", batchAmount, list);
	}



	@Override
	protected String getKey(JijinTradeSyncDTO item) {
		return String.valueOf(item.getId());
	}


	@Override
	protected void process(JijinTradeSyncDTO item){
		if(StringUtils.isEmpty(item.getLufaxRequestNo())){
			//如果流水号为空，或者流水号找不到对应记录，默认认为是第三方的数据,本次先不处理
			return;
		}
		//拿到对应的交易记录
		JijinTradeRecordDTO tradeRecord = jijinTradeRecordRepository.getRecordByAppNo(item.getLufaxRequestNo());
		if(null == tradeRecord){
			return;
		}
		//准备更新交易记录的字段
		Map<String,Object> tradeRecordUpdateMap = new HashMap<String,Object>();
		tradeRecordUpdateMap.put("id", tradeRecord.getId());
		tradeRecordUpdateMap.put("appSheetNo",item.getAppSheetNo());
		tradeRecordUpdateMap.put("trxTime", item.getTrxTime());
		tradeRecordUpdateMap.put("trxDate", item.getTrxDate());
		//修改成功
		if("0000".equals(item.getTradeResCode())){
			//设置tradeRecord的状态位
			tradeRecordUpdateMap.put("status", "SUCCESS");
			//将分红方式写回balance记录,更新状态
			jijinUserBalanceRepository.updateUserBalanceDividendStatusAndType(tradeRecord.getUserId(), tradeRecord.getFundCode(), BalanceDividendStatus.DONE.name(),item.getDividentType());

		}else{
			//修改失败,设置tradeRecord的状态位
			tradeRecordUpdateMap.put("status", "FAIL");
			//只更新balance的分红修改状态，不更改分红方式
			jijinUserBalanceRepository.updateUserBalanceDividendStatusAndType(tradeRecord.getUserId(), tradeRecord.getFundCode(), BalanceDividendStatus.DONE.name(),null);
		}
		//回写TradeSync
		jijinTradeSyncRepository.updateJijinTradeSyncStatusById(item.getId(), "DISPATCHED");
		//回写trade record
		//webdev8355:增加确认和到账日期
		String confirmDate = DateUtils.formatDateAddHMS(item.getTrxConfirmDate());
		tradeRecordUpdateMap.put("confirmDate",confirmDate);
		tradeRecord.setConfirmDate(confirmDate);
		jijinTradeRecordRepository.updateBusJijinTradeRecord(tradeRecordUpdateMap);
		//插入trade log
		saveDividendTypeModifyTradeLog(tradeRecord,item);
	}

	
	/**
	 * 保存tradeLog
	 * @param tradeRecord
	 * @param item
	 * @return
	 */
	private JijinTradeLogDTO saveDividendTypeModifyTradeLog(JijinTradeRecordDTO tradeRecord,JijinTradeSyncDTO item){
		JijinTradeLogDTO jijinTradeLogDTO = new JijinTradeLogDTO();
		jijinTradeLogDTO.setUserId(tradeRecord.getUserId());
		jijinTradeLogDTO.setFundCode(tradeRecord.getFundCode());
		jijinTradeLogDTO.setTrxTime(item.getTrxTime());
		jijinTradeLogDTO.setTrxDate(item.getTrxDate());
		jijinTradeLogDTO.setTradeRecordId(tradeRecord.getId());
		if("0000".equals(item.getTradeResCode())){
			jijinTradeLogDTO.setStatus(TradeRecordStatus.SUCCESS.name());
		}else{
			jijinTradeLogDTO.setStatus(TradeRecordStatus.FAIL.name());
		}
		jijinTradeLogDTO.setDividendType(item.getDividentType());
		jijinTradeLogDTO.setType(TradeRecordType.DIVIDEND_MODIFY);
		jijinTradeLogDTO.setCreatedBy("SYS");
		jijinTradeLogDTO.setConfirmDate(tradeRecord.getConfirmDate());
		return (JijinTradeLogDTO)jijinTradeLogRepository.insertBusJijinTradeLog(jijinTradeLogDTO);
	}
}
