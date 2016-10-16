package com.poligdzie.fragments;

import java.sql.SQLException;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.base.PoliGdzieMapFragment;
import com.poligdzie.listeners.OnMarkerClickCustomListener;
import com.poligdzie.singletons.MapDrawingProvider;

public class MapOutdoorFragment extends PoliGdzieMapFragment implements
															OnClickListener
{

	GoogleMap					map;
	public PolylineOptions		options;
	private SearchRouteFragment	searchRouteFragment;
	private SearchPlaceFragment	searchPlaceFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.map_outdoor_fragment,
				container, false);

		map = getMapFragment().getMap();
		
		

		searchPlaceFragment = (SearchPlaceFragment) getActivity()
				.getFragmentManager().findFragmentById(R.id.search_place_frag);
		searchPlaceFragment.setFragment(this);
		searchRouteFragment = (SearchRouteFragment) getActivity()
				.getFragmentManager().findFragmentById(R.id.search_route_frag);

		drawingProvider = MapDrawingProvider.getInstance();
		drawingProvider.setContext(this.getActivity());
    
		map = drawingProvider.getMapWithRoute(map, dbHelper);
		map.setOnMarkerClickListener(new OnMarkerClickCustomListener(this, map,
				dbHelper));

		
		
		return rootView;
	}

	@Override
	public void onClick(View v)
	{

	}

	public MapOutdoorFragment()
	{
		super();
	}

	public MapOutdoorFragment(String drawableId, String name, String viewTag)
	{
		super(drawableId, name, viewTag);
	}
	
	private MapFragment getMapFragment() {
	    FragmentManager fm = null;

	    Log.d(TAG, "sdk: " + Build.VERSION.SDK_INT);
	    Log.d(TAG, "release: " + Build.VERSION.RELEASE);

	    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
	        Log.d(TAG, "using getFragmentManager");
	        fm = getFragmentManager();
	    } else {
	        Log.d(TAG, "using getChildFragmentManager");
	        fm = getChildFragmentManager();
	    }

	    return (MapFragment) fm.findFragmentById(R.id.map_outdoor_googleMap);
	}

}
