package com.washingtongt.access;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	public static String WEB_AUTH_URL = "/login/login.jsf";
	private static String REDIRECT_URL = "redirect_url";
	public static String TOKEN_STRING = "WGTXM_Tokan";

	private static Set<String> unAuthPaths = new HashSet<String>();
	private static AccessAuthManager instance = null;

	static {
		unAuthPaths.add("/public/");
		unAuthPaths.add("/help/");
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
			return true;
		}

		if (url.equals(path + POST_AUTH_URL )){
			//do auth and redirect
			session = hRequest.getSession(true);
			String redirectURL = (String)session.getAttribute(REDIRECT_URL);
			
			String name = hRequest.getParameter("j_username");
			String password = hRequest.getParameter("j_password");

			token = AccessToken.issue(name);
			session.setAttribute(TOKEN_STRING, token);
			log.debug("login passed, go ahead token: expired" + token.isExpired()+ " "+ redirectURL);
			redirectPage(hResponse, redirectURL);
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
			request.getRequestDispatcher(url).forward(request,response);
		} catch (Exception ee) {
			throw new RuntimeException("Problem forwarding to URL:"+url,ee) ;
		}
	}

	public static void redirectPage(HttpServletResponse response, String desiredPage) {
		try {
			response.sendRedirect(LcdUtils.removeCRLF(desiredPage)) ;
		} catch(IOException xx) {
			throw new RuntimeException(xx) ;
		}
	}	

}
