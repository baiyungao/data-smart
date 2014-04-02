package com.washingtongt.data.model;

import java.util.Date;

public interface SerialBase {
	public static final int YEAR = 0;
	public static final int QUARTER = 1;
	public static final int MONTH = 2;
	public static final int DAY = 3;
	
	public void updateMatch();
	public Measurement getMeasurement();
	public Measurement getMeasurementYTD();
	public Date getStart();
	public Date getEnd();

}
