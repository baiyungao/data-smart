package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.washingtongt.data.model.SerialBase;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.TripProfileModel;
import com.washingtongt.ui.model.TableModel;

@ManagedBean(name="controller")
@SessionScoped
public class ConsoleController {
	static final Logger log = Logger.getLogger(ConsoleController.class);
	private TripProfileModel overAllTripModel;  
	
	public ConsoleController(){
		overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
		 
	}
	
	public String getHello(){
		return "I will win";
	}

	
	public TableModel getYearSummaryTable(){
		TableModel model = new TableModel(GsaConstants.INDEX_TRAVEL_COST_MEASURE, GsaConstants.IDT_TRIP_CT);
		
		for (SerialBase s: overAllTripModel.getSerialList())
		{
		BasicDBList results = s.getMeasurmentResults();
		model.addContent(results,s.getName());
		log.debug(s.getName() + ": "  + results);
		}
		return model;
	}
	
	public TableModel getSummaryTableByOrg(){
		TableModel model = new TableModel(GsaConstants.INDEX_TRAVEL_SUMMARY_MEASURE, GsaConstants.IDT_TRIP_CT);
		
		
		BasicDBList results = this.overAllTripModel.getTsmByOrganization().getResults();
		model.addContent(results,this.overAllTripModel.getTsmByDestination().getDescription());
		log.debug(this.overAllTripModel.getTsmByDestination().getDescription() + ": "  + results);
		return model;
	}
		
	
}
