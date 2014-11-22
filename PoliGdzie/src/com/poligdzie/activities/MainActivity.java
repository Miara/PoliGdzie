package com.poligdzie.activities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.poligdzie.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.json.Directions;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;




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
		
        options.position(MY_POINT);
        options.position(CENTRUM_WYKLADOWE);
        
		map.addMarker(options);
		
		
		DownloadDirectionsTask downloadTask = new DownloadDirectionsTask();
    	downloadTask.execute(getMapsApiDirectionsUrl());
    	
    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_PIOTROWO,
				16));
		addMarkers();
		
		
		DatabaseHelper dbHelper = new DatabaseHelper(this, "database.db", null, 1);
		try {
			Building building = new Building();
			building.setAddress("sassa");
			building.setCoordX(123.22);
			building.setCoordY(233.22);
			building.setHeight(100);
			building.setWidth(200);
			building.setId(1);
			dbHelper.getWritableDatabase();
			//dbHelper.getDaoContainer().getBuildingDao().create(building);
			Log.d("moje", Long.toString(dbHelper.getDaoContainer().getBuildingDao().countOf()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.w("moje", "nieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
			e.printStackTrace();
		} 
		}
    
    public void onClick_Piotrowo(View v) {
    	map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_PIOTROWO, 16);
		map.animateCamera(update);
		
	}

        
	private class DownloadDirectionsTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... url) {
			
			ObjectMapper mapper = new ObjectMapper(); 
	    	mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	    	
	    	try {
	    		
	    		Directions directions = mapper.readValue(new URL(url[0]), Directions.class);
	    		
	    		options = new PolylineOptions();
	    		options = directions.generatePolylineFromDirections(options);
	    					
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
	    	return "Not sure what should I return";
		}
	

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
						
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

}