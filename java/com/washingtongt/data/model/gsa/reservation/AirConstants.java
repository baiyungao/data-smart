package com.washingtongt.data.model.gsa.reservation;

public class AirConstants {
	
	public static String IDT_R_DATE = "Reservation_Date";
	public static String IDT_I_DATE = "Ticket_Issue_Date";
	public static String IDT_D_DATE = "Trip_Departure_Date";
	public static String IDT_A_NAME = "Airline_Name";
	public static String IDT_C_CLASS = "Cabin_Class";
	public static String IDT_D_I = "Domestic_International_Indicator";
	public static String IDT_O_CODE = "Origin_Airport_Code";
	public static String IDT_D_CODE = "Destination_Airport_Code";
	
	public static String IDT_I_AMOUNT = "Invoice_Amount";
	public static String IDT_TAX = "Tax_Amount";
	public static String IDT_T_AMOUNT = "Total_Amount";
	public static String IDT_MILEAGE = "Mileage";
	
	public static String IDT_REFUND = "Refund_Indicator";

	public static String IDT_ONLINE = "Online_Indicator";
	public static String IDT_F_CATEGORY = "Fare_Category";
	
	public static String IDT_R_DAYS = "Days Ahead";
	public static String IDT_T_COUNTS = "Counts";

	
	public static final String[] INDEX_AIR_COST_SUMMARY_MEASURE = 
		{ IDT_I_AMOUNT, IDT_T_COUNTS,IDT_TAX,IDT_T_AMOUNT,IDT_MILEAGE, IDT_R_DAYS	};		

}
