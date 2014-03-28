package com.washingtongt.data.model;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;

public class Indicator {
	
	static final Logger log = Logger.getLogger(Indicator.class);
	
	private AggregationOperator op;
	private String factors;
	private String label;
	private Object paramenter = 1;
	
	private BasicDBList value = new BasicDBList();

	public Indicator (AggregationOperator op, String factors, String label){

		this.op = op;
		this.factors = factors;
		this.label = label;
	}

	/***
	 * 
	 * @return
	 */
	
	public AggregationOperator getOp() {
		return op;
	}


	public void setOp(AggregationOperator op) {
		this.op = op;
	}


	public String getFactors() {
		return factors;
	}


	public void setFactors(String factors) {
		this.factors = factors;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}

	public Object getParamenter() {
		return paramenter;
	}

	public void setParamenter(Object paramenter) {
		this.paramenter = paramenter;
	}
	
	public void addValue(Object value){
		log.debug(this.label + "add value:" + value );
		this.value.add(value);
	}
	
	public BasicDBList getValue(){
		return this.value;
	}
	
}
