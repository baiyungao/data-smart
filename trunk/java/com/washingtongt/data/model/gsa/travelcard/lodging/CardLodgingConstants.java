package com.washingtongt.data.model.gsa.travelcard.lodging;

public class CardLodgingConstants {
	/*
	MERCHANT_CITY,MERCHANT_STATE,MERCHANT_ZIP,TRANSPOSTINGDATEID,TRANSACTION_AMOUNT,
	SIC_MCC_CODE,MERCHANT_GROUP,AGENCY_NAME,AGENCY_BUREAU_NAME,CHECKINDATEID,CHECKOUTDATEID,
	NUMBER_OF_ROOM_NIGHTS,TOTAL_ROOM_TAX,DAILY_ROOM_RATE,TAX_AMOUNT,EXTRA_CHARGES,
	LOCAL_TAX_AMOUNT,LOCAL_TAX_INDICATOR


	 */

	public static String IDT_P_DATE = "TRANSPOSTINGDATEID";
	public static String IDT_T_AMOUNT = "TRANSACTION_AMOUNT";
	
	public static String IDT_MERCHANT_CITY= "MERCHANT_CITY";
	public static String IDT_AGENCY_BUREAU_NAME = "AGENCY_BUREAU_NAME";
	public static String IDT_FY = "FY";
	
	public static String IDT_C_COUNTS = "Counts";
	
	
	public static final String[] INDEX_TC_AIR_SUMMARY_MEASURE = 
		{ IDT_T_AMOUNT,IDT_C_COUNTS};	
	}