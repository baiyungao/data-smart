package com.washingtongt.data.model;

import java.util.Date;

import org.apache.log4j.Logger;

import com.washingtongt.data.model.gsa.time.DateUtils;

public class ScaleUtils {
	static final Logger log = Logger.getLogger(ScaleUtils.class);
	public static Scale generateDayScale (Date start, Date end){
		
		
		Scale scale = new Scale();
		scale.setStart(start);
		scale.setEnd(end);
		
		Date time = start;
		
	    while (!time.after(end)){
	    	
	    	DayUnit day = new DayUnit();
	    	day.setStart(time);
	    	day.setEnd(DateUtils.getTodayEnd(time));
	    	scale.addUnit(day);
	    	
	    	time = DateUtils.getTomorrow(time);
	    }
		
		
		return scale;
	}
	
	public static void main(String[] args){
		Date start = DateUtils.getDate(2012, 1, 20);
		Date end = DateUtils.getDate(2013, 2, 20);
		
		Scale scale = generateDayScale(start,end);
		
		for (TimeUnit unit:scale.getUnits()){
			log.debug("Unit:" + unit);
		}
	
	}
}
