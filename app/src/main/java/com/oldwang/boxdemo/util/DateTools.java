package com.oldwang.boxdemo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTools {

	public static String getDate(String format) {
		SimpleDateFormat fm = new SimpleDateFormat(format);
		return fm.format(new Date());
	}

    public static String getFileNameStr(){
    	return getDate("yyyyMMdd");
    }
	
	public static String getDate1() {
		return getDate("HHmmss");
	}
	public static String getDate() {
		return getDate("HH:mm:ss");
	}
	public static String getCurrentDay() {
		return getDate("yyyy:MM:dd");
	}
	public static String getCrashDay() {
		return getDate("yyyy-MM-dd");
	}

	public static String getCrashSaveDay() {
		return getDate("yyyy-MM-dd-mm");
	}

	public static String getCoverDate() {
		return getDate("yyyy:MM:dd HH:mm:ss");
	}

	public static String getLineDate() {
		return getDate("yyyy-MM-dd HH-mm-ss");
	}
	public static String getMixedDate() {
		return getDate("yyyy-MM-dd HH:mm:ss");
	}
	public static String getStringTime(){
		return getDate("yyyyMMddHHmmss");
	}


	public static long gettime(String s){
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时");
		Date date=null;
		try {
			date = df.parse(s);

			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return 0;
	}

	public static String getFormat(long longtime){
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date(longtime);
		String format = fm.format(date);
		return format;
	}

	public static String getFormat(long longtime,String pattern){
		SimpleDateFormat fm = new SimpleDateFormat(pattern);
		Date date=new Date(longtime);
		String format = fm.format(date);
		return format;
	}

	public static long getcurrentTime(){
		return new Date().getTime();
	}


	public static String getOldDate(int count) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -count); // 得到前一天
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy:MM:dd");
		return df.format(date);
	}
	
	public static String getDateAsFileName(){
		return getDate("yyyyMMdd_HHmmss");
	}

	public static String getDateByString(String dateString) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
		Date date=null;
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dateString=df.format(date);
		return dateString;
	}


	private final static long minute = 60 * 1000;// 1分钟
	private final static long hour = 60 * minute;// 1小时
	private final static long day = 24 * hour;// 1天
	private final static long month = 31 * day;// 月
	private final static long year = 12 * month;// 年

	/**
	 * 返回文字描述的日期
	 *
	 * @param date
	 * @return
	 */
	public static String getTimeFormatText(long date) {
		long diff = new Date().getTime() - date;
		long r = 0;
		if (diff > year) {
			r = (diff / year);
			return r + "年前";
		}
		if (diff > month) {
			r = (diff / month);
			return r + "个月前";
		}
		if (diff > day) {
			r = (diff / day);
			return r + "天前";
		}
		if (diff > hour) {
			r = (diff / hour);
			return r + "个小时前";
		}
		if (diff > minute) {
			r = (diff / minute);
			return r + "分钟前";
		}
		return "刚刚";
	}

}
