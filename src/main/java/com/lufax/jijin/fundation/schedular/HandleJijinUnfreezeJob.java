package com.lufax.jijin.fundation.schedular;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.constant.JijinFreezeStatus;
import com.lufax.jijin.fundation.dto.JijinUnFreezeRecordDTO;
import com.lufax.jijin.fundation.dto.JijinUnFreezeScheduleDTO;
import com.lufax.jijin.fundation.dto.JijinUnfreezeRecordCountDTO;
import com.lufax.jijin.fundation.repository.JijinUnFreezeRecordRepository;
import com.lufax.jijin.fundation.repository.JijinUnFreezeScheduleRepository;
import com.lufax.jijin.fundation.service.JijinFreezeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liudong735
 * @date 2016年03月10日
 */
@Service
public class HandleJijinUnfreezeJob extends BaseBatchWithTimeLimitJob<JijinUnFreezeRecordDTO, Long> {
    @Autowired
    private JijinUnFreezeScheduleRepository jijinUnFreezeScheduleRepository;
    @Autowired
    private JijinUnFreezeRecordRepository jijinUnFreezeRecordRepository;
    @Autowired
    private JijinFreezeService jijinFreezeService;

    @Override
    protected Long getKey(JijinUnFreezeRecordDTO item) {
        return item.getId();
    }

    @Override
    protected List<JijinUnFreezeRecordDTO> fetchList(int batchAmount) {
        String busDate = DateUtils.formatDateAsString(new Date());
        JijinUnFreezeScheduleDTO scheduleDTO = jijinUnFreezeScheduleRepository.getJijinUnFreezeScheduleListByUnfreezeDate(busDate);
        if (scheduleDTO != null) {
            BigDecimal waitUnfreezeShare = scheduleDTO.getTotalFreezeShare().subtract(scheduleDTO.getTotalUnFreezeShare());
            Long waitUnfreezeCount = scheduleDTO.getTotalCount() - scheduleDTO.getTotalUnfreezeCount();
            JijinUnfreezeRecordCountDTO unfreezeRecordCountDTO = jijinUnFreezeRecordRepository.getJijinUnfreezeRecordCountDTO(busDate);

            if (!waitUnfreezeCount.equals(unfreezeRecordCountDTO.getCount()) ||
                    waitUnfreezeShare.compareTo(unfreezeRecordCountDTO.getTotalFreezeShare()) != 0) {
                //解冻计划表和解冻记录不一致
                Logger.info(this, String.format("Unfreeze schedule does not match with unfreeze_record. date: %s", busDate));
            }
            if (unfreezeRecordCountDTO.getCount() > 0) {
                return jijinUnFreezeRecordRepository.getJijinUnFreezeRecordByDate(busDate, JijinFreezeStatus.FROZEN.name(), batchAmount);
            }
        }
        return new ArrayList<JijinUnFreezeRecordDTO>();
    }

    @Override
    protected void process(JijinUnFreezeRecordDTO item) {
        jijinFreezeService.unFreezeFund(item);
    }
}
