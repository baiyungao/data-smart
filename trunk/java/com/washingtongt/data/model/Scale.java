package com.washingtongt.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Scale {
	
	public List<TimeUnit> units = new ArrayList<TimeUnit>();
	private Date start;
	private Date end;
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
	
	public List<TimeUnit> getUnits(){
		return this.units;
	}
	
	public void addUnit(TimeUnit unit){
		this.units.add(unit);
	}

}

