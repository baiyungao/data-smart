package com.washingtongt.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.phoid.util.VariableStore;

import com.washingtongt.access.AccessAuthManager;
import com.washingtongt.access.AccessToken;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.reservation.AirConstants;
import com.washingtongt.data.model.gsa.reservation.CarConstants;
import com.washingtongt.data.model.gsa.reservation.HotelConstants;
import com.washingtongt.data.model.gsa.travelcard.air.CardAirConstants;
import com.washingtongt.data.model.gsa.travelcard.car.CardCarConstants;
import com.washingtongt.data.model.gsa.travelcard.lodging.CardLodgingConstants;

public class WebController {

	protected String requestItem;
	private String paraName;
	private String paraValue;
	protected UILabelMap labelMap = new UILabelMap();
	protected DataModelMap dataModelMap = DataModelMap.getDefault();

	public WebController() {
		super();
	}

	public String getHello() {
		return "I will win";
	}
	
	public AccessToken getAuthToken(){
		HttpSession session = VariableStore.getRequest().getSession(true);
		AccessToken token = (AccessToken)session.getAttribute(AccessAuthManager.TOKEN_STRING);
		
		return token;
	}
	
	static private Map<String,Boolean> currencyFields = new HashMap<String,Boolean>();
	
	static {
		currencyFields.put(GsaConstants.IDT_AIR_EXPENSE,true);
		currencyFields.put(GsaConstants.IDT_CAR_RENTAL_EXPENSE,true);
		currencyFields.put(GsaConstants.IDT_OTHER_EXPENSE,true);
		currencyFields.put(GsaConstants.IDT_LODGING_EXPENSE,true);
		currencyFields.put(GsaConstants.IDT_MISC_EXPENSE,true);
		currencyFields.put(GsaConstants.IDT_MEALS_EXPENSE,true);
		currencyFields.put(GsaConstants.IDT_TOTAL_EXPENSE,true);
		
		currencyFields.put(CardAirConstants.IDT_T_AMOUNT,true);
		currencyFields.put(CardCarConstants.IDT_T_AMOUNT,true);
		currencyFields.put(CardLodgingConstants.IDT_T_AMOUNT,true);
		
		currencyFields.put(CarConstants.IDT_T_CHARGE,true);
		currencyFields.put(CarConstants.IDT_AV_RATE,true);
		
		currencyFields.put(AirConstants.IDT_T_AMOUNT,true);
		currencyFields.put(AirConstants.IDT_AVG_PRICE,true);
		
		currencyFields.put(HotelConstants.IDT_T_CHARGE,true);
		currencyFields.put(HotelConstants.IDT_AV_RATE,true);
		
		currencyFields.put("2011 Total_Expenses",true);
		currencyFields.put("2012 Total_Expenses",true);
		currencyFields.put("2013 Total_Expenses",true);
		
	}
	
	
	public Map<String, Boolean> getCurrencyFields(){
		return currencyFields;
	}

	public String getParaName() {
		return paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}
	
	

}