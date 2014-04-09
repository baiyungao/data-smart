package com.washingtongt.data.model.gsa.travelcard.air;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.GsaConstants;

public class CardAirSummaryMeasure extends Measurement {

public CardAirSummaryMeasure() {
		
		init();
	}


public CardAirSummaryMeasure(BasicDBObject match,String groupBy, BasicDBObject sortField,int limits){
	init();
	setMatchFields(match);
	setGroupby(groupBy);
	setSortBy(sortField);
	setLimits(limits);	
}

private void init(){
	this.setCollection(GsaConstants.DB_TC_AIR);
	this.setDescription("Summary of Travel Card Air");
	
	this.setIndexNames(CardAirConstants.INDEX_TC_AIR_SUMMARY_MEASURE);
	
	//TODO refactor to automatically find factory here
	
	//please make sure the indicator factory is right  
	for (String indexName: this.getIndexNames()){
		this.addIndicator(indexName, CardAirIndicatorFactory.getInstance(indexName));
	}

}
	
	
}
