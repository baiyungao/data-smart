package com.washingtongt.data.model.gsa.time;

public class FiscalYear {

	final public static FiscalYear FY2011 = new FiscalYear(2011, 2010, 10);	
	final public static FiscalYear FY2012 = new FiscalYear(2012, 2011, 10);
	final public static FiscalYear FY2013 = new FiscalYear(2013, 2012, 10);
	final public static FiscalYear FY2014 = new FiscalYear(2013, 2013, 10);
	
	private int fiscalYear;
	private int startYear;
	private int startMonth;
	Month[] monthes = new Month[12];
	Quarter[] quarters = new Quarter[4];
	
	public FiscalYear (int fy, int stYear, int stMonth){
		this.setFiscalYear(fy);
		this.setStartYear(stYear);
		this.setStartMonth(stMonth);
		
		int year = stYear;
		int mon = stMonth;
		
		for (int i = 0; i< 12; i++){
			Month month = monthes[i];
			month.setYear(year);
			month.setMonth(mon);
			
			mon++;
			if (mon > 12){
				year ++;
				mon = 1;
			}
			
			int qIndex = i/4;
			quarters[qIndex].addMonth(month);
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
	
	
}
