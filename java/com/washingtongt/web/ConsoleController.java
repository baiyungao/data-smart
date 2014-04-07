package com.washingtongt.web;

import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.CostDriverModel;
import com.washingtongt.data.model.gsa.GsaConstants;
import com.washingtongt.data.model.gsa.TripProfileModel;

@ManagedBean(name="controller")
@SessionScoped
public class ConsoleController {
	static final Logger log = Logger.getLogger(ConsoleController.class);
	private TripProfileModel overAllTripModel;
	private CostDriverModel costDriverModel;
	private String fy;           //parameter from request
	private String office;       //parameter from request
	private String organization; //parameter from requeste 
	private String requestItem;
	
	private UILabelMap labelMap = new UILabelMap();
	private UITripModelMap uiModelMap = UITripModelMap.getDefault();  //one instance
	
	private TripProfileModel orgModel  = null;
	
	public ConsoleController(){
		
		//all sessions share the data model to improve the performance
	
		
		Model model =uiModelMap.findModel("ALL", "TripProfileModel");
		
		if (model == null){
			overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
			overAllTripModel.populate(); 
			uiModelMap.load("ALL",  overAllTripModel);
		}
		else {
			overAllTripModel = (TripProfileModel)model;
			log.debug("load model from cache:" + overAllTripModel.getSerialList() );
		}
		
		model =uiModelMap.findModel("ALL", "CostDriverModel"); 
		
		if (model == null){
			costDriverModel = new CostDriverModel(null); 
			uiModelMap.load("ALL", costDriverModel);			
		}
		else {
			costDriverModel = (CostDriverModel)model;
			log.debug("load model from cache:" + costDriverModel.getCostDriverChartByMonth("2011") );
		}
		
	}
	
	public String getHello(){
		return "I will win";
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
		
		Model model = this.uiModelMap.findModel(this.organization, "TripProfileModel");
		
		if (model == null){
			//create a new instance here;
			BasicDBObject match = new BasicDBObject("Organization", this.organization);
			orgModel = new TripProfileModel(match, GsaConstants.ORG_LEVEL_Office);
			orgModel.populate(); 
			uiModelMap.load(this.organization,  orgModel);			
			
		}else
		{
			this.orgModel = (TripProfileModel) model;
		}
		
		
		
		return "showOrgDetail";
	}
	
	public CostDriverModel getCostDriverModel() {
		return costDriverModel;
	}

	private void setCostDriverModel(CostDriverModel costDriverModel) {
		this.costDriverModel = costDriverModel;
	}

	public UILabelMap getLabelMap() {
		return labelMap;
	}

	public String getRequestItem() {
		return requestItem;
	}

	public void setRequestItem(String requestItem) {
		this.requestItem = requestItem;
	}
	
	private void updateRequestItem(){
		if (this.requestItem != null){
			BasicDBObject object = (BasicDBObject) JSON.parse(this.requestItem);
			if (object != null){
				log.debug("object of request item:" + object);
				this.setFy(object.getString("fy"));
				this.setOrganization(object.getString("organization"));
				this.setOffice(object.getString("office"));
			}
			
		}
		
	}

	public TripProfileModel getOrgModel() {
		return orgModel;
	}

	
}
