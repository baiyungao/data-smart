package com.washingtongt.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.gsa.time.FiscalYear;

public class Model  {

	protected List<FiscalYear> serieList = new ArrayList<FiscalYear>();
	protected BasicDBObject match;
	private FiscalYear benchmark;
	protected Map<String, Object> uiModelMap = new HashMap<String, Object>();

	public Model() {
		super();
	}

	public List<FiscalYear> getSerieList() {
		return this.serieList;
	}

	public FiscalYear getBenchmark() {
		return benchmark;
	}

	public FiscalYear getFiscalYear(String year) {
		
			if (this.getSerieList()!=null){
			
			for (FiscalYear fyear: this.getSerieList()){
				
				if (fyear.getName().equalsIgnoreCase(year)){
					return fyear;
				}
				
			}
		}
		return null;
	}

	public void setBenchmark(FiscalYear baseLine) {
		this.benchmark = baseLine;
	}

}