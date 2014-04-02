package com.washingtongt.data.model;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;

public abstract class Serial implements SerialBase{
	
	static final Logger log = Logger.getLogger(Serial.class);
	
	
	private Class<? extends Measurement> measurementClass;
	protected Measurement measurement;
	private Serial parent;
	private BasicDBObject match;
	private String field;

	@Override
	public Measurement getMeasurement() {
		this.measurement.setMatchFields((BasicDBObject)match.clone());
		this.updateMatch();
		return measurement;
	}


	public void setMeasurement(Measurement measurement) {
		this.measurement = measurement;
	}

	public Serial getParent() {
		return parent;
	}

	public void setParent(Serial parent) {
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

}
