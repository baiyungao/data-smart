package com.washingtongt.ui.model;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class LineChartModel extends BasicDBList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(LineChartModel.class);
	
	private HashMap<String, ChartSerie> serialMap = new HashMap<String, ChartSerie>();
	
	
	
	public LineChartModel(){
		super();
	}
	
	public void add(ChartSerie cs){
		serialMap.put((String)cs.get("key"), cs);
		super.add(cs);
	}
	
	
	public void addContent(BasicDBObject row, long rowId, String[] charIndex){
		
		
		for (String colKey: charIndex ){
			
			log.debug("col:" + colKey);
			ChartSerie serial = (ChartSerie)(serialMap.get(colKey));
			
			int seriesIndex = this.indexOf(serial);
			BasicDBObject item = new BasicDBObject();
			item.put("series", seriesIndex);
			item.put("x", rowId);
			item.put("y", row.get(colKey));
		
			serial.getValue().add(item);
			
		}
	}

	public void addContent(BasicDBObject row, BasicDBObject benchMark, long rowId, String[] charIndex){
		
		
		for (String colKey: charIndex ){
			
			log.debug("col:" + colKey);
			ChartSerie serial = (ChartSerie)(serialMap.get(colKey));
			
			int seriesIndex = this.indexOf(serial);
			BasicDBObject item = new BasicDBObject();
			item.put("series", seriesIndex);
			item.put("x", rowId);
			
			double bm = benchMark.getDouble(colKey);
			double value = row.getDouble(colKey);
			double percent = ((double)(bm-value))/bm;
			item.put("y", percent);
		
			serial.getValue().add(item);
			
		}
	}	
	

}
