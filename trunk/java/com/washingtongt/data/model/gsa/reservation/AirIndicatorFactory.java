package com.washingtongt.data.model.gsa.reservation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.washingtongt.data.model.AggregationOperator;
import com.washingtongt.data.model.Indicator;
import com.washingtongt.data.model.gsa.GsaConstants;

public class AirIndicatorFactory {
	
	public static Indicator getInstance(String type){
		  switch (type){
		  case "Invoice_Amount":
			return new Indicator(AggregationOperator.SUM , AirConstants.IDT_I_AMOUNT);
		
		  case "Tax_Amount":	
			return  new Indicator(AggregationOperator.SUM , AirConstants.IDT_TAX);
			
		  case "Total_Amount":
			  return  new Indicator(AggregationOperator.SUM , AirConstants.IDT_T_AMOUNT);
		
		  case "Mileage":  
			return  new Indicator(AggregationOperator.SUM , AirConstants.IDT_MILEAGE);
			
		  case "Avg Amount":  
				return  new Indicator(AggregationOperator.AVG , AirConstants.IDT_T_AMOUNT, AirConstants.IDT_AVG_PRICE);	
			
			
		  case "Avg Rsv Days":
			  Indicator ind = new Indicator(AggregationOperator.AVG , AirConstants.IDT_R_DAYS); 
			  
				String[] timeDiff = new String[2]; 
				timeDiff[0] =  "$" + AirConstants.IDT_D_DATE;
				timeDiff[1] = "$" + AirConstants.IDT_R_DATE;
				DBObject time = new BasicDBObject("$subtract",timeDiff );
				
				Object[] dayPar = new Object[2];
				dayPar[0] = time;
				dayPar[1] = 24*60*60*1000;
				BasicDBObject dayDiff = new BasicDBObject("$divide",dayPar );
				
				Object[] dayOff = new Object[2];
				dayOff[0] = dayDiff;
				dayOff[1] = 1;
				
				BasicDBObject projectParameter = new BasicDBObject("$add",dayOff);
			  //"{$add: [ { $divide: [ { $subtract: [ \"$IDT_R_DATE\", \"$IDT_D_DATE\" ] }, 86400000 ] }, 1 ] }"
			  ind.setParameter(projectParameter);
			  return ind;
		 
		  case "Counts":
			  return new Indicator(AggregationOperator.COUNT , AirConstants.IDT_T_COUNTS);
		  }
	  
		  return null;
		}	

}
