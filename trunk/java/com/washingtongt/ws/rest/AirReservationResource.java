package com.washingtongt.ws.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.mongodb.util.JSON;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.reservation.AirProfileModel;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.ui.model.TableModel;
import com.washingtongt.web.AirReservationController;
import com.washingtongt.web.ConsoleController;

@Path("air")
public class AirReservationResource {
	static final Logger log = Logger.getLogger(AirReservationController.class);
    private AirReservationController getController(HttpServletRequest request){
    	
    	HttpSession session = request.getSession(true);
    	Object obj = session.getAttribute("arController");
 	
    	return (AirReservationController)obj;
    }
    
    
    /*
     *  the total tripcostsummary used in the home page
     */
    
    @GET
    @Path("airCostSummary")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripCostSummaryChart(@Context HttpServletRequest request) {
    	AirProfileModel model = null;
    	AirReservationController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getAirProfileModel();
    	}
    	else {
    		return "";
    	}
    	LinePlusBarChartModel lmodel = model.getSummaryChartByMonth();
    	if (lmodel != null){
			return JSON.serialize(lmodel.toArray());
		}
    	else{
    		return "";
    	}
    }      

    @GET
    @Path("airCostSummaryByAirline")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripCostSummaryByAirlineChart(@Context HttpServletRequest request) {
    	AirProfileModel model = null;
    	AirReservationController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getAirProfileModel();
    	}
    	else {
    		return "";
    	}
    	LinePlusBarChartModel lmodel = model.getSummaryChartByAirline();
    	if (lmodel != null){
			return JSON.serialize(lmodel.toArray());
		}
    	else{
    		return "";
    	}
    }
    
    
    @GET
    @Path("airCostSummaryByRouting")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripCostSummaryByRouting(@Context HttpServletRequest request) {
    	AirProfileModel model = null;
    	AirReservationController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getAirProfileModel();
    	}
    	else {
    		return "";
    	}
    	TableModel tmodel = model.getSummaryByRouting();
    	if (tmodel != null){
			return JSON.serialize(tmodel.getContents().toArray());
		}
    	else{
    		return "";
    	}
    }     
    
    
    @GET
    @Path("airCostSummaryByOrigin")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripCostSummaryByOrign(@Context HttpServletRequest request) {
    	AirProfileModel model = null;
    	AirReservationController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getAirProfileModel();
    	}
    	else {
    		return "";
    	}
    	TableModel tmodel = model.getSummaryByOrign();
    	if (tmodel != null){
			return JSON.serialize(tmodel.getContents().toArray());
		}
    	else{
    		return "";
    	}
    }      
    
    @GET
    @Path("airCostSummaryByDest")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTripCostSummaryByDest(@Context HttpServletRequest request) {
    	AirProfileModel model = null;
    	AirReservationController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getAirProfileModel();
    	}
    	else {
    		return "";
    	}
    	TableModel tmodel = model.getSummaryByDestination();
    	if (tmodel != null){
			return JSON.serialize(tmodel.getContents().toArray());
		}
    	else{
    		return "";
    	}
    }          
    
}
