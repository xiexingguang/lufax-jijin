package com.lufax.jijin.fundation.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lufax.jersey.utils.Logger;
import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.fundation.repository.JijinSequenceRepository;

@Service
public class SequenceService {
    @Autowired
    private JijinSequenceRepository jijinSequenceRepository;
    
    /**
     * 
     * 流水号规则：  业务代码+YYYYMMDDHHMMSS+15位序列号
     * @return
     */
    public String getSerialNumber(String bizType){

        if (bizType==null) {
            Logger.error(this, "Jijin serial num, bizType is null");
            return "";
        }

    	Date date = new Date();
    	String targetDate =DateUtils.formatDate(date, DateUtils.CMS_DRAW_SEQUENCE_FORMAT);
    	String sn = String.valueOf(jijinSequenceRepository.findJijinSequence());
    	StringBuilder sb = new StringBuilder();
    	sb.append(bizType).append(targetDate).append(sn);
    	return sb.toString();
    }

  
}
