package com.washingtongt.data.model.gsa.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.Serie;

public class Quarter extends Serie {
	static final Logger log = Logger.getLogger(Quarter.class);
	private Date start;
	private Date end;
	
	List<Month> monthes = new ArrayList<Month>();
	
	
	public Quarter(FiscalYear fy, String field, BasicDBObject match, Class<? extends Measurement> mClass){
		
		this.setMeasurementClass(mClass);
		this.setParent(fy);
		this.setField(field);
		this.setMatch(match);
		log.debug("Creat a new Quarter Serial");
	}
	
	
	public void addMonth(Month month){
		monthes.add(month);
		
		if (this.start == null) {
			this.start = month.getStart();
		}else {
			if (this.start.after(month.getStart())){
				this.start = month.getStart();
			}
		}
		
		if (this.end == null){
			this.end = month.getEnd();
		}else {
			
			if (this.end.before(month.getEnd())){
				this.end = month.getEnd();
			}
			
		} 
			
		
	}

	@Override
	public void updateMatch() {
	
		BasicDBObject matchFields =  this.measurement.getMatchFields();
		
		BasicDBObject timeRange = new BasicDBObject("$gte",start ).append("$lt",end);
		//BasicDBObject timeMatch = new BasicDBObject(this.getField(), timeRange);
		if (matchFields != null) {
			matchFields.append(this.getField(), timeRange);
		}
		else {
			matchFields = new BasicDBObject(this.getField(), timeRange);;
		}
		log.debug("matchfields: " + matchFields);		
		
	}

	@Override
	public Measurement getMeasurementYTD() {
		// TODO Auto-generated method stub
		this.measurementYTD = null;
		try {
			this.measurementYTD = this.getMeasurementClass().newInstance();
			this.measurementYTD.setMatchFields((BasicDBObject)this.getMatch().clone());

			BasicDBObject matchFields =  this.measurementYTD.getMatchFields();
			
			BasicDBObject timeRange = new BasicDBObject("$gte",this.getParent().getStart()).append("$lt",this.getEnd());
			//BasicDBObject timeMatch = new BasicDBObject(this.getField(), timeRange);
			if (matchFields != null) {
				matchFields.append(this.getField(), timeRange);
			}
			else {
				matchFields = new BasicDBObject(this.getField(), timeRange);;
			}
			log.debug("matchfields: " + matchFields);			
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			log.error(e);
		}
		return this.measurementYTD;
	}



	@Override
	public Date getStart() {
		// TODO Auto-generated method stub
		return start;
	}


	@Override
	public Date getEnd() {
		// TODO Auto-generated method stub
		return end;
	}

}
