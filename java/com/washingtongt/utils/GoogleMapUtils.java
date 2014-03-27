package com.washingtongt.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class GoogleMapUtils {

  public static class FeaturesListResponse {
    public Feature[] features;
  }

  public static class Feature {
    public Geometry geometry;
    public Map properties;
  }

  public static class Geometry {
  }

  public static class PointGeometry extends Geometry {
    public double[] coordinates;
  }

  public static class GeometryDeserializer implements JsonDeserializer {
    public Geometry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      JsonObject g = json.getAsJsonObject();
      JsonElement type = g.get("type");

      if (type.getAsJsonPrimitive().getAsString().equals("Point")) {
        PointGeometry p = new PointGeometry();
        JsonArray coords = g.getAsJsonArray("coordinates");
        p.coordinates = new double[coords.size()];

        for (int i = 0; i < coords.size(); ++i) {
          p.coordinates[i] = coords.get(i).getAsDouble();
        }
        return p;
      }
      return null;
    }
  }

  public static void main(String[] args) {

    try {

      URL url = new URL("https://www.googleapis.com/mapsengine/v1/tables/12421761926155747447-06672618218968397709/features?maxResults=500&version=published&key=AIzaSyCOLt4_2Um2lZjNBe0u4mNwiG6Gz53HNgM");
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("GET");
      connection.setDoOutput(true);
      connection.connect();

      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

      // Deserialize.
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Geometry.class, new GeometryDeserializer());
      Gson gson = builder.create();

      FeaturesListResponse map = gson.fromJson(reader, FeaturesListResponse.class);
      PointGeometry g = (PointGeometry)map.features[0].geometry;
      System.out.println(g.coordinates[0] + ", " + g.coordinates[1]);

      System.out.println((String)(map.features[0].properties.get("Fcilty_nam")));
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}

