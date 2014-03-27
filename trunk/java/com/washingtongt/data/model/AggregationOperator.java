package com.washingtongt.data.model;

public enum AggregationOperator {
	
	SUM("$sum"), AVG("$avg"), MIN("$min"), MAX("$max"), COUNT("1");
	
	String op;
	AggregationOperator(String _op){
		this.op = _op;
	}
	public String getOp() {
		return op;
	}
}
