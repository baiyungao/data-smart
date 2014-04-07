package com.washingtongt.access.web.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.washingtongt.access.AccessAuthManager;
import com.washingtongt.access.AccessToken;

/**
 * Servlet implementation class Logout
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logout(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		logout( request,  response);
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response){
		try {
			HttpSession session = request.getSession(true);
			AccessToken token = (AccessToken)session.getAttribute(AccessAuthManager.TOKEN_STRING);
			if (token != null){
				token.invalid();
				session.removeAttribute(AccessAuthManager.TOKEN_STRING);
			}
			String path = request.getContextPath();
			String loginUrl = path + AccessAuthManager.WEB_AUTH_URL;
			
			AccessAuthManager.redirectPage(response, loginUrl);			
			
		} finally {
			
		}
		return;
		
	}

}
