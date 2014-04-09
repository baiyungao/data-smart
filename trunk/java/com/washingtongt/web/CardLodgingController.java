package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.travelcard.lodging.CardLodgingConstants;
import com.washingtongt.data.model.gsa.travelcard.lodging.CardLodgingProfileModel;
import com.washingtongt.ui.model.TableModel;

@ManagedBean(name="clController")
@SessionScoped
public class CardLodgingController extends WebController {
	static final Logger log = Logger.getLogger(AirReservationController.class);
	private CardLodgingProfileModel cardLodgingProfileModel;
	
	public CardLodgingProfileModel getCardLodgingProfileModel() {
		return cardLodgingProfileModel;
	}
	
	public CardLodgingController(){
		this.init();
	}

	private void init(){
		Model model = dataModelMap.findModel("ALL", "CardLodgingProfileModel");
		if (model != null){
			this.cardLodgingProfileModel = (CardLodgingProfileModel) model;
		}else {
			//BasicDBObject priceCrietia = new BasicDBObject("$gt", 0);
			
			BasicDBObject match =  null; //new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
			this.cardLodgingProfileModel = new CardLodgingProfileModel(match);
			this.cardLodgingProfileModel.populate();
			dataModelMap.load("ALL",  cardLodgingProfileModel);
		}
	}		
	

	public TableModel getYearSummaryModel(){
		return this.cardLodgingProfileModel.getSerialListTable(CardLodgingConstants.INDEX_TC_AIR_SUMMARY_MEASURE, CardLodgingConstants.IDT_T_AMOUNT);
	}


}
