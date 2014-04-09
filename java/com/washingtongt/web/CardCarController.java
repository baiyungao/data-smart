package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.travelcard.car.CardCarConstants;
import com.washingtongt.data.model.gsa.travelcard.car.CardCarProfileModel;
import com.washingtongt.ui.model.TableModel;

@ManagedBean(name="ccController")
@SessionScoped
public class CardCarController extends WebController {
	static final Logger log = Logger.getLogger(AirReservationController.class);
	private CardCarProfileModel cardCarProfileModel;
	
	public CardCarProfileModel getCardCarProfileModel() {
		return cardCarProfileModel;
	}
	
	public CardCarController(){
		this.init();
	}

	private void init(){
		Model model = dataModelMap.findModel("ALL", "CardCarProfileModel");
		if (model != null){
			this.cardCarProfileModel = (CardCarProfileModel) model;
		}else {
			//BasicDBObject priceCrietia = new BasicDBObject("$gt", 0);
			
			BasicDBObject match =  null; //new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
			this.cardCarProfileModel = new CardCarProfileModel(match);
			this.cardCarProfileModel.populate();
			dataModelMap.load("ALL",  cardCarProfileModel);
		}
	}		
	

	public TableModel getYearSummaryModel(){
		return this.cardCarProfileModel.getSerialListTable(CardCarConstants.INDEX_TC_AIR_SUMMARY_MEASURE, CardCarConstants.IDT_T_AMOUNT);
	}


}
