package com.washingtongt.ui.model;

import java.util.Calendar;
import java.util.Set;

import com.mongodb.BasicDBObject;

public class ModelHelper {

	/** get the millisec from date variable 
	 * 
	 */
	
	public static long getSortingRowId(BasicDBObject row){
		
		int year = 0;
		int month = 0;
		int day = 0;
		
		BasicDBObject idObject = (BasicDBObject)(row.get("_id"));
		if (idObject.get("Year")instanceof String){
			year = Integer.parseInt((String)idObject.get("Year"));
		}
		else {
			year = (int)idObject.get("Year");
			}
		month = idObject.get("Month")!= null?(int)idObject.get("Month")-1: 0;
		day = idObject.get("Day")!= null?(int)idObject.getInt("Day"):1;
		
		Calendar date = Calendar.getInstance();
		date.set(year, month, day);
	
		return date.getTimeInMillis();
	}	
	
	public static String getRowId(BasicDBObject row){
		String id = "";
		BasicDBObject idObject = (BasicDBObject)(row.get("_id"));
		Set<String> idKeys = idObject.keySet();
		for (String key: idKeys){
			if (id.length() > 0) {id = id + "_";}
			id = id + idObject.get(key);
		}

		return id;
	}

}
