package com.poligdzie.singletons;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.WithDrawableId;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.Room;
import com.poligdzie.persistence.Unit;
import com.poligdzie.tasks.DownloadDirectionsTask;

public class RouteProvider implements Constants, WithDrawableId
{

	private static RouteProvider		instance			= null;
	private DatabaseHelper				dbHelper;
	private PolylineOptions				options;
	private Context						context;
	private Object						start;
	private Object						goal;
	private boolean						drawRoute			= false;
	private GoogleMap					map;
	private List<Marker>				markers;
	private FusedLocationProviderApi	api;

	static final LatLng					LOCATION_PIOTROWO	= new LatLng(
																	52.4022703,
																	16.9495847);

	// TODO: dostosowac nazwe

	protected RouteProvider()
	{
		// konstruktor zas³aniaj¹cy domyœlny publiczny konstruktor
		markers = new ArrayList<Marker>();
	}

	// implementacja singletona
	public static RouteProvider getInstance()
	{
		if (instance == null)
		{
			instance = new RouteProvider();
		}
		return instance;
	}

	// TODO: wyciagnac funkcje z wspolnego fragment kodu
	public GoogleMap getMapWithRoute(GoogleMap map, DatabaseHelper dbHelper)
	{

		this.dbHelper = dbHelper;
		this.map = map;

		map.setMyLocationEnabled(true);

		map = addAllMarkersToMap(map);

		String url = getMapsApiDirectionsUrl(map);
		if (drawRoute && url != null)
		{
			DownloadDirectionsTask downloadTask = new DownloadDirectionsTask(
					map, options);
			downloadTask.execute(url);
		}

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_PIOTROWO, 16));
		// addMarkers();
		return map;
	}

	public void drawRoute()
	{
		map.clear();
		map = this.addAllMarkersToMap(map);

		String url = getMapsApiDirectionsUrl(map);
		if (drawRoute && url != null)
		{
			DownloadDirectionsTask downloadTask = new DownloadDirectionsTask(
					map, options);
			downloadTask.execute(url);
		}
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

		return result;
	}

	private String getMapsApiDirectionsUrl(GoogleMap map)
	{
		// TODO: refaktor nazw
		HashMap<String, Double> toCoords = new HashMap<String, Double>();
		HashMap<String, Double> fromCoords = new HashMap<String, Double>();

		if (goal == null)
		{
			return null;
		}

		if (start == null)
		{
			return null;
		}

		fromCoords = getCoordsFromObject(start);
		toCoords = getCoordsFromObject(goal);

		if (fromCoords == null || toCoords == null)
		{
			return null;
		}

		String waypoints = "waypoints=optimize:true|" + fromCoords.get("X")
				+ "," + fromCoords.get("Y") + "|" + "|" + toCoords.get("X")
				+ "," + toCoords.get("Y");

		String sensor = "sensor=false";
		String mode = "mode=" + GOOGLE_MAP_MODE;
		String params = waypoints + "&" + sensor + "&" + mode;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + params;

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

	@Override
	public int getDrawableId(String name, Context context)
	{
		int resId = context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
		return resId;
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
}
