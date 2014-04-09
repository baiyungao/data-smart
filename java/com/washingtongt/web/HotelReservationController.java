package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.reservation.AirConstants;
import com.washingtongt.data.model.gsa.reservation.AirProfileModel;
import com.washingtongt.data.model.gsa.reservation.HotelConstants;
import com.washingtongt.data.model.gsa.reservation.HotelProfileModel;
import com.washingtongt.ui.model.TableModel;

@ManagedBean(name="hrController")
@SessionScoped
public class HotelReservationController extends WebController {
	static final Logger log = Logger.getLogger(AirReservationController.class);
	private HotelProfileModel hotelProfileModel;
	
	public HotelProfileModel getHotelProfileModel() {
		return hotelProfileModel;
	}
	
	public HotelReservationController(){
		this.init();
	}

	private void init(){
		Model model = dataModelMap.findModel("ALL", "HotelProfileModel");
		if (model != null){
			this.hotelProfileModel = (HotelProfileModel) model;
		}else {
			//BasicDBObject priceCrietia = new BasicDBObject("$gt", 0);
			
			BasicDBObject match =  null; //new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
			this.hotelProfileModel = new HotelProfileModel(match);
			this.hotelProfileModel.populate();
			dataModelMap.load("ALL",  hotelProfileModel);
		}
	}		
	

	public TableModel getYearSummaryModel(){
		return this.hotelProfileModel.getSerialListTable(HotelConstants.INDEX_HOTEL_COST_SUMMARY_MEASURE, HotelConstants.IDT_T_CHARGE);
	}


}
