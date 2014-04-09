package com.washingtongt.data.model.gsa.travelcard.lodging;

import org.apache.log4j.Logger;

import com.washingtongt.data.model.AggregationOperator;
import com.washingtongt.data.model.Indicator;

public class CardLodgingIndicatorFactory {
	static final Logger log = Logger.getLogger(CardLodgingIndicatorFactory.class);
	public static Indicator getInstance(String type){
		  switch (type){
		  case "TRANSACTION_AMOUNT":
			  return  new Indicator(AggregationOperator.SUM , CardLodgingConstants.IDT_T_AMOUNT);
		
		 
		  case "Counts":
			  return new Indicator(AggregationOperator.COUNT , CardLodgingConstants.IDT_C_COUNTS);
		  }
		  
		  log.error("cannot creat indicator for this: " + type);
		  return null;
		}	

}
