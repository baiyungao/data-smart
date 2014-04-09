package com.washingtongt.data.model.gsa.reservation;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.GsaConstants;

public class HotelSummaryMeasure extends Measurement {

public HotelSummaryMeasure() {
		
		init();
	}


public HotelSummaryMeasure(BasicDBObject match,String groupBy, BasicDBObject sortField,int limits){
	init();
	setMatchFields(match);
	setGroupby(groupBy);
	setSortBy(sortField);
	setLimits(limits);	
}

private void init(){
	this.setCollection(GsaConstants.DB_C_R_HOTEL);
	this.setDescription("Summary of Hotel reservation");
	
	this.setIndexNames(HotelConstants.INDEX_HOTEL_COST_SUMMARY_MEASURE);
	
	//TODO refactor to automatically find factory here
	
	//please make sure the indicator factory is right  
	for (String indexName: this.getIndexNames()){
		this.addIndicator(indexName, HotelIndicatorFactory.getInstance(indexName));
	}

}
	
	
}
