package com.wgt.access;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


public class AccessAuthFilter implements Filter {
	/**
	 * Logger
	 */
	static final Logger log = Logger.getLogger(AccessAuthFilter.class);
	
	private FilterConfig filterConfig = null;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
   
	public void destroy() {
		this.filterConfig = null;
	}
   
	@Override
	public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain)  
	throws IOException, ServletException {
	
		try {
			HttpServletRequest hRequest= (HttpServletRequest)request;
			HttpServletResponse hResponse = (HttpServletResponse)response;
			boolean ret = AccessAuthManager.getDefault().processRequest(hRequest, hResponse);
			if (ret) {
				chain.doFilter(request, response);
				return;
			}
		} finally {
			
		}
		return;
	}



}
