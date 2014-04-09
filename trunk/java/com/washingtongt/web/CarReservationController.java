package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.reservation.CarConstants;
import com.washingtongt.data.model.gsa.reservation.CarProfileModel;
import com.washingtongt.ui.model.TableModel;

@ManagedBean(name="crController")
@SessionScoped
public class CarReservationController extends WebController {
	static final Logger log = Logger.getLogger(AirReservationController.class);
	private CarProfileModel carProfileModel;
	
	public CarProfileModel getCarProfileModel() {
		return carProfileModel;
	}
	
	public CarReservationController(){
		this.init();
	}

	private void init(){
		Model model = dataModelMap.findModel("ALL", "CarProfileModel");
		if (model != null){
			this.carProfileModel = (CarProfileModel) model;
		}else {
			//BasicDBObject priceCrietia = new BasicDBObject("$gt", 0);
			
			BasicDBObject match =  null; //new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
			this.carProfileModel = new CarProfileModel(match);
			this.carProfileModel.populate();
			dataModelMap.load("ALL",  carProfileModel);
		}
	}		
	

	public TableModel getYearSummaryModel(){
		return this.carProfileModel.getSerialListTable(CarConstants.INDEX_CAR_COST_SUMMARY_MEASURE, CarConstants.IDT_T_CHARGE);
	}


}
