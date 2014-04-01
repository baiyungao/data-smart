package com.washingtongt.data.model.gsa.time;

import java.util.ArrayList;
import java.util.List;

public class Quarter {
	List<Month> monthes = new ArrayList<Month>();
	
	public void addMonth(Month month){
		monthes.add(month);
	}
}
