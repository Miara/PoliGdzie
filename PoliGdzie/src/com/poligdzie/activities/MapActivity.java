package com.poligdzie.activities;


import java.security.acl.LastOwnerException;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.fragments.MapIndoorFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.singletons.RouteProvider;

public class MapActivity extends PoliGdzieBaseActivity implements OnClickListener,OnMarkerClickListener {

	public PolylineOptions options;
	//protected DatabaseHelper dbHelper;
	RouteProvider provider;

	private TextView currentText;
	private Button previous;
	private Button next;

	MapOutdoorFragment outdoorMap;
	MapIndoorFragment indoorMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		provider = new RouteProvider();
		provider.clearFragments();

		outdoorMap = new MapOutdoorFragment();
		provider.addFragment(MAP_MODE_OUTDOOR, "Droga na zewn¹trz", 0, outdoorMap);
		switchFragment(R.id.fragment_map_container, outdoorMap, MAP_MODE_OUTDOOR);
		
		indoorMap = new MapIndoorFragment();
		provider.addFragment(MAP_MODE_INDOOR_LAST,"Centrum Wyk³adowe", 0, indoorMap);

		indoorMap = new MapIndoorFragment();
		provider.addFragment(MAP_MODE_INDOOR_LAST,"Centrum Wyk³adowe", 1, indoorMap);
		
		previous = (Button) findViewById(R.id.previous_map);
		previous.setOnClickListener(this);
		previous.setVisibility(View.GONE);
		
		next = (Button) findViewById(R.id.next_map);
		next.setOnClickListener(this);
		
		currentText = (TextView) findViewById(R.id.current_map);
		currentText.setText(provider.getCurrentFragmentHeader());
			
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		arg0.showInfoWindow();
		return false;
		
	}


	@Override
	public void onClick(View v) {
		Fragment frag;
		String tag;
		if( v == next)
		{
			frag = provider.getNextFragment();
			tag = provider.getCurrentFragmentTag();
			currentText.setText(provider.getCurrentFragmentHeader());
			switchFragment(R.id.fragment_map_container, frag, tag);
		}
		if( v == previous)
		{
			frag = provider.getPreviousFragment();
			tag = provider.getCurrentFragmentTag();
			currentText.setText(provider.getCurrentFragmentHeader());
			switchFragment(R.id.fragment_map_container, frag, tag);
		}
		
		if(provider.getFragmentPosition() <  ( provider.getFragmentsSize() - 1) )
			next.setVisibility(View.VISIBLE);
		else
			next.setVisibility(View.GONE);
		
		if(provider.getFragmentPosition() == 0)
			previous.setVisibility(View.GONE);
		else
			previous.setVisibility(View.VISIBLE);

	}
	
	


}
