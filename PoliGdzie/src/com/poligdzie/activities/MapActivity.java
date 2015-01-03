package com.poligdzie.activities;


import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.fragments.MapIndoorFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.fragments.PoliGdzieMapFragment;
import com.poligdzie.singletons.MapFragmentProvider;

public class MapActivity extends PoliGdzieBaseActivity implements OnClickListener {

	public PolylineOptions options;

	MapFragmentProvider mapProvider;

	private TextView currentText;
	private Button previous;
	private Button next;

	MapOutdoorFragment outdoorMap;
	MapIndoorFragment indoorMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		mapProvider = MapFragmentProvider.getInstance();
		

		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		
		// 0 - czyli brak bitmapy do rysowania
		outdoorMap = new MapOutdoorFragment(NO_BITMAP, "Mapa zewnêtrzna", "outdoor");
		
		switchFragment(R.id.map_container, outdoorMap, outdoorMap.getViewTag());
		
		indoorMap = new MapIndoorFragment(R.drawable.cw_test_parter, "Centrum wyk³adowe - parter", "cw0p", 0);
		indoorMap = new MapIndoorFragment(R.drawable.cw_test_parter, "Centrum wyk³adowe - pierwsze piêtro", "cw1p", 1);
		
				
		

		
		int mem = activityManager.getMemoryClass();
		Log.i("Poligdzie",""+mem);
		
		previous = (Button) findViewById(R.id.previous_map);
		previous.setOnClickListener(this);
		previous.setVisibility(View.GONE);
		
		next = (Button) findViewById(R.id.next_map);
		next.setOnClickListener(this);
		
		currentText = (TextView) findViewById(R.id.current_map);
		currentText.setText(mapProvider.getCurrentFragment().getName());
	
	}



	@Override
	public void onClick(View v) {
				
		PoliGdzieMapFragment frag;
		String tag;
		if( v == next)
		{
			frag = mapProvider.getNextFragment();
			tag = mapProvider.getNextKey();
			
			currentText.setText(frag.getName());
			switchFragment(R.id.map_container, frag, tag);
		}
		if( v == previous)
		{
			frag = mapProvider.getPreviousFragment();
			tag = mapProvider.getPreviousKey();
		
			currentText.setText(frag.getName());
			switchFragment(R.id.map_container, frag, tag);
		}
		
		
		if(mapProvider.getCurrentFragmentKeyPosition() >= (mapProvider.getFragmentsSize() - 1))
			next.setVisibility(View.GONE);
		else
			next.setVisibility(View.VISIBLE);
		
		if(mapProvider.getCurrentFragmentKeyPosition() <= 0)
			previous.setVisibility(View.GONE);
		else
			previous.setVisibility(View.VISIBLE);

	}



	public MapActivity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	


}
