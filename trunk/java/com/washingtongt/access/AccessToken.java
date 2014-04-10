package com.washingtongt.access;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

public final class AccessToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AccessToken.class);
	private String id;
	private Date issueTime;
	private Date updateTime;
	private boolean valid;

	private static int EXP_DURATION_MIN = 30;
	
	public static AccessToken issue(String user){
		AccessToken token = new  AccessToken(user);
		
		return token;
		
	}

	public String getId(){
		return id;
	}
	
	private AccessToken(String user){
		id = user;
		issueTime = new Date();
		updateTime = new Date();
		valid = true;
		log.debug("issue token:" + id + " " + issueTime);
	}
	
	@Override
	public String toString(){
		return "secret token";
	}
	
	public void invalid(){
		valid = false;
	}
	
	public void update(){
	
		if (!this.isExpired()){
			updateTime = new Date(); 
		}
	}
	
	public boolean isExpired(){
		
		if (valid){
			Date now = new Date();
			return (now.getTime() - updateTime.getTime()) > (EXP_DURATION_MIN * 60 * 1000);
		}
		
		return false;
	}
	
	

}
