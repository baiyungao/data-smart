package com.washingtongt.data.model;


public class Indicator {
	
	private AggregationOperator op;
	private String[] factors;
	private String label;

	
	public Indicator (AggregationOperator op, String[] factors, String label){

		
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


	public String[] getFactors() {
		return factors;
	}


	public void setFactors(String[] factors) {
		this.factors = factors;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}
	
}
