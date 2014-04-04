package com.washingtongt.data.model.gsa;

import com.washingtongt.data.model.Measurement;

public class TravelSumaryMeasure extends Measurement {

	
public TravelSumaryMeasure(){
	
		this.setDescription("Travel Summary Measure: total expense and counts");
		this.setIndexNames(GsaConstants.INDEX_TRAVEL_SUMMARY_MEASURE);
		
		for (String indexName: this.getIndexNames()){
			this.addIndicator(indexName, GSAIndicatorFactory.getInstance(indexName));
		}
	}	
}
