package com.poligdzie.listeners;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Point;
import android.util.Log;
import android.view.WindowManager;

import com.example.poligdzie.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.Marker;
import com.poligdzie.fragments.BuildingInfoFragment;

public class MarkerOnClickCustomListener implements OnMarkerClickListener {

	private Fragment fragment;
	private int clickCount;
	private BuildingInfoFragment buildingInfoFragment;
	private GoogleMap map;
	
	public MarkerOnClickCustomListener(Fragment fragment, GoogleMap map) {
		// TODO Auto-generated constructor stub
		this.fragment = fragment;
		this.map = map;
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		
			if(fragment.getFragmentManager().findFragmentByTag(arg0.getId()) != null 
			   && fragment.getFragmentManager().findFragmentByTag(arg0.getId()).isVisible()) {
				buildingInfoFragment = (BuildingInfoFragment) fragment.getFragmentManager().findFragmentByTag(arg0.getId());
				FragmentTransaction transaction = fragment.getFragmentManager()
						.beginTransaction();
				transaction.remove(buildingInfoFragment);
				transaction.commit();
				return true;	
			} else {
			BuildingInfoFragment buildingInfoFragment = new BuildingInfoFragment();

			FragmentTransaction transaction = fragment.getFragmentManager()
					.beginTransaction();
			transaction.add(R.id.map_outdoor_googleMap, buildingInfoFragment, arg0.getId());
			WindowManager.LayoutParams p = fragment.getActivity().getWindow().getAttributes();
			Projection projection = map.getProjection();
			Point point = projection.toScreenLocation(arg0.getPosition());
			p.x = point.x;
			p.y = point.y;
			
			fragment.getActivity().getWindow().setAttributes(p);
			
			Log.i("POLIGDZIE", p.toString());
			transaction.commit();
			return true;
			}
		}		
		
	

}
