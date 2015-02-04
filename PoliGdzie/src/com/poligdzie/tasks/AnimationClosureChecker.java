package com.poligdzie.tasks;

import java.util.List;

import android.app.FragmentTransaction;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.poligdzie.activities.MapActivity;
import com.poligdzie.callbacks.MarkerAnimationFinishCallback;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.fragments.SearchDetailsFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.persistence.Building;

public class AnimationClosureChecker extends AsyncTask<String, Void, String>
{

	private MarkerAnimationFinishCallback	callback;
	private GoogleMap						map;
	private Marker							marker;
	private MapOutdoorFragment				mapOutdoorFragment;
	private DatabaseHelper					dbHelper;

	@Override
	protected String doInBackground(String... params)
	{
		while (!callback.isFinished())
			;
		return null;
	}

	@Override
	protected void onPostExecute(String result)
	{
		try
		{
	
		LatLng coords = marker.getPosition();
		
			List<Building> buildings =dbHelper.getBuildingDao().queryBuilder().where()
					.eq("coordX", coords.latitude).and()
					.eq("coordY", coords.longitude).query();
			
			if(!buildings.isEmpty())
			{
				Building building = buildings.get(0);
				SearchDetailsFragment searchFrag 
					= ((MapActivity) mapOutdoorFragment.getActivity()).getSearchDetailsFragment();
		
				searchFrag.setTextViews(building.getName(), building.getAddress(), building);
			}
		
			
			
		} catch (java.sql.SQLException e)
		{
			Log.e("ERROR","AnimationClosureChecker SQLException");
		}		
	}

	public AnimationClosureChecker(MarkerAnimationFinishCallback callback,
			GoogleMap map, Marker marker,
			MapOutdoorFragment mapOutdoorFragment, DatabaseHelper dbHelper)
	{
		this.callback = callback;
		this.map = map;
		this.marker = marker;
		this.mapOutdoorFragment = mapOutdoorFragment;
		this.dbHelper = dbHelper;
	}

}
