package com.washingtongt.web;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.washingtongt.data.model.Model;

/**
 *  The model repository for the application, consider currently all data are same
 * 
 */

public class UITripModelMap extends HashMap<String, Model>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(UITripModelMap.class);
	
	private static UITripModelMap instance = new UITripModelMap();
	private UITripModelMap(){
		
	}
	
	public static UITripModelMap getDefault(){
		return instance;
	}

	public Model findModel(String name, String klass){
		Model model = (Model)this.get(this.generateKey(name,klass));
		
		log.debug("find model: " + name + "  " + klass + " model:" + model);
		
		return model;
		}
		
	
	private String generateKey(String name, String klass){
		
		return name + ":" + klass;
	}
	public void load(String name, Model model){
		
		this.put(this.generateKey(name,model.getClass().getSimpleName()),model);
		log.debug("load model: " + this.keySet());
	}
	
}
