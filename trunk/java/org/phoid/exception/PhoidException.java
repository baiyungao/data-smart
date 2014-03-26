package org.phoid.exception;

public class PhoidException extends RuntimeException {

	private String message;
	public PhoidException(String meg, Exception ex) {
		// TODO Auto-generated constructor stub
		message = meg;
	}

	/**
	 *  the Root Class for all Exception in Phoid System
	 *  
	 */
	private static final long serialVersionUID = 1L;
	
	

}
