package com.poligdzie.singletons;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.BuildingEntry;
import com.poligdzie.persistence.GPSLocation;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;
import com.poligdzie.tasks.DownloadDirectionsTask;

public class MapDrawingProvider extends PoliGdzieBaseClass
{

	private static MapDrawingProvider	instance			= null;
	private DatabaseHelper				dbHelper;
	private PolylineOptions				options;
	private Context						context;
	private Object						start;
	private Object						goal;
	private boolean						drawRoute			= true;
	private GoogleMap					map;
	private List<Marker>				markers;
	private FusedLocationProviderApi	api;
	private GPSLocation	gpsLocation;

	static final LatLng					LOCATION_PIOTROWO	= new LatLng(
																	52.4022703,
																	16.9495847);

	protected MapDrawingProvider()
	{
		// konstruktor zas³aniaj¹cy domyœlny publiczny konstruktor
		markers = new ArrayList<Marker>();
	}

	// implementacja singletona
	public static MapDrawingProvider getInstance()
	{
		if (instance == null)
		{
			instance = new MapDrawingProvider();
		}
		return instance;
	}

	private void drawOnMap(GoogleMap map)
	{
		map = this.addAllMarkersToMap(map);

		String url = getMapsApiDirectionsUrl(map);
		if (drawRoute && url != null)
		{
			Log.i("Poli","in draw route");
			DownloadDirectionsTask downloadTask = new DownloadDirectionsTask(map);
			downloadTask.execute(url);
		}
	}

	public GoogleMap getMapWithRoute(GoogleMap map, DatabaseHelper dbHelper)
	{

		this.dbHelper = dbHelper;
		this.map = map;

		map.setMyLocationEnabled(true);

		this.drawOnMap(map);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_PIOTROWO, 16));
		// addMarkers();
		return map;
	}

	public GoogleMap getMap()
	{
		return map;
	}

	public void setMap(GoogleMap map)
	{
		this.map = map;
	}

	public void drawRoute(GoogleMap map)
	{
		map.clear();
		this.drawOnMap(map);
	}

	private HashMap<String, Double> getCoordsFromObject(Object object)
	{
		HashMap<String, Double> result = new HashMap<String, Double>();

		if (object instanceof Building)
		{
			result.put("X", ((Building) object).getCoordX());
			result.put("Y", ((Building) object).getCoordY());
		}

		if (object instanceof Unit)
		{
			result.put("X", ((Unit) object).getBuilding().getCoordX());
			result.put("Y", ((Unit) object).getBuilding().getCoordY());
		}

		if (object instanceof Room)
		{
			result.put("X", ((Room) object).getBuilding().getCoordX());
			result.put("Y", ((Room) object).getBuilding().getCoordY());
		}
		
		if (object instanceof BuildingEntry)
		{
			result.put("X", ((BuildingEntry) object).getCoordX());
			result.put("Y", ((BuildingEntry) object).getCoordY());
		}
		if (object instanceof GPSLocation)
		{
			result.put("X", ((GPSLocation) object).getCoordX());
			result.put("Y", ((GPSLocation) object).getCoordY());
		}

		return result;
	}

	private String getMapsApiDirectionsUrl(GoogleMap map)
	{

		HashMap<String, Double> goalCoords = new HashMap<String, Double>();
		HashMap<String, Double> startCoords = new HashMap<String, Double>();

		if (goal == null)
		{
			return null;
		}

		if (start == null)
		{
			return null;
		}

		startCoords = getCoordsFromObject(start);
		goalCoords = getCoordsFromObject(goal);

		if (startCoords == null || goalCoords == null)
		{
			return null;
		}

		String waypoints = "origin=" + startCoords.get("X")
				+ "," + startCoords.get("Y") + "" + "&destination=" + goalCoords.get("X")
				+ "," + goalCoords.get("Y");

		String sensor = "sensor=false";
		String mode = "mode=" + GOOGLE_MAP_MODE;
		String params = waypoints + "&" + sensor + "&" + mode;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;// + "&KEY=AIzaSyDaH9Hu9g289RG5v9Md2inQtYSkgruI17U";
		
		
		
		Log.i("POLIGDZIE", url);
		return url;
	}

	private GoogleMap addAllMarkersToMap(GoogleMap map)
	{
		List<Building> buildings = new ArrayList<Building>();
		try
		{
			buildings = dbHelper.getBuildingDao().queryForAll();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		if (buildings == null)
			return map;
		MarkerOptions option = new MarkerOptions();

		for (Building b : buildings)
		{
			option.position(new LatLng(b.getCoordX(), b.getCoordY()))
					.title(b.getName())
					.snippet(b.getAddress())
					.icon(BitmapDescriptorFactory.fromResource(getDrawableId(
							b.getMarkerImageResource(), context)));

			markers.add(map.addMarker(option));
		}

		return map;

	}

	public Context getContext()
	{
		return context;
	}

	public void setContext(Context context)
	{
		this.context = context;
	}

	public List<Marker> getMarkers()
	{
		return markers;
	}

	public void setMarkers(List<Marker> markers)
	{
		this.markers = markers;
	}

	public Object getStart()
	{
		return start;
	}

	public void setStart(Object start)
	{
		this.start = start;
	}

	public Object getGoal()
	{
		return goal;
	}

	public void setGoal(Object goal)
	{
		this.goal = goal;
	}

	public boolean isDrawRoute()
	{
		return drawRoute;
	}

	public void setDrawRoute(boolean drawRoute)
	{
		this.drawRoute = drawRoute;
	}

	public void setGPS(GPSLocation location)
	{
		this.gpsLocation = location;
	}
	
	public GPSLocation getGPS()
	{
		return gpsLocation;
	}
	
	public void refresh()
	{
		this.drawRoute(this.getMap());
	}
}
