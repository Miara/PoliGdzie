package com.poligdzie.activities;

import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.poligdzie.R;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.base.PoliGdzieBaseActivity;
import com.poligdzie.base.PoliGdzieMapFragment;
import com.poligdzie.fragments.MapIndoorFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.fragments.RouteDetailsFragment;
import com.poligdzie.fragments.SearchDetailsFragment;
import com.poligdzie.fragments.SearchPlaceFragment;
import com.poligdzie.fragments.SearchRouteFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.Room;
import com.poligdzie.route.IndoorRouteFinder;
import com.poligdzie.singletons.DataProvider;
import com.poligdzie.singletons.MapFragmentProvider;

public class MapActivity extends PoliGdzieBaseActivity implements
														OnClickListener
{

	public PolylineOptions		options;

	MapFragmentProvider			mapProvider;

	private TextView			currentText;
	private Button				previous;
	private Button				next;

	private MapOutdoorFragment	outdoorMap;
	private MapIndoorFragment	indoorMap;
	
	private SearchPlaceFragment	searchPlaceFragment;
	private SearchRouteFragment	searchRouteFragment;
	private SearchDetailsFragment	searchDetailsFragment;
	private RouteDetailsFragment	routeDetailsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);


		mapProvider = MapFragmentProvider.getInstance();

		DataProvider dataProvider = DataProvider.getInstance();
		dataProvider.initialize(this, dbHelper);

		outdoorMap = new MapOutdoorFragment(NO_BITMAP, "Mapa zewnêtrzna",
				OUTDOOR_MAP_TAG);


		switchFragment(R.id.map_container, outdoorMap, outdoorMap.getViewTag());


		previous = (Button) findViewById(R.id.previous_map);
		previous.setOnClickListener(this);
		previous.setVisibility(View.GONE);

		mapProvider = MapFragmentProvider.getInstance();

		next = (Button) findViewById(R.id.next_map);
		next.setOnClickListener(this);

		if (mapProvider.getFragmentsSize() == 1)
			next.setVisibility(View.GONE);

		currentText = (TextView) findViewById(R.id.current_map);

		currentText.setText(mapProvider.getCurrentFragment().getName());
		
		searchPlaceFragment =  (SearchPlaceFragment) getFragmentManager().
				findFragmentById(R.id.search_place_frag);
		searchPlaceFragment.getView().setVisibility(View.VISIBLE);
		
		searchRouteFragment = (SearchRouteFragment) getFragmentManager().
				findFragmentById(R.id.search_route_frag);
		searchRouteFragment.getView().setVisibility(View.GONE);
		
		searchDetailsFragment = (SearchDetailsFragment) getFragmentManager().
		findFragmentById(R.id.search_description_frag);
		searchDetailsFragment.getView().setVisibility(View.GONE);
		
		routeDetailsFragment = (RouteDetailsFragment) getFragmentManager().
		findFragmentById(R.id.route_details_frag);
		routeDetailsFragment.getView().setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v)
	{

		PoliGdzieMapFragment frag = null;
		String tag = "";
		if (v == next)
		{
			frag = mapProvider.getNextFragment();
			tag = mapProvider.getNextKey();

			switchFragment(R.id.map_container, frag, tag);
		}
		if (v == previous)
		{
			frag = mapProvider.getPreviousFragment();
			tag = mapProvider.getPreviousKey();

			switchFragment(R.id.map_container, frag, tag);
		}

		setNavigationArrowsVisibility();

	}

	public void setNavigationArrowsVisibility()
	{
		if (mapProvider.getCurrentFragmentKeyPosition() >= (mapProvider
				.getFragmentsSize() - 1))
			next.setVisibility(View.GONE);
		else
			next.setVisibility(View.VISIBLE);

		if (mapProvider.getCurrentFragmentKeyPosition() <= 0)
			previous.setVisibility(View.GONE);
		else
			previous.setVisibility(View.VISIBLE);

		currentText.setText(mapProvider.getCurrentFragment().getName());
	}

	public MapActivity()
	{
		super();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        if(searchRouteFragment.isVisible())
	        {
	        	searchRouteFragment.getView().setVisibility(View.GONE);
	        	return true;
	        }
	        else
	        {
	        	this.finish();
	        }
	        
	    }
	    return super.onKeyDown(keyCode, event);   
	}
	// TODO : powstawiac w layoutach contentDescription
}
