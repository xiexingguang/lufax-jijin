package com.lufax.jijin.fundation.repository;

import org.springframework.stereotype.Repository;

import com.lufax.jijin.base.repository.BaseRepository;
import com.lufax.jijin.fundation.dto.JijinUserBalanceDTO;

@Repository
public class JijinSequenceRepository extends BaseRepository<JijinUserBalanceDTO>{

    protected String nameSpace(){
        return "BusJijinUserBalance";
    }

    public Long findJijinSequence(){
    	return (Long)super.queryObject("getSequence", null);
    }
}
