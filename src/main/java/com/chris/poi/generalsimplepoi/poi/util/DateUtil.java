/*
 * Copyright (c) 2005-2018.  FPX and/or its affiliates. All rights reserved.
 * Use, Copy is subject to authorized license.
 */

package com.chris.poi.generalsimplepoi.poi.util;


import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * @author pds devloper
 * 日期时间工具类
 */
public class DateUtil extends DateUtils {
    /**
     * 常用时间格式
     */
    public static final String Format_Date = "yyyy-MM-dd";
    public static final String Format_Time = "HH:mm:ss";
    public static final String Format_DateTime = "yyyy-MM-dd HH:mm:ss";
    public static final String Format_DateTime_LACK_SECOND = "yyyy/MM/dd HH:mm";
    /**
     * 目前只支持中国和美国时区切换
     */
    public static final HashMap<String, String> timeZone = new HashMap<String, String>() {
        {
            put("zh_CN", "GMT+8");
            put("en_US", "GMT-4");
            put("en_AU", "GMT+11");
        }
    };

    /**
     * 带时区的日期格式
     */
    public static final String Format_DateTime_UTC = "yyyy-MM-dd HH:mm:ss Z";

    /**
     * 默认的日期格式化器，格式为yyyy-MM-dd
     */
    private final static ThreadLocal<SimpleDateFormat> DEFAULT_DATE_FORMATER = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 默认的时间格式化器，格式为yyyy-MM-dd HH:mm:ss
     */
    private final static ThreadLocal<SimpleDateFormat> DEFAULT_DATETIME_FORMATER = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };


    /**
     * 取得当前日期（只有日期，没有时间，或者可以说是时间为0点0分0秒）
     *
     * @return
     * @throws ParseException
     */
    public static Date getCurrentDate() throws ParseException {
        Date date = new Date();
        date = DEFAULT_DATE_FORMATER.get().parse(DEFAULT_DATE_FORMATER.get().format(date));
        return date;
    }

    /**
     * 取得当前时间（包括日期和时间）
     *
     * @return
     */
    public static Date getCurrentDateTime() {
        Date date = new Date();
        return date;
    }

    /**
     * 用默认的日期格式，格式化一个Date对象
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return date == null ? "" : DEFAULT_DATE_FORMATER.get().format(date);
    }

    /**
     * 根据传入的格式，将日期对象格式化为日期字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        String s = "";
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            s = sdf.format(date);
        }

        return s;
    }

    /**
     * 用默认的日期时间格式，格式化一个Date对象
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return date == null ? "" : DEFAULT_DATETIME_FORMATER.get().format(date);
    }

    /**
     * 根据传入的格式，将日期对象格式化为时间字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatTime(Date date, String format) {
        String s = "";
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            s = sdf.format(date);
        }

        return s;
    }

    /**
     * 日期后推
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 利用默认的格式（yyyy-MM-dd）将一个表示日期的字符串解析为日期对象
     *
     * @param s
     * @return
     * @throws RuntimeException
     */
    public static Date parseDate(String s) {
        Date date = null;
        try {
            date = DEFAULT_DATE_FORMATER.get().parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * 将一个字符串，按照特定格式，解析为日期对象
     *
     * @param s
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String s, String format) {
        Date date = null;
        try {
            date = (new SimpleDateFormat(format)).parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }


    /**
     * 利用默认的格式（yyyy-MM-dd HH:mm:ss）将一个表示时间的字符串解析为日期对象
     *
     * @param s
     * @return
     * @throws ParseException
     */
    public static Date parseTime(String s) throws ParseException {
        return DEFAULT_DATETIME_FORMATER.get().parse(s);
    }

    /**
     * 得到当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 得到当前月份（1至12）
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取月份
     *
     * @param i 以当前月份为基准的数值
     * @return
     */
    public static int getMonth(int i) {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + i;
    }

    /**
     * 获取yyyy-MM-dd格式的当前系统日期
     *
     * @return
     */
    public static String getCurrentDateAsString() {
        return new SimpleDateFormat(Format_Date).format(new Date());
    }

    /**
     * 获取指定格式的当前系统日期
     *
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat t = new SimpleDateFormat(format);
        return t.format(new Date());
    }

    /**
     * 获取HH:mm:ss格式的当前系统时间
     *
     * @return
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat(Format_Time).format(new Date());
    }

    /**
     * 获取指定格式的当前系统时间
     *
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat t = new SimpleDateFormat(format);
        return t.format(new Date());
    }

    /**
     * 获取格式为yyyy-MM-dd HH:mm:ss的当前系统日期时间
     *
     * @return
     */
    public static String getCurrentDateTimeAsString() {
        return getCurrentDateTime(Format_DateTime);
    }

    public static int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取星期X中文名称
     *
     * @return
     */
    public static String getChineseDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        return getChineseDayOfWeek(cal.getTime());
    }

    /**
     * 获取星期X中文名称
     *
     * @param date
     * @return
     */
    public static String getChineseDayOfWeek(String date) {
        return getChineseDayOfWeek(parseDate(date));
    }

    /**
     * 获取星期X中文名称
     *
     * @param date
     * @return
     */
    public static String getChineseDayOfWeek(Date date) {
        int dateOfWeek = getDayOfWeek(date);
        if (dateOfWeek == Calendar.MONDAY) {
            return "星期一";
        } else if (dateOfWeek == Calendar.TUESDAY) {
            return "星期二";
        } else if (dateOfWeek == Calendar.WEDNESDAY) {
            return "星期三";
        } else if (dateOfWeek == Calendar.THURSDAY) {
            return "星期四";
        } else if (dateOfWeek == Calendar.FRIDAY) {
            return "星期五";
        } else if (dateOfWeek == Calendar.SATURDAY) {
            return "星期六";
        } else if (dateOfWeek == Calendar.SUNDAY) {
            return "星期日";
        }
        return null;
    }

    public static int getDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMaxDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static String getFirstDayOfMonth(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(date));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return new SimpleDateFormat(Format_Date).format(cal.getTime());
    }

    public static int getDayOfYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    public static int getDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    public static int getDayOfWeek(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(date));
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfMonth(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(date));
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfYear(String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parseDate(date));
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取指定格式的当前系统日期时间
     *
     * @param format
     * @return
     */
    public static String getCurrentDateTime(String format) {
        SimpleDateFormat t = new SimpleDateFormat(format);
        return t.format(new Date());
    }

    public static String toString(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(Format_Date).format(date);
    }

    public static String toDateTimeString(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(Format_DateTime).format(date);
    }

    public static String toString(Date date, String format) {
        SimpleDateFormat t = new SimpleDateFormat(format);
        return t.format(date);
    }

    public static String toTimeString(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(Format_Time).format(date);
    }

    /**
     * 时间戳转换
     *
     * @param time
     * @return
     */
    public static String longTimeToDateTimeString(Long time) {
        SimpleDateFormat format = new SimpleDateFormat(Format_DateTime);
        String d = format.format(time);
        return d;
    }

    /**
     * 时间戳转换
     *
     * @param time
     * @return
     */
    public static Date longTimeToDateTime(Long time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(Format_DateTime);
        String d = format.format(time);
        return parseTime(d);
    }

    /**
     * 获取当前日期的的上一天开始时间
     *
     * @return
     */
    public static String getYesterDayDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterdayBegin = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return yesterdayBegin;
    }

    public static String getDateAfter(int day) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        String yesterdayBegin = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return yesterdayBegin;
    }

    /**
     * 获取最近6个月，经常用于统计图表的X轴
     */
    public static String[] getLastMonths() {
        String[] last12Months = new String[12];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1); //要先+1,才能把本月的算进去</span>
        for (int i = 0; i < 6; i++) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1); //逐次往前推1个月
            last12Months[11 - i] = cal.get(Calendar.YEAR) + "-" + fillZero((cal.get(Calendar.MONTH) + 1));
        }
        return last12Months;
    }

    public static String fillZero(int i) {
        String str = "";
        if (i > 0 && i < 10) {
            str = "0" + i;
        } else {
            str = "" + i;
        }
        return str;
    }


    /**
     * 计算两个日期之间相差的天数
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static int getDiscrepantDays(Date dateStart, Date dateEnd) {
        return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
    }

    /**
     * 计算两个日期之间的天数
     *
     * @param start   开始日期
     * @param end     结束日期
     * @param pattern 日期格式化字符串
     * @return 相差的天数
     */
    public static int getDiscrepantDays(Date start, Date end, String pattern) {
        if (null == start || null == end) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            start = sdf.parse(sdf.format(start));
            end = sdf.parse(sdf.format(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) ((end.getTime() - start.getTime()) / 1000 / 60 / 60 / 24) + 1;
    }


    // 获得当前周- 周一的日期
    public static String getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    // 获得当前周- 周日  的日期
    public static String getPreviousSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    //获得当前日期与本周一相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }


    //获取上周一
    public static Date getLastWeekMonday(Date date) {
        Date a = DateUtils.addDays(date, -1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(a);
        cal.add(Calendar.WEEK_OF_YEAR, -1);// 一周
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }


    //获取上周日
    public static Date getLastWeekSunday(Date date) {

        Date a = DateUtils.addDays(date, -1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(a);
        cal.set(Calendar.DAY_OF_WEEK, 1);

        return cal.getTime();
    }

    /**
     * 获取UTC时间字符
     *
     * @return 例如：2017-05-08 02:00:00 +0800
     */
    public static String getUtcDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat(Format_DateTime_UTC);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MILLISECOND, -(c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)));
        return sdf.format(c.getTime());
    }

    /**
     * 根据格式化字符串获取UTC时间字符串
     *
     * @param fmt 例如："yyyy-MM-dd HH:mm:ss Z"
     * @return 例如：2017-05-08 02:00:00 +0800
     */
    public static String getUtcDateString(String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MILLISECOND, -(c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)));
        return sdf.format(c.getTime());
    }

    /**
     * 获取UTC时间
     *
     * @return 例如：Mon May 08 00:00:00 CST 2017
     */
    public static Date getUtcDate() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MILLISECOND, -(c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)));
        return c.getTime();
    }

    /**
     * 根据时区获取对应时间的Utc 时间
     *
     * @param date     需要转换的时间
     * @param timeZone 时区
     * @return 转换后的UTC时间
     */
    public static Date getUtcDate(Date date, String timeZone) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.setTimeZone(TimeZone.getTimeZone(timeZone));
        c.add(Calendar.MILLISECOND, -(c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)));
        return c.getTime();
    }

    /**
     * 根据UTC 时间返回本地[北京]时间
     *
     * @param utcDate UTC时间
     * @return 例如：Mon May 08 00:00:00 CST 2017
     */
    public static Date getLocaleDate(Date utcDate) {
        return getLocaleDate(utcDate, "GMT+8");
    }

    /**
     * 获取对应时区的当地时间
     *
     * @param utcDate  UTC 时间
     * @param timeZone 时区： 例如：北京-GMT+8
     * @return
     */
    public static Date getLocaleDate(Date utcDate, String timeZone) {
        Calendar c = Calendar.getInstance();
        c.setTime(utcDate);
        c.setTimeZone(TimeZone.getTimeZone(timeZone));
        c.add(Calendar.MILLISECOND, (c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)));
        return c.getTime();
    }

    /**
     * 指定时间时区转换
     *
     * @param date           需要转换的时区
     * @param sourceTimeZone 源时区
     * @param targetTimeZone 目标时区
     * @return 指定时区的时间
     */
    public static Date getLocaleDate(Date date, String sourceTimeZone, String targetTimeZone) {
        Date utcDate = getUtcDate(date, sourceTimeZone);
        return getLocaleDate(utcDate, targetTimeZone);
    }

    /**
     * 如果为空，获取系统时间
     *
     * @param operationTime 检验时间
     * @return 时间
     */
    public static Date operationTimeOrSystemTime(Date operationTime) {
        return operationTime == null ? new Date() : operationTime;
    }

    /**
     * 获取整点时间
     *
     * @param hour 时
     * @return 指定时间
     */
    public static Calendar getDate(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 获取整点结束时间
     *
     * @param hour 时
     * @return
     */
    public static Calendar getDateEnd(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }

    /**
     * 格式化日期
     *
     * @param date          日期
     * @param parsePatterns 格式
     * @param timeZone      时区
     * @return 日期字符串
     */
    public static String formatDate(Date date, String parsePatterns, TimeZone timeZone) {
        SimpleDateFormat df = new SimpleDateFormat(parsePatterns);
        if (timeZone != null) {
            df.setTimeZone(timeZone);
        }
        return df.format(date);
    }
}
