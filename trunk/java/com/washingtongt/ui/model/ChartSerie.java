package com.washingtongt.ui.model;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class ChartSerie extends BasicDBObject{

	/**
	 * series
	 */
	static final Logger log = Logger.getLogger(ChartSerie.class);
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int NONE = -1;
	
	private static final long serialVersionUID = 1L;
	private static String KEY = "key";
	public static String ORIGINAL_KEY = "originalKey";
	private static String VALUE = "values";
	private static String BAR = "bar";
	
	public void setKey(String value){
		this.put(KEY, value);
	}
	
	public ChartSerie(String key){
		super();
		BasicDBList list = new BasicDBList (); 
		this.put(KEY, key);
		this.put(VALUE, list);
		this.put(ORIGINAL_KEY, key);
	}


	public ChartSerie(String key,int position, boolean barFlag){
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
		case -1: //NONE
			this.put(KEY, key);
		}
		
		this.put(VALUE, list);
	}
	
	public BasicDBList getValue(){
		return (BasicDBList)this.get(VALUE);
	}
	
	public void addContent(BasicDBObject row, BasicDBObject benchMark, long rowId, String key, int serieIndex){
	
		BasicDBObject item = new BasicDBObject();
			item.put("series", serieIndex);
			item.put("x", rowId);
			
			double bm = benchMark.getDouble(key);
			
			double value = 0;
			if (row != null) {
				value = row.getDouble(key);
				}
			
			//double percent = ((double)(bm-value))/bm;  //TODO add formular here
			double percent = ((double)value)/bm;
			item.put("y", percent);
		
			this.getValue().add(item);
			
	
	}	
	

}
