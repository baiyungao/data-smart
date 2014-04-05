package com.washingtongt.data.model.gsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.Serial;
import com.washingtongt.data.model.SerialBase;
import com.washingtongt.data.model.gsa.time.FiscalYear;
import com.washingtongt.data.model.gsa.time.Month;
import com.washingtongt.ui.model.BarchartModel;
import com.washingtongt.ui.model.ChartSeries;
import com.washingtongt.ui.model.LinePlusBarChartModel;
import com.washingtongt.ui.model.TableModel;

/**
 * This model is repreent all details to profile trip data provide by GSA
 * 
 * 
 * the model contains all the fiscal year serial, for the sample it is FY2011, FY2012, FY2013
 * 
 * also the model contails  
 * 
 * 
 * @author gaob
 *
 */
public class TripProfileModel {
	
	static final Logger log = Logger.getLogger(TripProfileModel.class);
	
	private static String MODEL_TABLE_YEAR_SUMMARY = "YearSummaryTable";
	private static String MODEL_TABLE_SUMMARY_BY_ORG = "SummaryTableByOrg";
	private static String MODEL_TABLE_SUMMARY_BY_DESTINATION = "SummaryTableByDestination";
	
	private static String MODEL_CHART_SUMMARY_BY_MONTH = "SummaryChartByMonth";
	private static String MODEL_CHART_SUMMARY_BY_ORG = "SummaryChartByOrg";
	
	
	
	private List<FiscalYear> serialList = new ArrayList<FiscalYear>();
	
	private BasicDBObject match;
	private TravelSumaryMeasure tsmByOrganization;
	private TravelSumaryMeasure tsmByDestination;
	private String orgLevel; //organization or office
	private FiscalYear benchmark;
	
	private Map<String, Object> uiModelMap = new HashMap<String, Object>();
	
	public TripProfileModel(BasicDBObject match, String orgLevel){
		
		this.match = match;
		this.orgLevel = orgLevel;
		
		BasicDBObject matchfy2011 = (match == null)? new BasicDBObject("FY","2011"):((BasicDBObject)match.clone()).append("FY","2011");
		BasicDBObject matchfy2012 = (match == null)? new BasicDBObject("FY","2012"):((BasicDBObject)match.clone()).append("FY","2012");
		BasicDBObject matchfy2013 = (match == null)? new BasicDBObject("FY","2013"):((BasicDBObject)match.clone()).append("FY","2013");
		
		
		FiscalYear fy2011 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2011, 2011, 2010, 10, TravelSumaryMeasure.class);
		fy2011.setName("FY2011");
		fy2011.setBenchmark(true);
		serialList.add(fy2011);
		
		FiscalYear fy2012 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2012, 2012, 2011, 10, TravelSumaryMeasure.class);
		fy2012.setName("FY2012");
		serialList.add(fy2012);		
		
		FiscalYear fy2013 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2013, 2013, 2012, 10, TravelSumaryMeasure.class);
		fy2013.setName("FY2013");
		serialList.add(fy2013);		
		
		this.setBenchmark(fy2011);
		
		tsmByOrganization = new TravelSumaryMeasure();
		tsmByOrganization.setMatchFields(match);
		tsmByOrganization.setGroupby(orgLevel + ",FY");
		BasicDBObject sortFields =  new BasicDBObject(GsaConstants.IDT_FY, -1).append(GsaConstants.IDT_TOTAL_EXPENSE, -1);
		tsmByOrganization.setSortBy(sortFields);
		
		tsmByDestination = new TravelSumaryMeasure();
		tsmByDestination.setMatchFields(match);
		tsmByDestination.setGroupby(GsaConstants.FIELD_LOCATION);
		sortFields =  new BasicDBObject(GsaConstants.IDT_TOTAL_EXPENSE, -1);
		tsmByDestination.setSortBy(sortFields);
		tsmByDestination.setLimits(50);
		
	}
	
	public boolean populate(){

		MongoUtil.getMeasurement(this.tsmByOrganization);
		MongoUtil.getMeasurement(this.tsmByDestination);
		
		return true;
	}
	
	public List<FiscalYear> getSerialList(){
		return this.serialList;
	}

	public TravelSumaryMeasure getTsmByOrganization() {
		return tsmByOrganization;
	}

	public void setTsmByOrganization(TravelSumaryMeasure tsmByOrganization) {
		this.tsmByOrganization = tsmByOrganization;
	}

	public TravelSumaryMeasure getTsmByDestination() {
		return tsmByDestination;
	}

	public void setTsmByDestination(TravelSumaryMeasure tsmByDestination) {
		this.tsmByDestination = tsmByDestination;
	}

	public FiscalYear getBenchmark() {
		return benchmark;
	}

	public void setBenchmark(FiscalYear baseLine) {
		this.benchmark = baseLine;
	}
	
	/* the method to get the summary cost model to render to the bar plus line chart
	 * the chart 
	 * 
	 */
	
	
	public LinePlusBarChartModel getSummaryChartByMonth(){
		LinePlusBarChartModel chart =null;
		
		Object object = this.uiModelMap.get(MODEL_CHART_SUMMARY_BY_MONTH);
		if (object != null) {
			return (LinePlusBarChartModel)object;
		}
		
		chart = new LinePlusBarChartModel();
	
		ChartSeries cost_serial = new ChartSeries(GsaConstants.IDT_TOTAL_EXPENSE,ChartSeries.LEFT,true);
		ChartSeries ct_serial = new ChartSeries(GsaConstants.IDT_TRIP_CT,ChartSeries.RIGHT,false);
		//ChartSeries days_serial = new ChartSeries(GsaConstants.IDT_DAYS_OF_TRIP,ChartSeries.RIGHT,false);
		
		chart.add(cost_serial);
		chart.add(ct_serial);
		//chart.add(days_serial);
		
		
		for (FiscalYear fy: serialList){
			for (int i = 0; i < 12; i++){
				Month month = fy.getMonth(i);
				BasicDBList result = month.getMeasurmentResults();  //one row result only
				if (result != null){
					BasicDBObject row = (BasicDBObject)result.get(0);
					chart.addContent(row, month.getName());
				}
			}
		}
		return chart;
	}
	
	public BarchartModel getTripSummaryChartByOrg(){
		BarchartModel chart =null;
		
		Object object = this.uiModelMap.get(MODEL_CHART_SUMMARY_BY_ORG);
		if (object != null) {
			return (BarchartModel)object;
		}
		
		chart = new BarchartModel();
		chart.addContent(this.getSummaryTableByOrg());
		
		return chart;
	}

	public TableModel getYearSummaryTable(){
		TableModel model = null;
		
		Object object = this.uiModelMap.get(MODEL_TABLE_YEAR_SUMMARY);
		if (object != null) {
			return (TableModel)object;
		}
				
		
		model = new TableModel(GsaConstants.INDEX_TRAVEL_SUMMARY_MEASURE, GsaConstants.IDT_TRIP_CT, null);
		//add performance field here
		
		BasicDBList result = getBenchmark().getMeasurmentResults();
		BasicDBObject  row = (BasicDBObject)result.get(0);
		double benchmark = row.getDouble(GsaConstants.IDT_TOTAL_EXPENSE);
		
		
		for (SerialBase s: getSerialList())
		{
		BasicDBList results = s.getMeasurmentResults();
		model.addContent(results,s.getName(),GsaConstants.IDT_TOTAL_EXPENSE, benchmark);
		log.debug(s.getName() + ": "  + results);
		}
		return model;
	}
	
	public TableModel getSummaryTableByOrg(){
		TableModel model = null;
		Object object = this.uiModelMap.get(MODEL_TABLE_SUMMARY_BY_ORG);
		if (object != null) {
			return (TableModel)object;
		}
		model =	new TableModel(GsaConstants.INDEX_TRAVEL_SUMMARY_MEASURE, GsaConstants.IDT_TRIP_CT, GsaConstants.IDT_FY);
		BasicDBList results = this.getTsmByOrganization().getResults();
		model.addContentWithGroup(results, orgLevel, GsaConstants.IDT_FY );
		log.debug(this.getTsmByDestination().getDescription() + ": "  + model.getContents());
		return model;
	}
		
	public TableModel getSummaryTableByDestination(){
		TableModel model = null;
		
		Object object = this.uiModelMap.get(MODEL_TABLE_SUMMARY_BY_DESTINATION);
		if (object != null) {
			return (TableModel)object;
		}
				
		model =	new TableModel(GsaConstants.INDEX_TRAVEL_SUMMARY_MEASURE, GsaConstants.IDT_TRIP_CT,"");
		BasicDBList results = this.getTsmByDestination().getResults();
		model.addContent(results,"");
		log.debug(this.getTsmByDestination().getDescription() + ": "  + results);
		return model;
	}
			
	
}
