package com.washingtongt.data.model.gsa.time;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	public static Date getStartTime(int year,int  month){
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.YEAR, year);
		Date date = calendar.getTime();
		return date;
	}	
	
	public static Date getDate(int year,int month,int day){
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		
		Date date = calendar.getTime();
		return date;
	}
	
	public static Date getTomorrow(Date today){
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(new Date(today.getTime()));
		
		calendar.add(Calendar.DATE, 1);
		Date tomorrow = calendar.getTime();
		return tomorrow;
	}
	
	public static Date getTodayEnd(Date today){
		Date tomorrow = getTomorrow(today);
		return new Date(tomorrow.getTime() -1);
	}

	
	public static Date getEndTime(int year,int  month){
		
		if (month == 12) {
			year = year + 1;
			month = 0;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		Date date = calendar.getTime();
		date = new Date(date.getTime() -1);
		return date;
	}	

}
