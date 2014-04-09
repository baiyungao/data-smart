package com.washingtongt.data.model.gsa.travelcard.car;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.GsaConstants;

public class CardCarSummaryMeasure extends Measurement {

public CardCarSummaryMeasure() {
		
		init();
	}


public CardCarSummaryMeasure(BasicDBObject match,String groupBy, BasicDBObject sortField,int limits){
	init();
	setMatchFields(match);
	setGroupby(groupBy);
	setSortBy(sortField);
	setLimits(limits);	
}

private void init(){
	this.setCollection(GsaConstants.DB_TC_CAR);
	this.setDescription("Summary of Travel Card Car");
	
	this.setIndexNames(CardCarConstants.INDEX_TC_AIR_SUMMARY_MEASURE);
	
	//TODO refactor to automatically find factory here
	
	//please make sure the indicator factory is right  
	for (String indexName: this.getIndexNames()){
		this.addIndicator(indexName, CardCarIndicatorFactory.getInstance(indexName));
	}

}
	
	
}
