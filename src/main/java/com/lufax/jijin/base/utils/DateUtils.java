package com.lufax.jijin.base.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DOTE = "yyyy.MM.dd";
    public static final String DATE_FORMAT_CHINESE = "yyyy年MM月dd日";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String CMS_DRAW_SEQUENCE_FORMAT = "yyyyMMddHHmmss";
    public static final String XINBAO_DATE_FORMAT = "yyyy/MM/dd";
    public static final String CONTRACT_DATE_FORMAT = "yyyy/MM";
    public static final String MEMBER_SYSTEM_DATE_FORMAT = "yyyy-MM-dd";
    public static final String MATCH_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_STRING_FORMAT = "yyyyMMdd";
    public static final String XINBAO_MOBILENO_SEQUENCE_FORMATE = "yyyyMMddHHmmss";

    public static Date endOfToday() {
        return endOfDay(new Date());
    }

    public static Date startOfToday() {
        return startOfDay(new Date());
    }

    public static Date endOfDay(Date date) {
        DateTime startDateTime = new DateTime(date).dayOfYear().roundFloorCopy();
        return startDateTime.plusDays(1).minusMillis(1).toDate();
    }

    public static Date startOfDay(Date date) {
        return new DateTime(date).dayOfYear().roundFloorCopy().toDate();
    }

    public static Date startOfTomorrow() {
        return startOfDay(new DateTime().plusDays(1).toDate());
    }

    public static Date startOfYesterday() {
        return startOfDay(new DateTime().minusDays(1).toDate());
    }

    public static boolean beforeToday(Date date) {
        return date.compareTo(DateUtils.startOfToday()) < 0;
    }

    public static boolean afterToday(Date date) {
        return date.compareTo(DateUtils.startOfToday()) > 0;
    }

    public static boolean isOnSameDayOfMonth(DateTime datetime, DateTime other) {
        return datetime.getDayOfMonth() == other.getDayOfMonth();
    }

    public static String formatDate(Date date) {
        return formatDate(date, DATE_FORMAT);
    }

    public static String formatDateDote(Date date) {
        return formatDate(date, DATE_FORMAT_DOTE);
    }

    public static String formatDateToChinese(Date date) {
        return formatDate(date, DATE_FORMAT_CHINESE);
    }

    public static String formatDateInContract(Date date) {
        return formatDate(date, CONTRACT_DATE_FORMAT);
    }

    public static String formatDateTime(Date date) {
        return formatDate(date, DATE_TIME_FORMAT);
    }

    public static Date parseDate(String date) {
        return parseDate(date, DATE_FORMAT);
    }

    public static Date parseDateTime(String date) {
        return parseDate(date, DATE_TIME_FORMAT);
    }

    public static DateTime parseAsDateTime(String date) {
        return new DateTime(parseDate(date));
    }

    public static String formatDateAsCmsDrawSequence(Date date) {
        return formatDate(date, CMS_DRAW_SEQUENCE_FORMAT);
    }


    public static String formatDate(Date date, String dateFormat) {
        try {
            return new SimpleDateFormat(dateFormat).format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static Date parseDate(String dateString, String dateFormat) {
        try {
            return new SimpleDateFormat(dateFormat).parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getYearOfFourBits(Date date) {
        return new DateTime(date).getYear() + "";
    }

    public static String getMonthOfTwoBits(Date date) {
        String month = new DateTime(date).getMonthOfYear() + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        return month;
    }

    public static String getDayOfTwoBits(Date date) {
        String day = new DateTime(date).getDayOfMonth() + "";
        if (day.length() == 1) {
            day = "0" + day;
        }
        return day;
    }

    public static boolean isMondayToFriday(Date date) {
        int dayOfWeek = new DateTime(date).getDayOfWeek();
        return dayOfWeek != 6 && dayOfWeek != 7;
    }

    public static boolean isMonday(Date date) {
        int dayOfWeek = new DateTime(date).getDayOfWeek();
        return dayOfWeek == 1;
    }

    public static boolean isSameDay(Date date, Date other) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        String otherString = format.format(other);
        return dateString.equals(otherString);
    }

    public static int calculateIntervalDays(Date dateFrom, Date dateTo) {
        return Days.daysBetween(new DateTime(DateUtils.endOfDay(dateFrom)), new DateTime(DateUtils.endOfDay(dateTo))).getDays();
    }

    public static String getFormattedHoursByMinutes(int minutes) {
        return formatDate(new DateTime(startOfToday()).plusMinutes(minutes).toDate(), "H:mm");
    }

    public static String formatTimeDateDefault(Date date) {
        if (date == null) {
            return "";
        }
        return formatDate(date, DATE_TIME_FORMAT);
    }


    public static long diffSecond(Date start, Date end) {
        return render(end.getTime() - start.getTime(), 999, 1000);
    }

    public static long diffMinute(Date end) {
        return diffMinute(new Date(System.currentTimeMillis()), end);
    }

    public static long diffMinute(Date start, Date end) {
        return render(diffSecond(start, end), 59, 60);
    }

    public static long diffHour(Date start, Date end) {
        return render(diffMinute(start, end), 59, 60);
    }

    public static long diffDay(Date start, Date end) {
        return offset(start, end, Calendar.DAY_OF_YEAR);
    }

    private static long render(long i, int j, int k) {
        return (i + (i > 0 ? j : -j)) / k;
    }

    private static long offset(Date start, Date end, int offsetCalendarField) {

        boolean bool = start.before(end);
        long rtn = 0;
        Calendar s = Calendar.getInstance();
        Calendar e = Calendar.getInstance();

        s.setTime(bool ? start : end);
        e.setTime(bool ? end : start);

        rtn -= s.get(offsetCalendarField);
        rtn += e.get(offsetCalendarField);

        while (s.get(Calendar.YEAR) < e.get(Calendar.YEAR)) {
            rtn += s.getActualMaximum(offsetCalendarField);
            s.add(Calendar.YEAR, 1);
        }

        return bool ? rtn : -rtn;
    }

    public static Date add(Date date, int n, int calendarField) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, n);
        return c.getTime();
    }

    public static String formatDateAsString(Date date) {
        return formatDate(date, DATE_STRING_FORMAT);
    }


    public static String formatDateFromStringToString(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            return "";
        }
    }
    /**
     * 给定一个YYYY-MM-DD的日期，加上当前的时分秒
     * @param strDate
     * @return
     */
    public static String formatDateAddHMS(String strDate){
        String strNewDate ="";
        if(strDate!=null&&strDate.length()>8){
            if(strDate.length()<=14){
                strNewDate = strDate;
            }else {
                strNewDate = strDate.substring(0, 14);
            }
        }else{
            DateFormat df = DateFormat.getTimeInstance();
            String str = formatDateAsCmsDrawSequence(new Date());
            str = str.substring(8,str.length());
            strNewDate = strDate+str;
        }
        return formatDateTime(formatDate(strNewDate));
    }
    public static Date formatDate(String date) {
        return parseDate(date, CMS_DRAW_SEQUENCE_FORMAT);
    }
}
