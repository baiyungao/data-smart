package com.washingtongt.data.model.gsa;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.MongoUtil;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.SerieBase;
import com.washingtongt.data.model.gsa.time.FiscalYear;
import com.washingtongt.data.model.gsa.time.Month;
import com.washingtongt.ui.model.BarchartModel;
import com.washingtongt.ui.model.ChartSerie;
import com.washingtongt.ui.model.LineChartModel;
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
public class TripProfileModel extends Model {
	
	static final Logger log = Logger.getLogger(TripProfileModel.class);
	
	private static String MODEL_TABLE_YEAR_SUMMARY = "YearSummaryTable";
	private static String MODEL_TABLE_SUMMARY_BY_ORG = "SummaryTableByOrg";
	private static String MODEL_TABLE_SUMMARY_BY_DESTINATION = "SummaryTableByDestination";
	private static String MODEL_TABLE_COST_DRIVER = "CostDriverTable";
	
	private static String MODEL_CHART_SUMMARY_BY_MONTH = "SummaryChartByMonth";
	private static String MODEL_CHART_SUMMARY_BY_ORG = "SummaryChartByOrg";
	
	
	
	private TravelSumaryMeasure tsmByOrganization;
	private TravelSumaryMeasure tsmByDestination;
	private TravelCostDriverMeasure costDrivers;
	
	private String orgLevel; //organization or office
	public TripProfileModel(BasicDBObject match, String orgLevel){
		
		this.match = match;
		this.orgLevel = orgLevel;
		
		BasicDBObject matchfy2011 = (match == null)? new BasicDBObject("FY","2011"):((BasicDBObject)match.clone()).append("FY","2011");
		BasicDBObject matchfy2012 = (match == null)? new BasicDBObject("FY","2012"):((BasicDBObject)match.clone()).append("FY","2012");
		BasicDBObject matchfy2013 = (match == null)? new BasicDBObject("FY","2013"):((BasicDBObject)match.clone()).append("FY","2013");
		
		
		FiscalYear fy2011 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2011, 2011, 2010, 10, TravelSumaryMeasure.class);
		fy2011.setName("2011");
		fy2011.setBenchmark(true);
		serieList.add(fy2011);
		
		FiscalYear fy2012 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2012, 2012, 2011, 10, TravelSumaryMeasure.class);
		fy2012.setName("2012");
		serieList.add(fy2012);		
		
		FiscalYear fy2013 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2013, 2013, 2012, 10, TravelSumaryMeasure.class);
		fy2013.setName("2013");
		serieList.add(fy2013);		
		
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
		
		
		costDrivers = new TravelCostDriverMeasure();
		costDrivers.setMatchFields(match);
		costDrivers.setGroupby("FY");
		sortFields =  new BasicDBObject(GsaConstants.IDT_FY, 1);
		costDrivers.setSortBy(sortFields);
		
	}
	
	public boolean populate(){

		MongoUtil.getMeasurement(this.tsmByOrganization);
		MongoUtil.getMeasurement(this.tsmByDestination);
		MongoUtil.getMeasurement(this.costDrivers);
		
		return true;
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
	
		ChartSerie cost_serial = new ChartSerie(GsaConstants.IDT_TOTAL_EXPENSE,ChartSerie.LEFT,true);
		ChartSerie ct_serial = new ChartSerie(GsaConstants.IDT_TRIP_CT,ChartSerie.RIGHT,false);
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
					chart.addContent(row, month.getName(),GsaConstants.INDEX_TRAVEL_SUMMARY_CHART);
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
		Set<String> exs = new  HashSet<String>();
		for (String ex: GsaConstants.INDEX_TOTAL_COST_CHART_EX){
			exs.add(ex);
		}
		
		chart.addContent(this.getSummaryTableByOrg(),exs);
		
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
		BasicDBObject  row = ((result!= null)&&(result.size() > 0))?(BasicDBObject)result.get(0):null;
		double benchmark = (row != null)?row.getDouble(GsaConstants.IDT_TOTAL_EXPENSE):0;
		
		
		for (SerieBase s: getSerieList())
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

	public TravelCostDriverMeasure getCostDrivers() {
		return costDrivers;
	}

	public void setCostDrivers(TravelCostDriverMeasure costDrivers) {
		this.costDrivers = costDrivers;
	}
	
	public TableModel getCostDriverTable(){
		
		TableModel model = null;
		
		Object object = this.uiModelMap.get(MODEL_TABLE_COST_DRIVER);
		if (object != null) {
			return (TableModel)object;
		}
				
		model =	new TableModel(GsaConstants.INDEX_TRAVEL_COST_DRIVER_MEASURE, "","");
		model.addContent(this.costDrivers.getResults(), GsaConstants.IDT_FY);
		model.setOrderById(true);
		return model;
	}
	
public LineChartModel getCostReducePercentageYTDByMonth(){
		
		LineChartModel chart = new LineChartModel();
	
		String name = "ALL";
		if (this.match != null){
			name = match.getString(GsaConstants.ORG_LEVEL_ORGANIZATION);
		}
		
		ChartSerie bench_serial = GsaModelHelper.getTotalExpenseReduceBenchMarkByMonth("ALL", "2012", 0); //bench mark
		ChartSerie org_serial = GsaModelHelper.getTotalExpenseReduceBenchMarkByMonth(name, "2012", 1); //org
		org_serial.setKey("2012" + GsaConstants.IDT_TOTAL_EXPENSE + " (organization)");
		bench_serial.setKey("2012" + GsaConstants.IDT_TOTAL_EXPENSE + " (GSA)");
		
		ChartSerie bench_serial_1 = GsaModelHelper.getTotalExpenseReduceBenchMarkByMonth("ALL", "2013", 0); //bench mark
		ChartSerie org_serial_1 = GsaModelHelper.getTotalExpenseReduceBenchMarkByMonth(name, "2013", 1); //org
		org_serial_1.setKey("2013" + GsaConstants.IDT_TOTAL_EXPENSE + " (organization)");
		bench_serial_1.setKey("2013" + GsaConstants.IDT_TOTAL_EXPENSE  + " (GSA)");
		
		//bench_serial.getValue().addAll(bench_serial_1.getValue());
		//org_serial.getValue().addAll(org_serial_1.getValue());
		
		chart.add(bench_serial);
		chart.add(org_serial);
		chart.add(bench_serial_1);
		chart.add(org_serial_1);
		
		return chart;
	}
	
			
	
}
