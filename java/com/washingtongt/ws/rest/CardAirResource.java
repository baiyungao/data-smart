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
import com.washingtongt.data.model.gsa.travelcard.air.CardAirProfileModel;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.web.CarReservationController;
import com.washingtongt.web.CardAirController;

@Path("chargeAir")
public class CardAirResource {
	static final Logger log = Logger.getLogger(CardAirResource.class);
    private CardAirController getController(HttpServletRequest request){
    	
    	HttpSession session = request.getSession(true);
    	Object obj = session.getAttribute("caController");
 	
    	return (CardAirController)obj;
    }
    
    
    /*
     *  the total tripcostsummary used in the home page
     */
    
    @GET
    @Path("airChargeSummary")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAirChargeSummaryChart(@Context HttpServletRequest request) {
    	CardAirProfileModel model = null;
    	CardAirController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getCardAirProfileModel();
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
    @Path("airChargeSummaryByChain")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAirChargeSummaryByAirlineChart(@Context HttpServletRequest request) {
    	CardAirProfileModel model = null;
    	CardAirController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getCardAirProfileModel();
    	}
    	else {
    		return "";
    	}
    	LinePlusBarChartModel lmodel = model.getSummaryChartByChain();
    	if (lmodel != null){
			return JSON.serialize(lmodel.toArray());
		}
    	else{
    		return "";
    	}
    }
    
    
    @GET
    @Path("airChargeSummaryByBureau")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAirChargeSummaryByBureau(@Context HttpServletRequest request) {
    	CardAirProfileModel model = null;
    	CardAirController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getCardAirProfileModel();
    	}
    	else {
    		return "";
    	}
    	LinePlusBarChartModel lmodel = model.getSummaryChartByBureau();
    	if (lmodel != null){
			return JSON.serialize(lmodel.toArray());
		}
    	else{
    		return "";
    	}
    }    
     
}
