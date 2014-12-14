package com.poligdzie.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.adapters.InfoWindowCustomAdapter;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.RouteProvider;

public class MapOutdoorFragment extends Fragment implements OnClickListener,
Constants {
	
	GoogleMap map;
	public PolylineOptions options;
	//protected DatabaseHelper dbHelper;
	DatabaseHelper dbHelper;
	private RouteProvider provider;
	
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
	InfoWindowCustomAdapter adapter = new InfoWindowCustomAdapter(getActivity(), dbHelper);
	map.setInfoWindowAdapter(adapter);
	map.setOnInfoWindowClickListener(adapter);
	provider = RouteProvider.getInstance(getActivity());

	map = provider.getMapWithRoute(map, dbHelper);
	
	return rootView;
	}
	
	@Override
	public void onClick(View v) 
	{
	
	}
}
