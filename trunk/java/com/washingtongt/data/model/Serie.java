package com.washingtongt.data.model;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.MongoUtil;

public abstract class Serie implements SerieBase{
	
	static final Logger log = Logger.getLogger(Serie.class);
	
	
	private Class<? extends Measurement> measurementClass;
	protected Measurement measurement;
	protected Measurement measurementYTD;
	private Serie parent;
	private BasicDBObject match;
	private String field;
	private String name;
	private boolean isBenchmark;

	@Override
	public Measurement getMeasurement() {
		this.measurement.setMatchFields((BasicDBObject)match.clone());
		this.updateMatch();
		return measurement;
	}


	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public Serie getParent() {
		return parent;
	}

	public void setParent(Serie parent) {
		this.parent = parent;
	}


	public BasicDBObject getMatch() {
		return match;
	}

	public void setMatch(BasicDBObject match) {
		this.match = match;
	}

	public Class<? extends Measurement> getMeasurementClass() {
		return measurementClass;
	}

	public void setMeasurementClass(Class<? extends Measurement> measurementClass) {
		this.measurementClass = measurementClass;
		try {
			this.setMeasurement(measurementClass.newInstance());
		}  catch (InstantiationException e) {
			// TODO Auto-generated catch block
			log.error("instance measurement error:" + e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error("IllegalAccess Error:" + e);
		}		
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Override
	public BasicDBList getMeasurmentResults(){
		BasicDBList result = null;
		if (this.measurement !=null){
			result =  this.measurement.getResults();
			if (result == null) {
				result =  MongoUtil.getMeasurement(this.getMeasurement());
			}
		}
		return result;
	}
	
	@Override
	public BasicDBList getMeasurmentResultsYTD(){
		BasicDBList result = null;
		if (this.measurementYTD == null){
			this.measurementYTD = this.getMeasurementYTD();
		}
		if (this.measurementYTD !=null){
			result =  this.measurementYTD.getResults();
			if (result == null) {
				result =  MongoUtil.getMeasurement(this.measurementYTD);
			}
		}
		return result;	
	}
	@Override
	public String getDisplayName(){
		return this.getName() + (this.isBenchmark()?"-BK" :"");
	}	
	
	@Override
	public boolean isBenchmark() {
		return isBenchmark;
	}


	public void setBenchmark(boolean isBenchmark) {
		this.isBenchmark = isBenchmark;
	}
	

/*
	@Override
	public boolean populate() {

		MongoUtil.getMeasurement(this.getMeasurement());
		MongoUtil.getMeasurement(this.getMeasurementYTD());

		return false;
	}	
	*/
}
