package com.washingtongt.data.model;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

/**
 * Assume we have data for FY2011, this is the bench mark for cost analysis
 * 
 * 
 * @author gaob
 *
 */
public class Measurement {

	private String name;
	private String description;
	private String groupby;
	
	private BasicDBList results;
	private boolean populated;
	
	private Map<String, Indicator> indicators = new HashMap<String,Indicator>();

	private Map<String, String> matchMap = new HashMap<String, String>();
	
	private BasicDBObject compositionValue = null;

	public String getName() {
		return name;
	}

	public void setName(String category) {
		this.name = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Indicator> getIndicators() {
		return indicators;
	}


	public Map<String, String> getMatchMap() {
		return matchMap;
	}

	public void setMatchMap(Map<String, String> matchMap) {
		this.matchMap = matchMap;
	}
	
	public void addIndicator(String name, Indicator indicator){
		this.indicators.put(name, indicator);
	}
	
	public void addMatch(String field, String condition){
		this.matchMap.put(field, condition);
	}

	public String getGroupby() {
		return groupby;
	}

	public void setGroupby(String groupby) {
		this.groupby = groupby;
	}

	public BasicDBList getResults() {
		return results;
	}

	public void setResults(BasicDBList results) {
		this.results = results;
		//set values for each indicator
		BasicDBList totalValue = new BasicDBList();
		
		
		for (int i = 0; i < results.size(); i++){
			BasicDBObject row = (BasicDBObject)(results.get(i));
			double rowTotal = 0;
			for(String key: this.indicators.keySet()){
				
				Indicator indicator = this.indicators.get(key);
				Object value = row.get(indicator.getLabel());
				indicator.addValue(value);
				rowTotal = rowTotal + (double)value;
			}
			
		  totalValue.add(rowTotal);	
		}
		this.compositionValue = new BasicDBObject("Total", totalValue);
		this.setPopulated(true);
		
	}

	
	
	public boolean isPopulated() {
		return populated;
	}

	public void setPopulated(boolean populated) {
		this.populated = populated;
	}
}
