package com.washingtongt.data.model.gsa.time;

import java.util.Date;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.Serial;
import com.washingtongt.data.model.gsa.TravelCostMeasure;

public class FiscalYear extends Serial{

	
	static final Logger log = Logger.getLogger(FiscalYear.class);
	
	private int fiscalYear;
	private int startYear;
	private int startMonth;
	Month[] monthes = new Month[12];
	Quarter[] quarters = new Quarter[4];
	
	public FiscalYear (String field, BasicDBObject match, int fy, int stYear, int stMonth, Class<? extends Measurement> mClass){
		
		this.setMeasurementClass(mClass);
		
		this.setField(field);
		this.setMatch(match);
				
		this.setFiscalYear(fy);
		this.setStartYear(stYear);
		this.setStartMonth(stMonth);
		
		int year = stYear;
		int mon = stMonth;
		
		for (int i = 0; i< 12; i++){
			Month month = new Month(this, field, match, year, mon, this.getMeasurementClass());
			monthes[i] = month;

			
			mon++;
			if (mon > 12){
				year ++;
				mon = 1;
			}
			
			//prepare quarter;
			
			int qIndex = i/3;
			Quarter quarter = quarters[qIndex];
			if (quarter == null){
				quarter = new Quarter(this, field, match, this.getMeasurementClass());
				quarters[qIndex] = quarter;
			}
			quarter.addMonth(month);
		}
	}

	public int getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(int fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}
	
	public Month getMonth(int index){
		return monthes[index];
	}
	
	public Quarter getQuarter(int index){
		return quarters[index];
	}	

	@Override
	public Measurement getMeasurementYTD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateMatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getStart() {
		// TODO Auto-generated method stub
		return monthes[0]!=null?monthes[0].getStart():null;
	}

	@Override
	public Date getEnd() {
		// TODO Auto-generated method stub
		return monthes[11]!=null?monthes[11].getEnd():null;
	}
}
