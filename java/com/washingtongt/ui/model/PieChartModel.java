package com.washingtongt.ui.model;

import java.util.Set;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.gsa.GsaConstants;

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
	
public PieChartModel(BasicDBObject raw){
		
		super();
		BasicDBObject row = (BasicDBObject)raw.clone();
		Set<String> keySet = row.keySet();
		keySet.remove(GsaConstants.IDT_ID);
		keySet.remove("Item");
		log.debug("keys:" + keySet);
		
		for (String key: keySet){
				
				
			BasicDBObject item = new BasicDBObject();
				item.put("key", key);
				item.put("y", row.get(key));
			
				this.add(item);
			
		}
	}	

}
