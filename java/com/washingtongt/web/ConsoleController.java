package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.Logger;

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
	
	private UILabelMap labelMap = new UILabelMap();
	
	public ConsoleController(){
		overAllTripModel = new TripProfileModel(null, GsaConstants.ORG_LEVEL_ORGANIZATION);
		overAllTripModel.populate(); 
		
		setCostDriverModel(new CostDriverModel(null));
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

	public void setFy(String fy) {
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
		
		log.debug("select fy:" + fy);
		return "showFyDetail";
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
	
}
