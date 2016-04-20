package com.lufax.jijin.ylx.util;

import java.util.Calendar;
import java.util.Date;

public class TimeoutUtils {

      public static boolean isTimeout(String timeout){
          RequestTimer timer = RequestTimer.parse(timeout);
          Calendar calendar = Calendar.getInstance();
          calendar.set(Calendar.HOUR_OF_DAY,timer.getHour());
          calendar.set(Calendar.MINUTE,timer.getMinute());
          return new Date().after(calendar.getTime());
      }
}
