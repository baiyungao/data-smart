package com.washingtongt.ws.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.ui.model.BarchartModel;
import com.washingtongt.ui.model.ChartSeries;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.ui.model.PieChartModel;

/**
 * Root resource (exposed at "data" path)
 */
@Path("data")
public class DataResource {

	static final Logger log = Logger.getLogger(LinePlusBarChartModel.class);
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("summarybyorg")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCostSummaryByOrg() {
    	BasicDBList results = MongoUtil.getTravelCostSummary();
		
    	if (results != null){
			return JSON.serialize(new BarchartModel(results).toArray());
		}
    	
    	return "";
    }
    
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * This method return a benchmark data to sure organization level total counts of trips, average cost per trip, 
     * and average daily cost per trip; 
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("benchmark")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripBenchMarkChart() {
    	BasicDBList results = MongoUtil.getTravelTripAvgByMonth();
		
    	log.debug("getResults for bench mark:" + results);
    	
    	if (results != null){
			return JSON.serialize(new LinePlusBarChartModel(results,true).toArray());
		}
    	
    	return "";
    }    

    @GET
    @Path("orgbymonth")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripOrgChart() {
    	
    	String orgName="R6-The Heartland-KC, MO";
    	
    	BasicDBList results = MongoUtil.getTravelTripAvgOrgByMonth(orgName);
    	BasicDBList benchMark = MongoUtil.getTravelTripAvgByMonth();
    	
    	log.debug("getResults for bench mark:" + results);
    	LinePlusBarChartModel bModel = new LinePlusBarChartModel(benchMark,true);
    	ChartSeries bSerial = bModel.getSerialMap().get("avg");
    	
    	if (results != null){
			return JSON.serialize(new LinePlusBarChartModel(results,bSerial,true).toArray());
		}
    	
    	return "";
    }     
    
    @GET
    @Path("benchmarkfy")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripBenchMarkChartByYear() {
    	BasicDBList results = MongoUtil.getTravelTripAvgByFY();
		
    	log.debug("getResults for bench mark:" + results);
    	
    	if (results != null){
			return JSON.serialize(new LinePlusBarChartModel(results,true).toArray());
		}
    	
    	return "";
    } 
    
    @GET
    @Path("benchmarkorg")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripBenchMarkByOrg() {
    	BasicDBList results = MongoUtil.getTravelTripCountByMonthByOffice();
		
    	log.debug("getResults for bench mark:" + results);
    	
    	if (results != null){
			return JSON.serialize(new LinePlusBarChartModel(results,false).toArray());
		}
    	
    	return "";
    } 
    
    
    @GET
    @Path("costcomp")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCostComp() {
    	BasicDBList results = MongoUtil.getTravelCostCompose();
		
    	log.debug("getResults for bench mark:" + results);
    	
    	if (results != null){
			return JSON.serialize(new PieChartModel(results).toArray());
		}
    	
    	return "";
    }     
}
