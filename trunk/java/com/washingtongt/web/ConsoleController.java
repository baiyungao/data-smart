package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.CostDriverModel;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.TripProfileModel;
import com.washingtongt.data.model.gsa.reservation.AirConstants;
import com.washingtongt.data.model.gsa.reservation.AirProfileModel;

@ManagedBean(name="controller")
@SessionScoped
public class ConsoleController extends WebController {
	static final Logger log = Logger.getLogger(ConsoleController.class);
	private TripProfileModel overAllTripModel;
	private CostDriverModel costDriverModel;
	private String fy;           //parameter from request
	private String office;       //parameter from request
	private String organization; //parameter from requeste 
	private TripProfileModel orgModel  = null;
	

	
	
	public ConsoleController(){
		
		//all sessions share the data model to improve the performance
	
		
		Model model =dataModelMap.findModel("ALL", "TripProfileModel");
		
		if (model == null){
			overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
			overAllTripModel.populate(); 
			dataModelMap.load("ALL",  overAllTripModel);
		}
		else {
			overAllTripModel = (TripProfileModel)model;
			log.debug("load model from cache:" + overAllTripModel.getSerieList() );
		}
		
		model =dataModelMap.findModel("ALL", "CostDriverModel"); 
		
		if (model == null){
			costDriverModel = new CostDriverModel(null); 
			dataModelMap.load("ALL", costDriverModel);			
		}
		else {
			costDriverModel = (CostDriverModel)model;
			log.debug("load model from cache:" + costDriverModel.getCostDriverChartByMonth("2011") );
		}
		
		
	}
	

	
	public TripProfileModel getOverAllTripModel(){
		return this.overAllTripModel;
	}

	public String getFy() {
		return fy;
	}

	private void setFy(String fy) {
		this.fy = fy;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public String showFyDetail(){
		this.updateRequestItem();
		log.debug("select fy:" + fy);
		return "showFyDetail";
	}

	public String showOrgDetail(){
		this.updateRequestItem();
		log.debug("select org:" + this.organization);
		log.debug("select office:" + this.office);
		
		Model model = this.dataModelMap.findModel(this.organization, "TripProfileModel");
		
		if (model == null){
			//create a new instance here;
			
			BasicDBObject match = new BasicDBObject("Organization", this.organization);
			orgModel = new TripProfileModel(match, GsaConstants.ORG_LEVEL_Office);
			orgModel.populate(); 
			dataModelMap.load(this.organization,  orgModel);			
			
		}else
		{
			this.orgModel = (TripProfileModel) model;
		}
		
		
		
		return "showOrgDetail";
	}
	
	public CostDriverModel getCostDriverModel() {
		return costDriverModel;
	}


	public UILabelMap getLabelMap() {
		return labelMap;
	}


	
	private void updateRequestItem(){
	
		log.debug(this.getParaName() + ":" + this.getParaValue());
			if (this.getParaName().equalsIgnoreCase("fy")){
				this.setFy(this.getParaValue());
			}
			if (this.getParaName().equalsIgnoreCase("organization")){
				this.setOrganization(this.getParaValue());
			}
			if (this.getParaName().equalsIgnoreCase("office")){
				this.setOffice(this.getParaValue());
			}
			
		
	}

	public TripProfileModel getOrgModel() {
		return orgModel;
	}

	
}
