package com.poligdzie.singletons;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.poligdzie.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.json.DownloadDirectionsTask;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;

public class RouteProvider implements Constants{

	private static RouteProvider instance = null;
	private GoogleMap map;
	private DatabaseHelper dbHelper;
	private LayoutInflater layoutInflater;
	private PolylineOptions options;
	
	private Object from;
	private Object to;
	
	static final LatLng LOCATION_PIOTROWO = new LatLng(52.4022703, 16.9495847);
	private static final LatLng MY_POINT = new LatLng(52.4014141, 16.9511311);
	private static final LatLng CENTRUM_WYKLADOWE = new LatLng(52.4037379,
			16.9498138);

	
	protected RouteProvider() {
		// konstruktor zas³aniaj¹cy domyœlny publiczny konstruktor
	}
	
	//implementacja singletona
	public static RouteProvider getInstance() {
		if(instance == null) {
			instance =  new RouteProvider();
		}
		return instance;
	}
	
	
	public GoogleMap onCreate(GoogleMap map, DatabaseHelper dbHelper) {

		this.dbHelper = dbHelper;
		map.setInfoWindowAdapter(new InfoWindowAdapter() {

			// Use default InfoWindow frame
			@Override
			public View getInfoWindow(Marker arg0) {
				View v = layoutInflater.inflate(
						R.layout.window_marker_click, null);
				return v;
			}

			@Override
			public View getInfoContents(Marker arg0) {
				View v = layoutInflater.inflate(
						R.layout.window_marker_click, null);
				return v;

			}
		});

		map.setMyLocationEnabled(true);

		map = addAllMarkersToMap(map);

		
		String url = getMapsApiDirectionsUrl();
		if(url != null) {
			DownloadDirectionsTask downloadTask = new DownloadDirectionsTask(map,
					options);
			downloadTask.execute(url);
		}

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_PIOTROWO, 16));
		// addMarkers();
		return map;
	}
	
	private String getMapsApiDirectionsUrl() {
		List <Building> buildingFrom = new ArrayList <Building>();
		List <Building> buildingTo = new ArrayList <Building>();
		try {
			buildingFrom.addAll(dbHelper.getBuildingDao().queryForEq("name", from));
			buildingTo.addAll(dbHelper.getBuildingDao().queryForEq("name", to));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		if(buildingFrom == null || 
		   buildingTo == null || 
		   buildingFrom.size() == 0 || 
		   buildingTo.size() == 0) {
			return null;
		}
		
		Log.i("POLIGDZIE", Integer.toString(buildingFrom.size()));
		Log.i("POLIGDZIE", Integer.toString(buildingTo.size()));
		
		
		String waypoints = "waypoints=optimize:true|" + buildingFrom.get(0).getCoordX() + ","
				+ buildingFrom.get(0).getCoordY() + "|" + "|" + buildingTo.get(0).getCoordX()
				+ "," + buildingTo.get(0).getCoordY();

		String sensor = "sensor=false";
		String mode  = "mode=" + GOOGLE_MAP_MODE; 
		String params = waypoints + "&" + sensor + "&" + mode;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;
		
		Log.i("POLIGDZIE", url);
		return url;
	}

	private GoogleMap addAllMarkersToMap(GoogleMap map) {
		List<Building> buildings = new ArrayList<Building>();
		try {
			buildings = dbHelper.getBuildingDao().queryForAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (buildings == null)
			return map;
		MarkerOptions option = new MarkerOptions();

		for (Building b : buildings) {
			option.position(new LatLng(b.getCoordX(), b.getCoordY()))
					.title(b.getName())
					.snippet(b.getAddress())
					.icon(BitmapDescriptorFactory
							.fromResource(b.getImageResource()));

			map.addMarker(option);
		}

		return map;

	}

	public Object getFrom() {
		return from;
	}

	public void setFrom(Object from) {
		this.from = from;
	}

	public Object getTo() {
		return to;
	}

	public void setTo(Object to) {
		this.to = to;
	}
}
