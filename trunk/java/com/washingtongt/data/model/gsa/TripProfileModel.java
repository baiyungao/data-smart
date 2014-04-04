package com.washingtongt.data.model.gsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.SerialBase;
import com.washingtongt.data.model.gsa.time.FiscalYear;

/**
 * This model is repreent all details to profile trip data provide by GSA
 * 
 * 
 * the model contains all the fiscal year serial, for the sample it is FY2011, FY2012, FY2013
 * 
 * also the model contails  
 * 
 * 
 * @author gaob
 *
 */
public class TripProfileModel {
	
	static final Logger log = Logger.getLogger(TripProfileModel.class);
	

	
	private List<SerialBase> serialList = new ArrayList<SerialBase>();
	
	private BasicDBObject match;
	private TravelSumaryMeasure tsmByOrganization;
	private TravelSumaryMeasure tsmByDestination;
	private String orgLevel; //organization or office
	
	public TripProfileModel(BasicDBObject match, String orgLevel){
		
		this.match = match;
		this.orgLevel = orgLevel;
		
		BasicDBObject matchfy2011 = (match == null)? new BasicDBObject("FY","2011"):((BasicDBObject)match.clone()).append("FY","2011");
		BasicDBObject matchfy2012 = (match == null)? new BasicDBObject("FY","2012"):((BasicDBObject)match.clone()).append("FY","2012");
		BasicDBObject matchfy2013 = (match == null)? new BasicDBObject("FY","2013"):((BasicDBObject)match.clone()).append("FY","2013");
		
		
		FiscalYear fy2011 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2011, 2011, 2010, 10, TravelCostMeasure.class);
		fy2011.setName("FY 2011");
		serialList.add(fy2011);
		
		FiscalYear fy2012 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2012, 2012, 2011, 10, TravelCostMeasure.class);
		fy2012.setName("FY 2012");
		serialList.add(fy2012);		
		
		FiscalYear fy2013 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2013, 2013, 2012, 10, TravelCostMeasure.class);
		fy2013.setName("FY 2013");
		serialList.add(fy2013);		
		
		tsmByOrganization = new TravelSumaryMeasure();
		tsmByOrganization.setMatchFields(match);
		tsmByOrganization.setGroupby(orgLevel);
		
		tsmByDestination = new TravelSumaryMeasure();
		tsmByDestination.setMatchFields(match);
		tsmByDestination.setGroupby(GsaConstants.FIELD_LOCATION);
		
	}
	
	public boolean populate(){

		MongoUtil.getMeasurement(this.tsmByOrganization);
		MongoUtil.getMeasurement(this.tsmByDestination);
		
		return true;
	}
	
	public List<SerialBase> getSerialList(){
		return this.serialList;
	}

	public TravelSumaryMeasure getTsmByOrganization() {
		return tsmByOrganization;
	}

	public void setTsmByOrganization(TravelSumaryMeasure tsmByOrganization) {
		this.tsmByOrganization = tsmByOrganization;
	}

	public TravelSumaryMeasure getTsmByDestination() {
		return tsmByDestination;
	}

	public void setTsmByDestination(TravelSumaryMeasure tsmByDestination) {
		this.tsmByDestination = tsmByDestination;
	}
	
	
	
	
}
