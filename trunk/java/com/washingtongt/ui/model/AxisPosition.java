package com.washingtongt.ui.model;

public enum AxisPosition {
	LEFT("left"), RIGHT("right");
	
	private String position;
	 
	private AxisPosition(String s) {
		position = s;
	}
 
	public String getPosition() {
		return position;
	}
}
