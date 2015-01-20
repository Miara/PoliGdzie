package com.poligdzie.listeners;

import android.app.Fragment;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.poligdzie.fragments.BuildingInfoFragment;
import com.poligdzie.fragments.MapOutdoorFragment;
import com.poligdzie.singletons.MapDrawingProvider;

public class OnCameraChangeCustomListener implements OnCameraChangeListener
{

	private MapDrawingProvider drawingProvider;
	private MapOutdoorFragment mapOutdoorFragment;
	
	@Override
	public void onCameraChange(CameraPosition arg0)
	{
		// TODO Auto-generated method stub
		clearFragments();
	}
	
	private void clearFragments()
	{
		drawingProvider = MapDrawingProvider.getInstance();
		Fragment tempFragment = new Fragment();
		for (Marker m : drawingProvider.getMarkers())
		{
			tempFragment = (BuildingInfoFragment) mapOutdoorFragment
					.getFragmentManager().findFragmentByTag(m.getId());
			if (tempFragment != null)
			{
				mapOutdoorFragment.getFragmentManager().beginTransaction()
						.remove(tempFragment).commit();
			}
		}
	}

	public OnCameraChangeCustomListener(MapOutdoorFragment mapOutdoorFragment)
	{
		this.mapOutdoorFragment = mapOutdoorFragment;
	}

}