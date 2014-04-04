package com.washingtongt.data.model.gsa;

import com.washingtongt.data.model.Measurement;

public class TravelCostMeasure extends Measurement {
	


	public TravelCostMeasure(){
		
		
		this.setDescription("Sum total expense and individual expense driver");
		
		this.setIndexNames(GsaConstants.INDEX_TRAVEL_COST_MEASURE);
		
		for (String indexName: this.getIndexNames()){
			this.addIndicator(indexName, GSAIndicatorFactory.getInstance(indexName));
		}
		
		//this.addIndicator("Total Expenses", GSAIndicatorFactory.sum_total_expense);
		/*
		this.addIndicator("Air/Train Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_AIR_EXPENSE));
		this.addIndicator("Lodging Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_LODGING_EXPENSE));
		this.addIndicator("Meals Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_MEALS_EXPENSE));
		this.addIndicator("Car Rental Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_CAR_RENTAL_EXPENSE));
		this.addIndicator("Misc Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_MISC_EXPENSE));
		this.addIndicator("Travel Days", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_DAYS_OF_TRIP));
		this.addIndicator("Trip Counts", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_TRIP_CT));
		this.setGroupby(null);
		*/
	}
}
