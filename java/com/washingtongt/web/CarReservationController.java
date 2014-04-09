package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.travelcard.air.CardAirConstants;
import com.washingtongt.data.model.gsa.reservation.CarProfileModel;
import com.washingtongt.ui.model.TableModel;

@ManagedBean(name="caController")
@SessionScoped
public class CarReservationController extends WebController {
	static final Logger log = Logger.getLogger(AirReservationController.class);
	private CarProfileModel cardAirProfileModel;
	
	public CarProfileModel getCardAirProfileModel() {
		return cardAirProfileModel;
	}
	
	public CarReservationController(){
		this.init();
	}

	private void init(){
		Model model = dataModelMap.findModel("ALL", "CardAirProfileModel");
		if (model != null){
			this.cardAirProfileModel = (CarProfileModel) model;
		}else {
			
			BasicDBObject match =  null; //new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
			this.cardAirProfileModel = new CarProfileModel(match);
			this.cardAirProfileModel.populate();
			dataModelMap.load("ALL",  cardAirProfileModel);
		}
	}		
	

	public TableModel getYearSummaryModel(){
		return this.cardAirProfileModel.getSerialListTable(CardAirConstants.INDEX_TC_AIR_SUMMARY_MEASURE, CardAirConstants.IDT_T_AMOUNT);
	}


}
