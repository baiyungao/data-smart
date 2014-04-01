package com.washingtongt.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="controller")
@SessionScoped
public class ConsoleController {
	

	public String getHello(){
		return "I will win";
	}

}
