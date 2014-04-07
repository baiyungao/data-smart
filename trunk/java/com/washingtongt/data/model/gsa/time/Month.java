package com.washingtongt.data.model.gsa.time;

import java.util.Date;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.Serie;

public class Month extends Serie{
	static final Logger log = Logger.getLogger(Month.class);	
	static final String[] names = {"JAN", "FEB", "MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC" };
	
	private int year;
	private int month;
	private Date start;
	private Date end;
	
	@Override
	public String getName(){
		return names[month-1] + " " + year;
	}
	
	public Month(FiscalYear fy, String field, BasicDBObject match,int _year, int _month, Class<? extends Measurement> mClass){
		this.year = _year;
		this.month = _month;
		this.setMeasurementClass(mClass);
		
		this.setParent(fy);
		this.setField(field);
		this.setMatch(match);
		//$gte: ISODate("2013-09-10T00:00:00.000Z")
		
		
		start = DateUtils.getStartTime(year, month);
		end = DateUtils.getEndTime(year, month);
				
		log.debug("create a new Month Serial: " + this);

	}
	
	
	
	public String toString(){
		return "Year:" + year + " Month:" + month;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}


	@Override
	public void updateMatch() {
		// TODO Auto-generated method stub
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




	
}
