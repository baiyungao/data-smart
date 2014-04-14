package com.washingtongt.data.model;

import java.util.Date;

import com.mongodb.BasicDBObject;

public interface TimeUnit {
	
	public Date getStart();
	public Date getEnd();
	public BasicDBObject getValue();
	
}
