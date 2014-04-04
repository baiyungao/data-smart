package com.washingtongt.data.model.gsa;

public class GsaConstants {
	
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
		
	static final public String ORG_LEVEL_ORGANIZATION = "Organization";
	static final public String ORG_LEVEL_Office = "Office";
	
	static final public String FIELD_LOCATION = "First_Destination";	
	
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
		  	IDT_TRIP_CT
		};

}
