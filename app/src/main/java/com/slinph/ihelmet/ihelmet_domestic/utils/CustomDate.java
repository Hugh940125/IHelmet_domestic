package com.slinph.ihelmet.ihelmet_domestic.utils;

import java.io.Serializable;

/**
 * 自定义的日期类
 * @author huang
 *
 */
public class CustomDate implements Serializable {


	private static final long serialVersionUID = 1L;
	public int year;
	public int month;
	public int day;
	public int week;

	public CustomDate(int year, int month, int day){
		if(month > 12){
			month = 1;
			year++;
		}else if(month <1){
			month = 12;
			year--;
		}
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public CustomDate(){
		this.year = DateUtil.getYear();
		this.month = DateUtil.getMonth();
		this.day = DateUtil.getCurrentMonthDay();
	}

	/**
	 * @param date
	 * @param day
	 * @return
	 */
	public static CustomDate modifiDayForObject(CustomDate date,int day){
		CustomDate modifiDate = new CustomDate(date.year,date.month,day);
		return modifiDate;
	}
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(year);
		sb.append("/");
		if(month<10){
			sb.append("0");
		}
		sb.append(month);
		sb.append("/");
		if(day<10){
			sb.append("0");
		}
		sb.append(day);
		return sb.toString();
	}

	/**
	 * 获取本月最后一天,格式为MM月DD日
	 */
	public String getCurrentMonthLastDay(){

		return month+"月"+DateUtil.getMonthDays(year,month)+"日";
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

}
