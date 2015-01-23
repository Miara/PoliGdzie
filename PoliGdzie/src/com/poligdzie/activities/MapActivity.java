package com.poligdzie.activities;

import android.os.Bundle;
import android.util.Log;
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
<<<<<<< HEAD
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.NavigationPoint;
import com.poligdzie.persistence.Room;
import com.poligdzie.route.IndoorRouteFinder;
=======
>>>>>>> 3516068042f766e8805cb38b910aa213467466df
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		Log.i("poli", "map1");

		mapProvider = MapFragmentProvider.getInstance();

		DataProvider dataProvider = DataProvider.getInstance();
		dataProvider.initialize(this, dbHelper);

		outdoorMap = new MapOutdoorFragment(NO_BITMAP, "Mapa zewnętrzna",
				OUTDOOR_MAP_TAG);

		Log.i("poli", "map4");

		switchFragment(R.id.map_container, outdoorMap, outdoorMap.getViewTag());

		Log.i("poli","map5");
		


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
	// TODO : powstawiac w layoutach contentDescription
}
