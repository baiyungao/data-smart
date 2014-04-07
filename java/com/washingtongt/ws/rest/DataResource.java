package com.washingtongt.ws.rest;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.phoid.util.VariableStore;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.washingtongt.access.AccessToken;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.CostDriverModel;
import com.washingtongt.data.model.gsa.CostGapModel;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.TravelCostMeasure;
import com.washingtongt.data.model.gsa.TripProfileModel;
import com.washingtongt.ui.model.BarchartModel;
import com.washingtongt.ui.model.ChartSeries;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.ui.model.PieChartModel;
import com.washingtongt.web.ConsoleController;

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
    
    
    @GET
    @Path("costdriver/{fy}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCostDriver(@PathParam("fy") String fy, @Context HttpServletRequest request) {
    	
    	log.debug("path para: fy " + fy);
    	TripProfileModel overAllTripModel = null;
    	ConsoleController controller = this.getController(request);
    	if (controller != null){
    		overAllTripModel = controller.getOverAllTripModel();
    	}
    	else {
    		overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
    		overAllTripModel.populate();
    	}
    	
    	
    	BasicDBList results = overAllTripModel.getCostDrivers().getResults();
    	BasicDBObject result = null;
    	
    	for (int i = 0; i < results.size(); i++){
    		BasicDBObject row = (BasicDBObject)results.get(i);
    		BasicDBObject id = (BasicDBObject)row.get(GsaConstants.IDT_ID);
    		String fiscalYear = id.getString(GsaConstants.IDT_FY);
    		if (fiscalYear.equalsIgnoreCase(fy)){
    			result = row;
    		}
    		
    	}
    	
    	if (result != null){
			return JSON.serialize(new PieChartModel(result).toArray());
		}
    	
    	return "";
    }    
    
    /*
     *  the total tripcostsummary used in the home page
     */
    
    @GET
    @Path("tripCostSummary")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripCostSummaryChart(@Context HttpServletRequest request) {
    	TripProfileModel overAllTripModel = null;
    	ConsoleController controller = this.getController(request);
    	if (controller != null){
    		overAllTripModel = controller.getOverAllTripModel();
    	}
    	else {
    		overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
    		overAllTripModel.populate();
    	}
    
    	LinePlusBarChartModel model = overAllTripModel.getSummaryChartByMonth();
    	
    	if (model != null){
			return JSON.serialize(model.toArray());
		}
    	
    	return "";
    }    

    /*
     *  the total tripcostsummary used in the per organization page
     */
    
    @GET
    @Path("tripCostSummaryOrg")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripCostSummaryOrgChart(@Context HttpServletRequest request) {
    	TripProfileModel overAllTripModel = null;
    	ConsoleController controller = this.getController(request);
    	if (controller != null){
    		overAllTripModel = controller.getOrgModel();
    	}
    	else {
    		return null;
    	}
    
    	LinePlusBarChartModel model = overAllTripModel.getSummaryChartByMonth();
    	
    	if (model != null){
			return JSON.serialize(model.toArray());
		}
    	
    	return "";
    }    
    
    @GET
    @Path("SummaryChartByOfficePerOrg")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSummaryChartByOfficePerOrg(@Context HttpServletRequest request) {
    	
    	TripProfileModel overAllTripModel = null;
    	ConsoleController controller = this.getController(request);
    	if (controller != null){
    		overAllTripModel = controller.getOrgModel();
    	}
    	else {
    		return null;
    	}
    	 
    	BarchartModel model = overAllTripModel.getTripSummaryChartByOrg();
    	
    	if (model != null){
			return JSON.serialize(model.toArray());
		}
    	
    	return "";
    }      
        

    private ConsoleController getController(HttpServletRequest request){
    	
    	HttpSession session = request.getSession(true);
    	Object obj = session.getAttribute("controller");
		
    	
    	return (ConsoleController)obj;
    }
  
    
    @GET
    @Path("SummaryChartByOrg")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSummaryChartByOrg(@Context HttpServletRequest request) {
    	
    	TripProfileModel overAllTripModel = null;
    	ConsoleController controller = this.getController(request);
    	if (controller != null){
    		overAllTripModel = controller.getOverAllTripModel();
    	}
    	else {
    		overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
    		overAllTripModel.populate();
    	}
    	 
    	BarchartModel model = overAllTripModel.getTripSummaryChartByOrg();
    	
    	if (model != null){
			return JSON.serialize(model.toArray());
		}
    	
    	return "";
    }      
    
    @GET
    @Path("costgap")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCostGap() {
    	
    	/*
		ArrayList<Measurement> mList = new ArrayList<Measurement>();
		mList.add(TravelCostMeasure.benchMarkFY2011);
		mList.add(TravelCostMeasure.benchMarkFY2012);
		mList.add(TravelCostMeasure.benchMarkFY2013);
		
		CostGapModel model = new CostGapModel(mList, TravelCostMeasure.benchMarkFY2011);
		
		model.populate(); //fill data
		BasicDBList results = model.getDataForBarchart();
		
    	log.debug("getResults for costgapmodel:" + results);
    	
    	if (results != null){
			return JSON.serialize(new BarchartModel(results).toArray());
		}
    	*/
    	return "";
    }
    
    
    /**
     * get the cost driver distribution for a year
     */
    @GET
    @Path("costDriverDistribute/{fy}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCostDriverDistribute(@PathParam("fy") String fy, @Context HttpServletRequest request) {
    	log.debug("path para: fy " + fy);
    	
    	
    	 
    	CostDriverModel costdriverModel = null;
    	ConsoleController controller = this.getController(request);
    	if (controller != null){
    		costdriverModel = controller.getCostDriverModel();
    	}
    	else {
    		costdriverModel = new CostDriverModel(null);
    		
    	}
 	
    	if (costdriverModel != null){
			return JSON.serialize(costdriverModel.getCostDriverChartByMonth(fy).toArray());
		}
    	
    	return "";
    }    
    
    
    /**
     * get the cost driver distribution for a year
     */
    @GET
    @Path("costDriverYtmLineChart/{fy}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCostDriverYtmLineChart(@PathParam("fy") String fy, @Context HttpServletRequest request) {
    	log.debug("path para: fy " + fy);
    	
    	CostDriverModel costdriverModel = null;
    	ConsoleController controller = this.getController(request);
    	if (controller != null){
    		costdriverModel = controller.getCostDriverModel();
    	}
    	else {
    		costdriverModel = new CostDriverModel(null);
    		
    	}
 	
    	if (costdriverModel != null){
			return JSON.serialize(costdriverModel.getCostDriverChartYTDByMonth(fy).toArray());
		}
    	
    	return "";
    }       

    /**
     * get the cost driver distribution for a year
     */
    @GET
    @Path("costDriverYtmPctLineChart/{fy}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCostDriverYtmPctLineChart(@PathParam("fy") String fy, @Context HttpServletRequest request) {
    	log.debug("path para: fy " + fy);
    	
    	CostDriverModel costdriverModel = null;
    	ConsoleController controller = this.getController(request);
    	if (controller != null){
    		costdriverModel = controller.getCostDriverModel();
    	}
    	else {
    		costdriverModel = new CostDriverModel(null);
    		
    	}
 	
    	if (costdriverModel != null){
			return JSON.serialize(costdriverModel.getCostReducePercentageYTDByMonth(fy).toArray());
		}
    	
    	return "";
    }       


}
