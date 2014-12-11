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
import com.poligdzie.persistence.Building;
import com.poligdzie.persistence.DatabaseHelper;
import com.poligdzie.singletons.RouteProvider;

public class MapActivity extends PoliGdzieBaseActivity implements OnMarkerClickListener {

	public GoogleMap map;
	public PolylineOptions options;
	//protected DatabaseHelper dbHelper;

	private RouteProvider provider;
	

	static final LatLng LOCATION_PIOTROWO = new LatLng(52.4022703, 16.9495847);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		dbHelper = new DatabaseHelper(this, DATABASE_NAME, null,
				DATABASE_VERSION);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		map.setInfoWindowAdapter(new InfoWindowAdapter() {

			// Use default InfoWindow frame
			@Override
			public View getInfoWindow(Marker arg0) {
				View v = getLayoutInflater().inflate(R.layout.window_marker_click, null);
				return v;
			}

			@Override
			public View getInfoContents(Marker arg0) {
				View v = getLayoutInflater().inflate(R.layout.window_marker_click, null);
				return v;

			}
		});
		
		provider = RouteProvider.getInstance();

		map = provider.onCreate(map, dbHelper);
	}

	public void onClick_Piotrowo(View v) {
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
				LOCATION_PIOTROWO, 16);
		map.animateCamera(update);

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		arg0.showInfoWindow();
		return false;
		
	}


}
