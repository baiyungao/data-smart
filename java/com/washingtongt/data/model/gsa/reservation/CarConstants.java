package com.washingtongt.data.model.gsa.reservation;

public class CarConstants {
	
	/*
	 * GDS_Record_Locator,Reservation_Date,Car_Pick_Up_Date,Car_Drop_Off_Date,Car_Chain_Code,
	 * Car_Chain_Name,Car_Pick_Up_City,Car_Pick_Up_State,Car_Drop_Off_City,Car_Drop_Off_State,
	 * Car_Rental_Days,Car_Daily_Rate,Car_Category,Online_Indicator
	
	*/
	
	public static String IDT_R_DATE = "Reservation_Date";
	public static String IDT_PU_DATE = "Car_Pick_Up_Date";
	public static String IDT_DO_DATE = "Car_Drop_Off_Date";
	
	public static String IDT_C_CODE = "Car_Chain_Code";
	public static String IDT_C_NAME = "Car_Chain_Name";
	public static String IDT_C_PU_CITY = "Car_Pick_Up_City";
	public static String IDT_C_PU_STATE = "Car_Pick_Up_State";
	public static String IDT_C_DO_CITY = "Car_Drop_Off_City";
	public static String IDT_C_DO_STATE = "Car_Drop_Off_State";
	
	public static String IDT_C_R_DAYS = "Car_Rental_Days";
	public static String IDT_C_RATE = "Car_Daily_Rate";
	public static String IDT_C_R_ONLINE = "Online_Indicator";	
	
	// computing field
	
	public static String IDT_C_COUNTS = "Counts";
	public static String IDT_AV_RATE = "Avg Rate";
	public static String IDT_AV_DAYS = "Avg Days";
	public static String IDT_R_DAYS = "Res Days";
	public static String IDT_T_CHARGE = "Total_Charge";		
	
	public static final String[] INDEX_CAR_COST_SUMMARY_MEASURE = 
		{ IDT_C_COUNTS,IDT_T_CHARGE,IDT_AV_DAYS, IDT_AV_RATE, IDT_C_R_DAYS, IDT_R_DAYS	};	
	
	public static final String[] INDEX_CAR_SUMMARY_CHART = 
		{ 	IDT_T_CHARGE,
			IDT_C_COUNTS
		};	

}
