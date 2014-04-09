package com.washingtongt.data.model.gsa.travelcard.lodging;


import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.ui.model.TableModel;

public class CardLodgingUtilTest {
	static final Logger log = Logger.getLogger(CardLodgingUtilTest.class);
	
	public static void main(String[] args){
		
		
		log.debug("start air ticketing");
		
		DB gsaDB = MongoUtil.getMongoDB(GsaConstants.DB_NAME);
		DBCollection coll = gsaDB.getCollection(GsaConstants.DB_TC_CAR);
		
		BasicDBObject match = null;  // new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
		DBObject myDoc = coll.findOne(match);
		log.debug("find one:" + myDoc);
		
		
		CardLodgingProfileModel hModel = new CardLodgingProfileModel(null);
		TableModel tModel = hModel.getSerialListTable(CardLodgingConstants.INDEX_TC_AIR_SUMMARY_MEASURE, CardLodgingConstants.IDT_T_AMOUNT);
		log.debug("Summary by years:" + tModel.getContents());	
		hModel.populate();
		TableModel cModel = hModel.getSummaryByCity();
		log.debug("Summary by Chain:" + cModel.getContents());
		
		LinePlusBarChartModel lModel = hModel.getSummaryChartByCity();
		log.debug("Summary  Line Chart by CITY:" + lModel);
		
		TableModel ciModel = hModel.getSummaryByBureau();
		log.debug("Summary by Bureau:" + ciModel.getContents());
		
		lModel = hModel.getSummaryChartByBureau();
		log.debug("Summary  Line Chart by Bureau:" + lModel);
	
	}

}
