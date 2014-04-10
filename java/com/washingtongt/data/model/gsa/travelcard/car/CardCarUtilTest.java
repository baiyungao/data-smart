package com.washingtongt.data.model.gsa.travelcard.car;


import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.travelcard.lodging.CardLodgingProfileModel;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.ui.model.TableModel;

public class CardCarUtilTest {
	static final Logger log = Logger.getLogger(CardCarUtilTest.class);
	
	public static void main(String[] args){
		
		
		String a = "good";
		String b = "good";
		log.debug(a == b);
		
		/*
		log.debug("start air ticketing");
		
		DB gsaDB = MongoUtil.getMongoDB(GsaConstants.DB_NAME);
		DBCollection coll = gsaDB.getCollection(GsaConstants.DB_TC_LODGING);
		
		BasicDBObject match = null;  // new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
		DBObject myDoc = coll.findOne(match);
		log.debug("find one:" + myDoc);
		
		
		CardCarProfileModel hModel = new CardCarProfileModel(null);
		TableModel tModel = hModel.getSerialListTable(CardCarConstants.INDEX_TC_AIR_SUMMARY_MEASURE, CardCarConstants.IDT_T_AMOUNT);
		log.debug("Summary by years:" + tModel.getContents());	
		
		/*hModel.populate();
		TableModel cModel = hModel.getSummaryByCity();
		log.debug("Summary by City:" + cModel.getContents());
		
		LinePlusBarChartModel lModel = hModel.getSummaryChartByCity();
		log.debug("Summary  Line Chart by CITY:" + lModel);
		
		TableModel ciModel = hModel.getSummaryByBureau();
		log.debug("Summary by Bureau:" + ciModel.getContents());
		
		lModel = hModel.getSummaryChartByBureau();
		log.debug("Summary  Line Chart by Bureau:" + lModel);
		*/
	}

}
