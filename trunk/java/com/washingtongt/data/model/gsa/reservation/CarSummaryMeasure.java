package com.washingtongt.data.model.gsa.reservation;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.GsaConstants;

public class CarSummaryMeasure extends Measurement {

public CarSummaryMeasure() {
		
		init();
	}


public CarSummaryMeasure(BasicDBObject match,String groupBy, BasicDBObject sortField,int limits){
	init();
	setMatchFields(match);
	setGroupby(groupBy);
	setSortBy(sortField);
	setLimits(limits);	
}

private void init(){
	this.setCollection(GsaConstants.DB_C_R_CAR);
	this.setDescription("Summary of Car reservation");
	
	this.setIndexNames(CarConstants.INDEX_CAR_COST_SUMMARY_MEASURE);
	
	//TODO refactor to automatically find factory here
	
	//please make sure the indicator factory is right  
	for (String indexName: this.getIndexNames()){
		this.addIndicator(indexName, CarIndicatorFactory.getInstance(indexName));
	}

}
	
	
}
