package com.washingtongt.data;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mongodb.QueryBuilder;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.washingtongt.data.model.Indicator;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.CostGapModel;
import com.washingtongt.data.model.gsa.GSAIndicatorFactory;
import com.washingtongt.data.model.gsa.TravelCostMeasure;
import com.washingtongt.data.model.gsa.time.DateUtils;
import com.washingtongt.data.model.gsa.time.FiscalYear;
import com.washingtongt.data.model.gsa.time.Month;
import com.washingtongt.data.model.gsa.time.Quarter;
import com.washingtongt.ui.model.BarchartModel;
import com.washingtongt.ui.model.PieChartModel;

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
		
		/*
		if (measure.getMatchMap().size() > 0){
		
		 for (String matchField:measure.getMatchMap().keySet()){
			 if (matchFields == null){
				 matchFields = new BasicDBObject(matchField, measure.getMatchMap().get(matchField));
			 }
			 else  {
				 matchFields.put(matchField, measure.getMatchMap().get(matchField));
			 }
		 }
		 match = new BasicDBObject("$match", matchFields );
		}*/
		
		
		// build the $projection operation
		DBObject fields = new BasicDBObject("_id", 0);


		DBObject groupFields = null;
		
		groupFields = new BasicDBObject( "_id", measure.getGroupby());
	    
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
		
		log.debug("query string: " + dbList);
		AggregationOutput output = getConnection().aggregate( dbList);
		
		Collection<Object> results = output.getCommandResult().values();

		if (results != null){
			Iterator<Object>iResults =  results.iterator();
			while (iResults.hasNext()){

				Object result = iResults.next();
				if (result instanceof BasicDBList ){
					BasicDBList list = (BasicDBList)result;
					measure.setResults(list);
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
	
	
	private static DBCollection getConnection(){
			DB gsaDB = MongoUtil.getMongoDB(db_name);
		DBCollection coll = gsaDB.getCollection("travel_voucher");
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
		
		
		AggregationOutput output = getConnection().aggregate( dbList);
		
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
		
		AggregationOutput output = getConnection().aggregate( dbList);
		
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
		
		AggregationOutput output = getConnection().aggregate( dbList);
		
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
		
		AggregationOutput output = getConnection().aggregate( dbList);
		
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
		
		AggregationOutput output = getConnection().aggregate( dbList);
		
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
		DBObject match = new BasicDBObject("$match", new BasicDBObject("FY", "2011") );

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
		
		AggregationOutput output = getConnection().aggregate( dbList);
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
		
		AggregationOutput output = getConnection().aggregate( dbList);
		
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
		
		AggregationOutput output = getConnection().aggregate( dbList);
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
		
		
		
		
		//BasicDBList results = MongoUtil.getTravelTripCountsByMonth()
		//BasicDBList results = MongoUtil.getTravelTripAvgByMonth();
		//BasicDBList results = MongoUtil.getTravelTripAvgByFY();
		//BasicDBList results = MongoUtil.getTravelTripCountByMonthByOffice();
		//BasicDBList results = MongoUtil.getTravelCostCompose();
		//BasicDBList results = getTravelCostSummary();  //barchart
		//BasicDBList results = getTravelTripDaysByFY();
		
		
		//BasicDBList results = MongoUtil.getMeasurement(TravelCostMeasure.benchMarkFY2011);
		
		//log.debug(FiscalYear.FY2011);
		
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
		
		match = new BasicDBObject("FY", "2011") ;
		//match.append("Organization", "R9-Pacific Rim-SFO, CA");
		
		FiscalYear testYear = new FiscalYear(GSAIndicatorFactory.IDT_DATE_DEPARTURE, match, 2011,2010, 10,TravelCostMeasure.class);
		
		//timeRange.append("$lt", "ISODate(\"2012-10-10T00:00:00.000Z\")");
		//match.append("Date_Depart",timeRange);
		testYear.setMatch(match);
		testYear.setField("Date_Depart");
		//testYear.updateMatch();
		
		BasicDBList results = MongoUtil.getMeasurement(testYear.getMeasurement());
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
		
	}

}
