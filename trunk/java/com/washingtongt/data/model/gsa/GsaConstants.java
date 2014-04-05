package com.washingtongt.data.model.gsa;

import java.util.HashMap;
import java.util.Map;

public class GsaConstants {
	
	public static String IDT_TOTAL_EXPENSE = "Total_Expenses";
	public static String IDT_AIR_EXPENSE = "Common_Carrier_AIR_TRAIN";
	public static String IDT_CAR_RENTAL_EXPENSE = "Rental_Car";
	public static String IDT_LODGING_EXPENSE = "Lodging";
	public static String IDT_MEALS_EXPENSE = "Meals_Incidentals";
	public static String IDT_MISC_EXPENSE = "Misc_Expenses";
	
	public static String IDT_OTHER_EXPENSE = "Other";
	public static String IDT_DAYS_OF_TRIP = "Travel_Days";
	
	public static String IDT_DATE_DEPARTURE = "Date_Depart";
	public static String IDT_DATE_RETURN = "Date_Return";
	
	public static String IDT_TRIP_CT = "Trip_Counts";
	public static String IDT_ID = "_id";
	public static String IDT_LOCAL_TRANS = "Local_Trans";
	public static String IDT_POV = "POV";
	public static String IDT_TMC = "TMC_Fee";
	public static String IDT_VOUCHER_FEE = "Voucher_Fee";
	
	public static String IDT_FY = "FY";
	public static String IDT_PERFORMANCE = "Performance";
		
	static final public String ORG_LEVEL_ORGANIZATION = "Organization";
	static final public String ORG_LEVEL_Office = "Office";
	
	static final public String FIELD_LOCATION = "First_Destination";
	static public Map<String, String> labelMap;
	
	static{
		labelMap = new HashMap<String, String>();
		labelMap.put(IDT_AIR_EXPENSE, "Common_Carrier AIR_TRAIN");
		labelMap.put(IDT_MEALS_EXPENSE, "Meals Incidentals");
		/*
		labelMap.put(IDT_AIR_EXPENSE, IDT_AIR_EXPENSE);
		labelMap.put(IDT_CAR_RENTAL_EXPENSE, IDT_CAR_RENTAL_EXPENSE);
		labelMap.put(IDT_DATE_DEPARTURE, IDT_DATE_DEPARTURE);
		labelMap.put(IDT_DATE_RETURN, IDT_DATE_RETURN);
		labelMap.put(IDT_DAYS_OF_TRIP, IDT_DAYS_OF_TRIP);
		labelMap.put(IDT_FY, IDT_FY);
		labelMap.put(IDT_LOCAL_TRANS, IDT_LOCAL_TRANS);
		labelMap.put(IDT_LODGING_EXPENSE, IDT_LODGING_EXPENSE);
		labelMap.put(IDT_MEALS_EXPENSE, IDT_MEALS_EXPENSE);
		labelMap.put(IDT_MISC_EXPENSE, IDT_MISC_EXPENSE);
		labelMap.put(IDT_OTHER_EXPENSE, IDT_OTHER_EXPENSE);
		labelMap.put(IDT_TOTAL_EXPENSE, IDT_TOTAL_EXPENSE);
		*/
		
		
	}
	
	public static final String[] INDEX_TRAVEL_COST_DRIVER_MEASURE = 
		{ 	IDT_AIR_EXPENSE,
			IDT_MEALS_EXPENSE,
			IDT_LODGING_EXPENSE,
			IDT_CAR_RENTAL_EXPENSE,
			IDT_MISC_EXPENSE,
			IDT_OTHER_EXPENSE
			};	
	
	public static final String[] INDEX_TRAVEL_COST_MEASURE = 
		{ 	IDT_AIR_EXPENSE,
			IDT_MEALS_EXPENSE,
			IDT_LODGING_EXPENSE,
			IDT_CAR_RENTAL_EXPENSE,
			IDT_MISC_EXPENSE,
			IDT_DAYS_OF_TRIP,
			IDT_TRIP_CT
			};

	public static final String[] INDEX_TRAVEL_SUMMARY_MEASURE = 
		{ 	IDT_TOTAL_EXPENSE,
			IDT_DAYS_OF_TRIP,
		  	IDT_TRIP_CT
		};
	
	public static final String[] INDEX_TRAVEL_SUMMARY_CHART = 
		{ 	IDT_TOTAL_EXPENSE,
		  	IDT_TRIP_CT
		};	

	public static final String[] INDEX_TOTAL_COST_CHART_EX = 
		{ 	IDT_DAYS_OF_TRIP,
	  		IDT_TRIP_CT
		};	
	
}
