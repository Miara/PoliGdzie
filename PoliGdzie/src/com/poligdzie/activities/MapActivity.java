package com.poligdzie.activities;

import java.sql.SQLException;
import java.util.List;

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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		mapProvider = MapFragmentProvider.getInstance();

		DataProvider provider = DataProvider.getInstance();
		
		try
		{
			provider.setBuildings(dbHelper.getBuildingDao().queryForAll());
			provider.setRooms(dbHelper.getRoomDao().queryForAll());
			provider.setUnits(dbHelper.getUnitDao().queryForAll());
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outdoorMap = new MapOutdoorFragment(NO_BITMAP, "Mapa zewnętrzna",
				OUTDOOR_MAP_TAG);
		

		switchFragment(R.id.map_container, outdoorMap, outdoorMap.getViewTag());
		
		//ind
		try
		{
			Room r1 = dbHelper.getRoomDao().queryBuilder().where().eq("id", 16).queryForFirst();
			Room r2 = dbHelper.getRoomDao().queryBuilder().where().eq("id", 22).queryForFirst();
			IndoorRouteFinder test = new IndoorRouteFinder(dbHelper);
			List<NavigationPoint> list = test.findRoute(r1, r2);
			String mRoute ="";
			for(NavigationPoint p : list)
			{
				mRoute = mRoute + p.getId() + "-" ;
			}
			Log.i("ROUTE",""+mRoute);
		} catch (SQLException e)
		{
			Log.i("ROUTE","FAIL!");
			e.printStackTrace();
		}
		//ind

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

		PoliGdzieMapFragment frag;
		String tag;
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

		currentText.setText(mapProvider.getCurrentFragment().getName());

		if (mapProvider.getCurrentFragmentKeyPosition() >= (mapProvider
				.getFragmentsSize() - 1))
			next.setVisibility(View.GONE);
		else
			next.setVisibility(View.VISIBLE);

		if (mapProvider.getCurrentFragmentKeyPosition() <= 0)
			previous.setVisibility(View.GONE);
		else
			previous.setVisibility(View.VISIBLE);

	}

	public MapActivity()
	{
		super();
	}

}
