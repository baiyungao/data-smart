package com.washingtongt.data.model;

import java.util.Date;

import com.mongodb.BasicDBList;

public interface SerieBase {
	public static final int YEAR = 0;
	public static final int QUARTER = 1;
	public static final int MONTH = 2;
	public static final int DAY = 3;
	
	public void updateMatch();
	public Measurement getMeasurement();
	public Measurement getMeasurementYTD();
	public Date getStart();
	public Date getEnd();
	public String getName();
	public String getDisplayName();

	public BasicDBList getMeasurmentResults();
	public BasicDBList getMeasurmentResultsYTD();
	public boolean isBenchmark();

}
