package com.washingtongt.access;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.ClientBuilder;

import org.apache.log4j.Logger;

import com.washingtongt.utils.LcdUtils;

/**
 * The class responsible for 
 * @author gaob
 *
 */
public class AccessAuthManager {

	private static final Logger log = Logger.getLogger(AccessAuthManager.class);

	private static String POST_AUTH_URL = "/login/postauth.wgt";
	private static String REQ_ACCESS_URL = "/login/req.access.wgt";
	public static String WEB_AUTH_URL = "/login/login.jsf";
	
	public static String WEB_HOME_URL = "/console/dashboard.jsf";
	private static String REDIRECT_URL = "redirect_url";
	public static String TOKEN_STRING = "WGTXM_Tokan";
	

	private static Set<String> unAuthPaths = new HashSet<String>();
	private static AccessAuthManager instance = null;
	private static HashMap<String, String> requestAccessMap = new HashMap<String, String>();

	static {
		unAuthPaths.add("/public/");
		unAuthPaths.add("/help/");
		unAuthPaths.add("/data/");
		unAuthPaths.add("/login/");
		unAuthPaths.add("/styles/");
		unAuthPaths.add("/images/");
		unAuthPaths.add("/imgs/");
		unAuthPaths.add("/js/");
		unAuthPaths.add("/nvd3/");
		unAuthPaths.add("/nvd3/");
	}

	public static synchronized AccessAuthManager getDefault(){
		if (instance == null){
			instance = new AccessAuthManager();
		}
		return instance;
	}

	private AccessAuthManager(){

	}

	/************************************************
	 *  Business logic to handle the authentication logic
	 *  
	 *  1. if the request path in the unauthPath set, go ahead to process
	 *  2. if the request path is postAuth flag, redirect page to the URL remember in previous re
	 *  3. otherwise, check if the valid token in the session or not
	 *  4. remember current request for redirect purpose
	 *  5. redirect to login page for user authentication
	 *  
	 *  
	 */

	public boolean processRequest(HttpServletRequest hRequest, HttpServletResponse hResponse){
		//TODO here for auth acess
		String path = hRequest.getContextPath();
		String url = hRequest.getRequestURI();
		log.debug("request path:" + path + " url" + url + " unprotected: " + this.isUnauthPath(url, path));

		HttpSession session = hRequest.getSession(true);
		AccessToken token = (AccessToken)session.getAttribute(TOKEN_STRING);

		log.debug("got token:" + (token==null?  "None":" expired:" + token.isExpired()));
		
		if ((token!=null) &&(!token.isExpired())){
			token.update(); 
			if ((url.equals(path + WEB_AUTH_URL ))||(url.equals(path + POST_AUTH_URL ))||(url.equals(path + REQ_ACCESS_URL ))){
				redirectPage(hResponse, path + WEB_HOME_URL);
				return false;
			}
			return true;
		}
		//request access logic
		if (url.equals(path + REQ_ACCESS_URL )){
			return this.requestAccess(hRequest,hResponse);
			
		}		
		
		
		//authentication logic
		if (url.equals(path + POST_AUTH_URL )){
			//do auth and redirect
			session = hRequest.getSession(true);
			String redirectURL = (String)session.getAttribute(REDIRECT_URL);
			
			String name = hRequest.getParameter("j_username");
			String password = hRequest.getParameter("j_password");
			
			String tknString = hRequest.getParameter(TOKEN_STRING);
			log.debug("token:" + tknString);
			if (tknString != null){
				
				String id = requestAccessMap.get(tknString);
				if (id != null){
					token = AccessToken.issue(id);
					session.setAttribute(TOKEN_STRING, token);
					requestAccessMap.remove(tknString);
					redirectPage(hResponse, path + WEB_HOME_URL);
					return false;
				}
			}

			if (this.auth(name, password)) {
				token = AccessToken.issue(name);
				session.setAttribute(TOKEN_STRING, token);
				log.debug("login passed, go ahead token: expired" + token.isExpired()+ " "+ redirectURL);
				redirectPage(hResponse, path + WEB_HOME_URL);
				return false;
			}
			else {
				redirectPage(hResponse, path + WEB_AUTH_URL);
				log.error("login error:" + name + " " + password + " from:" + hRequest.getRemoteAddr() + " host:" + hRequest.getRemoteHost());
			}
			
			return false;
		}

		if (!this.isUnauthPath(url, path)){
			
			session = hRequest.getSession(true);
			session.setAttribute(REDIRECT_URL, url);
			String loginUrl = path + AccessAuthManager.WEB_AUTH_URL;
			log.debug("protected page: login needed" + loginUrl);
			redirectPage(hResponse, loginUrl);
			return false;
		}
		else {
			return true;
		}
	}

	
	private boolean requestAccess(HttpServletRequest hRequest,  HttpServletResponse hResponse){
		
		
		String name = hRequest.getParameter("j_username");
		String title = hRequest.getParameter("j_title");
		String company = hRequest.getParameter("j_company");
		String email = hRequest.getParameter("j_email");
		String confirmationUrl = hRequest.getParameter("confirmationUrl");
		
		
		log.debug("request access:" + name + " " + email);
		try {
		//generate a token
		
		
		String passcode = PasscodeFactory.generatePasscode(16,PasscodeFactory.PASSCODE_TYPE_NUMBER_LETTER);
		requestAccessMap.put(passcode, email);
		
		String url = hRequest.getRequestURL().toString(); 
		int index = url.indexOf("/login");
		
		String accessLink = url.substring(0, index) + POST_AUTH_URL
		                  + "?" + TOKEN_STRING + "=" + passcode;
		
		log.debug("add token to map: " + passcode + " " + email);
		
		
		//send notification email
		
			String responseEntity = ClientBuilder.newClient()
		            .target("http://wgt-capp.appspot.com/webapi").path("email/ping")
		                        .request().get(String.class);
			
			System.out.println(responseEntity);
			
			responseEntity = ClientBuilder.newClient()
		            .target("http://wgt-capp.appspot.com/webapi").path("email/send")
		            .queryParam("to", email)
		            .queryParam("application", "open Data Expert")
		            .queryParam("subject", "GSA Data Challenge Request Access")
		            .queryParam("content", accessLink)
		            .request().get(String.class);
			
			System.out.println(responseEntity);
			
			//redirect to confirmation
			String path = hRequest.getContextPath();
			redirectPage(hResponse,  path + confirmationUrl);
			//forwardRequest(hRequest,hResponse, path + confirmationUrl);
			return false;
		  } catch (Exception e) {
	 
			e.printStackTrace();
	 
		  }
		
		return false;
	}
	
	private boolean auth(String name, String password){
		String value = userMap.get(name);
		log.debug("password:" + password + " value:" + value);
		return ((value!=null) && value.equals(password));
	}
	
	private static Map<String, String> userMap = new HashMap<String, String>();
	
	static{
		
		userMap.put("gsa", "0d3pt");
		userMap.put("odataxpt", "abcd1234*");
		userMap.put("demo", "g00d");
	}
	
	private boolean isUnauthPath(String url, String path){

		for (String upath: unAuthPaths ){

			String fPath = path + upath;
			if (url.startsWith(fPath)){
				return true;
			}

		}

		return false;
	}

	private static void forwardRequest(HttpServletRequest request,HttpServletResponse response, String url) {
		try {
			log.debug("forward to:" + url);
			request.getRequestDispatcher(url).forward(request,response);
		} catch (Exception ee) {
			throw new RuntimeException("Problem forwarding to URL:"+url,ee) ;
		}
	}

	public static void redirectPage(HttpServletResponse response, String desiredPage) {
		try {
			log.debug("redirect to:" + desiredPage);
			response.sendRedirect(LcdUtils.removeCRLF(desiredPage)) ;
		} catch(IOException xx) {
			throw new RuntimeException(xx) ;
		}
	}	

}
