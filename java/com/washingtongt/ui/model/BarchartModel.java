package com.washingtongt.ui.model;

import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class BarchartModel extends BasicDBList{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(BarchartModel.class);
	
	//Map<String, ChartSerial> serialMap = new HashMap<String, ChartSerial>();
	
	
	public BarchartModel(BasicDBList list){
		
		super();
		
		HashMap<String, ChartSeries> serialMap = new HashMap<String, ChartSeries>();
		
		Set<String> keySet = list.keySet();
	
		for (String key: keySet){
			BasicDBObject row = (BasicDBObject)(list.get(key));
			log.debug(key  + ":" + row + " " + row.getClass() );
			
			Set<String> colKeys = row.keySet();
			
			String rowId = ModelHelper.getRowId(row);
			colKeys.remove("_id");
			log.debug("rowID:" + rowId);
			
			for (String colKey: colKeys ){
				
				log.debug("col:" + colKey);
				ChartSeries serial = (ChartSeries)(serialMap.get(colKey));
				if (serial == null){
					serial = new ChartSeries(colKey);
					serialMap.put(colKey, serial);
					this.add(serial);
				}
				
				int seriesIndex = this.indexOf(serial);
				BasicDBObject item = new BasicDBObject();
				item.put("series", seriesIndex);
				item.put("x", rowId);
				item.put("y", row.get(colKey));
			
				serial.getValue().add(item);
				
			}
		}
    	
		
	}
	


}
