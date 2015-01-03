package com.poligdzie.listeners;

import android.app.Fragment;
import android.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;
import com.poligdzie.callbacks.MarkerAnimationFinishCallback;
import com.poligdzie.fragments.BuildingInfoFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.helpers.DatabaseHelper;
import com.poligdzie.singletons.RouteProvider;
import com.poligdzie.tasks.AnimationClosureChecker;

public class MarkerOnClickCustomListener implements OnMarkerClickListener {

	private MapOutdoorFragment mapOutdoorFragment;
	private BuildingInfoFragment buildingInfoFragment;
	private GoogleMap map;
	private RouteProvider routeProvider;
	private DatabaseHelper dbHelper;

	public MarkerOnClickCustomListener(MapOutdoorFragment fragment,
			GoogleMap map, DatabaseHelper dbHelper) {
		// TODO Auto-generated constructor stub
		this.mapOutdoorFragment = fragment;
		this.map = map;
		routeProvider = RouteProvider.getInstance();
		this.dbHelper = dbHelper;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {

		if (isFragmentVisible(arg0)) {
			buildingInfoFragment = (BuildingInfoFragment) mapOutdoorFragment
					.getFragmentManager().findFragmentByTag(arg0.getId());
			FragmentTransaction transaction = mapOutdoorFragment
					.getFragmentManager().beginTransaction();
			transaction.remove(buildingInfoFragment);
			transaction.commit();
			return true;
		} else {

			clearFragments();

			MarkerAnimationFinishCallback callback = new MarkerAnimationFinishCallback();
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(
					arg0.getPosition(), 17), callback);
			
			
	
			AnimationClosureChecker checker = new AnimationClosureChecker(callback, map, arg0, mapOutdoorFragment, dbHelper);
			checker.execute();
			return true;
		}
	}

	private boolean isFragmentVisible(Marker arg0) {
		if (mapOutdoorFragment.getFragmentManager().findFragmentByTag(
				arg0.getId()) != null
				&& mapOutdoorFragment.getFragmentManager()
						.findFragmentByTag(arg0.getId()).isVisible()) {
			return true;
		} else {
			return false;
		}
	}

	private void clearFragments() {
		Fragment tempFragment = new Fragment();
		for (Marker m : routeProvider.getMarkers()) {
			tempFragment = (BuildingInfoFragment) mapOutdoorFragment
					.getFragmentManager().findFragmentByTag(m.getId());
			if (tempFragment != null) {
				mapOutdoorFragment.getFragmentManager().beginTransaction()
						.remove(tempFragment).commit();
			}
		}
	}

}
