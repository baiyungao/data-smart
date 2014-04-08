package com.washingtongt.data.model.gsa;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.washingtongt.data.model.Model;
import com.washingtongt.data.model.gsa.time.FiscalYear;
import com.washingtongt.data.model.gsa.time.Month;
import com.washingtongt.ui.model.ChartSerie;
import com.washingtongt.web.DataModelMap;

public class GsaModelHelper {
	
	static public ChartSerie getTotalExpenseReduceBenchMarkByMonth(String name, String year, int serieIndex ){
	
		
		Model model = (Model) DataModelMap.getDefault().findModel(name, "TripProfileModel");
		//TODO: General Serial here.
		
		FiscalYear benchMark = model.getBenchmark();
		FiscalYear fy = model.getFiscalYear(year);
		
		ChartSerie bSerie = new ChartSerie(GsaConstants.IDT_TOTAL_EXPENSE);
		
		for (int i = 0; i < 12; i++){
			Month month = fy.getMonth(i);
			Month bMonth = benchMark.getMonth(i);
			BasicDBList result = month.getMeasurmentResultsYTD();  //one row result only
			BasicDBList bResult = bMonth.getMeasurmentResultsYTD();  //one row result only
			if (result != null){
				BasicDBObject row = null;
				if (result.size()  >0){
					row = (BasicDBObject)result.get(0);
				 }
				BasicDBObject bRow = (BasicDBObject)bResult.get(0);
				bSerie.addContent(row, bRow, month.getStart().getTime(), GsaConstants.IDT_TOTAL_EXPENSE, serieIndex);
				
			}
		}
		
		
		return bSerie;
		
	}
	


}
