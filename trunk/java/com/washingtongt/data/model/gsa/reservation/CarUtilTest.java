package com.washingtongt.data.model.gsa.reservation;


import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.ui.model.TableModel;

public class CarUtilTest {
	static final Logger log = Logger.getLogger(CarUtilTest.class);
	
	public static void main(String[] args){
		
		
		log.debug("start air ticketing");
		
		DB gsaDB = MongoUtil.getMongoDB(GsaConstants.DB_NAME);
		DBCollection coll = gsaDB.getCollection(GsaConstants.DB_C_R_CAR);
		
		BasicDBObject match = null;  // new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
		DBObject myDoc = coll.findOne(match);
		log.debug("find one:" + myDoc);
		
		
		CarProfileModel hModel = new CarProfileModel(null);
		TableModel tModel = hModel.getSerialListTable(CarConstants.INDEX_CAR_COST_SUMMARY_MEASURE, CarConstants.IDT_T_CHARGE);
		log.debug("Summary by years:" + tModel.getContents());	
		hModel.populate();
		TableModel cModel = hModel.getSummaryByChain();
		log.debug("Summary by Chain:" + cModel.getContents());
		
		LinePlusBarChartModel lModel = hModel.getSummaryChartByChain();
		log.debug("Summary  Line Chart by Chain:" + lModel);
		
		TableModel ciModel = hModel.getSummaryByCity();
		log.debug("Summary by City:" + ciModel.getContents());
		
		lModel = hModel.getSummaryChartByCity();
		log.debug("Summary  Line Chart by City:" + lModel);
	
	}

}
