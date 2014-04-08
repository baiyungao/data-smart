package com.washingtongt.web;

public class WebController {

	protected String requestItem;
	protected UILabelMap labelMap = new UILabelMap();
	protected DataModelMap dataModelMap = DataModelMap.getDefault();

	public WebController() {
		super();
	}

	public String getHello() {
		return "I will win";
	}

}