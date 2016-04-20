package com.lufax.jijin.base.repository;

import com.lufax.jijin.base.dto.SpecialDayDTO;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SpecialDayRepository extends CfgdataBaseRepository<SpecialDayDTO> {

    private final static String NAMESPACE_SPECIAL_DAYS = "SPECIAL_DAYS";


    public SpecialDayDTO getWorkday(Date date) {
        return query("getWorkday", date);
    }

    @Override
    protected String nameSpace() {
        return NAMESPACE_SPECIAL_DAYS;
    }
}
