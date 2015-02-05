package com.poligdzie.listeners;

import android.app.Fragment;
import android.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.poligdzie.base.PoliGdzieBaseClass;
import com.poligdzie.callbacks.MarkerAnimationFinishCallback;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.singletons.MapDrawingProvider;
import com.poligdzie.tasks.AnimationClosureChecker;

public class OnMarkerClickCustomListener extends PoliGdzieBaseClass implements
																	OnMarkerClickListener
{

	private MapOutdoorFragment		mapOutdoorFragment;
	private GoogleMap				map;


	public OnMarkerClickCustomListener(MapOutdoorFragment fragment,
			GoogleMap map, DatabaseHelper dbHelper)
	{
		this.mapOutdoorFragment = fragment;
		this.map = map;
		this.dbHelper = dbHelper;
	}

	@Override
	public boolean onMarkerClick(Marker arg0)
	{

		MarkerAnimationFinishCallback callback = new MarkerAnimationFinishCallback();
		map.animateCamera(
				CameraUpdateFactory.newLatLngZoom(arg0.getPosition(), 17),
				callback);

		AnimationClosureChecker checker = new AnimationClosureChecker(
				callback, map, arg0, mapOutdoorFragment, dbHelper);
		checker.execute();
		return true;
	}



}
