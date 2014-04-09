package com.washingtongt.data.model.gsa.reservation;

public class HotelConstants {
	
	
	/*
	 * GDS_Record_Locator,Reservation_Date,Check_In_Date,Check_Out_Date,
	 * Hotel_Chain_Code,Hotel_Chain_Name,Hotel_Property_Name,Hotel_Address,
	 * Hotel_City_Name,Hotel_State,Hotel_Zip_Code,Hotel_Country_Code,
	 * Number_of_Room_Nights,Daily_Rate,Room_Rate_Code,Online_Indicator
	 */
	public static String IDT_R_DATE = "Reservation_Date";
	public static String IDT_CI_DATE = "Check_In_Date";
	public static String IDT_CO_DATE = "Check_Out_Date";
	
	public static String IDT_H_CODE = "Hotel_Chain_Code";
	public static String IDT_H_NAME = "Hotel_Chain_Name";
	public static String IDT_H_P_NAME = "Hotel_Property_Name";
	public static String IDT_H_P_ADDR = "Hotel_Address";
	public static String IDT_H_P_CITY = "Hotel_City_Name";
	
	public static String IDT_H_P_STATE = "Hotel_State";
	public static String IDT_H_P_ZIP = "Hotel_Zip_Code";
	public static String IDT_H_P_COUNTRY = "Hotel_Country_Code";
	
	public static String IDT_H_NIGHTS = "Number_of_Room_Nights";
	public static String IDT_H_R_RATE = "Daily_Rate";
	public static String IDT_H_R_CODE = "Room_Rate_Code";
	public static String IDT_R_ONLINE = "Online_Indicator";
	
	public static String IDT_R_DAYS = "Reservation_Days";
	public static String IDT_T_CHARGE = "Total_Charge";
	public static String IDT_AV_DAYS = "Avg Stay Days";
	public static String IDT_AV_RATE = "Avg Rate";
	public static String IDT_H_COUNTS = "Counts";
	
	public static final String[] INDEX_HOTEL_COST_SUMMARY_MEASURE = 
		{ IDT_H_COUNTS,IDT_T_CHARGE,IDT_AV_DAYS, IDT_AV_RATE, IDT_H_NIGHTS, IDT_R_DAYS	};	
	
	public static final String[] INDEX_HOTEL_SUMMARY_CHART = 
		{ 	IDT_T_CHARGE,
			IDT_H_COUNTS
		};	

}
