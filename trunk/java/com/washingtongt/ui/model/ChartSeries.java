package com.washingtongt.ui.model;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class ChartSeries extends BasicDBObject{

	/**
	 * series
	 */
	private static final long serialVersionUID = 1L;
	private static String KEY = "key";
	private static String ORIGINAL_KEY = "originalKey";
	private static String VALUE = "values";
	private static String BAR = "bar";
	
	public ChartSeries(String key){
		super();
		BasicDBList list = new BasicDBList (); 
		this.put(KEY, key);
		this.put(VALUE, list);
	}


	public ChartSeries(String key,int position, boolean barFlag){
		super();
		BasicDBList list = new BasicDBList (); 
		this.put(BAR, barFlag);
		this.put(ORIGINAL_KEY, key);
		
		switch (position) {
		case 0: //left
			this.put(KEY, key + " (left axis)" );
			break;
		case 1: //left
			this.put(KEY, key + " (right axis)" );
			break;
		}
		
		this.put(VALUE, list);
	}
	
	public BasicDBList getValue(){
		return (BasicDBList)this.get(VALUE);
	}
	

}
