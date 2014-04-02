package com.washingtongt.data.model.gsa;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.washingtongt.data.model.AggregationOperator;
import com.washingtongt.data.model.Indicator;

public class GSAIndicatorFactory {
	
	public static String IDT_TOTAL_EXPENSE = "Total_Expenses";
	public static String IDT_AIR_EXPENSE = "Common_Carrier_AIR_TRAIN";
	public static String IDT_CAR_RENTAL_EXPENSE = "Rental_Car";
	public static String IDT_LODGING_EXPENSE = "Lodging";
	public static String IDT_MEALS_EXPENSE = "Meals_Incidentals";
	public static String IDT_MISC_EXPENSE = "Misc_Expenses";
	
	public static String IDT_DAYS_OF_TRIP = "Days_Trip";
	
	public static String IDT_DATE_DEPARTURE = "Date_Depart";
	public static String IDT_DATE_RETURN = "Date_Return";
	
	public static String IDT_TRIP_CT = "Trip_Counts";
	public static String IDT_ID = "_id";
	
	public static String IDT_FY = "FY";
	
	/*
	public static Indicator sum_total_expense;
	public static Indicator sum_air_expense;
	public static Indicator sum_car_expense;
	public static Indicator sum_meals_expense;
	public static Indicator sum_lodging_expense;
	public static Indicator sum_misc_expense;
	
	public static Indicator avg_total_expense;
	
	public static Indicator counts; 
	
	static{
		//create indicator here
		sum_total_expense = new Indicator(AggregationOperator.SUM , IDT_TOTAL_EXPENSE, "total expense");	
		
		sum_air_expense = new Indicator(AggregationOperator.SUM , IDT_AIR_EXPENSE, "Air Train");
		sum_car_expense = new Indicator(AggregationOperator.SUM , IDT_CAR_RENTAL_EXPENSE, "Car Rental");
		sum_meals_expense = new Indicator(AggregationOperator.SUM , IDT_MEALS_EXPENSE, "Meals");
		sum_lodging_expense = new Indicator(AggregationOperator.SUM , IDT_LODGING_EXPENSE, "Lodging");
		sum_misc_expense = new Indicator(AggregationOperator.SUM , IDT_MISC_EXPENSE, "Misc");
		
	}
	*/
	
	public static Indicator getInstance(String type){
	  switch (type){
	  case "Total_Expenses":
		return new Indicator(AggregationOperator.SUM , IDT_TOTAL_EXPENSE, "total expense");
	
	  case "Common_Carrier_AIR_TRAIN":	
		return  new Indicator(AggregationOperator.SUM , IDT_AIR_EXPENSE, "Air Train");
		
	  case "Rental_Car":
		  return  new Indicator(AggregationOperator.SUM , IDT_CAR_RENTAL_EXPENSE, "Car Rental");
	
	  case "Meals_Incidentals":  
		return  new Indicator(AggregationOperator.SUM , IDT_MEALS_EXPENSE, "Meals");
		
	  case "Lodging":	
		return new Indicator(AggregationOperator.SUM , IDT_LODGING_EXPENSE, "Lodging");
	  
	  case "Misc_Expenses":
		  return new Indicator(AggregationOperator.SUM , IDT_MISC_EXPENSE, "Misc");
	  
	  case "Days_Trip":
		  Indicator ind = new Indicator(AggregationOperator.SUM , IDT_DAYS_OF_TRIP, "Travel Days"); 
		  
			String[] timeDiff = new String[2]; 
			timeDiff[0] =  "$Date_Return";
			timeDiff[1] = "$Date_Depart";
			DBObject time = new BasicDBObject("$subtract",timeDiff );
			
			Object[] dayPar = new Object[2];
			dayPar[0] = time;
			dayPar[1] = 24*60*60*1000;
			BasicDBObject dayDiff = new BasicDBObject("$divide",dayPar );
			
			Object[] dayOff = new Object[2];
			dayOff[0] = dayDiff;
			dayOff[1] = 1;
			
			BasicDBObject projectParameter = new BasicDBObject("$add",dayOff);
		  //"{$add: [ { $divide: [ { $subtract: [ \"$Date_Return\", \"$Date_Depart\" ] }, 86400000 ] }, 1 ] }"
		  ind.setParameter(projectParameter);
		  return ind;
	 
	  case "Trip_Counts":
		  return new Indicator(AggregationOperator.COUNT , IDT_ID, "Trip Counts");	  
	  }
  
	  return null;
	}
	
	
	
}
