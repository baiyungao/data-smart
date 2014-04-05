package com.washingtongt.ui.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BarchartModel extends BasicDBList{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(BarchartModel.class);
	
	
	Map<String, ChartSeries> serialMap = new HashMap<String, ChartSeries>();
	
	public BarchartModel(){
		super();
		serialMap = new HashMap<String, ChartSeries>();
	}
	

	public BarchartModel(BasicDBList list){
		
		super();
		
		serialMap = new HashMap<String, ChartSeries>();
		
		for (int i = 0; i<list.size(); i++){
			BasicDBObject row = (BasicDBObject)(list.get(i));
			//log.debug(key  + ":" + row + " " + row.getClass() );
			
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
	

public void addContent(TableModel model){
		
		List<String> colKeys = model.getCols();
		
		for (DBObject row :model.getContents()){
		
		for (String colKey: colKeys ){
			
			log.debug("col:" + colKey);
			if (colKey.indexOf("Trip_Counts")> 0){
				continue; //bypass the counts data;
			}
			ChartSeries serial = (ChartSeries)(this.serialMap.get(colKey));
			if (serial == null) {
				serial = new ChartSeries(colKey);
				serialMap.put(colKey, serial);
				this.add(serial);
			}
			
			int seriesIndex = this.indexOf(serial);
			BasicDBObject item = new BasicDBObject();
			item.put("series", seriesIndex);
			item.put("x", row.get("Item"));
			item.put("y", row.get(colKey));
		
			serial.getValue().add(item);
			
		}
		}
	}

}
