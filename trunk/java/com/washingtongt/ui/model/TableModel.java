package com.washingtongt.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.washingtongt.data.model.Measurement;
import com.washingtongt.data.model.gsa.GsaConstants;

public class TableModel  {
	
	static final Logger log = Logger.getLogger(TableModel.class);
	private ArrayList<String> cols;
	private String header="Default Table";
	private int clientRows = 30;
	private List<DBObject> contents =new ArrayList<DBObject>();
	private String groupKey = null;  //by FY or other
	
	private boolean orderById = false; 
	
	//add sorting here
	
	Map<String, BasicDBObject> sortingMap = new TreeMap<String, BasicDBObject>();	

	public TableModel(String[] headers, String defaultSorting, String group){
		super();
		
		this.groupKey = group;
		this.setCols(headers);
	}
	
	private void setCols(String[] headers){
		cols = new ArrayList<String>();
		for (String col: headers){
			cols.add(col);
		}
		
	}
	public TableModel(Measurement measurement, String keyColName){
		
		new TableModel(measurement.getIndexNames(), (String)null, (String)null);
		
		this.setCols(measurement.getIndexNames());
		
		if (measurement.isPopulated()){
			this.addContent(measurement.getResults(), keyColName);
		}
		
		log.debug("check the cols:" + this.cols);
	}
	
	
	public void addContentWithGroup(BasicDBList result, String item, String group)
	{
		Map<String, BasicDBObject> contentMap = new HashMap<String,BasicDBObject>();
		
		ArrayList<String> extCols = new ArrayList<String>();
		
		Set<String> groupSet = new HashSet<String>();
		if (result != null){
			for (int i = 0; i < result.size(); i++){
				//HashMap<String, Object> row  = new HashMap<String,Object>();
				BasicDBObject  row = (BasicDBObject)((BasicDBObject)result.get(i)).clone();
				Object id = row.get("_id");
				if (id == null) {
					continue;
				}
				String rowKey = ((BasicDBObject)id).getString(item);
				String groupValue = ((BasicDBObject)id).getString(group);
				
				if (!groupSet.contains(groupValue)){
					groupSet.add(groupValue);
					for(String col:cols){
						extCols.add(groupValue + " " + col);
					}
				}
				
				
				BasicDBObject contentRow = contentMap.get(rowKey);
				if (contentRow == null) {
					contentRow = new BasicDBObject("Item", rowKey);
					contentMap.put(rowKey, contentRow);
					this.contents.add(contentRow);
				}
				
				row.remove("_id");
				
				for (String colKey: row.keySet()){
					contentRow.append(groupValue + " " + colKey, row.get(colKey));
					
				}
				
			}
			this.cols = extCols;
		}		
		
	}	
	
	public void addContent(BasicDBList result, String item)
	{
		this.addContent(result, item, null, 0);
	}
	public void addContent(BasicDBList result, String item, String performanceField, double benchmark){
		
		
		
		if (result != null){
			for (int i = 0; i < result.size(); i++){
				//HashMap<String, Object> row  = new HashMap<String,Object>();
				
				BasicDBObject  row = (BasicDBObject)result.get(i);
				Object id = row.get("_id");
				if (id == null){
					row.put("Item", item);
					this.sortingMap.put(item, row);
				}
				else 
				{
					row.put("Item", ModelHelper.getRowId(row));
					this.sortingMap.put(ModelHelper.getRowId(row), row);
				}
				
				if (performanceField != null){
					double pValue = (benchmark-row.getDouble(performanceField))/benchmark;
					if (pValue == 0){
						row.put(GsaConstants.IDT_PERFORMANCE,null );}
					else {
						row.put(GsaConstants.IDT_PERFORMANCE,pValue );
					}
				}
				
				this.contents.add(row);
			}
		}
	}


	private void updateContent(){
		this.contents.clear();
		this.contents.addAll(this.sortingMap.values());
		
	}
	
	public List<String> getCols() {
		return cols;
	}

	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public int getClientRows() {
		return clientRows;
	}
	public void setClientRows(int clientRows) {
		this.clientRows = clientRows;
	}
	
	public List<DBObject> getContents(){
		if (this.isOrderById()) {
			this.updateContent();}
		return this.contents;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public boolean isOrderById() {
		return orderById;
	}

	public void setOrderById(boolean orderById) {
		this.orderById = orderById;
	}


}
