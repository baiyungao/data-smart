package com.washingtongt.data.model.gsa;

import com.washingtongt.data.model.Measurement;

public class TravelCostDriverMeasure extends Measurement{
	
public TravelCostDriverMeasure(){
		
		this.setCollection(GsaConstants.DB_C_VOURCHER);
		this.setDescription("Sum individual expense driver");
		
		this.setIndexNames(GsaConstants.INDEX_TRAVEL_COST_DRIVER_MEASURE);
		
		for (String indexName: this.getIndexNames()){
			this.addIndicator(indexName, GSAIndicatorFactory.getInstance(indexName));
		}
		
	}	

}
