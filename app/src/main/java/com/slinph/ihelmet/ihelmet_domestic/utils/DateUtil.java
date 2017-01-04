package com.slinph.ihelmet.ihelmet_domestic.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

	public static String[] weekName = { "星期日", "星期一","星期二", "星期三", "星期四", "星期五", "星期六"};

	/**
	 *输入年月,获取当月的天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthDays(int year, int month) {
		if (month > 12) {
			month = 1;
			year += 1;
		} else if (month < 1) {
			month = 12;
			year -= 1;
		}
		//一年中每个月的天数
		int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int days = 0;
		//计算是否是闰年
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			arr[1] = 29; // 闰年2月29天
		}
		//获取当月的天数
		try {
			days = arr[month - 1];
		} catch (Exception e) {
			e.getStackTrace();
		}
		return days;
	}

	/**
	 *获取当前年份
	 * @return
	 */
	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 *获取当前月份
	 * @return
	 */
	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 *获得今天在本月的第几天(获得当前日)
	 * @return
	 */
	public static int getCurrentMonthDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得某个时间是对应月的第几天
	 * @return
	 */
	public static int getMonthDay(Long time){
		Calendar instance = Calendar.getInstance();
		instance.setTime(new Date(time));
		return instance.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 *获得今天在本周的第几天
	 * @return
	 */
	public static int getWeekDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 *
	 * @return
	 */
	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	/**
	 *
	 * @return
	 */
	public static int getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	/**
	 *获取下周日日期
	 * @return
	 */
	public static CustomDate getNextSunday() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 7 - getWeekDay()+1);
		CustomDate date = new CustomDate(c.get(Calendar.YEAR),
				c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
		return date;
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @param pervious
	 * @return
	 */
	public static int[] getWeekSunday(int year, int month, int day, int pervious) {
		int[] time = new int[3];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.add(Calendar.DAY_OF_MONTH, pervious);
		time[0] = c.get(Calendar.YEAR);
		time[1] = c.get(Calendar.MONTH )+1;
		time[2] = c.get(Calendar.DAY_OF_MONTH);
		return time;
	}

	/**
	 *获取当前月第一天在日历的第几周(也就是在日历第几行)
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getWeekDayFromDate(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(year, month));//在日历上设置当月第一天的data对象
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	/**
	 *输入年月,输出当月第一天的data对象
	 * @param year
	 * @param month
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date getDateFromString(int year, int month) {
		String dateString = year + "-" + (month > 9 ? month : ("0" + month))
				+ "-01";
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return date;
	}

	/**
	 *判断date是否是当天
	 * @param date
	 * @return
	 */
	public static boolean isToday(CustomDate date){
		return(date.year == DateUtil.getYear() &&
				date.month == DateUtil.getMonth()
				&& date.day == DateUtil.getCurrentMonthDay());
	}

	/**
	 *判断date是否是当月
	 * @param date
	 * @return
	 */
	public static boolean isCurrentMonth(CustomDate date){
		return(date.year == DateUtil.getYear() &&
				date.month == DateUtil.getMonth());
	}
}
