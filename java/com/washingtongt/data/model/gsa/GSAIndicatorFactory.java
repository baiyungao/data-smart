package com.washingtongt.data.model.gsa;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.washingtongt.data.model.AggregationOperator;
import com.washingtongt.data.model.Indicator;

public class GSAIndicatorFactory {
	

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
		return new Indicator(AggregationOperator.SUM , GsaConstants.IDT_TOTAL_EXPENSE, GsaConstants.IDT_TOTAL_EXPENSE);
	
	  case "Common_Carrier_AIR_TRAIN":	
		return  new Indicator(AggregationOperator.SUM , GsaConstants.IDT_AIR_EXPENSE, GsaConstants.IDT_AIR_EXPENSE);
		
	  case "Rental_Car":
		  return  new Indicator(AggregationOperator.SUM , GsaConstants.IDT_CAR_RENTAL_EXPENSE, GsaConstants.IDT_CAR_RENTAL_EXPENSE);
	
	  case "Meals_Incidentals":  
		return  new Indicator(AggregationOperator.SUM , GsaConstants.IDT_MEALS_EXPENSE,  GsaConstants.IDT_MEALS_EXPENSE);
		
	  case "Lodging":	
		return new Indicator(AggregationOperator.SUM , GsaConstants.IDT_LODGING_EXPENSE, GsaConstants.IDT_LODGING_EXPENSE);
	  
	  case "Misc_Expenses":
		  return new Indicator(AggregationOperator.SUM , GsaConstants.IDT_MISC_EXPENSE, GsaConstants.IDT_MISC_EXPENSE);
	  
	  case "Days_Trip":
		  Indicator ind = new Indicator(AggregationOperator.SUM , GsaConstants.IDT_DAYS_OF_TRIP,  GsaConstants.IDT_DAYS_OF_TRIP); 
		  
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
		  
	  case "Other":
		  Indicator indOther = new Indicator(AggregationOperator.SUM , GsaConstants.IDT_OTHER_EXPENSE,  GsaConstants.IDT_OTHER_EXPENSE); 
		  
			String[] others = new String[3]; 
			others[0] =  "$" + GsaConstants.IDT_POV;
			others[1] =  "$" + GsaConstants.IDT_TMC;
			others[2] =  "$" + GsaConstants.IDT_VOUCHER_FEE;
		
			BasicDBObject otherProjectParameter = new BasicDBObject("$add",others);
			indOther.setParameter(otherProjectParameter);
		  return indOther;		  
	 
	  case "Trip_Counts":
		  return new Indicator(AggregationOperator.COUNT , GsaConstants.IDT_TRIP_CT, GsaConstants.IDT_TRIP_CT);
		  
		  
	  case "Local_trans":
		  return new Indicator(AggregationOperator.COUNT , GsaConstants.IDT_LOCAL_TRANS, GsaConstants.IDT_LOCAL_TRANS);	  
	  
	  }
  
	  return null;
	}
	
	
	
}
