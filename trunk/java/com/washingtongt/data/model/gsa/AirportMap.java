package com.washingtongt.data.model.gsa;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.MongoUtil;

public class AirportMap {

	public static Map<String, BasicDBObject> airportMap = new HashMap<String, BasicDBObject> ();
	static {
		BasicDBList airports = MongoUtil.getAirportGeo();
		for (int i = 0; i < airports.size();i++){
			BasicDBObject airport = (BasicDBObject) airports.get(i);
			airportMap.put((String)airport.get("CODE"), airport);
		}
		
		
	}
	
}
