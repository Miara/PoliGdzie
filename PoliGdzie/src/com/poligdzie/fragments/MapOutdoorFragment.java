package com.poligdzie.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.listeners.MarkerOnClickCustomListener;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.MapFragmentProvider;
import com.poligdzie.singletons.RouteProvider;

public class MapOutdoorFragment extends Fragment implements OnClickListener,
Constants {
	
	GoogleMap map;
	public PolylineOptions options;
	//protected DatabaseHelper dbHelper;
	DatabaseHelper dbHelper;
	private RouteProvider routeProvider;
	private MapFragmentProvider mapProvider;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) 
	{
	View rootView = inflater.inflate(R.layout.map_outdoor_fragment,
			container, false);
	dbHelper = new DatabaseHelper(getActivity(), DATABASE_NAME, null,
			DATABASE_VERSION);
	map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map_outdoor_googleMap))
			.getMap();
	
	
	
	
	routeProvider = RouteProvider.getInstance();
	routeProvider.setContext(this.getActivity());
	
	map = routeProvider.getMapWithRoute(map, dbHelper);
	map.setOnMarkerClickListener(new MarkerOnClickCustomListener(this, map, dbHelper));
	return rootView;
	}
	
	@Override
	public void onClick(View v) 
	{
	
	}
	
}
