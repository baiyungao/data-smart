package com.washingtongt.data.model.gsa.travelcard.car;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.time.FiscalYear;
import com.washingtongt.data.model.gsa.time.Month;
import com.washingtongt.ui.model.ChartSerie;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.ui.model.ModelHelper;
import com.washingtongt.ui.model.TableModel;

public class CardCarProfileModel extends Model {
	static final Logger log = Logger.getLogger(CardCarProfileModel.class);
	private static String UI_LINEBAR_CHART_MODEL = "UI_LINEBAR_CHART_MODEL";
	private static String UI_LINEBAR_CHART_MODEL_BUREAU = "UI_LINEBAR_CHART_MODEL_BUREAU";
	private static String UI_LINEBAR_CHART_MODEL_CITY = "UI_LINEBAR_CHART_MODEL_CITY";
	
	
	private static String MG_CAR_SUM_BY_CITY = "MG_CAR_SUM_BY_CITY";
	private static String MG_CAR_SUM_BY_BUREAU = "MG_CAR_SUM_BY_BUREAU";
	
	
	public CardCarProfileModel(BasicDBObject match){
		
		this.match = match;
		
		//remember to change date field for new model  ---
		FiscalYear fy2011 =new FiscalYear(CardCarConstants.IDT_P_DATE, match, 2011, 2010, 10, CardCarSummaryMeasure.class);
		fy2011.setName("2011");
		fy2011.setBenchmark(true);
		serieList.add(fy2011);
		
		FiscalYear fy2012 =new FiscalYear(CardCarConstants.IDT_P_DATE, match, 2012, 2011, 10, CardCarSummaryMeasure.class);
		fy2012.setName("2012");
		serieList.add(fy2012);		
		
		FiscalYear fy2013 =new FiscalYear(CardCarConstants.IDT_P_DATE, match, 2013, 2012, 10, CardCarSummaryMeasure.class);
		fy2013.setName("2013");
		serieList.add(fy2013);		
		
		this.setBenchmark(fy2011);
		
		BasicDBObject sortFields =  new BasicDBObject(CardCarConstants.IDT_T_AMOUNT, -1);
		
		Measurement airSumByCity = new CardCarSummaryMeasure(match,CardCarConstants.IDT_MERCHANT_CITY,sortFields,15);
		this.measurementMap.put(MG_CAR_SUM_BY_CITY, airSumByCity);
		
		sortFields =  new BasicDBObject(CardCarConstants.IDT_T_AMOUNT, -1);
		Measurement airSumByOffice = new CardCarSummaryMeasure(match,CardCarConstants.IDT_AGENCY_BUREAU_NAME,sortFields,15);
		this.measurementMap.put(MG_CAR_SUM_BY_BUREAU, airSumByOffice);

	}	
	
	
	//TODO: Refactor here
	@Override
	public boolean populate() {
		// TODO Auto-generated method stub
		for (Measurement measurement:this.measurementMap.values()){
			MongoUtil.getMeasurement(measurement);
		}
		return true;
	}	
	
	
	public LinePlusBarChartModel getSummaryChartByMonth(){
		LinePlusBarChartModel chart =null;
		
		Object object = this.uiModelMap.get(UI_LINEBAR_CHART_MODEL + this.getClass().getSimpleName());
		if (object != null) {
			return (LinePlusBarChartModel)object;
		}
		
		chart = new LinePlusBarChartModel();
	
		ChartSerie cost_serial = new ChartSerie(CardCarConstants.IDT_T_AMOUNT,ChartSerie.LEFT,true);
		ChartSerie ct_serial = new ChartSerie(CardCarConstants.IDT_C_COUNTS,ChartSerie.RIGHT,false);
		//ChartSeries days_serial = new ChartSeries(GsaConstants.IDT_DAYS_OF_TRIP,ChartSeries.RIGHT,false);
		
		chart.add(cost_serial);
		chart.add(ct_serial);
		//chart.add(days_serial);
		
		
		for (FiscalYear fy: serieList){
			for (int i = 0; i < 12; i++){
				Month month = fy.getMonth(i);
				BasicDBList result = month.getMeasurmentResults();  //one row result only
				if (result != null){
					BasicDBObject row = null;
					if (result.size() >0) {
						row = (BasicDBObject)result.get(0);
					}
					chart.addContent(row, month.getName(),CardCarConstants.INDEX_TC_AIR_SUMMARY_MEASURE);
				}
			}
		}
		return chart;
	}
	
	//summaryByCity
	
	public TableModel getSummaryByCity(){
		
		TableModel table = new TableModel(this.getMeasurementMap().get(MG_CAR_SUM_BY_CITY),"");
		log.debug("check the cols:" + table.getCols());
		return table;
	}	
	
	
	public LinePlusBarChartModel getSummaryChartByCity(){
		
		LinePlusBarChartModel chart =null;
		Object object = this.uiModelMap.get(UI_LINEBAR_CHART_MODEL_CITY + this.getClass().getSimpleName());
		if (object != null) {
			return (LinePlusBarChartModel)object;
		}
		
		chart = new LinePlusBarChartModel();
	
		ChartSerie cost_serial = new ChartSerie(CardCarConstants.IDT_T_AMOUNT);
		//ChartSerie ct_serial = new ChartSerie(CardCarConstants.IDT_C_COUNTS,ChartSerie.RIGHT,false);
		
		chart.add(cost_serial);
		//chart.add(ct_serial);
		
		BasicDBList results = this.getMeasurementMap().get(MG_CAR_SUM_BY_CITY).getResults();
		for (int i = 0; i < results.size();i++){
			BasicDBObject row = (BasicDBObject)results.get(i);
					chart.addContent(row,ModelHelper.getRowId(row) ,CardCarConstants.INDEX_TC_AIR_SUMMARY_MEASURE);
		}
		return chart;
	}	
	
	public TableModel getSummaryByBureau(){
		
		TableModel table = new TableModel(this.getMeasurementMap().get(MG_CAR_SUM_BY_BUREAU),"");
		log.debug("check the cols:" + table.getCols());
		return table;
	}	
	
	
	public LinePlusBarChartModel getSummaryChartByBureau(){
		
		LinePlusBarChartModel chart =null;
		Object object = this.uiModelMap.get( UI_LINEBAR_CHART_MODEL_BUREAU+ this.getClass().getSimpleName());
		if (object != null) {
			return (LinePlusBarChartModel)object;
		}
		
		chart = new LinePlusBarChartModel();
	
		ChartSerie cost_serial = new ChartSerie(CardCarConstants.IDT_T_AMOUNT);
		//ChartSerie ct_serial = new ChartSerie(CardCarConstants.IDT_C_COUNTS,ChartSerie.RIGHT,false);
		
		chart.add(cost_serial);
		//chart.add(ct_serial);
		
		BasicDBList results = this.getMeasurementMap().get(MG_CAR_SUM_BY_BUREAU).getResults();
		for (int i = 0; i < results.size();i++){
			BasicDBObject row = (BasicDBObject)results.get(i);
					chart.addContent(row,ModelHelper.getRowId(row) ,CardCarConstants.INDEX_TC_AIR_SUMMARY_MEASURE);
		}
		return chart;
	}		
	
}
