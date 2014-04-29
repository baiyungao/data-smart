package com.washingtongt.service;
 
/**
 * The message service is delivery various type of 
 * @author gaob
 *
 */
public interface MessageService {
	
	public boolean ping(String account);
	public boolean deliver(String account, String subject, String content);

}
