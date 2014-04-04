package com.washingtongt.ui.model;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

public class TableModel  {
	
	private ArrayList<String> cols;
	private String header="Default Table";
	private int clientRows = 30;
	private List<DBObject> contents =new ArrayList<DBObject>();

	public TableModel(String[] headers, String defaultSorting){
		super();
		
		cols = new ArrayList<String>();
		
		for (String col: headers){
			cols.add(col);
		}
		
	}
	public void addContent(BasicDBList result, String item){
		
		if (result != null){
			for (int i = 0; i < result.size(); i++){
				//HashMap<String, Object> row  = new HashMap<String,Object>();
				
				DBObject  row = (DBObject)result.get(i);
				Object id = row.get("_id");
				if (id == null){
					row.put("Item", item);
				}
				else 
				{
					row.put("Item", item + " " + id.toString());
				}
								
				this.contents.add(row);
			}
		}
		
		
		}

	public List<String> getCols() {
		return cols;
	}

	private String format(Number n) {
        NumberFormat format = DecimalFormat.getInstance();
        format.setRoundingMode(RoundingMode.FLOOR);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        return format.format(n);
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
		return this.contents;
	}

}
