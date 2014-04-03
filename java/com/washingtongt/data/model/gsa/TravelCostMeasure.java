package com.washingtongt.data.model.gsa;

import com.washingtongt.data.model.Measurement;

public class TravelCostMeasure extends Measurement {
	
	
	static public TravelCostMeasure benchMarkFY2011;
	static public TravelCostMeasure benchMarkFY2012;
	static public TravelCostMeasure benchMarkFY2013;
	
	static public TravelCostMeasure benchMarkFY2013OfficeSample;
	
	static
	{
		benchMarkFY2011 = new TravelCostMeasure();
		benchMarkFY2011.setName("FY2011");
		//benchMarkFY2011.addMatch("FY", "2011");
		
		benchMarkFY2012 = new TravelCostMeasure();
		benchMarkFY2012.setName("FY2012");
		//benchMarkFY2012.addMatch("FY", "2012");
		
		benchMarkFY2013 = new TravelCostMeasure();
		benchMarkFY2013.setName("FY2013");
		//benchMarkFY2013.addMatch("FY", "2013");
				
		benchMarkFY2013OfficeSample = new TravelCostMeasure();
		//benchMarkFY2013OfficeSample.addMatch("FY", "2013");
		//benchMarkFY2013OfficeSample.addMatch("Organization", "R9-Pacific Rim-SFO, CA");
	}
	
	public TravelCostMeasure(){
		
		
		this.setDescription("Sum total expense and individual expense driver");
		//this.addIndicator("Total Expenses", GSAIndicatorFactory.sum_total_expense);
		//this.addIndicator("Air/Train Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_AIR_EXPENSE));
		//this.addIndicator("Lodging Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_LODGING_EXPENSE));
		//this.addIndicator("Meals Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_MEALS_EXPENSE));
		//this.addIndicator("Car Rental Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_CAR_RENTAL_EXPENSE));
		//this.addIndicator("Misc Expenses", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_MISC_EXPENSE));
		this.addIndicator("Travel Days", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_DAYS_OF_TRIP));
		this.addIndicator("Trip Counts", GSAIndicatorFactory.getInstance(GSAIndicatorFactory.IDT_TRIP_CT));
		this.setGroupby(null);
	}
}
