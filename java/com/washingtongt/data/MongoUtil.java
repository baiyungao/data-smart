package com.washingtongt.data;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.washingtongt.data.model.Indicator;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.AirportMap;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.TripProfileModel;
import com.washingtongt.data.model.gsa.time.DateUtils;
import com.washingtongt.ui.model.LineChartModel;
import com.washingtongt.web.DataModelMap;

public class MongoUtil {
	
	static final Logger log = Logger.getLogger(MongoUtil.class);
	private static final int port = 27017;
	private static final String host = "localhost";
	private static final String db_name = "gsa";
	private static MongoClient mongo = null;


	public static Mongo getMongo() {
		if (mongo == null) {
			try {
				mongo = new MongoClient( host , port );
				
			} catch (UnknownHostException | MongoException e) {
				
			}
		}
		return mongo;
	}	
	
	public static DB getMongoDB(String dbName){
		return getMongo().getDB(dbName);
	}

	public static BasicDBList getMeasurement(Measurement measure){
		
		List<DBObject> dbList = new ArrayList<DBObject>();
		// create our pipeline operations, first with the $match
		
		//prepare match pipeline
		DBObject match = null;
		
		BasicDBObject matchFields = measure.getMatchFields();
		
		if (matchFields != null){
			match = new BasicDBObject("$match", matchFields );
		}
		
		
		// build the $projection operation
		DBObject fields = new BasicDBObject("_id", 0);


		DBObject groupFields = null;
		
		
		String[] groupIds = null;
		
		if (measure.getGroupby() != null){
			groupIds = measure.getGroupby().split(",");
			BasicDBObject groupIdFields = null;
			for (String id: groupIds){
				fields.put(id, 1);
				if (groupIdFields == null){
					groupIdFields = new BasicDBObject(id, "$"+id);
				}
				else {
					groupIdFields.append(id, "$"+id);
				}
			}
			groupFields = new BasicDBObject( "_id", groupIdFields);
		}else {
			groupFields = new BasicDBObject( "_id", measure.getGroupby());
		}
		
	    
	    Map<String,Indicator> indicatorMap = measure.getIndicators();
	    for (String key : indicatorMap.keySet()){
	    	Indicator indicator = indicatorMap.get(key);
	    	
	    	
	    	if (indicator.isCountIndicator()){
	    		groupFields.put(indicator.getLabel(), new BasicDBObject( "$sum",  1));
	    	}
	    	else {
	    	//project fields
	    	fields.put(indicator.getFactors(), indicator.getParameter()); 
	    	
	    	//groupFields
	    	groupFields.put(indicator.getLabel(), new BasicDBObject( indicator.getOp().getOp(),  "$" + indicator.getFactors()));
	    	}
	    }
	    
	    
		DBObject project = new BasicDBObject("$project", fields );
		DBObject group = new BasicDBObject("$group", groupFields);
		
		
		
		
		if (match != null){
			dbList.add(match);
		}
		dbList.add(project);
		dbList.add(group);
		
		//add sorting here;
		if (measure.getSortBy() != null){
			DBObject sort = new BasicDBObject("$sort", measure.getSortBy());
			dbList.add(sort);
		}
		
		//add limit here
		if (measure.getLimits() > 0){
			BasicDBObject limit = new BasicDBObject("$limit", measure.getLimits());
			dbList.add(limit);
		}
		
		log.debug("query string: " + dbList);
		AggregationOutput output = getConnection(measure.getCollection()).aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					measure.setResults(list);
					measure.setPopulated(true);
					return list;
				}
			}
		}		
		return null;
	}
	
	public static BasicDBList getTravelTripDaysByFY(){
		List<DBObject> dbList = new ArrayList<DBObject>();
		DB gsaDB = MongoUtil.getMongoDB(db_name);
		DBCollection coll = gsaDB.getCollection("travel_voucher");
		
		// create our pipeline operations, first with the $match
		DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("FY", 1);
		String[] timeDiff = new String[2]; 
		timeDiff[0] =  "$Date_Return";
		timeDiff[1] = "$Date_Depart";
		DBObject time = new BasicDBObject("$subtract",timeDiff );
		
		Object[] dayPar = new Object[2];
		dayPar[0] = time;
		dayPar[1] = 24*60*60*1000;
		BasicDBObject dayDiff = new BasicDBObject("$divide",dayPar );
		
		Object[] dayOff = new Object[2];
		dayOff[0] = dayDiff;
		dayOff[1] = 1;
		
		fields.put("days",new BasicDBObject("$add",dayOff));

		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );
		
		

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", null);
		groupFields.put("avg day per trip", new BasicDBObject( "$sum","$days"));
		
		DBObject group = new BasicDBObject("$group", groupFields);

		// run aggregation
		dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		
		AggregationOutput output = coll.aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
	}
	
	
	/*
	 *  The method returns how many trips by year, month or day 
	 */
	public static BasicDBList getTravelTripCountsByFY(){
		List<DBObject> dbList = new ArrayList<DBObject>();
		DB gsaDB = MongoUtil.getMongoDB(db_name);
		DBCollection coll = gsaDB.getCollection("travel_voucher");
		
		// create our pipeline operations, first with the $match
		//DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("FY", 1);

		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("Year", "$FY"));
		groupFields.put("count", new BasicDBObject( "$sum", 1));
		
		DBObject group = new BasicDBObject("$group", groupFields);

		// run aggregation
		//dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		
		AggregationOutput output = coll.aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
	}
	
	
	private static DBCollection getConnection(String collectionName){
		DB gsaDB = MongoUtil.getMongoDB(db_name);
		
		if ((collectionName == null) ||(collectionName.length() ==0)){
			collectionName = GsaConstants.DB_C_VOURCHER;
		}
		
		DBCollection coll = gsaDB.getCollection(collectionName);
		return coll;
	}
	
	/**
	 * Average cost per trip group by month 
	 * @return
	 */
	public static BasicDBList getTravelTripAvgByFY(){
		List<DBObject> dbList = new ArrayList<DBObject>();
		// create our pipeline operations, first with the $match
		//DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("Month",  new BasicDBObject( "$month", "$Date_Depart"));
		fields.put("FY",  1);
		fields.put("Total_Expenses", 1);
	    fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("Year", "$FY"));
		groupFields.put("avg", new BasicDBObject( "$avg", "$Total_Expenses"));
		
		DBObject group = new BasicDBObject("$group", groupFields);
		groupFields.put("count", new BasicDBObject( "$sum", 1));
		
		dbList.add(project);
		dbList.add(group);
		
		
		AggregationOutput output = getConnection(null).aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
	}	
	
	/**
	 * Average cost per trip group by month 
	 * @return
	 */
	public static BasicDBList getTravelTripAvgOrgByMonth(String org){
		List<DBObject> dbList = new ArrayList<DBObject>();
		// create our pipeline operations, first with the $match
		DBObject match = new BasicDBObject("$match", new BasicDBObject("Organization", org) );

		// build the $projection operation
		DBObject fields = new BasicDBObject("Month",  new BasicDBObject( "$month", "$Date_Depart"));
		fields.put("Year",  new BasicDBObject( "$year", "$Date_Depart"));
		fields.put("Total_Expenses", 1);
	    fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("Year", "$Year").append("Month", "$Month"));
		groupFields.put(org + " avg", new BasicDBObject( "$avg", "$Total_Expenses"));
		
		DBObject group = new BasicDBObject("$group", groupFields);
		groupFields.put("count", new BasicDBObject( "$sum", 1));
		
		//DBObject sortFields =  new BasicDBObject("Year", 1);

		//DBObject sort = new BasicDBObject("$sort", sortFields);

		// run aggregation
		dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		//dbList.add(sort);
		
		AggregationOutput output = getConnection(null).aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
	}
	
	
	
	public static BasicDBList getTravelTripAvgByMonth(){
		List<DBObject> dbList = new ArrayList<DBObject>();
		// create our pipeline operations, first with the $match
		//DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("Month",  new BasicDBObject( "$month", "$Date_Depart"));
		fields.put("Year",  new BasicDBObject( "$year", "$Date_Depart"));
		fields.put("Total_Expenses", 1);
	    fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("Year", "$Year").append("Month", "$Month"));
		groupFields.put("avg", new BasicDBObject( "$avg", "$Total_Expenses"));
		
		DBObject group = new BasicDBObject("$group", groupFields);
		groupFields.put("count", new BasicDBObject( "$sum", 1));
		
		//DBObject sortFields =  new BasicDBObject("Year", 1);

		//DBObject sort = new BasicDBObject("$sort", sortFields);

		// run aggregation
		//dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		//dbList.add(sort);
		
		AggregationOutput output = getConnection(null).aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
	}	
	
	
	/**
	 * Average cost per trip per day group by month
	 * @return
	 */
	
	
	
	public static BasicDBList getTravelTripAvgPerDayByMonth(){
		List<DBObject> dbList = new ArrayList<DBObject>();
		// create our pipeline operations, first with the $match
		//DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("Month",  new BasicDBObject( "$month", "$Date_Depart"));
		fields.put("Year",  new BasicDBObject( "$year", "$Date_Depart"));
		fields.put("Total_Expenses", 1);
		ArrayList<String> ops = new ArrayList<String>();
		ops.add("$Date_Return");
		ops.add("$Date_Depart");
		fields.put("Days", new BasicDBObject( "$subtract", ops));
	    fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("Year", "$Year").append("Month", "$Month"));
		groupFields.put("avg", new BasicDBObject( "$avg", "$Total_Expenses"));
		groupFields.put("avgDays", new BasicDBObject( "$avg", "$Days"));
		
		DBObject group = new BasicDBObject("$group", groupFields);
		groupFields.put("count", new BasicDBObject( "$sum", 1));
		DBObject sortFields =  new BasicDBObject("avg", -1);

		DBObject sort = new BasicDBObject("$sort", sortFields);

		// run aggregation
		//dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		dbList.add(sort);
		
		AggregationOutput output = getConnection(null).aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
	}	
	
	public static BasicDBList getAirportGeo(){
		BasicDBList airportList = new BasicDBList();
		DB gsaDB = MongoUtil.getMongoDB(db_name);
		DBCollection coll = gsaDB.getCollection(GsaConstants.DB_C_AIRPORT);
		DBCursor results =  coll.find();
		while (results.hasNext()){
			DBObject airport = results.next();
			airportList.add(airport);
		}
		log.debug("airports:" + airportList);
		return airportList;
	}
	
	public static BasicDBList getTravelTripCountsByMonth(){
		
		List<DBObject> dbList = new ArrayList<DBObject>();
		// create our pipeline operations, first with the $match
		//DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("Month",  new BasicDBObject( "$month", "$Date_Depart"));
		fields.put("Year",  new BasicDBObject( "$year", "$Date_Depart"));
		fields.put("FY", 1);


		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("Year", "$Year").append("Month", "$Month"));
		groupFields.put("count", new BasicDBObject( "$sum", 1));
		
		DBObject group = new BasicDBObject("$group", groupFields);
		
		DBObject sortFields =  new BasicDBObject("count", 1);

		DBObject sort = new BasicDBObject("$sort", sortFields);

		// run aggregation
		//dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		dbList.add(sort);
		
		AggregationOutput output = getConnection(null).aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
	}	
	
	public static BasicDBList getTravelCostSummary(){
		List<DBObject> dbList = new ArrayList<DBObject>();
			// create our pipeline operations, first with the $match
		//DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("Office", 1);
		fields.put("Organization", 1);
		fields.put("Common_Carrier_AIR_TRAIN", 1);
		fields.put("Rental_Car", 1);
		fields.put("Misc_Expenses", 1);
		fields.put("Lodging", 1);
		fields.put("Meals_Incidentals", 1);
		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("Organization", "$Organization"));
		groupFields.put("AIR TRAIN", new BasicDBObject( "$sum", "$Common_Carrier_AIR_TRAIN"));
		groupFields.put("Rental Car", new BasicDBObject( "$sum", "$Rental_Car"));
		groupFields.put("Misc", new BasicDBObject( "$sum", "$Misc_Expenses"));
		groupFields.put("Lodging", new BasicDBObject( "$sum", "$Lodging"));
		groupFields.put("Meals", new BasicDBObject( "$sum", "$Meals_Incidentals"));
		DBObject group = new BasicDBObject("$group", groupFields);

		// run aggregation
		//dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		
		AggregationOutput output = getConnection(null).aggregate( dbList);
		//System.out.print("results: " + output.getCommandResult() );
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
		
	}
	
	public static BasicDBList getTravelTripCountByMonthByOffice(){
		
		List<DBObject> dbList = new ArrayList<DBObject>();
		DB gsaDB = MongoUtil.getMongoDB(db_name);
		DBCollection coll = gsaDB.getCollection("travel_voucher");
		
		// create our pipeline operations, first with the $match
		//DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("FY", 1);
		fields.put("Organization", 1);
		fields.put("Total_Expenses", 1);
		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("Organization", "$Organization"));
		groupFields.put("count", new BasicDBObject( "$sum", 1));
		groupFields.put("avg cost per trip", new BasicDBObject( "$avg", "$Total_Expenses"));
		//groupFields.put("total expenses", new BasicDBObject( "$sum", "$Total_Expenses"));
		DBObject group = new BasicDBObject("$group", groupFields);

		DBObject sortFields =  new BasicDBObject("count", -1);

		DBObject sort = new BasicDBObject("$sort", sortFields);		
		
		// run aggregation
		//dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		dbList.add(sort);
		
		AggregationOutput output = coll.aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
	}		
	
	/**
	 * TODO here
	 * @param Office
	 * @return
	 */
	
	public static BasicDBList getTravelTripAvgByMonthByOffice(String Office){
		List<DBObject> dbList = new ArrayList<DBObject>();
		// create our pipeline operations, first with the $match
		//DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("Month",  new BasicDBObject( "$month", "$Date_Depart"));
		fields.put("Year",  new BasicDBObject( "$year", "$Date_Depart"));
		fields.put("Total_Expenses", 1);
	    fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject("Year", "$Year").append("Month", "$Month"));
		groupFields.put("avg", new BasicDBObject( "$avg", "$Total_Expenses"));
		groupFields.put("count", new BasicDBObject( "$sum", 1));		
		DBObject group = new BasicDBObject("$group", groupFields);

		
		//DBObject sortFields =  new BasicDBObject("Year", 1);

		//DBObject sort = new BasicDBObject("$sort", sortFields);

		// run aggregation
		//dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		//dbList.add(sort);
		
		AggregationOutput output = getConnection(null).aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
	}
	

	public static BasicDBList getTravelCostCompose(){
		List<DBObject> dbList = new ArrayList<DBObject>();
			// create our pipeline operations, first with the $match
		//DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

		// build the $projection operation
		DBObject fields = new BasicDBObject("Common_Carrier_AIR_TRAIN", 1);
		
		fields.put("Rental_Car", 1);
		fields.put("Misc_Expenses", 1);
		fields.put("Lodging", 1);
		fields.put("Meals_Incidentals", 1);
		fields.put("_id", 0);
		DBObject project = new BasicDBObject("$project", fields );

		// Now the $group operation
		DBObject groupFields = new BasicDBObject( "_id", null);
		groupFields.put("AIR TRAIN", new BasicDBObject( "$sum", "$Common_Carrier_AIR_TRAIN"));
		groupFields.put("Rental Car", new BasicDBObject( "$sum", "$Rental_Car"));
		groupFields.put("Misc", new BasicDBObject( "$sum", "$Misc_Expenses"));
		groupFields.put("Lodging", new BasicDBObject( "$sum", "$Lodging"));
		groupFields.put("Meals", new BasicDBObject( "$sum", "$Meals_Incidentals"));
		DBObject group = new BasicDBObject("$group", groupFields);

		// run aggregation
		//dbList.add(match);
		dbList.add(project);
		dbList.add(group);
		
		AggregationOutput output = getConnection(null).aggregate( dbList);
		//System.out.print("results: " + output.getCommandResult() );
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					return list;
				}
			}
		}		
		
		return null;
		
	}
	
	
	public static void main(String[] args){
		
		DB gsaDB = MongoUtil.getMongoDB(db_name);
		DBCollection coll = gsaDB.getCollection("travel_voucher");
		Date start = DateUtils.getStartTime(2012, 2);
		Date end = DateUtils.getStartTime(2012, 3);
		BasicDBObject timeRange = new BasicDBObject("$gte",start ).append("$lt",end);
		BasicDBObject match = new BasicDBObject("Date_Depart", timeRange);
		DBObject myDoc = coll.findOne(match);
		log.debug("find one:" + myDoc);
		
		
		//CostDriverModel cmodel = new CostDriverModel(null);
		TripProfileModel overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
		overAllTripModel.populate(); 
		
		DataModelMap.getDefault().load("ALL", overAllTripModel);
		
		match = new BasicDBObject("Organization", "Ofc of the Chief Acquisition") ;  //R9-Pacific Rim-SFO, CA
		
		
		// TEST Trip Profile Model
		TripProfileModel model = new TripProfileModel(match, GsaConstants.ORG_LEVEL_ORGANIZATION);
		
		model.populate();
		
		DataModelMap.getDefault().load("Ofc of the Chief Acquisition", model);
		
		
		
		//LinePlusBarChartModel chartModel = cmodel.getCostDriverChartByMonth("2011");
		
		//LineChartModel lineChart = cmodel.getCostDriverChartYTDByMonth("2011");
		
		//log.debug("char:" + lineChart);
		
		//LineChartModel lineChart = cmodel.getCostReducePercentageYTDByMonth("2012");
		LineChartModel lineChart =  model.getCostReducePercentageYTDByMonth();
		log.debug("char:" + lineChart);
		
		//log.debug("linebar:" + model.getSummaryChartByMonth());
		
		/*
		TripProfileModel overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
		overAllTripModel.populate(); 
		TableModel model = overAllTripModel.getSummaryTableByOrg();
		
		log.debug("cols:" + model.getCols());
		
		BarchartModel chart  = overAllTripModel.getTripSummaryChartByOrg();
		log.debug("bar chart:" + chart );
		
		BasicDBList results = overAllTripModel.getCostDrivers().getResults();
		log.debug("drivers:" + results);
		
		model = overAllTripModel.getCostDriverTable();
		log.debug("Cost Driver Table:" + model.getContents());
		
		/*
		
		//BasicDBObject groupField = new BasicDBObject("Organization", "$Organization");
		
		//BasicDBList results = MongoUtil.getTravelTripCountsByMonth()
		//BasicDBList results = MongoUtil.getTravelTripAvgByMonth();
		//BasicDBList results = MongoUtil.getTravelTripAvgByFY();
		//BasicDBList resultsof = MongoUtil.getTravelTripCountByMonthByOffice();
		//log.debug(resultsof);
		
		
		//BasicDBList results = MongoUtil.getTravelCostCompose();
		//BasicDBList results = getTravelCostSummary();  //barchart
		//BasicDBList results = getTravelTripDaysByFY();
		
		
		//BasicDBList results = MongoUtil.getMeasurement(TravelCostMeasure.benchMarkFY2011);
		
		
		Measurement tsmByDestination = new TravelSumaryMeasure();
		tsmByDestination.setMatchFields(match);
		tsmByDestination.setGroupby(GsaConstants.FIELD_LOCATION);
		BasicDBObject sortFields =  new BasicDBObject(GsaConstants.IDT_TOTAL_EXPENSE, -1);
		tsmByDestination.setSortBy(sortFields);
		tsmByDestination.setLimits(50);
		
		
		Measurement tsmByOrganization = new TravelSumaryMeasure();
		tsmByOrganization.setMatchFields(match);
		tsmByOrganization.setGroupby(GsaConstants.ORG_LEVEL_ORGANIZATION + ",FY");
		BasicDBObject sortFields =  new BasicDBObject(GsaConstants.IDT_FY, -1).append(GsaConstants.IDT_TOTAL_EXPENSE, -1);
		tsmByOrganization.setSortBy(sortFields);		
		
		BasicDBList results = MongoUtil.getMeasurement(tsmByOrganization);
		
		
		log.debug(results);
		
		//test month measure
		/*
		Month testMonth = new Month(2011,10,TravelCostMeasure.class);
		match = new BasicDBObject("FY", "2012") ;
		//match.append("Organization", "R9-Pacific Rim-SFO, CA");
		
		//timeRange.append("$lt", "ISODate(\"2012-10-10T00:00:00.000Z\")");
		//match.append("Date_Depart",timeRange);
		testMonth.setMatch(match);
		testMonth.setField("Date_Depart");
		testMonth.updateMatch();
		
		MongoUtil.getMeasurement(testMonth.getMeasurement());
		
		*/
		//Month testMonth = new Month(2011,10,TravelCostMeasure.class);
		
		/*/  ---- TEST of Fiscal Year, Month and Quarter
		match = new BasicDBObject("Organization", "R9-Pacific Rim-SFO, CA") ;
		
		
		// TEST Trip Profile Model
		TripProfileModel model = new TripProfileModel(match, TripProfileModel.ORG_LEVEL_ORGANIZATION);
		
		model.populate();
		
		for (SerialBase s: model.getSerialList())
		{
		BasicDBList results = s.getMeasurmentResults();
		
		log.debug(s.getName() + ": "  + results);
		
		}
		//match.append("Organization", "R9-Pacific Rim-SFO, CA");
		
		
		
		/*
		//------------------ TEST the time serial --------------------
		FiscalYear testYear = new FiscalYear(GSAIndicatorFactory.IDT_DATE_DEPARTURE, match, 2011,2010, 10,TravelCostMeasure.class);
		
		//timeRange.append("$lt", "ISODate(\"2012-10-10T00:00:00.000Z\")");
		//match.append("Date_Depart",timeRange);
		testYear.setMatch(match);
		testYear.setField("Date_Depart");
		//testYear.updateMatch();
		
		Measurement m = testYear.getMeasurement();
		m.setGroupby("Organization");
		BasicDBList results = MongoUtil.getMeasurement(m);
		log.debug(results);
		
		Month month = testYear.getMonth(2);
		//month.updateMatch();
		BasicDBList results1 = MongoUtil.getMeasurement(month.getMeasurementYTD());
		log.debug(results1);
		
		
		Quarter quarter = testYear.getQuarter(1);
		//quarter.updateMatch();
		BasicDBList results2 = MongoUtil.getMeasurement(quarter.getMeasurement());
		log.debug(results2);

	
		//quarter.updateMatch();
		BasicDBList results3 = MongoUtil.getMeasurement(quarter.getMeasurementYTD());
		log.debug(results3);		
				
		
		
		//log.debug("Date:" + DateUtils.getStartTime(2011, 1));
		//log.debug("Date:" + DateUtils.getEndTime(2011, 2));
		
		/*
		results = MongoUtil.getMeasurement(TravelCostMeasure.benchMarkFY2011);
		results = MongoUtil.getMeasurement(TravelCostMeasure.benchMarkFY2011);
		
		ArrayList<Measurement> mList = new ArrayList<Measurement>();
		mList.add(TravelCostMeasure.benchMarkFY2011);
		mList.add(TravelCostMeasure.benchMarkFY2012);
		mList.add(TravelCostMeasure.benchMarkFY2013);
		
		CostGapModel model = new CostGapModel(mList, TravelCostMeasure.benchMarkFY2011);
		
		model.populate(); //fill data
		//BasicDBList data = model.getDataForBarchart();
		
		log.debug(data);
		
		
		
		if (results != null){
			
			//new PieChartModel(results).toArray();
			//new LinePlusBarChartModel(results).toArray();
			log.debug(results);
		}
		*/
	
		log.debug("IAD" + AirportMap.airportMap.get("IAD"));
	}

}
