package com.washingtongt.data.model.gsa;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.SerialBase;
import com.washingtongt.data.model.gsa.time.FiscalYear;

/**
 * This model is repreent all details to profile trip data provide by GSA
 * 
 * @author gaob
 *
 */
public class TripProfileModel {
	
	static final Logger log = Logger.getLogger(TripProfileModel.class);
	private Map<String, SerialBase> serialMap = new HashMap<String, SerialBase>();
	
	private BasicDBObject match;
	
	public TripProfileModel(BasicDBObject match){
		
		this.match = match;
		
		FiscalYear fy2011 =new FiscalYear(GSAIndicatorFactory.IDT_DATE_DEPARTURE, match, 2011, 2010, 10, TravelCostMeasure.class);
		
		
		
	}
	
}
