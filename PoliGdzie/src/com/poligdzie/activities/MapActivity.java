package com.poligdzie.activities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;

import com.example.poligdzie.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.adapters.InfoWindowCustomAdapter;
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.RouteProvider;

public class MapActivity extends PoliGdzieBaseActivity implements OnMarkerClickListener {

	public GoogleMap map;
	public PolylineOptions options;
	//protected DatabaseHelper dbHelper;

	private RouteProvider provider;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		dbHelper = new DatabaseHelper(this, DATABASE_NAME, null,
				DATABASE_VERSION);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		InfoWindowCustomAdapter adapter = new InfoWindowCustomAdapter(this, dbHelper);
		map.setInfoWindowAdapter(adapter);
		map.setOnInfoWindowClickListener(adapter);
		provider = RouteProvider.getInstance(this);

		map = provider.getMapWithRoute(map, dbHelper);
	}


	@Override
	public boolean onMarkerClick(Marker arg0) {
		arg0.showInfoWindow();
		return false;
		
	}


}
