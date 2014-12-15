package com.poligdzie.singletons;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.poligdzie.interfaces.Constants;
import com.poligdzie.interfaces.NewFunctions;
import com.poligdzie.persistence.DatabaseHelper;

public class MapFragmentProvider implements Constants,NewFunctions{

	private static MapFragmentProvider instance = null;
	private DatabaseHelper dbHelper;
	private PolylineOptions options;
	private static Context context;
	
	private List<Fragment> fragments;
	private List<String> fragmentTags;
	private List<String> fragmentHeaders;
	private int fragmentPosition = 0;
	
	
	
	static final LatLng LOCATION_PIOTROWO = new LatLng(52.4022703, 16.9495847);

	
	protected MapFragmentProvider() {
		// konstruktor zas³aniaj¹cy domyœlny publiczny konstruktor
		fragments = new ArrayList<Fragment>();
		fragmentTags = new ArrayList<String>();
		fragmentHeaders = new ArrayList<String>();
	}
	
	//implementacja singletona
	public static MapFragmentProvider getInstance() {
		if(instance == null) {
			instance =  new MapFragmentProvider();
		}
		return instance;
	}
	
	
	public int getFragmentPosition() {
		return fragmentPosition;
	}

	public void setFragmentPosition(int fragmentPosition) {
		this.fragmentPosition = fragmentPosition;
	}
	
	public void addFragment(String tag, String header,  int floor, Fragment fragment)
	{
		String mFloor = "";
		String mTag = tag;
		if( tag != MAP_MODE_OUTDOOR) 
		{
			mFloor = " piêtro" + Integer.toString(floor);
			mTag+=Integer.toString(floor);	
		}
		fragmentTags.add(mTag);
		fragmentHeaders.add(header + mFloor);
		fragments.add(fragment);
		
	}
	
	public Fragment getPreviousFragment()
	{
		if(fragments.get(fragmentPosition-1) != null)
		{
			setFragmentPosition(fragmentPosition - 1);
		}
		return fragments.get(fragmentPosition);
	}
	
	public Fragment getNextFragment()
	{
		if(fragments.get(fragmentPosition+1) != null)
		{
			setFragmentPosition(fragmentPosition + 1);
		}
		return fragments.get(fragmentPosition);
	}
	
	public String getCurrentFragmentHeader()
	{
		return fragmentHeaders.get(fragmentPosition);
	}
	public String getCurrentFragmentTag()
	{
		return fragmentTags.get(fragmentPosition);
	}
	
	public int getFragmentsSize()
	{
		return fragments.size();
	}
	
	public void clearFragments()
	{
		fragments.clear();
		fragmentTags.clear();
		fragmentHeaders.clear();
	}
	
	

	@Override
	public int getDrawableId(String name, Context context) {
		int resId =context.getResources().getIdentifier(name, "drawable", context.getPackageName());
		return resId;
	}



	

	
}
