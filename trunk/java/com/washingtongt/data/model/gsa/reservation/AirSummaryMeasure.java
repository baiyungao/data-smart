package com.washingtongt.data.model.gsa.reservation;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.GsaConstants;

public class AirSummaryMeasure extends Measurement {
	
public AirSummaryMeasure() {
		
		init();
	}


public AirSummaryMeasure(BasicDBObject match,String groupBy, BasicDBObject sortField,int limits){
	init();
	setMatchFields(match);
	setGroupby(groupBy);
	setSortBy(sortField);
	setLimits(limits);	
}

private void init(){
	this.setCollection(GsaConstants.DB_C_R_AIR);
	this.setDescription("Summary of Air tickets reservation");
	
	this.setIndexNames(AirConstants.INDEX_AIR_COST_SUMMARY_MEASURE);
	//TODO refactor to automatically find factory here
	//please make sure the indicator factory is right  
	for (String indexName: this.getIndexNames()){
		this.addIndicator(indexName, AirIndicatorFactory.getInstance(indexName));
	}

}

}
