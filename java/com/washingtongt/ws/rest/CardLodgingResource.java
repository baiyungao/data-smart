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
import com.washingtongt.data.model.gsa.travelcard.lodging.CardLodgingProfileModel;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.web.CardLodgingController;


@Path("chargeLodging")
public class CardLodgingResource {
	static final Logger log = Logger.getLogger(CardLodgingResource.class);
    private CardLodgingController getController(HttpServletRequest request){
    	
    	HttpSession session = request.getSession(true);
    	Object obj = session.getAttribute("clController");
 	
    	return (CardLodgingController)obj;
    }
    
    
    /*
     *  the total tripcostsummary used in the home page
     */
    
    @GET
    @Path("lodgingChargeSummary")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAirChargeSummaryChart(@Context HttpServletRequest request) {
    	CardLodgingProfileModel model = null;
    	CardLodgingController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getCardLodgingProfileModel();
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
    @Path("lodgingChargeSummaryByCity")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAirChargeSummaryByCity(@Context HttpServletRequest request) {
    	CardLodgingProfileModel model = null;
    	CardLodgingController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getCardLodgingProfileModel();
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
    @Path("lodgingChargeSummaryByBureau")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAirChargeSummaryByBureau(@Context HttpServletRequest request) {
    	CardLodgingProfileModel model = null;
    	CardLodgingController controller = this.getController(request);
    	if (controller != null){
    		model = controller.getCardLodgingProfileModel();
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
