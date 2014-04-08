package com.washingtongt.ui.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.gsa.GsaConstants;

public class LinePlusBarChartModel extends BasicDBList{
	/**
	 * this model the X axis is the time line, we need sort it first
	 */
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(LinePlusBarChartModel.class);
	private HashMap<String, ChartSerie> serialMap = new HashMap<String, ChartSerie>();
	
	public void add(ChartSerie cs){
		serialMap.put((String)cs.get(ChartSerie.ORIGINAL_KEY), cs);
		super.add(cs);
	}
	
	public LinePlusBarChartModel(){
		super();
	}
	
	public LinePlusBarChartModel(BasicDBList list, boolean sort){
		
		super();
		if (sort) {
			list= sort(list);
		}
	
		for (Object object: list){
			BasicDBObject row = (BasicDBObject)(object);
			
			
			Set<String> colKeys = row.keySet();
			
			Object rowId = sort?ModelHelper.getSortingRowId(row):ModelHelper.getRowId(row);
			colKeys.remove("_id");
			log.debug("rowID:" + rowId);
			
			for (String colKey: colKeys ){
				
				log.debug("col:" + colKey);
				ChartSerie serial = (ChartSerie)(serialMap.get(colKey));
				if (serial == null){
					if (colKey.equalsIgnoreCase("count")){
						serial = new ChartSerie(colKey,0,true);}
					else 
					{
						serial = new ChartSerie(colKey,1,false);
					}
					//serialMap.put(colKey, serial);
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
	
public LinePlusBarChartModel(BasicDBList list, ChartSerie bench, boolean sort){
		
		super();
		if (sort) {
			list= sort(list);
		}
	
		for (Object object: list){
			BasicDBObject row = (BasicDBObject)(object);
			
			
			Set<String> colKeys = row.keySet();
			
			Object rowId = sort?ModelHelper.getSortingRowId(row):ModelHelper.getRowId(row);
			colKeys.remove("_id");
			log.debug("rowID:" + rowId);
			
			for (String colKey: colKeys ){
				
				log.debug("col:" + colKey);
				ChartSerie serial = (ChartSerie)(serialMap.get(colKey));
				if (serial == null){
					if (colKey.equalsIgnoreCase("count")){
						serial = new ChartSerie(colKey,0,true);}
					else 
					{
						serial = new ChartSerie(colKey,1,false);
					}
					//serialMap.put(colKey, serial);
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
    	
		this.add(bench);
	}
		
	
	private BasicDBList sort(BasicDBList list){
		TreeMap<Long,BasicDBObject> sortingTree = new TreeMap<Long,BasicDBObject>();
		Set<String> keySet = list.keySet();

		for (String key: keySet){
			BasicDBObject row = (BasicDBObject)(list.get(key));
			log.debug(key  + ":" + row + " " + row.getClass() );
			long rowId = ModelHelper.getSortingRowId(row);
			log.debug("rowID:" + rowId);
			sortingTree.put(rowId, row);
		}
		
		BasicDBList result = new BasicDBList();
		Collection<BasicDBObject> values = sortingTree.values();
		result.addAll(values);
		return result;
		
	}

	public HashMap<String, ChartSerie> getSerialMap(){
	 return this.serialMap;
	}
	
	public void addContent(BasicDBObject row, String rowId, String[] charIndex){
		
		
		for (String colKey: charIndex ){
			
				log.debug("col:" + colKey);
				ChartSerie serie = (ChartSerie)(serialMap.get(colKey));
				
				int seriesIndex = this.indexOf(serie);
				BasicDBObject item = new BasicDBObject();
				item.put("series", seriesIndex);
				item.put("x", rowId);
				if (row != null){
				item.put("y", row.get(colKey));
			}
			else {
				item.put("y", 0);
			}
		    if (serie!= null){
		    	serie.getValue().add(item);
		    }
		}
	}
	
}
