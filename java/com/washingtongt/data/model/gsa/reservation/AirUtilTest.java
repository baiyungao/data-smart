package com.washingtongt.data.model.gsa.reservation;


import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.time.FiscalYear;
import com.washingtongt.ui.model.TableModel;

public class AirUtilTest {
	static final Logger log = Logger.getLogger(AirUtilTest.class);
	
	public static void main(String[] args){
		
		
		log.debug("start air ticketing");
		
		DB gsaDB = MongoUtil.getMongoDB(GsaConstants.DB_NAME);
		DBCollection coll = gsaDB.getCollection(GsaConstants.DB_C_R_AIR);
		//Date start = DateUtils.getStartTime(2012, 2);
		//Date end = DateUtils.getStartTime(2012, 3);
		//BasicDBObject timeRange = new BasicDBObject("$gte",start ).append("$lt",end);
		//BasicDBObject match = new BasicDBObject("Date_Depart", timeRange);
		//price: { $lt: 9.95 } }
		BasicDBObject priceCrietia = new BasicDBObject("$gt", 0);
		
		BasicDBObject match = new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
		DBObject myDoc = coll.findOne(match);
		log.debug("find one:" + myDoc);
		
		
		AirProfileModel model = new AirProfileModel(match);
		
		FiscalYear year = model.getFiscalYear("2011");
		log.debug(year.getMeasurmentResults());
		//TableModel tModel = model.getSerialListTable(AirConstants.INDEX_AIR_COST_SUMMARY_MEASURE, AirConstants.IDT_T_AMOUNT);
		
			
		model.populate();
		
		Measurement me = model.getAirsumByAirline();
		
		log.debug(" result:" + me.getResults());
		
		TableModel tModel = model.getSummaryByAirline();
		log.debug("Air line table:" +tModel.getContents() );
		log.debug("By Orign table:" +model.getSummaryByOrign().getContents() );
		log.debug("By Desc table:" +model.getSummaryByDestination().getContents());
		log.debug("By Routing table:" +model.getSummaryByRouting().getContents());
		
		
		log.debug("JSON:" + JSON.serialize(model.getSummaryByRouting().getContents().toArray()));
	
		
		//LinePlusBarChartModel lModel = model.getSummaryChartByAirline();
				
		//log.debug("Line bar:" +lModel );
		
		//log.debug("IAD" + AirportMap.airportMap.get("IAD"));
				
	}

}
