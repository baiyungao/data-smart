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

public class HotelUtilTest {
	static final Logger log = Logger.getLogger(HotelUtilTest.class);
	
	public static void main(String[] args){
		
		
		log.debug("start air ticketing");
		
		DB gsaDB = MongoUtil.getMongoDB(GsaConstants.DB_NAME);
		DBCollection coll = gsaDB.getCollection(GsaConstants.DB_C_R_HOTEL);
		//Date start = DateUtils.getStartTime(2012, 2);
		//Date end = DateUtils.getStartTime(2012, 3);
		//BasicDBObject timeRange = new BasicDBObject("$gte",start ).append("$lt",end);
		//BasicDBObject match = new BasicDBObject("Date_Depart", timeRange);
		//price: { $lt: 9.95 } }
		//BasicDBObject priceCrietia = new BasicDBObject("$gt", 0);
		
		BasicDBObject match = null;  // new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
		DBObject myDoc = coll.findOne(match);
		log.debug("find one:" + myDoc);
		
		HotelProfileModel hModel = new HotelProfileModel(null);
		TableModel tModel = hModel.getSerialListTable(HotelConstants.INDEX_HOTEL_COST_SUMMARY_MEASURE, HotelConstants.IDT_T_CHARGE);
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
