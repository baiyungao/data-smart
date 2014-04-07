package com.washingtongt.data.model.gsa;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.gsa.time.FiscalYear;
import com.washingtongt.data.model.gsa.time.Month;
import com.washingtongt.ui.model.ChartSeries;
import com.washingtongt.web.UITripModelMap;

public class GsaModelHelper {
	
	static public ChartSeries getTotalExpenseReduceBenchMarkByMonth(String name, String year, int serieIndex ){
	
		
		TripProfileModel model = (TripProfileModel) UITripModelMap.getDefault().findModel(name, "TripProfileModel");
		//TODO: General Serial here.
		
		FiscalYear benchMark = model.getBenchmark();
		FiscalYear fy = model.getFiscalYear(year);
		
		ChartSeries bSerie = new ChartSeries("Expenses BM FY" + year);
		
		for (int i = 0; i < 12; i++){
			Month month = fy.getMonth(i);
			Month bMonth = benchMark.getMonth(i);
			BasicDBList result = month.getMeasurmentResultsYTD();  //one row result only
			BasicDBList bResult = bMonth.getMeasurmentResultsYTD();  //one row result only
			if (result != null){
				BasicDBObject row = (BasicDBObject)result.get(0);
				BasicDBObject bRow = (BasicDBObject)bResult.get(0);
				bSerie.addContent(row, bRow, month.getStart().getTime(), GsaConstants.IDT_TOTAL_EXPENSE, serieIndex);
				
			}
		}
		
		
		return bSerie;
		
	}
	


}
