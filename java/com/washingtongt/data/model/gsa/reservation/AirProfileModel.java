package com.washingtongt.data.model.gsa.reservation;

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

public class AirProfileModel extends Model {
	static final Logger log = Logger.getLogger(AirProfileModel.class);
	
	private static String UI_LINEBAR_CHART_MODEL = "UI_LINEBAR_CHART_MODEL";
	private static String UI_LINEBAR_CHART_MODEL_AL = "UI_LINEBAR_CHART_MODEL_AL";
	
	private static String MG_AIR_SUM_BY_AIRLINE = "MG_AIR_SUM_BY_AIRLINE";
	private static String MG_AIR_SUM_BY_ORIGN = "MG_AIR_SUM_BY_ORIGN";
	private static String MG_AIR_SUM_BY_DESTINATION = "MG_AIR_SUM_BY_DESTINATION";
	private static String MG_AIR_SUM_BY_ROUTING = "MG_AIR_SUM_BY_ROUTING";
	
	private AirSummaryMeasure airsumByAirline;
	private AirSummaryMeasure airsumByOrign;
	private AirSummaryMeasure airsumByDestination;
	private AirSummaryMeasure airsumByRouting;
	
	public AirProfileModel(BasicDBObject match){
		
		this.match = match;
		FiscalYear fy2011 =new FiscalYear(AirConstants.IDT_D_DATE, match, 2011, 2010, 10, AirSummaryMeasure.class);
		fy2011.setName("2011");
		fy2011.setBenchmark(true);
		serieList.add(fy2011);
		
		FiscalYear fy2012 =new FiscalYear(AirConstants.IDT_D_DATE, match, 2012, 2011, 10, AirSummaryMeasure.class);
		fy2012.setName("2012");
		serieList.add(fy2012);		
		
		FiscalYear fy2013 =new FiscalYear(AirConstants.IDT_D_DATE, match, 2013, 2012, 10, AirSummaryMeasure.class);
		fy2013.setName("2013");
		serieList.add(fy2013);		
		
		this.setBenchmark(fy2011);
		
		BasicDBObject sortFields =  new BasicDBObject(AirConstants.IDT_T_AMOUNT, -1);
		
		airsumByAirline = new AirSummaryMeasure(match,AirConstants.IDT_A_NAME,sortFields,20);
		this.measurementMap.put(MG_AIR_SUM_BY_AIRLINE, airsumByAirline);
		
		sortFields =  new BasicDBObject(AirConstants.IDT_T_COUNTS, -1);
		
		this.airsumByOrign = new AirSummaryMeasure(match,AirConstants.IDT_O_CODE,sortFields,25);
		this.measurementMap.put(MG_AIR_SUM_BY_ORIGN, airsumByOrign);
		
		this.airsumByDestination = new AirSummaryMeasure(match,AirConstants.IDT_D_CODE,sortFields,25);
		this.measurementMap.put(MG_AIR_SUM_BY_DESTINATION, airsumByDestination);
		
		String routing = AirConstants.IDT_O_CODE+ "," + AirConstants.IDT_D_CODE;
		this.airsumByRouting = new AirSummaryMeasure(match,routing,sortFields,25);
		this.measurementMap.put(MG_AIR_SUM_BY_ROUTING, airsumByRouting);
		log.debug("aireby:" + this.airsumByAirline);
		
	}
	
	public LinePlusBarChartModel getSummaryChartByMonth(){
		LinePlusBarChartModel chart =null;
		
		Object object = this.uiModelMap.get(UI_LINEBAR_CHART_MODEL + this.getClass().getSimpleName());
		if (object != null) {
			return (LinePlusBarChartModel)object;
		}
		
		chart = new LinePlusBarChartModel();
	
		ChartSerie cost_serial = new ChartSerie(AirConstants.IDT_T_AMOUNT,ChartSerie.LEFT,true);
		ChartSerie ct_serial = new ChartSerie(AirConstants.IDT_T_COUNTS,ChartSerie.RIGHT,false);
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
					chart.addContent(row, month.getName(),AirConstants.INDEX_AIR_SUMMARY_CHART);
				}
			}
		}
		return chart;
	}
	
	public LinePlusBarChartModel getSummaryChartByAirline(){
		LinePlusBarChartModel chart =null;
		
		Object object = this.uiModelMap.get(UI_LINEBAR_CHART_MODEL_AL + this.getClass().getSimpleName());
		if (object != null) {
			return (LinePlusBarChartModel)object;
		}
		
		chart = new LinePlusBarChartModel();
	
		ChartSerie cost_serial = new ChartSerie(AirConstants.IDT_T_AMOUNT);
		//ChartSerie ct_serial = new ChartSerie(AirConstants.IDT_T_COUNTS,ChartSerie.RIGHT,false);
		//ChartSeries av_serial = new ChartSeries(AirConstants.IDT_AVG_PRICE,ChartSeries.RIGHT,false);
		//ChartSeries days_serial = new ChartSeries(GsaConstants.IDT_DAYS_OF_TRIP,ChartSeries.RIGHT,false);
		
		chart.add(cost_serial);
		//chart.add(ct_serial);
		//chart.add(av_serial);
		
		BasicDBList results = this.getAirsumByAirline().getResults();
		for (int i = 0; i < results.size();i++){
			BasicDBObject row = (BasicDBObject)results.get(i);
					chart.addContent(row,ModelHelper.getRowId(row) ,AirConstants.INDEX_AIR_SUMMARY_CHART);
		}
		return chart;
	}
	
	
	@Override
	public boolean populate() {
		// TODO Auto-generated method stub
		for (Measurement measurement:this.measurementMap.values()){
			MongoUtil.getMeasurement(measurement);
		}
		return true;
	}	
	
	public AirSummaryMeasure getAirsumByAirline(){
		return airsumByAirline;
	}

	
	public TableModel getSummaryByAirline(){
		
		TableModel table = new TableModel(this.airsumByAirline,"");
		log.debug("check the cols:" + table.getCols());
		return table;
	}
	
	public TableModel getSummaryByOrign(){
		
		TableModel table = new TableModel(this.airsumByOrign,"");
		log.debug("check the cols:" + table.getCols());
		
		//extends the table by the Geo Data
		/*
		for (DBObject row: table.getContents()){
			String airport = (String)row.get(AirConstants.IDT_O_CODE);
			//"Latitude" : -34.916667 , "Longitude" : -57.95
			BasicDBObject geoCode = AirportMap.airportMap.get(airport);
		}
		//
		*/
		
		return table;
	}
	
	public TableModel getSummaryByDestination(){
		
		TableModel table = new TableModel(this.airsumByDestination,"");
		log.debug("check the cols:" + table.getCols());
		return table;
	}	
	
	public TableModel getSummaryByRouting(){
		
		TableModel table = new TableModel(this.airsumByRouting,"");
		log.debug("check the cols:" + table.getCols());
		return table;
	}	

	public AirSummaryMeasure getAirsumByOrign() {
		return airsumByOrign;
	}

	public void setAirsumByOrign(AirSummaryMeasure airsumByOrign) {
		this.airsumByOrign = airsumByOrign;
	}

	public AirSummaryMeasure getAirsumByDestination() {
		return airsumByDestination;
	}

	public void setAirsumByDestination(AirSummaryMeasure airsumByDestination) {
		this.airsumByDestination = airsumByDestination;
	}

	public AirSummaryMeasure getAirsumByRouting() {
		return airsumByRouting;
	}

	public void setAirsumByRouting(AirSummaryMeasure airsumByRouting) {
		this.airsumByRouting = airsumByRouting;
	}	
}
