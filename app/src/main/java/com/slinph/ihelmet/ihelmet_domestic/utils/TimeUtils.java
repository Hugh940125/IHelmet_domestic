package com.slinph.ihelmet.ihelmet_domestic.utils;

import android.text.format.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Waiting on 2015/9/8.
 */
public class TimeUtils {
    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    /**
     * 时间戳转MM月dd日
     * @param time 时间戳
     * @return "MM月dd日"
     */
    public static String dateToStringMMDD(long time) {
        Date date=new Date(time);
        SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");
        SimpleDateFormat sdf_dd = new SimpleDateFormat("dd");
        String str_MM=sdf_MM.format(date);
        String str_dd=sdf_dd.format(date);
        if(str_MM.substring(0,1).equals("0")){
            str_MM=str_MM.substring(1);
        }
        if(str_dd.substring(0,1).equals("0")){
            str_dd=str_dd.substring(1);
        }
        return str_MM+"月"+str_dd+"日";
    }

    /**
     * 时间戳转换成字符串
     * @param time
     * @return "yyyy-MM-dd"
     */
    public static String getDateToString(Long time) {
        Date d =new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    /**
     * 时间戳转换成字符串
     * @param date
     * @return "yyyy-MM-dd"
     */
    public static String getDateToString(Date date) {
        int year = getYear(date);
        int month = getMonth(date);
        int day = getDate(date);
        return year+"-"+month+"-"+day;
    }



    /**
     * 时间戳转换成字符串
     * @param time
     * @return
     */
    public static String getDateToStringChinaYYYYMMDD(Long time) {
        Date d =new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    /**
     * 时间戳转换成字符串
     * @param time
     * @return
     */
    public static String getDateToStringChinaHHMMSS(Long time) {
        Date d =new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        return sf.format(d);
    }

    public static String getDateToStringYYMMddHHmmss(Long time) {
        Date d =new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
        return sf.format(d);
    }

    /**
     * 时间戳只转日
     * @param date
     * @return
     */
    public static int dateToDay(Long date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd");
        String day = simpleDateFormat.format(new Date(date));
        return Integer.parseInt(day);
    }

    public static int getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 判断是否是本月的日期,如果是,返回日期否则返回0
     */
    public static int dateToCurrentMonthDay(Long date){
        Time time = new Time("GMT+8");
        time.setToNow();
        int currentMonth = time.month+1;
        if(getIntMonth(date)==currentMonth){
            return dateToDay(date);
        }
        return 0;
    }

    /**
     *获取当前月份
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;

    }

    /**
     *通过时间戳获取月份
     * @param time
     * @return
     */
    public static int getIntMonth(long time){
        Date d=new Date(time);
        return getMonth(d);
    }

    public static int getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);

    }

    public static String getWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String weekStr = "";
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        switch (week) {
            case 1:
                weekStr = "星期日";
                break;
            case 2:
                weekStr = "星期一";
                break;
            case 3:
                weekStr = "星期二";
                break;
            case 4:
                weekStr = "星期三";
                break;
            case 5:
                weekStr = "星期四";
                break;
            case 6:
                weekStr = "星期五";
                break;
            case 7:
                weekStr = "星期六";
                break;
        }
        return weekStr;
    }

    public static int getDaysCount(Date oldDate, Date newDate) {

        Calendar c1 = Calendar.getInstance();
        c1.setTime(oldDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(newDate);
        int day = 0;
        while (c1.before(c2)) {
            day++;//初始化时候day=0;代表相隔天数
            c1.add(Calendar.DAY_OF_YEAR, 1);

        }
        //(int) ((newDate.getTime() - oldDate.getTime()) / (1000 * 60 * 60 * 24))
        return day;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    public static String timeBetween(long millisecond){

        long minute=millisecond/1000/60;
        int hour= (int) (minute/60);
        int newMinute= (int) (minute%60);
        if (hour==0)
            return newMinute+"m";
        else if (newMinute==0)
            return hour+"h";
        else if (newMinute<10)
            return hour+"h"+"0"+newMinute+"m";
        return hour+"h"+newMinute+"m";
    }



}
