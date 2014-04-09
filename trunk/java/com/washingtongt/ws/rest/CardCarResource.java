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
import com.washingtongt.data.model.gsa.travelcard.car.CardCarProfileModel;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.web.CardCarController;

@Path("chargeCar")
public class CardCarResource {
	static final Logger log = Logger.getLogger(CardCarResource.class);
    private CardCarController getController(HttpServletRequest request){
    	
    	HttpSession session = request.getSession(true);
    	Object obj = session.getAttribute("ccController");
 	
    	return (CardCarController)obj;
    }
    
    
    /*
     *  the total tripcostsummary used in the home page
     */
    
    @GET
    @Path("carChargeSummary")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAirChargeSummaryChart(@Context HttpServletRequest request) {
    	CardCarProfileModel model = null;
    	CardCarController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getCardCarProfileModel();
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
    @Path("carChargeSummaryByCity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAirChargeSummaryByCity(@Context HttpServletRequest request) {
    	CardCarProfileModel model = null;
    	CardCarController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getCardCarProfileModel();
    	}
    	else {
    		return "";
    	}
    	LinePlusBarChartModel lmodel = model.getSummaryChartByCity();
    	if (lmodel != null){
			return JSON.serialize(lmodel.toArray());
		}
    	else{
    		return "";
    	}
    }
    
    
    @GET
    @Path("carChargeSummaryByBureau")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAirChargeSummaryByBureau(@Context HttpServletRequest request) {
    	CardCarProfileModel model = null;
    	CardCarController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getCardCarProfileModel();
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
