package com.washingtongt.web;
/*
 * Web Controller for Air Reservation Part
 */
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.reservation.AirConstants;
import com.washingtongt.data.model.gsa.reservation.AirProfileModel;
import com.washingtongt.ui.model.TableModel;


@ManagedBean(name="arController")
@SessionScoped
public class AirReservationController extends WebController {
	static final Logger log = Logger.getLogger(AirReservationController.class);
	private static String googleMapApi = "https://maps.googleapis.com/maps/api/js?v=3.15&key=AIzaSyCOLt4_2Um2lZjNBe0u4mNwiG6Gz53HNgM&sensor=false";
	
	private AirProfileModel airProfileModel = null;
	
	public AirReservationController(){
		this.initForAirReservation();
	}

	private void initForAirReservation(){
		Model model = dataModelMap.findModel("ALL", "AirProfileModel");
		if (model != null){
			this.airProfileModel = (AirProfileModel) model;
		}else {
			BasicDBObject priceCrietia = new BasicDBObject("$gt", 0);
			
			BasicDBObject match = new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
			this.airProfileModel = new AirProfileModel(match);
			this.airProfileModel.populate();
			dataModelMap.load("ALL",  airProfileModel);
		}
	}	
	
	public AirProfileModel getAirProfileModel() {
		return airProfileModel;
	}

	public TableModel getYearSummaryModel(){
		return this.airProfileModel.getSerialListTable(AirConstants.INDEX_AIR_COST_SUMMARY_MEASURE, AirConstants.IDT_T_AMOUNT);
	}

	public String getGoogleMapApi() {
		return googleMapApi;
	}

	
	
}
