package com.washingtongt.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.washingtongt.data.model.gsa.GsaConstants;

public class UILabelMap extends HashMap<String, String>{
	
	private Map<String, String> content;
	private static final long serialVersionUID = 1L;

	public UILabelMap(){
		content = GsaConstants.labelMap;
	}
	
	@Override
	public String get(Object key) {
		// TODO Auto-generatd method stub
		return content.get(key)==null?(String)key:content.get(key);
	}

	}
