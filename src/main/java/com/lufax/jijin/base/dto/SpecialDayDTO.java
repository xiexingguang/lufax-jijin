package com.lufax.jijin.base.dto;

import java.util.Date;

public class SpecialDayDTO extends BaseDTO {

    private static final String YES = "Y";

    private Date workDay;
    private String isWorkDay;
    private String memo;

    public Date getWorkDay() {
        return workDay;
    }

    public boolean isWorkDay() {
        return YES.equalsIgnoreCase(isWorkDay);
    }
}
