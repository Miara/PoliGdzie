package com.poligdzie.singletons;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.NewFunctions;
import com.poligdzie.json.DownloadDirectionsTask;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;

public class RouteProvider implements Constants,NewFunctions{

	private static RouteProvider instance = null;
	private DatabaseHelper dbHelper;
	private PolylineOptions options;
	private static Context context;
	
	private List<Fragment> fragments;
	private List<String> fragmentTags;
	private List<String> fragmentHeaders;
	private int fragmentPosition = 0;
	
	private Object from;
	private Object to;
	
	static final LatLng LOCATION_PIOTROWO = new LatLng(52.4022703, 16.9495847);

	
	public RouteProvider() {
		// konstruktor zas�aniaj�cy domy�lny publiczny konstruktor
		fragments = new ArrayList<Fragment>();
		fragmentTags = new ArrayList<String>();
		fragmentHeaders = new ArrayList<String>();
	}
	
	//implementacja singletona
	public static RouteProvider getInstance(Context cnt) {
		if(instance == null) {
			instance =  new RouteProvider();
		}
		context = cnt;
		return instance;
	}
	
	
	public GoogleMap getMapWithRoute(GoogleMap map, DatabaseHelper dbHelper) {

		this.dbHelper = dbHelper;
		

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
	
	private HashMap <String, Double> getCoordsFromObject(Object object) {
		HashMap <String, Double> result = new HashMap <String, Double>();
		
		if(object instanceof Building) {
			result.put("X", ((Building) object).getCoordX());
			result.put("Y", ((Building) object).getCoordY());
		}
		
		if(object instanceof Unit) {
			result.put("X", ((Unit) object).getBuilding().getCoordX());
			result.put("Y", ((Unit) object).getBuilding().getCoordY());
		}
		
		if(object instanceof Room) {
			result.put("X", ((Room) object).getBuilding().getCoordX());
			result.put("Y", ((Room) object).getBuilding().getCoordY());
		}
		
		return result;
	}
	
	private String getMapsApiDirectionsUrl() {
			
		HashMap <String, Double> toCoords = new HashMap <String, Double>(); 
		HashMap <String, Double> fromCoords = new HashMap <String, Double>();
		
		if(from == null || to == null) {
			return null;
		}
		
		toCoords = getCoordsFromObject(to);
		fromCoords = getCoordsFromObject(from);
		
			
		String waypoints = "waypoints=optimize:true|" + toCoords.get("X") + ","
				+ toCoords.get("Y") + "|" + "|" + fromCoords.get("X")
				+ "," + fromCoords.get("Y");

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
							.fromResource(getDrawableId(b.getImageResource(), context)));

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
	
	public int getFragmentPosition() {
		return fragmentPosition;
	}

	public void setFragmentPosition(int fragmentPosition) {
		this.fragmentPosition = fragmentPosition;
	}
	
	public void addFragment(String tag, String header,  int floor, Fragment fragment)
	{
		String mFloor = "";
		String mTag = tag;
		if( tag != MAP_MODE_OUTDOOR) 
		{
			mFloor = " pi�tro" + Integer.toString(floor);
			mTag+=Integer.toString(floor);	
		}
		fragmentTags.add(mTag);
		fragmentHeaders.add(header + mFloor);
		fragments.add(fragment);
		
	}
	
	public Fragment getPreviousFragment()
	{
		if(fragments.get(fragmentPosition-1) != null)
		{
			setFragmentPosition(fragmentPosition - 1);
		}
		return fragments.get(fragmentPosition);
	}
	
	public Fragment getNextFragment()
	{
		if(fragments.get(fragmentPosition+1) != null)
		{
			setFragmentPosition(fragmentPosition + 1);
		}
		return fragments.get(fragmentPosition);
	}
	
	public String getCurrentFragmentHeader()
	{
		return fragmentHeaders.get(fragmentPosition);
	}
	public String getCurrentFragmentTag()
	{
		return fragmentTags.get(fragmentPosition);
	}
	
	public int getFragmentsSize()
	{
		return fragments.size();
	}
	
	public void clearFragments()
	{
		fragments.clear();
		fragmentTags.clear();
		fragmentHeaders.clear();
	}
	
	

	@Override
	public int getDrawableId(String name, Context context) {
		int resId =context.getResources().getIdentifier(name, "drawable", context.getPackageName());
		return resId;
	}



	

	
}
