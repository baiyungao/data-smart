package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.travelcard.air.CardAirConstants;
import com.washingtongt.data.model.gsa.travelcard.air.CardAirProfileModel;
import com.washingtongt.ui.model.TableModel;

@ManagedBean(name="caController")
@SessionScoped
public class CardAirController extends WebController {
	static final Logger log = Logger.getLogger(AirReservationController.class);
	private CardAirProfileModel cardAirProfileModel;
	
	public CardAirProfileModel getCardAirProfileModel() {
		return cardAirProfileModel;
	}
	
	public CardAirController(){
		this.init();
	}

	private void init(){
		Model model = dataModelMap.findModel("ALL", "CardAirProfileModel");
		if (model != null){
			this.cardAirProfileModel = (CardAirProfileModel) model;
		}else {
			//BasicDBObject priceCrietia = new BasicDBObject("$gt", 0);
			
			BasicDBObject match =  null; //new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
			this.cardAirProfileModel = new CardAirProfileModel(match);
			this.cardAirProfileModel.populate();
			dataModelMap.load("ALL",  cardAirProfileModel);
		}
	}		
	

	public TableModel getYearSummaryModel(){
		return this.cardAirProfileModel.getSerialListTable(CardAirConstants.INDEX_TC_AIR_SUMMARY_MEASURE, CardAirConstants.IDT_T_AMOUNT);
	}


}
