package com.washingtongt.data.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Assume we have data for FY2011, this is the bench mark for cost analysis
 * 
 * 
 * @author gaob
 *
 */
public class Measurement {
	static final Logger log = Logger.getLogger(Measurement.class);
	private String name;
	private String description;
	private String groupby;
	private DBObject sortBy;
	private int  limits;
	
	private BasicDBList results;
	private boolean populated;
	
	private String[] indexNames;
	private Map<String, Indicator> indicators = new HashMap<String,Indicator>();


	private BasicDBObject matchFields =null;
	private BasicDBObject compositionValue = null;
	
	private String collection=null;  //where to get the data;
	

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



	
	public void addIndicator(String name, Indicator indicator){
		this.indicators.put(name, indicator);
		log.debug("add indicator:" + name + " indicatoros" + this.indicators.keySet());
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
	
	
	/**
	 * This method take in item in aggregation item, and find out what are the database records back on this data
	 * @return
	 */
	public BasicDBList getRecordSet(BasicDBObject id){
		
		if ((this.results == null)|| (this.results.isEmpty())){
			return null;
		}
		BasicDBObject selectMatch = null;
		if (this.getMatchFields()!= null) {
			selectMatch =(BasicDBObject)(this.getMatchFields().clone());
			}
		
		if (id!= null){

			for (String idKey : id.keySet()){
				if (idKey.equalsIgnoreCase("_id")) {
					continue;
				}

				if (selectMatch == null){
					selectMatch = new BasicDBObject(idKey, id.get(idKey));
				}
				else {
					selectMatch.append(idKey,id.get(idKey));
				}
			}

		}

		
		
		return null;
	}

	public void setResults(BasicDBList results) {
		this.results = results;
		//set values for each indicator
		
		for (int i = 0; i < results.size(); i++){
			BasicDBObject row = (BasicDBObject)(results.get(i));
			for(String key: this.indicators.keySet()){
				Indicator indicator = this.indicators.get(key);
				Object value = row.get(indicator.getLabel());
				indicator.addValue(value);
			}
		}
		
		this.setPopulated(true);
		
	}

	
	
	public boolean isPopulated() {
		return populated;
	}

	public void setPopulated(boolean populated) {
		this.populated = populated;
	}

	public BasicDBObject getCompositionValue() {
		return compositionValue;
	}

	public void setCompositionValue(BasicDBObject compositionValue) {
		this.compositionValue = compositionValue;
	}

	public BasicDBObject getMatchFields() {
		return matchFields;
	}

	public void setMatchFields(BasicDBObject matchFields) {
		this.matchFields = matchFields;
	}

	public String[] getIndexNames() {
		return indexNames;
	}

	public void setIndexNames(String[] indexNames) {
		this.indexNames = indexNames;
	}

	public DBObject getSortBy() {
		return sortBy;
	}

	public void setSortBy(DBObject sortBy) {
		this.sortBy = sortBy;
	}

	public int getLimits() {
		return limits;
	}

	public void setLimits(int limits) {
		this.limits = limits;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}
}
