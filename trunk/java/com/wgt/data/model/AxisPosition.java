package com.wgt.data.model;

public enum AxisPosition {
	LEFT("left"), RIGHT("right");
	
	private String position;
	 
	private AxisPosition(String s) {
		position = s;
	}
 
	public String getStatusCode() {
		return position;
	}
}
