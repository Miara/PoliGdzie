package com.example.poligdzie;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;







import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.*;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.json.poligdzie.Directions;
import com.json.poligdzie.Legs;
import com.json.poligdzie.Routes;
import com.json.poligdzie.Steps;




public class MainActivity extends Activity {

	private GoogleMap map;
	static final LatLng LOCATION_PIOTROWO = new LatLng(52.4022703,16.9495847);
	private static final LatLng MY_POINT = new LatLng(52.4014141,16.9511311);
	private static final LatLng CENTRUM_WYKLADOWE = new LatLng(52.4037379,16.9498138);
	private PolylineOptions options;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        map.addMarker(new MarkerOptions().position(LOCATION_PIOTROWO).title("Tutaj jest kampus Piotrowo!"));
        
        MarkerOptions options = new MarkerOptions();
		/*options.position(LOWER_MANHATTAN);
		options.position(BROOKLYN_BRIDGE);
		options.position(WALL_STREET);*/
        options.position(MY_POINT);
        options.position(CENTRUM_WYKLADOWE);
        
		map.addMarker(options);
		String url = getMapsApiDirectionsUrl();
		ReadTask downloadTask = new ReadTask();
		downloadTask.execute(url);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_PIOTROWO,
				16));
		addMarkers();

    }
    
    public void onClick_Piotrowo(View v) {
    	map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_PIOTROWO, 16);
		map.animateCamera(update);
		
	}

    
    /* test purposes now */
    public void onClick_JSON_Test(View v) {
    	TestTask test = new TestTask();
    	test.execute(getMapsApiDirectionsUrl());
    	
	}
    
	private class TestTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... url) {
			
			ObjectMapper mapper = new ObjectMapper(); 
	    	mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    	
	    	try {
	    		
	    		Directions directions = mapper.readValue(new URL(url[0]), Directions.class);
	    		
	    		options = new PolylineOptions();
	    		for(Routes route : directions.getRoutes()) {
	    			for(Legs leg : route.getLegs()) {
	    				for(Steps step : leg.getSteps()) {
	    					options.add(step.getPolyline().decodePolyline());
	    				}
	    			}
	    		}
	    		
				
				
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
							
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
					
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
					
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
					
				e.printStackTrace();
			}
	    	return "blabla";
		}
	

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//new ParserTask().execute(result);
			
			options.color(Color.RED);
			options.width(10);
			map.addPolyline(options);	
		}
	}
	

    private String getMapsApiDirectionsUrl() {
		/*String waypoints = "waypoints=optimize:true|"
				+ LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude
				+ "|" + "|" + BROOKLYN_BRIDGE.latitude + ","
				+ BROOKLYN_BRIDGE.longitude + "|" + WALL_STREET.latitude + ","
				+ WALL_STREET.longitude;*/
    	String waypoints = "waypoints=optimize:true|"
		+ MY_POINT.latitude + "," + MY_POINT.longitude
		+ "|" + "|" + CENTRUM_WYKLADOWE.latitude + ","
		+ CENTRUM_WYKLADOWE.longitude;
    	
		String sensor = "sensor=false";
		String params = waypoints + "&" + sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		return url;
	}

    
	private void addMarkers() {
		if (map != null) {
			map.addMarker(new MarkerOptions().position(MY_POINT)
					.title("First Point"));
			map.addMarker(new MarkerOptions().position(CENTRUM_WYKLADOWE)
					.title("Second Point"));
			//map.addMarker(new MarkerOptions().position(WALL_STREET)
			//		.title("Third Point"));
		}
	}

	private class ReadTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... url) {
			String data = "";
			try {
				HttpConnection http = new HttpConnection();
				data = http.readUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			new ParserTask().execute(result);
		}
	}
	private class ParserTask extends
	AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {
		
			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;
		
			try {
				jObject = new JSONObject(jsonData[0]);
				PathJSONParser parser = new PathJSONParser();
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}
		
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
			ArrayList<LatLng> points = null;
			PolylineOptions polyLineOptions = null;
		
			// traversing through routes
			for (int i = 0; i < routes.size(); i++) {
				points = new ArrayList<LatLng>();
				polyLineOptions = new PolylineOptions();
				List<HashMap<String, String>> path = routes.get(i);
		
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);
		
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);
		
					points.add(position);
				}
		
				polyLineOptions.addAll(points);
				polyLineOptions.width(2);
				polyLineOptions.color(Color.BLUE);
			}
		
			//map.addPolyline(polyLineOptions);
		}
	}

}