package com.washingtongt.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.gsa.time.FiscalYear;
import com.washingtongt.ui.model.TableModel;

public abstract class Model  {

	protected List<FiscalYear> serieList = new ArrayList<FiscalYear>();
	protected BasicDBObject match;
	private FiscalYear benchmark;
	protected Map<String, Object> uiModelMap = new HashMap<String, Object>();
	protected Map<String, Measurement> measurementMap = new HashMap<String, Measurement>();

	private static String UI_MODEL_TABLE_SERIES_LIST = "UI_MODEL_TABLE_SERIES_LIST";

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

	public TableModel getSerialListTable(String[] indexes, String pIndex){
		TableModel model = null;

		Object object = this.uiModelMap.get(UI_MODEL_TABLE_SERIES_LIST);

		if (object != null) {
			return (TableModel)object;
		}


		model = new TableModel(indexes, pIndex, null);
		//add performance field here

		BasicDBList result = getBenchmark().getMeasurmentResults();
		BasicDBObject  row = (BasicDBObject)result.get(0);
		double benchmark = row.getDouble(pIndex);


		for (SerieBase s: getSerieList())
		{
			BasicDBList results = s.getMeasurmentResults();
			model.addContent(results,s.getName(),pIndex, benchmark);
		}
		return model;
	}	
	
	abstract public boolean populate();

	protected Map<String, Measurement> getMeasurementMap() {
		return measurementMap;
	}

	

}