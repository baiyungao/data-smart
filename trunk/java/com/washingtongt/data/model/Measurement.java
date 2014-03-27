package com.washingtongt.data.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Assume we have data for FY2011, this is the bench mark for cost analysis
 * 
 * 
 * @author gaob
 *
 */
public class Measurement {

	private String category;
	private String description;
	
	private Map<String, Object> indicators = new HashMap<String,Object>();
	private Date start;
	private Date end;
	
	
}
