package com.washingtongt.utils;
import javax.script.*;

import org.apache.log4j.Logger;
import org.renjin.sexp.DoubleVector;

public class Rtest {
	static final Logger log = Logger.getLogger(Rtest.class);
	
	public static void main(String[] args){
		
		 // create a script engine manager
	    ScriptEngineManager factory = new ScriptEngineManager();
	    // create a Renjin engine
	    ScriptEngine engine = factory.getEngineByName("Renjin");
	    // evaluate R code from String, cast SEXP to a DoubleVector and store in the 'res' variable
	    DoubleVector res = null;
		try {
			res = (DoubleVector)engine.eval("a <- 2; b <- 3; a*b");
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("The result of a*b is: " + res);   		
		
	}

}
