package com.washingtongt.access.web.login;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;



@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean implements Serializable {
	private static final Logger log = Logger.getLogger(LoginBean.class.getName());	
	static final public int PHASE_ID_INPUT = 0;
	static final public int PHASE_PASSOCDE_VERIFY = 1;
	static final public int PHASE_SIGN_ON = 2;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String loginId = "";
	private String passcode = "";
	

	public void setLoginId(String id)
	{
		if (id != null)
			loginId = id;
	}
	
	public void setPasscode(String code)
	{
		if (code != null)
			passcode = code;
	}
	
	public String getLoginId()
	{
		return loginId;
	}
	
	public String getPasscode()
	{
		return passcode;
	}
	
	
}
