package com.lufax.jijin.base.utils;

import org.joda.time.DateTime;

import java.util.Date;

public class Day {
    private DateTime dateTime;

    public Day() {
        dateTime = new DateTime();
    }

    public Day(Date date) {
        dateTime = new DateTime(date);
    }

    public Day(int year, int month, int day) {
        dateTime = new DateTime(year, month, day, 0, 0, 0, 0);
    }

    public Date toDate() {
        return dateTime.toDate();
    }

    public Date yesterday() {
        return dateTime.minusDays(1).toDate();
    }
}
