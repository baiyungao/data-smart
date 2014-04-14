package com.washingtongt.data.model;

import java.util.Date;

import com.mongodb.BasicDBObject;

public class DayUnit implements TimeUnit{
	
	private Date start;
	private Date end;
	
	@Override
	public Date getStart() {
		// TODO Auto-generated method stub
		return this.start;
	}

	@Override
	public Date getEnd() {
		// TODO Auto-generated method stub
		return this.end;
	}

	@Override
	public BasicDBObject getValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString(){
		return start + "," + end;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	

}
