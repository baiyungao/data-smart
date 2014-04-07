package com.washingtongt.data.model.gsa;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.time.FiscalYear;
import com.washingtongt.data.model.gsa.time.Month;
import com.washingtongt.ui.model.ChartSeries;
import com.washingtongt.ui.model.LineChartModel;
import com.washingtongt.ui.model.LinePlusBarChartModel;

public class CostDriverModel extends Model {

	static final Logger log = Logger.getLogger(CostDriverModel.class);

	public CostDriverModel(BasicDBObject match){

		this.match = match;


		BasicDBObject matchfy2011 = (match == null)? new BasicDBObject("FY","2011"):((BasicDBObject)match.clone()).append("FY","2011");
		BasicDBObject matchfy2012 = (match == null)? new BasicDBObject("FY","2012"):((BasicDBObject)match.clone()).append("FY","2012");
		BasicDBObject matchfy2013 = (match == null)? new BasicDBObject("FY","2013"):((BasicDBObject)match.clone()).append("FY","2013");


		FiscalYear fy2011 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2011, 2011, 2010, 10, TravelCostDriverMeasure.class);
		fy2011.setName("2011");
		fy2011.setBenchmark(true);
		serieList.add(fy2011);

		FiscalYear fy2012 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2012, 2012, 2011, 10, TravelCostDriverMeasure.class);
		fy2012.setName("2012");
		serieList.add(fy2012);		

		FiscalYear fy2013 =new FiscalYear(GsaConstants.IDT_DATE_DEPARTURE, matchfy2013, 2013, 2012, 10, TravelCostDriverMeasure.class);
		fy2013.setName("2013");
		serieList.add(fy2013);		

		this.setBenchmark(fy2011);

	}
	public LineChartModel getCostDriverChartYTDByMonth(String year){
		
		LineChartModel chart = new LineChartModel();
		ChartSeries meal_serial = new ChartSeries(GsaConstants.IDT_MEALS_EXPENSE);
		ChartSeries carrental_serial = new ChartSeries(GsaConstants.IDT_CAR_RENTAL_EXPENSE);
		ChartSeries lodging_serial = new ChartSeries(GsaConstants.IDT_LODGING_EXPENSE);
		ChartSeries air_serial = new ChartSeries(GsaConstants.IDT_AIR_EXPENSE);
		ChartSeries misc_serial = new ChartSeries(GsaConstants.IDT_MISC_EXPENSE);
		ChartSeries other_serial = new ChartSeries(GsaConstants.IDT_OTHER_EXPENSE);
		
		chart.add(meal_serial);
		chart.add(carrental_serial);
		chart.add(lodging_serial);
		chart.add(air_serial);
		chart.add(misc_serial);
		chart.add(other_serial);

		FiscalYear fy = this.getFYSerial(year);
		
		if ( fy != null){
			for (int i = 0; i < 12; i++){
				Month month = fy.getMonth(i);
				BasicDBList result = month.getMeasurmentResultsYTD();  //one row result only
				if (result != null){
					BasicDBObject row = (BasicDBObject)result.get(0);
					chart.addContent(row, month.getStart().getTime(), GsaConstants.INDEX_TRAVEL_COST_DRIVER_MEASURE);
				}
			}
		}
		return chart;
	}

public LineChartModel getCostReducePercentageYTDByMonth(String year){
		
		LineChartModel chart = new LineChartModel();
		ChartSeries meal_serial = new ChartSeries(GsaConstants.IDT_MEALS_EXPENSE);
		ChartSeries carrental_serial = new ChartSeries(GsaConstants.IDT_CAR_RENTAL_EXPENSE);
		ChartSeries lodging_serial = new ChartSeries(GsaConstants.IDT_LODGING_EXPENSE);
		ChartSeries air_serial = new ChartSeries(GsaConstants.IDT_AIR_EXPENSE);
		ChartSeries misc_serial = new ChartSeries(GsaConstants.IDT_MISC_EXPENSE);
		ChartSeries other_serial = new ChartSeries(GsaConstants.IDT_OTHER_EXPENSE);
		
		String name = "ALL";
		if (this.match != null){
			name = match.getString(GsaConstants.ORG_LEVEL_ORGANIZATION);
		}
		ChartSeries bench_serial = GsaModelHelper.getTotalExpenseReduceBenchMarkByMonth(name, year, 0);

		chart.add(bench_serial);
		chart.add(air_serial);
		chart.add(meal_serial);
		chart.add(lodging_serial);
		chart.add(misc_serial);
		chart.add(carrental_serial);
		chart.add(other_serial);
		
		
		FiscalYear fy = this.getFYSerial(year);
		
		if ( fy != null){
			for (int i = 0; i < 12; i++){
				Month month = fy.getMonth(i);
				Month bMonth = this.getBenchmark().getMonth(i);
				BasicDBList result = month.getMeasurmentResultsYTD();  //one row result only
				BasicDBList bResult = bMonth.getMeasurmentResultsYTD();  //one row result only
				if (result != null){
					BasicDBObject row = (BasicDBObject)result.get(0);
					BasicDBObject bRow = (BasicDBObject)bResult.get(0);
					chart.addContent(row, bRow, month.getStart().getTime(), GsaConstants.INDEX_TRAVEL_COST_DRIVER_MEASURE);
				}
			}
		}
		return chart;
	}
	
	
	
	public LinePlusBarChartModel getCostDriverChartByMonth(String year){
		LinePlusBarChartModel chart =null;
		
			
		chart = new LinePlusBarChartModel();
	
		ChartSeries meal_serial = new ChartSeries(GsaConstants.IDT_MEALS_EXPENSE,ChartSeries.LEFT,true);
		ChartSeries carrental_serial = new ChartSeries(GsaConstants.IDT_CAR_RENTAL_EXPENSE,ChartSeries.LEFT,true);
		ChartSeries lodging_serial = new ChartSeries(GsaConstants.IDT_LODGING_EXPENSE,ChartSeries.LEFT,true);
		ChartSeries air_serial = new ChartSeries(GsaConstants.IDT_AIR_EXPENSE,ChartSeries.LEFT,true);
		ChartSeries misc_serial = new ChartSeries(GsaConstants.IDT_MISC_EXPENSE,ChartSeries.LEFT,true);
		ChartSeries other_serial = new ChartSeries(GsaConstants.IDT_OTHER_EXPENSE,ChartSeries.LEFT,true);
		
		//ChartSeries ct_serial = new ChartSeries(GsaConstants.IDT_TRIP_CT,ChartSeries.RIGHT,false);
		//ChartSeries days_serial = new ChartSeries(GsaConstants.IDT_DAYS_OF_TRIP,ChartSeries.RIGHT,false);
		
		chart.add(meal_serial);
		chart.add(carrental_serial);
		chart.add(lodging_serial);
		chart.add(air_serial);
		chart.add(misc_serial);
		chart.add(other_serial);

		FiscalYear fy = this.getFYSerial(year);
		
		if ( fy != null){
			for (int i = 0; i < 12; i++){
				Month month = fy.getMonth(i);
				BasicDBList result = month.getMeasurmentResults();  //one row result only
				if (result != null){
					BasicDBObject row = (BasicDBObject)result.get(0);
					chart.addContent(row, month.getName(), GsaConstants.INDEX_TRAVEL_COST_DRIVER_MEASURE);
				}
			}
		}
		return chart;
	}

	private FiscalYear getFYSerial(String fy){
		
		for (FiscalYear year: this.serieList){
			
			if (year.getName().equalsIgnoreCase(fy)){
				return year;
			}
		}
		return null;
		
	}
}
