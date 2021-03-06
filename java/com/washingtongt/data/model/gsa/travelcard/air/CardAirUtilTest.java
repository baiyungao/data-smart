package com.washingtongt.data.model.gsa.travelcard.air;


import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.ui.model.TableModel;

public class CardAirUtilTest {
	static final Logger log = Logger.getLogger(CardAirUtilTest.class);
	
	public static void main(String[] args){
		
		
		log.debug("start air ticketing");
		String organization="R11-Nat'l Capital-Wash., DC";
		String replace = "\\\\" + "'";
		log.debug("replacement:" + replace );
		String orgName = organization.replaceAll("'",replace);
		log.debug("orgName:" + orgName);
		/*
		DB gsaDB = MongoUtil.getMongoDB(GsaConstants.DB_NAME);
		DBCollection coll = gsaDB.getCollection(GsaConstants.DB_TC_AIR);
		
		BasicDBObject match = null;  // new BasicDBObject(AirConstants.IDT_T_AMOUNT, priceCrietia);;
		DBObject myDoc = coll.findOne(match);
		log.debug("find one:" + myDoc);
		
		
		CardAirProfileModel hModel = new CardAirProfileModel(null);
		TableModel tModel = hModel.getSerialListTable(CardAirConstants.INDEX_TC_AIR_SUMMARY_MEASURE, CardAirConstants.IDT_T_AMOUNT);
		log.debug("Summary by years:" + tModel.getContents());	
		
		/*
		hModel.populate();
		TableModel cModel = hModel.getSummaryByChain();
		log.debug("Summary by Chain:" + cModel.getContents());
		
		LinePlusBarChartModel lModel = hModel.getSummaryChartByChain();
		log.debug("Summary  Line Chart by Chain:" + lModel);
		
		TableModel ciModel = hModel.getSummaryByBureau();
		log.debug("Summary by City:" + ciModel.getContents());
		
		lModel = hModel.getSummaryChartByBureau();
		log.debug("Summary  Line Chart by Bureau:" + lModel);
		*/
	}

}
