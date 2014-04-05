package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.TripProfileModel;

@ManagedBean(name="controller")
@SessionScoped
public class ConsoleController {
	static final Logger log = Logger.getLogger(ConsoleController.class);
	private TripProfileModel overAllTripModel;  
	
	public ConsoleController(){
		overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
		overAllTripModel.populate(); 
	}
	
	public String getHello(){
		return "I will win";
	}

	public TripProfileModel getOverAllTripModel(){
		return this.overAllTripModel;
	}
	
}
