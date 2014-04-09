package com.washingtongt.data.model.gsa.reservation;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.washingtongt.data.model.AggregationOperator;
import com.washingtongt.data.model.Indicator;

public class CarIndicatorFactory {
	static final Logger log = Logger.getLogger(CarIndicatorFactory.class);
	public static Indicator getInstance(String type){
		  switch (type){
		  case "Total_Charge":
			Indicator t_ind = new Indicator(AggregationOperator.SUM , CarConstants.IDT_T_CHARGE);
			
			Object[] formu = new Object[2];
			formu[0] = "$" + CarConstants.IDT_C_R_DAYS;
			formu[1] = "$" + CarConstants.IDT_C_RATE;
			
			BasicDBObject projectParameter = new BasicDBObject("$multiply",formu);
		  //"{$add: [ { $divide: [ { $subtract: [ \"$IDT_R_DATE\", \"$IDT_D_DATE\" ] }, 86400000 ] }, 1 ] }"
			t_ind.setParameter(projectParameter);			
			
			return t_ind;
			
		
		  case "Car_Rental_Days":	
			return  new Indicator(AggregationOperator.SUM , CarConstants.IDT_C_R_DAYS);
			
		  case "Avg Rate":  
				return  new Indicator(AggregationOperator.AVG , CarConstants.IDT_C_RATE, CarConstants.IDT_AV_RATE);	
		
			
		  case "Avg Days":  
				return  new Indicator(AggregationOperator.AVG , CarConstants.IDT_C_R_DAYS, CarConstants.IDT_AV_DAYS);	
			
			
		  case "Res Days":
			  Indicator ind = new Indicator(AggregationOperator.AVG , CarConstants.IDT_R_DAYS); 
			  
				String[] timeDiff = new String[2]; 
				timeDiff[0] =  "$" + CarConstants.IDT_PU_DATE;  //check in date
				timeDiff[1] = "$" + CarConstants.IDT_R_DATE;    //Rsv. date
				DBObject time = new BasicDBObject("$subtract",timeDiff );
				
				Object[] dayPar = new Object[2];
				dayPar[0] = time;
				dayPar[1] = 24*60*60*1000;
				BasicDBObject dayDiff = new BasicDBObject("$divide",dayPar );
				
				Object[] dayOff = new Object[2];
				dayOff[0] = dayDiff;
				dayOff[1] = 1;
				
				BasicDBObject projectParameter1 = new BasicDBObject("$add",dayOff);
			  //"{$add: [ { $divide: [ { $subtract: [ \"$IDT_R_DATE\", \"$IDT_D_DATE\" ] }, 86400000 ] }, 1 ] }"
			  ind.setParameter(projectParameter1);
			  return ind;
		 
		  case "Counts":
			  return new Indicator(AggregationOperator.COUNT , CarConstants.IDT_C_COUNTS);
		  }
		  
		  log.error("cannot creat indicator for this: " + type);
		  return null;
		}	

}
