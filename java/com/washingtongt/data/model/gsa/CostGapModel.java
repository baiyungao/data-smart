package com.washingtongt.data.model.gsa;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.Indicator;
import com.washingtongt.data.model.Measurement;

/*
 *  This model takes array of measurement, and generator UI model for presentation.
 *  [ { "Sum of meals expense" : 117244.9 , 
 *      "Sum of lodging expense" : 158009.86000000004 , 
 *      "Sum of air/train expense" : 177211.15999999916 , 
 *      "Sum of misc expense" : 48065.839999999946}]
 *   
 */
public class CostGapModel {
	
	static final Logger log = Logger.getLogger(CostGapModel.class);

	List<Measurement> mList = new ArrayList<Measurement>();

	public CostGapModel(List<Measurement> list){
		
		mList = list;
	
	}
	
	public BasicDBList getDataForBarchart(){
		
		BasicDBList model = new BasicDBList();	
		Measurement previous = null;
		
		for (Measurement m: mList){
			log.debug("current measurement:" + m);	
			log.debug("previous measurement:" + previous);
			
				if (previous != null){
					//add gap here
					for (String key:m.getIndicators().keySet()){
						
						Indicator cIndicator = m.getIndicators().get(key);
						Indicator preIndicator = previous.getIndicators().get(key);
						
						Object cV = cIndicator.getValue();

						log.debug("curent Indicator" + cIndicator + " value " + cIndicator.getValue() );
						log.debug("previous Indicator" + preIndicator + " value " + preIndicator.getValue());

						double cval = (double)cIndicator.getValue().get(0);
						double preval = (double)preIndicator.getValue().get(0);
					   
						double value = cval - preval;
						
						BasicDBObject object = new BasicDBObject("_id", new BasicDBObject("Gap " , m.getName() + cIndicator.getLabel()));
						object.put(key, value);
						model.add(object);
					}
				}
				
				BasicDBObject row = (BasicDBObject)( m.getResults().get(0));
				row.remove("_id");
				row.put("_id", new BasicDBObject("Measure",m.getName()));
				
				//add measurement
				model.add(row);				
				
				previous = m;
				
			}
	
	return model;
	}
	
	/**
	 *  retrieve all data from DB and fill out the 
	 */
	public void populate(){
		
		for (Measurement m: mList){
			
			MongoUtil.getMeasurement(m);
		}
	}
	
}
