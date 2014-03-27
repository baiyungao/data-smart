package com.washingtongt.ui.model;

import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class PieChartModel extends BasicDBList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(PieChartModel.class);
	
	public PieChartModel(BasicDBList list){
		
		super();
		
		Set<String> keySet = list.keySet();
	
		for (String key: keySet){
			BasicDBObject row = (BasicDBObject)(list.get(key));
			log.debug(key  + ":" + row + " " + row.getClass() );
			
			Set<String> colKeys = row.keySet();
			
			//String rowId = ModelHelper.getRowId(row);
			colKeys.remove("_id");
			//log.debug("rowID:" + rowId);
			
			for (String colKey: colKeys ){
				
				BasicDBObject item = new BasicDBObject();
				item.put("key", colKey);
				item.put("y", row.get(colKey));
			
				this.add(item);
			}
		}
	}	
	

}
