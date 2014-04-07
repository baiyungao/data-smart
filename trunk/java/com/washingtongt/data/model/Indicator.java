package com.washingtongt.data.model;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;

public class Indicator {
	
	static final Logger log = Logger.getLogger(Indicator.class);
	
	private AggregationOperator op;
	private String factors;
	private String label;
	private Object parameter = 1;
	
	private boolean countIndicator = false;
	
	private BasicDBList value = new BasicDBList();

	public Indicator (AggregationOperator op, String factors){

		this.op = op;
		this.factors = factors;
		this.label = factors;
		
		if (op.equals(AggregationOperator.COUNT)){
			this.setCountIndicator(true);
			log.debug("it is a count index:" + label);
			
		}
	}	
	
	public Indicator (AggregationOperator op, String factors, String label){

		this.op = op;
		this.factors = factors;
		this.label = label;
		
		if (op.equals(AggregationOperator.COUNT)){
			this.setCountIndicator(true);
			log.debug("it is a count index:" + label);
			
		}
		
		//parameter = "$" + factors;
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

	public Object getParameter() {
		return parameter;
	}

	public boolean isComputing(){
		return (this.parameter.toString().length() > 1);
	}
	
	public void setParameter(Object paramenter) {
		this.parameter = paramenter;
	}
	
	public void addValue(Object value){
		//log.debug(this.label + "add value:" + value );
		this.value.add(value);
	}
	
	public BasicDBList getValue(){
		return this.value;
	}

	public boolean isCountIndicator() {
		return countIndicator;
	}

	public void setCountIndicator(boolean countIndicator) {
		this.countIndicator = countIndicator;
	}
	
}
