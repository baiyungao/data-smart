package com.wgt.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wgt.utils.GoogleMapUtils.FeaturesListResponse;
import com.wgt.utils.GoogleMapUtils.Geometry;
import com.wgt.utils.GoogleMapUtils.GeometryDeserializer;
import com.wgt.utils.GoogleMapUtils.PointGeometry;

public class PostQuery {

	
	  public static void main(String[] args) {

		    try {

		      int num  = 719128;
		      String packId = "CP271719128CN";
		      URL url = new URL("https://tools.usps.com/go/TrackConfirmAction.action?tLabels=" + packId);
		      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		      connection.setRequestMethod("GET");
		      //connection.setDoOutput(true);
		      connection.connect();

		      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		      StringWriter writer = new StringWriter();
		      IOUtils.copy(reader, writer);
		      String theString = writer.toString();
		     
		      System.out.println(packId + "size:" + theString.length() + " " + theString);
		      
		    } catch (IOException e) {
		      System.out.println(e.getMessage());
		    } catch (Throwable t) {
		      t.printStackTrace();
		    }
		  }	
}
